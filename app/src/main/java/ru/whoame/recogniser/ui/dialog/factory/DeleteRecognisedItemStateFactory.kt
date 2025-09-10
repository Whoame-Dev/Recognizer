package ru.whoame.recogniser.ui.dialog.factory

import ru.whoame.recogniser.ui.dialog.model.DeleteRecognisedItemState
import ru.whoame.recogniser.ui.dialog.model.DeleteRecognisedItemUiState
import ru.whoame.recogniser.ui.viewmodel.contracts.BaseStateFactory
import ru.whoame.recogniser.utils.isLoading

class DeleteRecognisedItemStateFactory : BaseStateFactory<DeleteRecognisedItemState, DeleteRecognisedItemUiState> {

    override fun convert(state: DeleteRecognisedItemState) = DeleteRecognisedItemUiState(
        itemName = state.item.title,
        itemId = state.item.id,
        isLoading = state.deleteRequest?.isLoading() ?: false,
    )

}
