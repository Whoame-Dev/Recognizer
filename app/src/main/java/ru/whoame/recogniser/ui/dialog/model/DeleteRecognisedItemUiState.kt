package ru.whoame.recogniser.ui.dialog.model

import androidx.compose.runtime.Immutable

@Immutable
data class DeleteRecognisedItemUiState(
    val itemName: String,
    val itemId: Long,
    val isLoading: Boolean,
)
