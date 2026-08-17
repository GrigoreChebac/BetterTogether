package better.together.benefits.forever.data.profile

import better.together.benefits.forever.data.remote.SupabaseProvider
import io.github.jan.supabase.postgrest.from

class ProfileRepository {
    private val profiles = SupabaseProvider.client.from("profiles")

    suspend fun getProfile(userId: String): Profile? = profiles
        .select {
            filter {
                eq("id", userId)
            }
        }
        .decodeSingleOrNull()

    suspend fun createProfile(userId: String, displayName: String): Profile {
        profiles.upsert(
            NewProfile(
                id = userId,
                displayName = displayName,
            ),
        ) {
            onConflict = "id"
            ignoreDuplicates = true
        }

        return requireNotNull(getProfile(userId)) {
            "The profile could not be loaded after creation."
        }
    }

    suspend fun ensureCurrentUserProfile(userId: String): Profile =
        getProfile(userId) ?: createProfile(
            userId = userId,
            displayName = DEFAULT_DISPLAY_NAME,
        )

    private companion object {
        const val DEFAULT_DISPLAY_NAME = "You"
    }
}
