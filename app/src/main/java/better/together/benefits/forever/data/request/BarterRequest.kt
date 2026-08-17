package better.together.benefits.forever.data.request

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class BarterRequest(
    val id: String,
    val ownerId: String,
    val ownerDisplayName: String,
    val need: String,
    val offer: String,
    val description: String,
    val createdAt: Instant,
) {
    val personName: String
        get() = ownerDisplayName
}

@Serializable
internal data class RequestRow(
    val id: String,
    @SerialName("owner_id") val ownerId: String,
    val need: String,
    val offer: String,
    val description: String? = null,
    @SerialName("created_at") val createdAt: Instant,
)

@Serializable
internal data class NewRequest(
    @SerialName("owner_id") val ownerId: String,
    val need: String,
    val offer: String,
    val description: String? = null,
)
