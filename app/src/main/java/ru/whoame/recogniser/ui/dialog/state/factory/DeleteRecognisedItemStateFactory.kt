package ru.whoame.recogniser.ui.dialog.state.factory

import ru.whoame.recogniser.ui.dialog.state.model.DeleteRecognisedItemState
import ru.whoame.recogniser.ui.dialog.state.model.DeleteRecognisedItemUiState
import ru.whoame.recogniser.utils.isLoading
import ru.whoame.state_machine.contract.BaseStateFactory

class DeleteRecognisedItemStateFactory :
    BaseStateFactory<DeleteRecognisedItemState, DeleteRecognisedItemUiState> {

    override fun convert(state: DeleteRecognisedItemState) = DeleteRecognisedItemUiState(
        itemName = state.item.title,
        isLoading = state.deleteRequest?.isLoading() ?: false,
    )

}
