package better.together.benefits.forever.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import better.together.benefits.forever.ui.components.BetterTogetherBottomBar
import better.together.benefits.forever.ui.theme.BetterTogetherTheme

data class LocalProfile(
    val displayName: String,
    val offeredSkills: List<String>,
    val wantedSkills: List<String>,
)

data class ProfileStatistics(
    val requestsCreated: Int,
    val offersSent: Int,
    val exchangesAccepted: Int,
)

val currentLocalProfile = LocalProfile(
    displayName = "You",
    offeredSkills = listOf("Android development", "English practice"),
    wantedSkills = listOf("German practice", "Design help"),
)

@Composable
fun ProfileScreen(
    profile: LocalProfile,
    statistics: ProfileStatistics,
    hasProAccess: Boolean?,
    onExplorePro: () -> Unit,
    onOpenHome: () -> Unit,
    onOpenExchanges: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BetterTogetherBottomBar(
                selectedDestination = 2,
                onHome = onOpenHome,
                onExchanges = onOpenExchanges,
                onProfile = {},
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                start = 20.dp,
                top = innerPadding.calculateTopPadding() + 24.dp,
                end = 20.dp,
                bottom = innerPadding.calculateBottomPadding() + 24.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            item {
                Text(
                    text = "Profile",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                )
                Row(
                    modifier = Modifier.padding(top = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Surface(
                        shape = MaterialTheme.shapes.extraLarge,
                        color = MaterialTheme.colorScheme.primaryContainer,
                    ) {
                        Text(
                            text = profile.displayName.take(1),
                            modifier = Modifier.padding(horizontal = 22.dp, vertical = 16.dp),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    Column {
                        Text(
                            text = profile.displayName,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(
                            text = "BetterTogether member",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            item {
                ProfileCard(title = "About you") {
                    SkillList("Can offer", profile.offeredSkills)
                    Spacer(modifier = Modifier.height(16.dp))
                    SkillList("Looking for", profile.wantedSkills)
                }
            }

            item {
                ProfileCard(title = "Activity") {
                    StatisticRow("Requests created", statistics.requestsCreated)
                    StatisticRow("Offers sent", statistics.offersSent)
                    StatisticRow("Exchanges accepted", statistics.exchangesAccepted)
                }
            }

            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "BetterTogether Pro",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = "Get more from every exchange",
                            modifier = Modifier.padding(top = 4.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            text = when (hasProAccess) {
                                true -> "BetterTogether Pro"
                                false -> "Free plan"
                                null -> "Checking plan…"
                            },
                            modifier = Modifier.padding(top = 16.dp),
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Button(
                            onClick = onExplorePro,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                        ) {
                            Text("Explore Pro")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileCard(title: String, content: @Composable () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.height(16.dp))
            content()
        }
    }
}

@Composable
private fun SkillList(label: String, skills: List<String>) {
    Text(label, fontWeight = FontWeight.SemiBold)
    skills.forEach { skill ->
        Text(text = "• $skill", modifier = Modifier.padding(top = 4.dp))
    }
}

@Composable
private fun StatisticRow(label: String, value: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(label)
        Text(value.toString(), fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfilePreview() {
    BetterTogetherTheme(dynamicColor = false) {
        ProfileScreen(
            profile = currentLocalProfile,
            statistics = ProfileStatistics(1, 2, 1),
            hasProAccess = false,
            onExplorePro = {},
            onOpenHome = {},
            onOpenExchanges = {},
        )
    }
}
