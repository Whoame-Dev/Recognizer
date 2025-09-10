package ru.whoame.recogniser.ui.composable.model

import androidx.compose.runtime.Immutable

/**
 * UI model representing a loading placeholder for a recognised object.
 *
 * Used to render [RecognisedObjectCardShimmer][ru.whoame.recogniser.ui.composable.recognisedobjectcard.RecognisedObjectCardShimmer]
 *
 * @property id stable id
 **/
@Immutable
data class RecognisedObjectLoadingUiModel(
    override val id: Long,
) : BaseUiModel
