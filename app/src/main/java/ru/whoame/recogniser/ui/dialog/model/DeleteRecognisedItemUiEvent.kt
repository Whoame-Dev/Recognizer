package ru.whoame.recogniser.ui.dialog.model

sealed interface DeleteRecognisedItemUiEvent {

    data class Delete(val id: Long) : DeleteRecognisedItemUiEvent

}
