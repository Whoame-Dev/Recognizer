package ru.whoame.recogniser.ui.dialog.state.model

import androidx.compose.runtime.Immutable

@Immutable
data class DeleteRecognisedItemUiState(
    val itemName: String,
    val isLoading: Boolean,
)
