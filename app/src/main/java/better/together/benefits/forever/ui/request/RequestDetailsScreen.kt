package better.together.benefits.forever.ui.request

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import better.together.benefits.forever.data.request.BarterRequest
import kotlinx.datetime.Instant
import better.together.benefits.forever.ui.theme.BetterTogetherTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestDetailsScreen(
    request: BarterRequest,
    currentUserId: String,
    onBack: () -> Unit,
    onMakeOffer: () -> Unit,
) {
    val isOwnRequest = request.ownerId == currentUserId
    val ownerName = if (isOwnRequest) "You" else request.ownerDisplayName

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Request details") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Back") }
                },
            )
        },
        bottomBar = {
            Column(modifier = Modifier.padding(20.dp)) {
                if (isOwnRequest) {
                    Text(
                        text = "This is your request",
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                } else {
                    Button(
                        onClick = onMakeOffer,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Make an offer")
                    }
                }
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = ownerName,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.padding(top = 4.dp))
            DetailSection(title = "Needs", value = request.need)
            if (request.description.isNotBlank()) {
                Text(
                    text = request.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Spacer(modifier = Modifier.padding(top = 8.dp))
            DetailSection(title = "Offers", value = request.offer)
        }
    }
}

@Composable
private fun DetailSection(title: String, value: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.SemiBold,
    )
    Text(text = value, style = MaterialTheme.typography.bodyLarge)
}

@Preview(showBackground = true)
@Composable
private fun RequestDetailsPreview() {
    BetterTogetherTheme(dynamicColor = false) {
        RequestDetailsScreen(
            request = BarterRequest("preview", "owner", "Alex", "Logo help", "English lessons", "", Instant.DISTANT_PAST),
            currentUserId = "current",
            onBack = {},
            onMakeOffer = {},
        )
    }
}
