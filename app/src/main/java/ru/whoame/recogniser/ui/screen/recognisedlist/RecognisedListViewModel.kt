package ru.whoame.recogniser.ui.screen.recognisedlist

import ru.whoame.recogniser.ui.screen.recognisedlist.state.factory.ListScreenDefaultsFactory
import ru.whoame.recogniser.ui.screen.recognisedlist.state.factory.ListScreenStateFactory
import ru.whoame.recogniser.ui.screen.recognisedlist.state.handler.ListScreenDomainEventHandler
import ru.whoame.recogniser.ui.screen.recognisedlist.state.handler.ListScreenUiEventHandler
import ru.whoame.recogniser.ui.screen.recognisedlist.state.model.*
import ru.whoame.recogniser.ui.viewmodel.StateMachineViewModel

class RecognisedListViewModel(
    defaultsFactory: ListScreenDefaultsFactory,
    stateFactory: ListScreenStateFactory,
    uiEventHandler: ListScreenUiEventHandler,
    domainEventHandler: ListScreenDomainEventHandler,
) : StateMachineViewModel<ListScreenUiState, ListScreenState, ListScreenSideEffect, ListScreenUiEvent, ListScreenDomainEvent>(
    defaultsFactory = defaultsFactory,
    stateFactory = stateFactory,
    uiEventHandler = uiEventHandler,
    domainEventHandler = domainEventHandler,
) {

    fun itemClick(id: Long?) = launchEvent {
        ListScreenUiEvent.ItemClick(id)
    }

    fun deleteClick(id: Long) = launchEvent {
        ListScreenUiEvent.DeleteClick(id)
    }

}
