package better.together.benefits.forever.ui.offer

import java.util.UUID

enum class OfferStatus {
    Pending,
}

data class BarterOffer(
    val id: String = UUID.randomUUID().toString(),
    val requestId: String,
    val requesterName: String,
    val senderName: String,
    val offering: String,
    val wantsInReturn: String,
    val message: String,
    val status: OfferStatus = OfferStatus.Pending,
)
