package app.le.bondstracker.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.le.bondstracker.data.repository.BondRepository
import app.le.bondstracker.domain.model.Bond
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import com.google.gson.Gson
import app.le.bondstracker.data.mapper.toDto
import java.text.SimpleDateFormat
import java.util.Locale

data class PortfolioSummary(
    val title: String,
    val totalInvested: Double,
    val totalReturns: Double,
    val totalPrincipalReceived: Double,
    val totalInterestReceived: Double,
    val bondCount: Int,
    val bondCountLabel: String
)

data class HomeUiState(
    val bonds: List<Bond> = emptyList(),
    val filteredBonds: List<Bond> = emptyList(),
    
    val overallSummary: PortfolioSummary = PortfolioSummary("Overall Portfolio", 0.0, 0.0, 0.0, 0.0, 0, "Total Bonds"),
    val activeSummary: PortfolioSummary = PortfolioSummary("Active Bonds", 0.0, 0.0, 0.0, 0.0, 0, "Active Bonds"),
    val closedSummary: PortfolioSummary = PortfolioSummary("Closed/Matured", 0.0, 0.0, 0.0, 0.0, 0, "Closed Bonds"),
    
    val isLoading: Boolean = true,
    // Filter state
    val availablePlatforms: List<String> = emptyList(),
    val availableInvestors: List<String> = emptyList(),
    val availableStatuses: List<String> = emptyList(),
    val selectedPlatforms: Set<String> = emptySet(),
    val selectedInvestors: Set<String> = emptySet(),
    val selectedStatuses: Set<String> = emptySet(),
    val selectedSortOption: SortOption = SortOption.EARLIEST_PAYOUT
)

