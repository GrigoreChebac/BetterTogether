package better.together.benefits.forever

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import better.together.benefits.forever.ui.home.HomeScreen
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
                HomeScreen()
            }
        }
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
