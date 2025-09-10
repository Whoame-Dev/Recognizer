package ru.whoame.recogniser.ui.dialog.handler

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.whoame.recogniser.data.repository.RecognisedObjectRepository
import ru.whoame.recogniser.ui.dialog.model.DeleteRecognisedItemDomainEvent
import ru.whoame.recogniser.ui.dialog.model.DeleteRecognisedItemState
import ru.whoame.recogniser.ui.viewmodel.handler.BaseDomainEventHandler
import ru.whoame.recogniser.utils.Resource

class DeleteRecognisedItemDomainEventHandler(
    private val repository: RecognisedObjectRepository,
) : BaseDomainEventHandler<DeleteRecognisedItemState, Nothing, DeleteRecognisedItemDomainEvent>() {

    override fun handleEvent(event: DeleteRecognisedItemDomainEvent): Flow<*> = when (event) {
        is DeleteRecognisedItemDomainEvent.Delete -> delete(event.id)
    }

    private fun delete(id: Long) = repository.delete(id)
        .map { resource ->
            if (resource !is Resource.Data) {
                reduceState { state ->
                    state.copy(deleteRequest = resource)
                }
            }
        }

}
