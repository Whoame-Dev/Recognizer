package ru.whoame.recogniser.ui.screen.recognisedlist.state.handler

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.whoame.recogniser.data.repository.RecognisedObjectRepository
import ru.whoame.recogniser.ui.screen.recognisedlist.state.model.ListScreenDomainEvent
import ru.whoame.recogniser.ui.screen.recognisedlist.state.model.ListScreenSideEffect
import ru.whoame.recogniser.ui.screen.recognisedlist.state.model.ListScreenState
import ru.whoame.recogniser.ui.viewmodel.handler.BaseDomainEventHandler

class ListScreenDomainEventHandler(
    private val repository: RecognisedObjectRepository,
) : BaseDomainEventHandler<ListScreenState, ListScreenSideEffect, ListScreenDomainEvent>() {

    override fun handleEvent(event: ListScreenDomainEvent): Flow<*> = when (event) {
        is ListScreenDomainEvent.GetRecognisedList -> getRecognisedList()
    }

    private fun getRecognisedList(): Flow<*> = repository.getAll()
        .map { resource ->
            reduceState { state ->
                state.copy(list = resource)
            }
        }

}
