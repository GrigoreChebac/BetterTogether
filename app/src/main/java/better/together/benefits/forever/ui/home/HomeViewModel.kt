package better.together.benefits.forever.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import better.together.benefits.forever.data.request.BarterRequest
import better.together.benefits.forever.data.request.RequestRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(val requests: List<BarterRequest>) : HomeUiState
    data class Error(val message: String) : HomeUiState
}

data class CreateRequestUiState(
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
)

class HomeViewModel(
    private val repository: RequestRepository = RequestRepository(),
) : ViewModel() {
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _createState = MutableStateFlow(CreateRequestUiState())
    val createState: StateFlow<CreateRequestUiState> = _createState.asStateFlow()

    init { refresh() }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            try {
                _uiState.value = HomeUiState.Success(repository.loadRequests())
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.value = HomeUiState.Error(exception.message ?: "Could not load requests.")
            }
        }
    }

    fun createRequest(
        ownerId: String,
        need: String,
        offer: String,
        description: String,
        onSuccess: () -> Unit,
    ) {
        if (_createState.value.isSubmitting) return
        _createState.value = CreateRequestUiState(isSubmitting = true)
        viewModelScope.launch {
            try {
                repository.createRequest(ownerId, need, offer, description)
                _createState.value = CreateRequestUiState()
                _uiState.value = HomeUiState.Success(repository.loadRequests())
                onSuccess()
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _createState.value = CreateRequestUiState(
                    errorMessage = exception.message ?: "Could not publish request. Please try again.",
                )
            }
        }
    }

    fun clearCreateError() {
        _createState.value = _createState.value.copy(errorMessage = null)
    }
}
