package better.together.benefits.forever.ui.offer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import better.together.benefits.forever.ui.theme.BetterTogetherTheme

@Composable
fun OfferSentScreen(
    requesterName: String,
    onBackToHome: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Offer sent",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "Your barter offer was sent to $requesterName.",
            modifier = Modifier.padding(top = 12.dp, bottom = 28.dp),
            style = MaterialTheme.typography.bodyLarge,
        )
        Button(
            onClick = onBackToHome,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Back to Home")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OfferSentPreview() {
    BetterTogetherTheme(dynamicColor = false) {
        OfferSentScreen(requesterName = "Alex", onBackToHome = {})
    }
}
