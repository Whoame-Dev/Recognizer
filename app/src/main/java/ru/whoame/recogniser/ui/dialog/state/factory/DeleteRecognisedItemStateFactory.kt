package ru.whoame.recogniser.ui.dialog.state.factory

import ru.whoame.recogniser.R
import ru.whoame.recogniser.ui.dialog.state.model.DeleteRecognisedItemState
import ru.whoame.recogniser.ui.dialog.state.model.DeleteRecognisedItemUiState
import ru.whoame.recogniser.utils.Resource
import ru.whoame.recogniser.utils.toStringArgs
import ru.whoame.state_machine.contract.BaseStateFactory

class DeleteRecognisedItemStateFactory :
  BaseStateFactory<DeleteRecognisedItemState, DeleteRecognisedItemUiState> {

  override fun convert(state: DeleteRecognisedItemState): DeleteRecognisedItemUiState = when (state.deleteRequest) {
    is Resource.Loading -> DeleteRecognisedItemUiState(
      icon = null,
      title = R.string.delete_recognised_item_dialog_title.toStringArgs(),
      message = R.string.delete_recognised_item_dialog_text.toStringArgs(state.item.title),
      dismissText = null,
      isConfirmVisible = false,
      isLoading = true,
      isItemDeleted = false,
    )

    is Resource.Data -> DeleteRecognisedItemUiState(
      icon = DeleteRecognisedItemUiState.IconModel.Done(),
      title = R.string.delete_recognised_item_dialog_success_title.toStringArgs(),
      message = null,
      dismissText = R.string.action_done.toStringArgs(),
      isConfirmVisible = false,
      isLoading = false,
      isItemDeleted = true,
    )

    is Resource.Error -> DeleteRecognisedItemUiState(
      icon = DeleteRecognisedItemUiState.IconModel.Error(),
      title = R.string.delete_recognised_item_dialog_error_title.toStringArgs(),
      message = R.string.default_error.toStringArgs(),
      dismissText = R.string.action_cancel.toStringArgs(),
      isConfirmVisible = false,
      isLoading = false,
      isItemDeleted = false,
    )

    null -> DeleteRecognisedItemUiState(
      icon = null,
      title = R.string.delete_recognised_item_dialog_title.toStringArgs(),
      message = R.string.delete_recognised_item_dialog_text.toStringArgs(state.item.title),
      dismissText = R.string.action_cancel.toStringArgs(),
      isConfirmVisible = true,
      isLoading = false,
      isItemDeleted = false,
    )
  }

}
