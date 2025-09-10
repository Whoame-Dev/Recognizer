package ru.whoame.recogniser.ui.screen.recognisedlist.state.handler

import kotlinx.coroutines.flow.Flow
import ru.whoame.recogniser.ui.screen.recognisedlist.state.model.*
import ru.whoame.recogniser.ui.viewmodel.handler.BaseUiEventHandler
import ru.whoame.recogniser.utils.flowOf

class ListScreenUiEventHandler :
    BaseUiEventHandler<ListScreenState, ListScreenSideEffect, ListScreenUiEvent, ListScreenDomainEvent>() {

    override fun handleEvent(event: ListScreenUiEvent): Flow<*> = when (event) {
        is ListScreenUiEvent.ItemClick -> flowOf { itemClick(event.id) }
        is ListScreenUiEvent.DeleteClick -> flowOf { deleteClick(event.id) }
    }

    private suspend fun itemClick(id: Long?) = reduceState { state ->
        state.copy(selectedItemId = id)
    }

    private suspend fun deleteClick(id: Long) = reduceSideEffect {
        ListScreenSideEffect.DeleteItem(id)
    }

}
