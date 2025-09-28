package ru.whoame.recogniser.ui.dialog.state.model

sealed interface DeleteRecognisedItemDomainEvent {

    data object Delete : DeleteRecognisedItemDomainEvent

}
