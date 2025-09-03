package ru.whoame.recogniser.ui.composable.model

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable

/**
 * UI model representing a recognised object.
 *
 * Used as input for
 * [RecognisedObjectSwipeToDeleteCard][ru.whoame.recogniser.ui.composable.recognisedobjectcard.RecognisedObjectSwipeToDeleteCard]
 * and
 * [RecognisedObjectCard][ru.whoame.recogniser.ui.composable.recognisedobjectcard.RecognisedObjectCard]
 * to render content.
 *
 * @property id Stable identifier for list diffing.
 * @property image Drawable resource id of the object's image/thumbnail.
 * @property title Display title of the recognised object.
 * @property date Formatted save date.
 **/
@Immutable
data class RecognisedObjectUiModel(
    override val id: Long,
    @get:DrawableRes val image: Int,
    val title: String,
    val date: String,
) : BaseUiModel
