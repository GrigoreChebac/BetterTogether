package better.together.benefits.forever.data.auth

import better.together.benefits.forever.data.profile.Profile

sealed interface AuthState {
    data object Loading : AuthState

    data class Authenticated(
        val userId: String,
        val profile: Profile,
    ) : AuthState

    data object Error : AuthState
}
