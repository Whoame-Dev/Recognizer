package ru.whoame.recogniser.ui.dialog

import ru.whoame.recogniser.ui.base.StateMachineViewModel
import ru.whoame.recogniser.ui.dialog.state.factory.DeleteRecognisedItemDefaultsFactory
import ru.whoame.recogniser.ui.dialog.state.factory.DeleteRecognisedItemStateFactory
import ru.whoame.recogniser.ui.dialog.state.handler.DeleteRecognisedItemDomainEventHandler
import ru.whoame.recogniser.ui.dialog.state.handler.DeleteRecognisedItemUiEventHandler
import ru.whoame.recogniser.ui.dialog.state.model.DeleteRecognisedItemDomainEvent
import ru.whoame.recogniser.ui.dialog.state.model.DeleteRecognisedItemState
import ru.whoame.recogniser.ui.dialog.state.model.DeleteRecognisedItemUiEvent
import ru.whoame.recogniser.ui.dialog.state.model.DeleteRecognisedItemUiState

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

    fun confirm() = launchEvent {
        DeleteRecognisedItemUiEvent.Delete
    }

}
