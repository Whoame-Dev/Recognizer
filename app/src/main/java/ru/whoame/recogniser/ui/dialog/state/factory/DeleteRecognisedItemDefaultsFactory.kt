package ru.whoame.recogniser.ui.dialog.state.factory

import org.koin.core.annotation.Provided
import ru.whoame.recogniser.model.RecognisedObject
import ru.whoame.recogniser.ui.dialog.state.model.DeleteRecognisedItemDomainEvent
import ru.whoame.recogniser.ui.dialog.state.model.DeleteRecognisedItemState
import ru.whoame.state_machine.contract.BaseDefaultsFactory

class DeleteRecognisedItemDefaultsFactory(
    @Provided private val item: RecognisedObject,
) : BaseDefaultsFactory<DeleteRecognisedItemState, DeleteRecognisedItemDomainEvent> {

    override fun state(): DeleteRecognisedItemState = DeleteRecognisedItemState(item)

}
