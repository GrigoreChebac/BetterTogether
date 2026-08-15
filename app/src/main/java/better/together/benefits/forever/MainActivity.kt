package better.together.benefits.forever

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import better.together.benefits.forever.ui.theme.BetterTogetherTheme
import com.revenuecat.purchases.LogLevel
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesConfiguration
import com.revenuecat.purchases.getCustomerInfoWith
import androidx.compose.runtime.*
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
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PaywallScreen(modifier = Modifier.padding(innerPadding))
                }
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
