package better.together.benefits.forever.data.auth

sealed interface AuthState {
    data object Loading : AuthState

    data class Authenticated(val userId: String) : AuthState

    data object Error : AuthState
}
