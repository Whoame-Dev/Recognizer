package ru.whoame.recogniser.ui.dialog.factory

import ru.whoame.recogniser.model.RecognisedObject
import ru.whoame.recogniser.ui.dialog.model.DeleteRecognisedItemDomainEvent
import ru.whoame.recogniser.ui.dialog.model.DeleteRecognisedItemState
import ru.whoame.state_machine.contract.BaseDefaultsFactory

class DeleteRecognisedItemDefaultsFactory(
    private val item: RecognisedObject,
) : BaseDefaultsFactory<DeleteRecognisedItemState, DeleteRecognisedItemDomainEvent> {

    override fun state(): DeleteRecognisedItemState = DeleteRecognisedItemState(item)

}
