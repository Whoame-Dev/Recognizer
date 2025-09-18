package ru.whoame.recogniser.ui.screen.recognisedlist

import ru.whoame.recogniser.ui.base.StateMachineViewModel
import ru.whoame.recogniser.ui.screen.recognisedlist.state.factory.ListScreenDefaultsFactory
import ru.whoame.recogniser.ui.screen.recognisedlist.state.factory.ListScreenStateFactory
import ru.whoame.recogniser.ui.screen.recognisedlist.state.handler.ListScreenDomainEventHandler
import ru.whoame.recogniser.ui.screen.recognisedlist.state.handler.ListScreenUiEventHandler
import ru.whoame.recogniser.ui.screen.recognisedlist.state.model.ListScreenDomainEvent
import ru.whoame.recogniser.ui.screen.recognisedlist.state.model.ListScreenState
import ru.whoame.recogniser.ui.screen.recognisedlist.state.model.ListScreenUiEvent
import ru.whoame.recogniser.ui.screen.recognisedlist.state.model.ListScreenUiState

class RecognisedListViewModel(
  defaultsFactory: ListScreenDefaultsFactory,
  stateFactory: ListScreenStateFactory,
  uiEventHandler: ListScreenUiEventHandler,
  domainEventHandler: ListScreenDomainEventHandler,
) : StateMachineViewModel<ListScreenUiState, ListScreenState, Nothing, ListScreenUiEvent, ListScreenDomainEvent>(
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

  fun dismissDeleteDialog() = launchEvent {
    ListScreenUiEvent.DismissDeleteDialog
  }

}
