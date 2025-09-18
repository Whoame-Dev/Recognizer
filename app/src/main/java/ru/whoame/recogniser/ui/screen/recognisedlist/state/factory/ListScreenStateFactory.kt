package ru.whoame.recogniser.ui.screen.recognisedlist.state.factory

import ru.whoame.recogniser.R
import ru.whoame.recogniser.ui.composable.model.RecognisedObjectLoadingUiModel
import ru.whoame.recogniser.ui.composable.model.RecognisedObjectUiModel
import ru.whoame.recogniser.ui.screen.recognisedlist.state.model.ListScreenState
import ru.whoame.recogniser.ui.screen.recognisedlist.state.model.ListScreenUiState
import ru.whoame.recogniser.utils.DateFormatter
import ru.whoame.recogniser.utils.Resource
import ru.whoame.state_machine.contract.BaseStateFactory

class ListScreenStateFactory : BaseStateFactory<ListScreenState, ListScreenUiState> {

  override fun convert(
    state: ListScreenState,
  ): ListScreenUiState = when (val resource = state.list) {
    is Resource.Loading -> ListScreenUiState(list = getLoadingList())

    is Resource.Data -> ListScreenUiState(
      list = resource.value.map { model ->
        // ToDo Need to replace image placeholder with real data
        RecognisedObjectUiModel(
          id = model.id,
          image = R.drawable.image_placeholder,
          title = model.title,
          date = DateFormatter.defaultFormat(model.date),
        )
      },
      selectedItemId = state.selectedItemId,
      itemToDelete = state.itemToDelete,
    )

    is Resource.Error -> ListScreenUiState(isErrorVisible = true)
  }

  private fun getLoadingList() = listOf(
    RecognisedObjectLoadingUiModel(0),
    RecognisedObjectLoadingUiModel(1),
    RecognisedObjectLoadingUiModel(2),
    RecognisedObjectLoadingUiModel(3),
    RecognisedObjectLoadingUiModel(4),
  )

}
