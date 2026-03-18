package app.le.bondstracker.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.le.bondstracker.data.repository.BondRepository
import app.le.bondstracker.domain.model.Bond
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailUiState(
    val bond: Bond? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class BondDetailViewModel @Inject constructor(
    private val repository: BondRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val bondId: String = checkNotNull(savedStateHandle["bondId"])

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState

    init {
        loadBond()
    }

    private fun loadBond() {
        viewModelScope.launch {
            try {
                val bond = repository.getBondById(bondId)
                _uiState.value = DetailUiState(bond = bond, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = DetailUiState(isLoading = false, error = e.message)
            }
        }
    }

    fun deleteBond(onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                repository.deleteBondById(bondId)
                onSuccess()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }
}
