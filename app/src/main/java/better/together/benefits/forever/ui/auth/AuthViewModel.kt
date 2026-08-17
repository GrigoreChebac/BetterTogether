package better.together.benefits.forever.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import better.together.benefits.forever.data.auth.AuthManager
import better.together.benefits.forever.data.auth.AuthState
import better.together.benefits.forever.data.profile.Profile
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authManager: AuthManager = AuthManager(),
) : ViewModel() {
    val authState: StateFlow<AuthState> = authManager.state

    val currentUserId: String?
        get() = authManager.currentUserId

    val currentProfile: Profile?
        get() = authManager.currentProfile

    init {
        authenticate()
    }

    fun retry() {
        authenticate()
    }

    private fun authenticate() {
        viewModelScope.launch {
            authManager.authenticate()
        }
    }
}
