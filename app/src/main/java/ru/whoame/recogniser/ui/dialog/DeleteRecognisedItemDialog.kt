package ru.whoame.recogniser.ui.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.whoame.recogniser.R
import ru.whoame.recogniser.model.RecognisedObject
import ru.whoame.recogniser.ui.DarkLightPreviews
import ru.whoame.recogniser.ui.theme.RecogniserTheme

@Composable
fun DeleteRecognisedItemDialog(
    item: RecognisedObject,
    onDismissRequest: () -> Unit,
    viewModel: DeleteRecognisedItemViewModel = koinViewModel { parametersOf(item) },
) {
    val state by viewModel.uiStateFlow.collectAsStateWithLifecycle()

    DeleteRecognisedItemDialogStateless(
        name = state.itemName,
        isLoading = state.isLoading,
        onConfirmRequest = { viewModel.confirm() },
        onDismissRequest = onDismissRequest,
    )
}

@Composable
private fun DeleteRecognisedItemDialogStateless(
    name: String,
    isLoading: Boolean,
    onConfirmRequest: () -> Unit,
    onDismissRequest: () -> Unit,
) = AlertDialog(
    title = {
        Text(
            text = stringResource(R.string.delete_recognised_item_dialog_title),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
    },
    text = {
        Text(stringResource(R.string.delete_recognised_item_dialog_text, name))
    },
    onDismissRequest = {
        if (!isLoading) onDismissRequest.invoke()
    },
    dismissButton = {
        if (!isLoading) {
            Text(
                text = stringResource(R.string.action_cancel),
                modifier = Modifier.clickable(onClick = onDismissRequest),
            )
        }
    },
    confirmButton = {
        if (!isLoading) {
            Text(
                text = stringResource(R.string.action_delete),
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.clickable(onClick = onConfirmRequest),
            )
        } else {
            CircularProgressIndicator()
        }
    },
)

@DarkLightPreviews
@Composable
private fun Preview() = RecogniserTheme {
    DeleteRecognisedItemDialogStateless("One", false, {}, {})
}

@DarkLightPreviews
@Composable
private fun LoadingPreview() = RecogniserTheme {
    DeleteRecognisedItemDialogStateless("One", true, {}, {})
}
