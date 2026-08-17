package better.together.benefits.forever.ui.offer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import better.together.benefits.forever.data.request.BarterRequest
import kotlinx.datetime.Instant
import better.together.benefits.forever.ui.theme.BetterTogetherTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MakeOfferScreen(
    request: BarterRequest,
    onBack: () -> Unit,
    onSendOffer: (BarterOffer) -> Unit,
) {
    var offering by remember { mutableStateOf("") }
    var wantsInReturn by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var showValidationErrors by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val offeringIsInvalid = showValidationErrors && offering.isBlank()
    val returnIsInvalid = showValidationErrors && wantsInReturn.isBlank()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Make an offer") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Back") }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Text(
                text = "Offer a barter to ${request.personName}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("They need", fontWeight = FontWeight.SemiBold)
                Text(request.need, style = MaterialTheme.typography.bodyLarge)
            }
            OfferTextField(
                value = offering,
                onValueChange = { offering = it },
                label = "What can you offer?",
                placeholder = "e.g. I can design your logo",
                isError = offeringIsInvalid,
                errorText = "Please enter what you can offer",
                onNext = { focusManager.moveFocus(FocusDirection.Down) },
            )
            OfferTextField(
                value = wantsInReturn,
                onValueChange = { wantsInReturn = it },
                label = "What would you like in return?",
                placeholder = "e.g. Two English lessons",
                isError = returnIsInvalid,
                errorText = "Please enter what you would like in return",
                onNext = { focusManager.moveFocus(FocusDirection.Down) },
            )
            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Message") },
                placeholder = { Text("Add a message") },
                minLines = 4,
                maxLines = 8,
            )
            Button(
                onClick = {
                    showValidationErrors = true
                    if (offering.isNotBlank() && wantsInReturn.isNotBlank()) {
                        focusManager.clearFocus()
                        onSendOffer(
                            BarterOffer(
                                requestId = request.id,
                                requesterName = request.personName,
                                senderName = "You",
                                offering = offering.trim(),
                                wantsInReturn = wantsInReturn.trim(),
                                message = message.trim(),
                            ),
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Send offer")
            }
        }
    }
}

@Composable
private fun OfferTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    isError: Boolean,
    errorText: String,
    onNext: () -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        singleLine = true,
        isError = isError,
        supportingText = if (isError) ({ Text(errorText) }) else null,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(onNext = { onNext() }),
    )
}

@Preview(showBackground = true)
@Composable
private fun MakeOfferPreview() {
    BetterTogetherTheme(dynamicColor = false) {
        MakeOfferScreen(
            request = BarterRequest("preview", "owner", "Alex", "Logo help", "English lessons", "", Instant.DISTANT_PAST),
            onBack = {},
            onSendOffer = {},
        )
    }
}
