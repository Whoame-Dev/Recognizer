package ru.whoame.recogniser.ui.screen.recognisedlist.state.handler

import kotlinx.coroutines.flow.Flow
import ru.whoame.recogniser.ui.screen.recognisedlist.state.model.ListScreenDomainEvent
import ru.whoame.recogniser.ui.screen.recognisedlist.state.model.ListScreenState
import ru.whoame.recogniser.ui.screen.recognisedlist.state.model.ListScreenUiEvent
import ru.whoame.recogniser.utils.flowOf
import ru.whoame.recogniser.utils.valueOrNull
import ru.whoame.state_machine.handler.BaseUiEventHandler

class ListScreenUiEventHandler :
  BaseUiEventHandler<ListScreenState, Nothing, ListScreenUiEvent, ListScreenDomainEvent>() {

  override fun handleEvent(event: ListScreenUiEvent): Flow<*> = when (event) {
    is ListScreenUiEvent.ItemClick -> flowOf { itemClick(event.id) }
    is ListScreenUiEvent.DeleteClick -> flowOf { deleteClick(event.id) }
    is ListScreenUiEvent.DismissDeleteDialog -> flowOf { dismissDeleteDialog() }
  }

  private suspend fun itemClick(id: Long?) = reduceState { state ->
    state.copy(selectedItemId = id)
  }

  private suspend fun deleteClick(id: Long) = state.list.valueOrNull()
    ?.find { it.id == id }
    ?.let { item ->
      reduceState {
        state.copy(itemToDelete = item)
      }
    }

  private suspend fun dismissDeleteDialog() = reduceState {
    state.copy(itemToDelete = null)
  }

}
