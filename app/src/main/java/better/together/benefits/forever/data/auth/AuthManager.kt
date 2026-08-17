package better.together.benefits.forever.data.auth

import better.together.benefits.forever.data.remote.SupabaseProvider
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class AuthManager {
    private val auth = SupabaseProvider.client.auth
    private val bootstrapMutex = Mutex()
    private val _state = MutableStateFlow<AuthState>(AuthState.Loading)

    val state: StateFlow<AuthState> = _state.asStateFlow()

    val currentUserId: String?
        get() = (_state.value as? AuthState.Authenticated)?.userId

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
                    AuthState.Authenticated(user.id)
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
