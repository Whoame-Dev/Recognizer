package ru.whoame.recogniser.ui.screen.recognisedlist.state.model

import androidx.compose.runtime.Immutable
import ru.whoame.recogniser.ui.composable.model.BaseUiModel

@Immutable
data class ListScreenUiState(
    val list: List<BaseUiModel>,
    val selectedItemId: Long?,
    val isErrorVisible: Boolean,
)
