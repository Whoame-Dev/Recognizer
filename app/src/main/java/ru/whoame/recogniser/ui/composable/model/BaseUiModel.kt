package ru.whoame.recogniser.ui.composable.model

import androidx.compose.runtime.Immutable

/**
 * Base contract for UI models used by composables.
 *
 * Exposes a stable [id] for list keys and efficient diffing.
 * Implementations should be immutable to aid Compose stability.
 **/
@Immutable
sealed interface BaseUiModel {

    /**
     * Stable unique identifier for this UI item.
     * Must remain constant for the logical lifetime of the item.
     **/
    val id: Long

}
