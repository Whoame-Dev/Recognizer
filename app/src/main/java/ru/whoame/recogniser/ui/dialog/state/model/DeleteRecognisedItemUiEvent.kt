package ru.whoame.recogniser.ui.dialog.state.model

sealed interface DeleteRecognisedItemUiEvent {

    data object Delete : DeleteRecognisedItemUiEvent

}
