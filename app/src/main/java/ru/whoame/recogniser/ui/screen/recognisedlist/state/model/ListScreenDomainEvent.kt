package ru.whoame.recogniser.ui.screen.recognisedlist.state.model

sealed interface ListScreenDomainEvent {

    data object GetRecognisedList : ListScreenDomainEvent

}
