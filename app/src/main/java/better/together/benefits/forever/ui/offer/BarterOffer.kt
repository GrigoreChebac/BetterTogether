package better.together.benefits.forever.ui.offer

import java.util.UUID

enum class OfferStatus {
    Pending,
    Accepted,
    Declined,
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

val initialReceivedOffers = listOf(
    BarterOffer(
        requestId = "local-android-request",
        requesterName = "You",
        senderName = "Maria",
        offering = "Professional photography session",
        wantsInReturn = "Help building an Android screen",
        message = "",
    ),
    BarterOffer(
        requestId = "local-workout-request",
        requesterName = "You",
        senderName = "Daniel",
        offering = "German conversation practice",
        wantsInReturn = "Help with a workout plan",
        message = "",
    ),
)
