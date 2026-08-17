package better.together.benefits.forever.data.profile

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val id: String,
    @SerialName("display_name")
    val displayName: String,
    @SerialName("created_at")
    val createdAt: Instant,
    @SerialName("updated_at")
    val updatedAt: Instant,
)

@Serializable
internal data class NewProfile(
    val id: String,
    @SerialName("display_name")
    val displayName: String,
)
