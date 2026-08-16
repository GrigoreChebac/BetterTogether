package better.together.benefits.forever.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import better.together.benefits.forever.ui.theme.BetterTogetherTheme

data class BarterRequest(
    val id: String = java.util.UUID.randomUUID().toString(),
    val personName: String,
    val need: String,
    val offer: String,
    val description: String = "",
)

val initialBarterRequests = listOf(
    BarterRequest(
        personName = "Alex",
        need = "Help with a logo",
        offer = "English lessons",
    ),
    BarterRequest(
        personName = "Maria",
        need = "Android development help",
        offer = "Photography",
    ),
    BarterRequest(
        personName = "Daniel",
        need = "Help improving his German",
        offer = "Personal training",
    ),
)

@Composable
fun HomeScreen(
    requests: List<BarterRequest> = initialBarterRequests,
    onAddRequest: () -> Unit = {},
    onViewBarter: (BarterRequest) -> Unit = {},
) {
    var selectedDestination by remember { mutableIntStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = onAddRequest) {
                Text(
                    text = "+",
                    style = MaterialTheme.typography.headlineMedium,
                )
            }
        },
        bottomBar = {
            NavigationBar {
                listOf("Home", "Exchanges", "Profile").forEachIndexed { index, label ->
                    NavigationBarItem(
                        selected = selectedDestination == index,
                        onClick = { selectedDestination = index },
                        icon = { Text(text = label.take(1)) },
                        label = { Text(text = label) },
                    )
                }
            }
        },
    ) { innerPadding ->
        HomeContent(
            requests = requests,
            onViewBarter = onViewBarter,
            contentPadding = innerPadding,
        )
    }
}

@Composable
private fun HomeContent(
    requests: List<BarterRequest>,
    onViewBarter: (BarterRequest) -> Unit,
    contentPadding: PaddingValues,
) {
    var searchQuery by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 20.dp,
            top = contentPadding.calculateTopPadding() + 24.dp,
            end = 20.dp,
            bottom = contentPadding.calculateBottomPadding() + 24.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Text(
                text = "BetterTogether",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "Find what you need. Offer what you can.",
                modifier = Modifier.padding(top = 4.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search skills or services") },
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Requests",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
            )
        }

        items(requests) { request ->
            BarterRequestCard(
                request = request,
                onViewBarter = { onViewBarter(request) },
            )
        }
    }
}

@Composable
private fun BarterRequestCard(
    request: BarterRequest,
    onViewBarter: () -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = request.personName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.height(12.dp))
            RequestDetail(label = "Needs", value = request.need)
            Spacer(modifier = Modifier.height(8.dp))
            RequestDetail(label = "Offers", value = request.offer)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onViewBarter) {
                Text("View barter")
            }
        }
    }
}

@Composable
private fun RequestDetail(label: String, value: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "$label:",
            fontWeight = FontWeight.SemiBold,
        )
        Text(text = value)
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    BetterTogetherTheme(dynamicColor = false) {
        HomeScreen()
    }
}
