package better.together.benefits.forever.ui.exchanges

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import better.together.benefits.forever.ui.components.BetterTogetherBottomBar
import better.together.benefits.forever.ui.offer.BarterOffer
import better.together.benefits.forever.ui.offer.OfferStatus
import better.together.benefits.forever.ui.offer.initialReceivedOffers
import better.together.benefits.forever.ui.theme.BetterTogetherTheme

@Composable
fun ExchangesScreen(
    receivedOffers: List<BarterOffer>,
    sentOffers: List<BarterOffer>,
    onUpdateReceivedStatus: (String, OfferStatus) -> Unit,
    onOpenHome: () -> Unit,
    onOpenProfile: () -> Unit,
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val displayedOffers = if (selectedTab == 0) receivedOffers else sentOffers

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BetterTogetherBottomBar(
                selectedDestination = 1,
                onHome = onOpenHome,
                onExchanges = {},
                onProfile = onOpenProfile,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            Text(
                text = "Exchanges",
                modifier = Modifier.padding(start = 20.dp, top = 24.dp, end = 20.dp, bottom = 16.dp),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
            )
            TabRow(selectedTabIndex = selectedTab) {
                listOf("Received", "Sent").forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) },
                    )
                }
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                if (displayedOffers.isEmpty()) {
                    item {
                        Text(
                            text = if (selectedTab == 0) "No received offers yet" else "No sent offers yet",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                items(displayedOffers, key = { it.id }) { offer ->
                    OfferCard(
                        offer = offer,
                        isReceived = selectedTab == 0,
                        onAccept = { onUpdateReceivedStatus(offer.id, OfferStatus.Accepted) },
                        onDecline = { onUpdateReceivedStatus(offer.id, OfferStatus.Declined) },
                    )
                }
            }
        }
    }
}

@Composable
private fun OfferCard(
    offer: BarterOffer,
    isReceived: Boolean,
    onAccept: () -> Unit,
    onDecline: () -> Unit,
) {
    val cardColor = when (offer.status) {
        OfferStatus.Accepted -> MaterialTheme.colorScheme.tertiaryContainer
        OfferStatus.Declined -> MaterialTheme.colorScheme.surfaceVariant
        OfferStatus.Pending -> MaterialTheme.colorScheme.surface
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardColor),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = if (isReceived) offer.senderName else offer.requesterName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.height(12.dp))
            OfferDetail(
                label = if (isReceived) "They offer" else "You offer",
                value = offer.offering,
            )
            Spacer(modifier = Modifier.height(8.dp))
            OfferDetail(
                label = if (isReceived) "They want" else "You want",
                value = offer.wantsInReturn,
            )
            Spacer(modifier = Modifier.height(12.dp))
            AssistChip(
                onClick = {},
                label = { Text(offer.status.name) },
            )
            if (offer.status == OfferStatus.Accepted) {
                Text(
                    text = "It's a match!",
                    modifier = Modifier.padding(top = 8.dp),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                )
            }
            if (isReceived && offer.status == OfferStatus.Pending) {
                Row(
                    modifier = Modifier.padding(top = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Button(onClick = onAccept) { Text("Accept") }
                    TextButton(onClick = onDecline) { Text("Decline") }
                }
            }
        }
    }
}

@Composable
private fun OfferDetail(label: String, value: String) {
    Text(label, fontWeight = FontWeight.SemiBold)
    Text(value)
}

@Preview(showBackground = true)
@Composable
private fun ExchangesPreview() {
    BetterTogetherTheme(dynamicColor = false) {
        ExchangesScreen(
            receivedOffers = initialReceivedOffers,
            sentOffers = emptyList(),
            onUpdateReceivedStatus = { _, _ -> },
            onOpenHome = {},
            onOpenProfile = {},
        )
    }
}
