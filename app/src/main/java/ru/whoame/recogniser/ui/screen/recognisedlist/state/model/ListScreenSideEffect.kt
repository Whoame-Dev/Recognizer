package ru.whoame.recogniser.ui.screen.recognisedlist.state.model

sealed interface ListScreenSideEffect {

    data class DeleteItem(val id: Long) : ListScreenSideEffect

}
