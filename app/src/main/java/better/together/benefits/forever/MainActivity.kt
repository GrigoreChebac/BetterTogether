package better.together.benefits.forever

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import better.together.benefits.forever.ui.create.CreateRequestScreen
import better.together.benefits.forever.ui.home.BarterRequest
import better.together.benefits.forever.ui.home.HomeScreen
import better.together.benefits.forever.ui.home.initialBarterRequests
import better.together.benefits.forever.ui.offer.BarterOffer
import better.together.benefits.forever.ui.offer.MakeOfferScreen
import better.together.benefits.forever.ui.offer.OfferSentScreen
import better.together.benefits.forever.ui.request.RequestDetailsScreen
import better.together.benefits.forever.ui.theme.BetterTogetherTheme
import com.revenuecat.purchases.LogLevel
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesConfiguration
import com.revenuecat.purchases.getCustomerInfoWith
import com.revenuecat.purchases.ui.revenuecatui.Paywall
import com.revenuecat.purchases.ui.revenuecatui.PaywallOptions


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        Purchases.logLevel = LogLevel.DEBUG
        Purchases.configure(
            PurchasesConfiguration.Builder(this, "test_AmJSpGSlwPUFSVZhCWcMbRhgfbi")
            .build())

        Purchases.sharedInstance.getCustomerInfoWith(
            onError = { error -> println("Error: ${error.message}") },
            onSuccess = { customerInfo ->
                val hasProAccess = customerInfo.entitlements.active["BetterTogether Pro"] != null
                if (hasProAccess) {
                    println("User has pro access!")
                } else {
                    println("User does not have pro access")
                }
            },
        )


        enableEdgeToEdge()
        setContent {
            BetterTogetherTheme {
                BetterTogetherApp()
            }
        }
    }
}

private enum class AppScreen {
    Home,
    CreateRequest,
    RequestDetails,
    MakeOffer,
    OfferSent,
}

@Composable
private fun BetterTogetherApp() {
    var currentScreen by remember { mutableStateOf(AppScreen.Home) }
    var selectedRequest by remember { mutableStateOf<BarterRequest?>(null) }
    val requests = remember { mutableStateListOf<BarterRequest>().apply { addAll(initialBarterRequests) } }
    val offers = remember { mutableStateListOf<BarterOffer>() }

    fun returnHome() {
        selectedRequest = null
        currentScreen = AppScreen.Home
    }

    when (currentScreen) {
        AppScreen.Home -> HomeScreen(
            requests = requests,
            onAddRequest = { currentScreen = AppScreen.CreateRequest },
            onViewBarter = { request ->
                selectedRequest = request
                currentScreen = AppScreen.RequestDetails
            },
        )

        AppScreen.CreateRequest -> {
            BackHandler { returnHome() }
            CreateRequestScreen(
                onBack = { returnHome() },
                onPublish = { request ->
                    requests.add(0, request)
                    returnHome()
                },
            )
        }

        AppScreen.RequestDetails -> selectedRequest?.let { request ->
            BackHandler { returnHome() }
            RequestDetailsScreen(
                request = request,
                onBack = { returnHome() },
                onMakeOffer = { currentScreen = AppScreen.MakeOffer },
            )
        } ?: returnHome()

        AppScreen.MakeOffer -> selectedRequest?.let { request ->
            BackHandler { currentScreen = AppScreen.RequestDetails }
            MakeOfferScreen(
                request = request,
                onBack = { currentScreen = AppScreen.RequestDetails },
                onSendOffer = { offer ->
                    offers.add(offer)
                    currentScreen = AppScreen.OfferSent
                },
            )
        } ?: returnHome()

        AppScreen.OfferSent -> selectedRequest?.let { request ->
            BackHandler { returnHome() }
            OfferSentScreen(
                requesterName = request.personName,
                onBackToHome = { returnHome() },
            )
        } ?: returnHome()
    }
}

@Composable
fun PaywallScreen(modifier: Modifier = Modifier) {
    var showPaywall by remember { mutableStateOf(true) }
    if (showPaywall) {
        Box(modifier = modifier) {
            Paywall(
                options = PaywallOptions
                    .Builder(dismissRequest = { showPaywall = false })
                    .setShouldDisplayDismissButton(true)
                    .build(),
            )
        }
    }
}
