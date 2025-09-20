package ru.whoame.recogniser.ui.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.whoame.recogniser.R
import ru.whoame.recogniser.model.RecognisedObject
import ru.whoame.recogniser.ui.DarkLightPreviews
import ru.whoame.recogniser.ui.dialog.state.model.DeleteRecognisedItemUiState
import ru.whoame.recogniser.ui.theme.RecogniserTheme
import ru.whoame.recogniser.utils.format

@Composable
fun DeleteRecognisedItemDialog(
  item: RecognisedObject,
  onDismissRequest: (Boolean) -> Unit,
  viewModel: DeleteRecognisedItemViewModel = koinViewModel { parametersOf(item) },
) {
  val state by viewModel.uiStateFlow.collectAsStateWithLifecycle()

  DeleteRecognisedItemDialogStateless(
    icon = state.icon,
    title = state.title.format(),
    message = state.message?.format(),
    dismissButtonText = state.dismissText?.format(),
    isConfirmVisible = state.isConfirmVisible,
    isLoading = state.isLoading,
    onConfirmRequest = viewModel::confirm,
    onDismissRequest = { onDismissRequest.invoke(state.isItemDeleted) },
  )
}

@Composable
private fun DeleteRecognisedItemDialogStateless(
  icon: DeleteRecognisedItemUiState.IconModel?,
  title: String,
  message: String?,
  dismissButtonText: String?,
  isConfirmVisible: Boolean,
  isLoading: Boolean,
  onConfirmRequest: () -> Unit,
  onDismissRequest: () -> Unit,
) = AlertDialog(
  icon = {
    icon?.let { value ->
      Icon(
        imageVector = value.vector,
        contentDescription = null,
        tint = value.tint,
        modifier = Modifier.size(dimensionResource(R.dimen.delete_item_dialog_icon_size)),
      )
    }
  },
  title = {
    Text(
      text = title,
      textAlign = TextAlign.Center,
      modifier = Modifier.fillMaxWidth(),
    )
  },
  text = {
    message?.let { value ->
      Text(value)
    }
  },
  onDismissRequest = {
    if (!isLoading) onDismissRequest.invoke()
  },
  dismissButton = {
    dismissButtonText?.let { value ->
      Text(
        text = value,
        modifier = Modifier.clickable(onClick = onDismissRequest),
      )
    }
  },
  confirmButton = {
    if (isLoading) {
      CircularProgressIndicator()
    } else if (isConfirmVisible) {
      Text(
        text = stringResource(R.string.action_delete),
        color = MaterialTheme.colorScheme.error,
        modifier = Modifier.clickable(onClick = onConfirmRequest),
      )
    }
  },
)

@DarkLightPreviews
@Composable
private fun Preview() = RecogniserTheme {
  DeleteRecognisedItemDialogStateless(
    icon = null,
    title = "Title",
    message = "Message",
    dismissButtonText = "Cancel",
    isConfirmVisible = true,
    isLoading = false,
    onConfirmRequest = {},
    onDismissRequest = {},
  )
}

@DarkLightPreviews
@Composable
private fun LoadingPreview() = RecogniserTheme {
  DeleteRecognisedItemDialogStateless(
    icon = null,
    title = "Title",
    message = "Message",
    dismissButtonText = null,
    isConfirmVisible = true,
    isLoading = false,
    onConfirmRequest = {},
    onDismissRequest = {},
  )
}

@DarkLightPreviews
@Composable
private fun ErrorPreview() = RecogniserTheme {
  DeleteRecognisedItemDialogStateless(
    icon = DeleteRecognisedItemUiState.IconModel.Error(),
    title = "Title",
    message = "Message",
    dismissButtonText = "Done",
    isConfirmVisible = false,
    isLoading = false,
    onConfirmRequest = {},
    onDismissRequest = {},
  )
}

@DarkLightPreviews
@Composable
private fun DonePreview() = RecogniserTheme {
  DeleteRecognisedItemDialogStateless(
    icon = DeleteRecognisedItemUiState.IconModel.Done(),
    title = "Title",
    message = "Message",
    dismissButtonText = "Cancel",
    isConfirmVisible = false,
    isLoading = false,
    onConfirmRequest = {},
    onDismissRequest = {},
  )
}
