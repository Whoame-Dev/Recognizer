package ru.whoame.recogniser.ui.dialog.state.handler

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.whoame.recogniser.data.repository.RecognisedObjectRepository
import ru.whoame.recogniser.ui.dialog.state.model.DeleteRecognisedItemDomainEvent
import ru.whoame.recogniser.ui.dialog.state.model.DeleteRecognisedItemState
import ru.whoame.recogniser.utils.Resource
import ru.whoame.state_machine.handler.BaseDomainEventHandler

class DeleteRecognisedItemDomainEventHandler(
    private val repository: RecognisedObjectRepository,
) : BaseDomainEventHandler<DeleteRecognisedItemState, Nothing, DeleteRecognisedItemDomainEvent>() {

    override fun handleEvent(event: DeleteRecognisedItemDomainEvent): Flow<*> = when (event) {
        is DeleteRecognisedItemDomainEvent.Delete -> delete()
    }

    private fun delete() = repository.delete(state.item.id)
        .map { resource ->
            if (resource !is Resource.Data) {
                reduceState { state ->
                    state.copy(deleteRequest = resource)
                }
            }
        }

}
