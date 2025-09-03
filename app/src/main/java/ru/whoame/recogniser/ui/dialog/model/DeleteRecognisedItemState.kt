package ru.whoame.recogniser.ui.dialog.model

import ru.whoame.recogniser.model.RecognisedObject
import ru.whoame.recogniser.utils.Resource

data class DeleteRecognisedItemState(
    val item: RecognisedObject,
    val deleteRequest: Resource<Unit>? = null,
)
