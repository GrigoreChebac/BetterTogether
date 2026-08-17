package better.together.benefits.forever.data.auth

import better.together.benefits.forever.data.remote.SupabaseProvider
import better.together.benefits.forever.data.profile.Profile
import better.together.benefits.forever.data.profile.ProfileRepository
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class AuthManager(
    private val profileRepository: ProfileRepository = ProfileRepository(),
) {
    private val auth = SupabaseProvider.client.auth
    private val bootstrapMutex = Mutex()
    private val _state = MutableStateFlow<AuthState>(AuthState.Loading)

    val state: StateFlow<AuthState> = _state.asStateFlow()

    val currentUserId: String?
        get() = (_state.value as? AuthState.Authenticated)?.userId

    val currentProfile: Profile?
        get() = (_state.value as? AuthState.Authenticated)?.profile

    suspend fun authenticate() {
        bootstrapMutex.withLock {
            _state.value = AuthState.Loading

            try {
                auth.awaitInitialization()

                val user = auth.currentUserOrNull() ?: run {
                    auth.signInAnonymously()
                    auth.currentUserOrNull()
                }

                _state.value = if (user != null) {
                    val profile = profileRepository.ensureCurrentUserProfile(user.id)
                    AuthState.Authenticated(
                        userId = user.id,
                        profile = profile,
                    )
                } else {
                    AuthState.Error
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                _state.value = AuthState.Error
            }
        }
    }
}
