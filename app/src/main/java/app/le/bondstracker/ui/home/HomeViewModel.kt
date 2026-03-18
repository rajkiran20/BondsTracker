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
    val bondCount: Int,
    val bondCountLabel: String
)

data class HomeUiState(
    val bonds: List<Bond> = emptyList(),
    val filteredBonds: List<Bond> = emptyList(),
    
    val overallSummary: PortfolioSummary = PortfolioSummary("Overall Portfolio", 0.0, 0.0, 0, "Total Bonds"),
    val activeSummary: PortfolioSummary = PortfolioSummary("Active Bonds", 0.0, 0.0, 0, "Active Bonds"),
    val closedSummary: PortfolioSummary = PortfolioSummary("Closed/Matured", 0.0, 0.0, 0, "Closed Bonds"),
    
    val isLoading: Boolean = true,
    // Filter state
    val availablePlatforms: List<String> = emptyList(),
    val availableInvestors: List<String> = emptyList(),
    val availableStatuses: List<String> = emptyList(),
    val selectedPlatforms: Set<String> = emptySet(),
    val selectedInvestors: Set<String> = emptySet(),
    val selectedStatuses: Set<String> = emptySet(),
    val selectedSortOption: SortOption = SortOption.EARLIEST_MATURITY
)

enum class SortOption {
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
    private val _selectedSortOption = MutableStateFlow(SortOption.EARLIEST_MATURITY)

    val uiState: StateFlow<HomeUiState> = combine(
        repository.getAllBonds(),
        _selectedPlatforms,
        _selectedInvestors,
        _selectedStatuses,
        _selectedSortOption
    ) { rawBonds, platforms, investors, statuses, sortOption ->
        val bonds = rawBonds.map {
            if (it.status.equals("closed", ignoreCase = true)) it.copy(status = "matured") else it
        }
        
        val filtered = bonds
            .let { list -> if (platforms.isNotEmpty()) list.filter { it.platform in platforms } else list }
            .let { list -> if (investors.isNotEmpty()) list.filter { it.investor in investors } else list }
            .let { list -> if (statuses.isNotEmpty()) list.filter { it.status.replaceFirstChar { c -> c.uppercase() } in statuses } else list }

        val sorted = when (sortOption) {
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
                totalReturns = bonds.sumOf { it.returnsReceived },
                bondCount = bonds.size,
                bondCountLabel = "Total Bonds"
            ),
            activeSummary = PortfolioSummary(
                title = "Active Portfolio",
                totalInvested = activeBonds.sumOf { it.investmentAmount },
                totalReturns = activeBonds.sumOf { it.returnsReceived },
                bondCount = activeBonds.size,
                bondCountLabel = "Active Bonds"
            ),
            closedSummary = PortfolioSummary(
                title = "Closed/Matured Portfolio",
                totalInvested = closedBonds.sumOf { it.investmentAmount },
                totalReturns = closedBonds.sumOf { it.returnsReceived },
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
