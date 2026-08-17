package better.together.benefits.forever.data.request

import better.together.benefits.forever.data.profile.Profile
import better.together.benefits.forever.data.remote.SupabaseProvider
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order

class RequestRepository {
    private val client = SupabaseProvider.client

    suspend fun loadRequests(): List<BarterRequest> {
        val rows = client.from("requests").select {
            order("created_at", Order.DESCENDING)
        }.decodeList<RequestRow>()

        if (rows.isEmpty()) return emptyList()

        // One bulk profile query avoids an N+1 request pattern. RLS allows authenticated reads.
        val displayNames = client.from("profiles")
            .select()
            .decodeList<Profile>()
            .associate { it.id to it.displayName }

        return rows.map { row ->
            BarterRequest(
                id = row.id,
                ownerId = row.ownerId,
                ownerDisplayName = displayNames[row.ownerId] ?: "Unknown member",
                need = row.need,
                offer = row.offer,
                description = row.description.orEmpty(),
                createdAt = row.createdAt,
            )
        }
    }

    suspend fun createRequest(ownerId: String, need: String, offer: String, description: String) {
        client.from("requests").insert(
            NewRequest(
                ownerId = ownerId,
                need = need,
                offer = offer,
                description = description.ifBlank { null },
            ),
        )
    }
}
