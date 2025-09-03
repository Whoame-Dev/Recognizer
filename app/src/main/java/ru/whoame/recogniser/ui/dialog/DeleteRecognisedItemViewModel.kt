package ru.whoame.recogniser.ui.dialog

import ru.whoame.recogniser.ui.dialog.factory.DeleteRecognisedItemDefaultsFactory
import ru.whoame.recogniser.ui.dialog.factory.DeleteRecognisedItemStateFactory
import ru.whoame.recogniser.ui.dialog.handler.DeleteRecognisedItemDomainEventHandler
import ru.whoame.recogniser.ui.dialog.handler.DeleteRecognisedItemUiEventHandler
import ru.whoame.recogniser.ui.dialog.model.*
import ru.whoame.recogniser.ui.viewmodel.StateMachineViewModel

class DeleteRecognisedItemViewModel(
    defaultsFactory: DeleteRecognisedItemDefaultsFactory,
    stateFactory: DeleteRecognisedItemStateFactory,
    uiEventHandler: DeleteRecognisedItemUiEventHandler,
    domainEventHandler: DeleteRecognisedItemDomainEventHandler,
) : StateMachineViewModel<DeleteRecognisedItemUiState, DeleteRecognisedItemState, Nothing, DeleteRecognisedItemUiEvent, DeleteRecognisedItemDomainEvent>(
    defaultsFactory = defaultsFactory,
    stateFactory = stateFactory,
    uiEventHandler = uiEventHandler,
    domainEventHandler = domainEventHandler,
) {

    fun delete(id: Long) = launchEvent {
        DeleteRecognisedItemUiEvent.Delete(id)
    }

}
