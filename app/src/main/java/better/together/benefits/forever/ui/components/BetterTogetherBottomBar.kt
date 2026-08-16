package better.together.benefits.forever.ui.components

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun BetterTogetherBottomBar(
    selectedDestination: Int,
    onHome: () -> Unit,
    onExchanges: () -> Unit,
    onProfile: () -> Unit,
) {
    NavigationBar {
        listOf("Home", "Exchanges", "Profile").forEachIndexed { index, label ->
            NavigationBarItem(
                selected = selectedDestination == index,
                onClick = {
                    when (index) {
                        0 -> onHome()
                        1 -> onExchanges()
                        2 -> onProfile()
                    }
                },
                icon = { Text(text = label.take(1)) },
                label = { Text(text = label) },
            )
        }
    }
}
