package ru.whoame.recogniser.ui.screen.recognisedlist.state.handler

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.threeten.bp.LocalDateTime
import ru.whoame.recogniser.data.repository.RecognisedObjectRepository
import ru.whoame.recogniser.model.RecognisedObject
import ru.whoame.recogniser.ui.screen.recognisedlist.state.model.ListScreenDomainEvent
import ru.whoame.recogniser.ui.screen.recognisedlist.state.model.ListScreenState
import ru.whoame.recogniser.utils.mapResourceWithDelay
import ru.whoame.state_machine.handler.BaseDomainEventHandler

class ListScreenDomainEventHandler(
  private val repository: RecognisedObjectRepository,
) : BaseDomainEventHandler<ListScreenState, Nothing, ListScreenDomainEvent>() {

  override fun handleEvent(event: ListScreenDomainEvent): Flow<*> = when (event) {
    is ListScreenDomainEvent.GetRecognisedList -> getRecognisedList()
  }

  private fun getRecognisedList(): Flow<*> = repository.getAll()
    // FixMe Mock for dev
    .mapResourceWithDelay(5000) { list ->
      list.ifEmpty {
        listOf(
          RecognisedObject(0, "One", LocalDateTime.now()),
          RecognisedObject(1, "Two", LocalDateTime.now().minusDays(1)),
          RecognisedObject(2, "Three", LocalDateTime.now().minusDays(2)),
          RecognisedObject(3, "Four", LocalDateTime.now().minusDays(3)),
          RecognisedObject(4, "Five", LocalDateTime.now().minusDays(4)),
        )
      }
    }
    .map { resource ->
      reduceState { state ->
        state.copy(list = resource)
      }
    }

}
