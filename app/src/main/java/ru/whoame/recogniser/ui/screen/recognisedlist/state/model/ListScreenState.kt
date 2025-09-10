package ru.whoame.recogniser.ui.screen.recognisedlist.state.model

import ru.whoame.recogniser.model.RecognisedObject
import ru.whoame.recogniser.utils.Resource

data class ListScreenState(
    val list: Resource<List<RecognisedObject>> = Resource.Loading(),
    val selectedItemId: Long? = null,
)
