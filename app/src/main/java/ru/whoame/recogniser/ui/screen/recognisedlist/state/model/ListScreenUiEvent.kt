package ru.whoame.recogniser.ui.screen.recognisedlist.state.model

sealed interface ListScreenUiEvent {

  data class ItemClick(val id: Long?) : ListScreenUiEvent

  data class DeleteClick(val id: Long) : ListScreenUiEvent

  data class DismissDeleteDialog(val isItemDeleted: Boolean) : ListScreenUiEvent

}
