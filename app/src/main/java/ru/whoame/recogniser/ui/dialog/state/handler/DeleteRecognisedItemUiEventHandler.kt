package ru.whoame.recogniser.ui.dialog.state.handler

import kotlinx.coroutines.flow.Flow
import ru.whoame.recogniser.ui.dialog.state.model.DeleteRecognisedItemDomainEvent
import ru.whoame.recogniser.ui.dialog.state.model.DeleteRecognisedItemState
import ru.whoame.recogniser.ui.dialog.state.model.DeleteRecognisedItemUiEvent
import ru.whoame.recogniser.utils.flowOf
import ru.whoame.state_machine.handler.BaseUiEventHandler

class DeleteRecognisedItemUiEventHandler :
    BaseUiEventHandler<DeleteRecognisedItemState, Nothing, DeleteRecognisedItemUiEvent, DeleteRecognisedItemDomainEvent>() {

    override fun handleEvent(event: DeleteRecognisedItemUiEvent): Flow<*> = when (event) {
        is DeleteRecognisedItemUiEvent.Delete -> flowOf {
            reduceEvent {
                DeleteRecognisedItemDomainEvent.Delete
            }
        }
    }

}
