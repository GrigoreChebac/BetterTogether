package better.together.benefits.forever.ui.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import better.together.benefits.forever.ui.home.BarterRequest
import better.together.benefits.forever.ui.theme.BetterTogetherTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRequestScreen(
    onBack: () -> Unit,
    onPublish: (BarterRequest) -> Unit,
) {
    var need by remember { mutableStateOf("") }
    var offer by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var showValidationErrors by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val needIsInvalid = showValidationErrors && need.isBlank()
    val offerIsInvalid = showValidationErrors && offer.isBlank()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Create request") },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("Back")
                    }
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
            OutlinedTextField(
                value = need,
                onValueChange = { need = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("What do you need?") },
                placeholder = { Text("e.g. Help designing a logo") },
                singleLine = true,
                isError = needIsInvalid,
                supportingText = if (needIsInvalid) {
                    { Text("Please enter what you need") }
                } else {
                    null
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) },
                ),
            )

            OutlinedTextField(
                value = offer,
                onValueChange = { offer = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("What can you offer?") },
                placeholder = { Text("e.g. English lessons") },
                singleLine = true,
                isError = offerIsInvalid,
                supportingText = if (offerIsInvalid) {
                    { Text("Please enter what you can offer") }
                } else {
                    null
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) },
                ),
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Description") },
                minLines = 4,
                maxLines = 8,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default),
            )

            Button(
                onClick = {
                    showValidationErrors = true
                    if (need.isNotBlank() && offer.isNotBlank()) {
                        focusManager.clearFocus()
                        onPublish(
                            BarterRequest(
                                personName = "You",
                                need = need.trim(),
                                offer = offer.trim(),
                                description = description.trim(),
                            ),
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Publish request")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CreateRequestScreenPreview() {
    BetterTogetherTheme(dynamicColor = false) {
        CreateRequestScreen(onBack = {}, onPublish = {})
    }
}
