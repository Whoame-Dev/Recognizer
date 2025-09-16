package ru.whoame.recogniser.ui.screen.recognisedlist.state.model

import ru.whoame.recogniser.model.RecognisedObject

sealed interface ListScreenSideEffect {

    data class DeleteItem(val item: RecognisedObject) : ListScreenSideEffect

}
