package ru.whoame.recogniser.ui.dialog.handler

import kotlinx.coroutines.flow.Flow
import ru.whoame.recogniser.ui.dialog.model.DeleteRecognisedItemDomainEvent
import ru.whoame.recogniser.ui.dialog.model.DeleteRecognisedItemState
import ru.whoame.recogniser.ui.dialog.model.DeleteRecognisedItemUiEvent
import ru.whoame.recogniser.ui.viewmodel.handler.BaseUiEventHandler
import ru.whoame.recogniser.utils.flowOf

class DeleteRecognisedItemUiEventHandler :
    BaseUiEventHandler<DeleteRecognisedItemState, Nothing, DeleteRecognisedItemUiEvent, DeleteRecognisedItemDomainEvent>() {

    override fun handleEvent(event: DeleteRecognisedItemUiEvent): Flow<*> = when (event) {
        is DeleteRecognisedItemUiEvent.Delete -> flowOf {
            reduceEvent {
                DeleteRecognisedItemDomainEvent.Delete(event.id)
            }
        }
    }

}
