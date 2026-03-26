package app.le.bondstracker.ui.addbond

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.le.bondstracker.data.repository.BondRepository
import app.le.bondstracker.domain.model.Bond
import app.le.bondstracker.notification.NotificationScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import app.le.bondstracker.data.dto.BondDto
import app.le.bondstracker.data.mapper.toDomain
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class AddBondUiState(
    val jsonInput: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AddBondViewModel @Inject constructor(
    private val repository: BondRepository,
    private val notificationScheduler: NotificationScheduler,
    private val gson: Gson
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddBondUiState())
    val uiState: StateFlow<AddBondUiState> = _uiState

    fun onJsonChanged(json: String) {
        _uiState.update { it.copy(jsonInput = json, error = null) }
    }

    fun addBond() {
        val json = _uiState.value.jsonInput.trim()
        if (json.isEmpty()) {
            _uiState.update { it.copy(error = "Please paste a valid JSON array") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                repository.addBondsFromJson(json)
                // Schedule notifications — reload to get the saved bond details
                val bonds = parseBondsForNotifications(json)
                bonds?.forEach { scheduleNotifications(it) }
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Invalid JSON array: ${e.message}"
                    )
                }
            }
        }
    }

    fun importFromFile(json: String) {
        onJsonChanged(json)
        addBond()
    }

    private fun parseBondsForNotifications(json: String): List<BondDto>? {
        return try {
            val type = object : TypeToken<List<BondDto>>() {}.type
            gson.fromJson(json, type)
        } catch (e: Exception) {
            null
        }
    }

    private fun scheduleNotifications(bond: BondDto) {
        // Schedule reminders for each upcoming payout
        bond.payouts.filter { it.status.lowercase() == "upcoming" }.forEach { payout ->
            notificationScheduler.schedulePayoutReminder(
                bondId = bond.investmentId,
                companyName = bond.companyName,
                investorName = bond.investor,
                payoutDate = payout.date,
                amount = payout.amount
            )
        }
        // Schedule maturity reminder
        notificationScheduler.scheduleMaturityReminder(
            bondId = bond.investmentId,
            companyName = bond.companyName,
            investorName = bond.investor,
            maturityDate = bond.maturityDate
        )
    }

    fun clearSuccess() {
        _uiState.update { it.copy(isSuccess = false, jsonInput = "") }
    }
}
