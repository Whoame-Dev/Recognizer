package ru.whoame.recogniser.ui.dialog.model

sealed interface DeleteRecognisedItemDomainEvent {

    data class Delete(val id: Long) : DeleteRecognisedItemDomainEvent

}