enum class SortOption {
    EARLIEST_PAYOUT,
    RECENT_PAYOUT,
    EARLIEST_MATURITY,
    CHRONOLOGICAL,
    INTEREST_RATE,
    INVESTMENT_AMOUNT,
    SHORTEST_TENURE
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    repository: BondRepository
) : ViewModel() {

    private val _selectedPlatforms = MutableStateFlow<Set<String>>(emptySet())
    private val _selectedInvestors = MutableStateFlow<Set<String>>(emptySet())
    private val _selectedStatuses = MutableStateFlow<Set<String>>(emptySet())
    private val _selectedSortOption = MutableStateFlow(SortOption.EARLIEST_PAYOUT)

    val uiState: StateFlow<HomeUiState> = combine(
        repository.getAllBonds(),
        _selectedPlatforms,
        _selectedInvestors,
        _selectedStatuses,
        _selectedSortOption
    ) { rawBonds, platforms, investors, statuses, sortOption ->
        val today = java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.HOUR_OF_DAY, 0)
            set(java.util.Calendar.MINUTE, 0)
            set(java.util.Calendar.SECOND, 0)
            set(java.util.Calendar.MILLISECOND, 0)
        }.timeInMillis

        val bonds = rawBonds.map { rawBond ->
            var isMaturedByDate = false
            try {
                if (!rawBond.maturityDate.isNullOrBlank()) {
                    val format = if (rawBond.maturityDate.contains("T")) SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US) else SimpleDateFormat("yyyy-MM-dd", Locale.US)
                    val maturityTime = format.parse(rawBond.maturityDate)?.time ?: Long.MAX_VALUE
                    if (maturityTime < today) {
                        isMaturedByDate = true
                    }
                }
            } catch (e: Exception) {
                // ignore
            }
            
            val bondStatus = if (rawBond.status.equals("closed", ignoreCase = true) || isMaturedByDate) "matured" else rawBond.status
            var dynamicNextPayout = rawBond.nextPayoutDate
            
            if (rawBond.payouts.isNotEmpty() && bondStatus.equals("active", ignoreCase = true)) {
                val futureOrTodayPayouts = rawBond.payouts.filter { p ->
                    try {
                        val format = if (p.date.contains("T")) SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US) else SimpleDateFormat("yyyy-MM-dd", Locale.US)
                        val t = format.parse(p.date)?.time ?: 0L
                        t >= today
                    } catch (e: Exception) { false }
                }
                
                dynamicNextPayout = futureOrTodayPayouts.minByOrNull { p ->
                    try {
                        val format = if (p.date.contains("T")) SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US) else SimpleDateFormat("yyyy-MM-dd", Locale.US)
                        format.parse(p.date)?.time ?: Long.MAX_VALUE
                    } catch (e: Exception) { Long.MAX_VALUE }
                }?.date
            } else if (bondStatus.equals("matured", ignoreCase = true)) {
                dynamicNextPayout = null
            }
            
            rawBond.copy(status = bondStatus, nextPayoutDate = dynamicNextPayout)
        }
        
        val filtered = bonds
            .let { list -> if (platforms.isNotEmpty()) list.filter { it.platform in platforms } else list }
            .let { list -> if (investors.isNotEmpty()) list.filter { it.investor in investors } else list }
            .let { list -> if (statuses.isNotEmpty()) list.filter { it.status.replaceFirstChar { c -> c.uppercase() } in statuses } else list }

        val sorted = when (sortOption) {
            SortOption.EARLIEST_PAYOUT -> filtered.sortedBy { getNextPayoutTime(it.nextPayoutDate, it.status) }
            SortOption.RECENT_PAYOUT -> filtered.sortedByDescending { getRecentPayoutTime(it) }
            SortOption.CHRONOLOGICAL -> filtered.sortedByDescending { it.orderDate }
            SortOption.INTEREST_RATE -> filtered.sortedByDescending { it.interestRate }
            SortOption.INVESTMENT_AMOUNT -> filtered.sortedByDescending { it.investmentAmount }
            SortOption.EARLIEST_MATURITY -> filtered.sortedBy { it.maturityDate }
            SortOption.SHORTEST_TENURE -> filtered.sortedBy { getTenureDays(it.startDate, it.maturityDate) }
        }

        val activeBonds = bonds.filter { it.status.lowercase() == "active" }
        val closedBonds = bonds.filter { it.status.lowercase() == "matured" || it.status.lowercase() == "defaulted" }

        HomeUiState(
            bonds = bonds,
            filteredBonds = sorted,
            overallSummary = PortfolioSummary(
                title = "Overall Portfolio",
                totalInvested = bonds.sumOf { it.investmentAmount },
                totalReturns = bonds.sumOf { 
                    if (it.returnsReceived == 0.0) {
                        it.payouts.filter { p -> p.status.lowercase() == "received" }.sumOf { p -> p.amount }
                    } else {
                        it.returnsReceived
                    }
                },
                totalPrincipalReceived = bonds.sumOf { 
                    if (it.totalPrincipalRepaid == 0.0) {
                        it.payouts.filter { p -> p.status.lowercase() == "received" }.sumOf { p -> p.principalComponent }
                    } else {
                        it.totalPrincipalRepaid
                    }
                },
                totalInterestReceived = bonds.sumOf { 
                    if (it.interestPaid == 0.0) {
                        it.payouts.filter { p -> p.status.lowercase() == "received" }.sumOf { p -> p.interestComponent }
                    } else {
                        it.interestPaid
                    }
                },
                bondCount = bonds.size,
                bondCountLabel = "Total Bonds"
            ),
            activeSummary = PortfolioSummary(
                title = "Active Portfolio",
                totalInvested = activeBonds.sumOf { it.investmentAmount },
                totalReturns = activeBonds.sumOf { 
                    if (it.returnsReceived == 0.0) {
                        it.payouts.filter { p -> p.status.lowercase() == "received" }.sumOf { p -> p.amount }
                    } else {
                        it.returnsReceived
                    }
                },
                totalPrincipalReceived = activeBonds.sumOf { 
                    if (it.totalPrincipalRepaid == 0.0) {
                        it.payouts.filter { p -> p.status.lowercase() == "received" }.sumOf { p -> p.principalComponent }
                    } else {
                        it.totalPrincipalRepaid
                    }
                },
                totalInterestReceived = activeBonds.sumOf { 
                    if (it.interestPaid == 0.0) {
                        it.payouts.filter { p -> p.status.lowercase() == "received" }.sumOf { p -> p.interestComponent }
                    } else {
                        it.interestPaid
                    }
                },
                bondCount = activeBonds.size,
                bondCountLabel = "Active Bonds"
            ),
            closedSummary = PortfolioSummary(
                title = "Closed/Matured Portfolio",
                totalInvested = closedBonds.sumOf { it.investmentAmount },
                totalReturns = closedBonds.sumOf { 
                    if (it.returnsReceived == 0.0) {
                        it.payouts.filter { p -> p.status.lowercase() == "received" }.sumOf { p -> p.amount }
                    } else {
                        it.returnsReceived
                    }
                },
                totalPrincipalReceived = closedBonds.sumOf { 
                    if (it.totalPrincipalRepaid == 0.0) {
                        it.payouts.filter { p -> p.status.lowercase() == "received" }.sumOf { p -> p.principalComponent }
                    } else {
                        it.totalPrincipalRepaid
                    }
                },
                totalInterestReceived = closedBonds.sumOf { 
                    if (it.interestPaid == 0.0) {
                        it.payouts.filter { p -> p.status.lowercase() == "received" }.sumOf { p -> p.interestComponent }
                    } else {
                        it.interestPaid
                    }
                },
                bondCount = closedBonds.size,
                bondCountLabel = "Closed Bonds"
            ),
            isLoading = false,
            availablePlatforms = bonds.map { it.platform }.distinct().sorted(),
            availableInvestors = bonds.map { it.investor }.distinct().sorted(),
            availableStatuses = bonds.map { it.status.replaceFirstChar { c -> c.uppercase() } }.distinct().sorted(),
            selectedPlatforms = platforms,
            selectedInvestors = investors,
            selectedStatuses = statuses,
            selectedSortOption = sortOption
        )
    }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState()
    )

    fun selectPlatform(platform: String) {
        val current = _selectedPlatforms.value
        _selectedPlatforms.value = if (current.contains(platform)) current - platform else current + platform
    }

    fun selectInvestor(investor: String) {
        val current = _selectedInvestors.value
        _selectedInvestors.value = if (current.contains(investor)) current - investor else current + investor
    }
    
    fun selectStatus(status: String) {
        val current = _selectedStatuses.value
        _selectedStatuses.value = if (current.contains(status)) current - status else current + status
    }

    fun setStatusFilter(status: String?) {
        _selectedStatuses.value = if (status == null) emptySet() else setOf(status)
    }

    fun selectSortOption(option: SortOption) {
        _selectedSortOption.value = option
    }

    fun clearFilters() {
        _selectedPlatforms.value = emptySet()
        _selectedInvestors.value = emptySet()
        _selectedStatuses.value = emptySet()
    }

    fun exportBondsToJson(): String {
        val gson = Gson()
        // Ensure exported JSON matches the DTO schema perfectly so imports don't crash
        val dtos = uiState.value.bonds.map { it.toDto() }
        return gson.toJson(dtos)
    }

    private fun getNextPayoutTime(dateStr: String?, status: String): Long {
        if (!status.equals("active", ignoreCase = true)) return Long.MAX_VALUE
        if (dateStr.isNullOrBlank()) return Long.MAX_VALUE
        return try {
            val format = if (dateStr.contains("T")) SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US) else SimpleDateFormat("yyyy-MM-dd", Locale.US)
            format.parse(dateStr)?.time ?: Long.MAX_VALUE
        } catch (e: Exception) {
            Long.MAX_VALUE
        }
    }

    private fun getRecentPayoutTime(bond: Bond): Long {
        val today = java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.HOUR_OF_DAY, 0)
            set(java.util.Calendar.MINUTE, 0)
            set(java.util.Calendar.SECOND, 0)
            set(java.util.Calendar.MILLISECOND, 0)
        }.timeInMillis

        return bond.payouts.mapNotNull { p ->
            try {
                val format = if (p.date.contains("T")) SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US) else SimpleDateFormat("yyyy-MM-dd", Locale.US)
                val t = format.parse(p.date)?.time
                if (t != null && t <= today) t else null
            } catch (e: Exception) { null }
        }.maxOrNull() ?: 0L
    }

    private fun getTenureDays(startStr: String?, maturityStr: String?): Long {
        if (startStr.isNullOrBlank() || maturityStr.isNullOrBlank()) return 0L
        return try {
            val format1 = if (startStr.contains("T")) SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US) else SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val format2 = if (maturityStr.contains("T")) SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US) else SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val start = format1.parse(startStr)?.time ?: 0L
            val end = format2.parse(maturityStr)?.time ?: 0L
            end - start
        } catch (e: Exception) {
            0L
        }
    }
}
