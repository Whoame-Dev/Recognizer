package ru.whoame.recogniser.ui.composable.recognisedobjectcard

import androidx.annotation.FloatRange
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import ru.whoame.recogniser.R
import ru.whoame.recogniser.ui.DarkLightPreviews
import ru.whoame.recogniser.ui.composable.model.RecognisedObjectUiModel
import ru.whoame.recogniser.ui.theme.RecogniserTheme

/** 40% of the card width to trigger deletion **/
private const val DELETION_THRESHOLD = 0.4f

/**
 * Variant of [RecognisedObjectCard] that supports swipe-to-delete interaction.
 *
 * The card content is provided by [RecognisedObjectCard]. Swiping from end to start reveals
 * a delete background and triggers [onDelete] once the swipe reaches the dismiss threshold.
 * Swiping is disabled while the card is expanded (when [isExpanded] is true).
 *
 * @param model The UI model containing the data to display in the card.
 * @param isExpanded Whether the card is currently expanded. When true, swipe-to-delete is disabled.
 * @param onClick Invoked when the card content is clicked.
 * @param onDelete Invoked when the card is dismissed by swiping from end to start.
 * @param modifier Optional [Modifier] for this card.
 * @param initialSwipeToDismissBoxValue Initial value for the swipe state. Useful for previews or restoring state.
 **/
@Composable
fun RecognisedObjectSwipeToDeleteCard(
    model: RecognisedObjectUiModel,
    isExpanded: Boolean,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    initialSwipeToDismissBoxValue: SwipeToDismissBoxValue = SwipeToDismissBoxValue.Settled,
) {
    val swipeToDismissBoxState = rememberSwipeToDismissBoxState(
        initialValue = initialSwipeToDismissBoxValue,
        confirmValueChange = { direction ->
            if (direction == SwipeToDismissBoxValue.EndToStart) {
                onDelete.invoke()
            }
            false
        },
        positionalThreshold = { distance -> distance * DELETION_THRESHOLD },
    )
    SwipeToDismissBox(
        state = swipeToDismissBoxState,
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = !isExpanded,
        modifier = modifier,
        backgroundContent = {
            BackgroundDeleteIcon(
                backgroundColorFraction = swipeToDismissBoxState.getFraction(),
            )
        },
    ) {
        RecognisedObjectCard(
            model,
            state = if (isExpanded) RecognisedObjectCardState.EXPAND else RecognisedObjectCardState.FOLD,
            onClick = onClick,
        )
    }
}

/**
 * Displays a delete icon with an animated background color that changes based on swipe progress.
 *
 * @param backgroundColorFraction Progress fraction between 0.0 and 1.0 that determines
 * the background color interpolation from normal background to error container color.
 * @param modifier Optional [Modifier] for this composable.
 **/
@Composable
private fun BackgroundDeleteIcon(
    @FloatRange(0.0, 1.0) backgroundColorFraction: Float,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = lerp(
        start = MaterialTheme.colorScheme.background,
        stop = MaterialTheme.colorScheme.errorContainer,
        fraction = backgroundColorFraction,
    )
    val iconModifier = Modifier
        .fillMaxSize()
        .clip(MaterialTheme.shapes.medium)
        .background(backgroundColor)
        .wrapContentSize(Alignment.CenterEnd)
        .padding(dimensionResource(R.dimen.padding_medium))
    Icon(
        imageVector = Icons.Default.Delete,
        contentDescription = stringResource(R.string.delete_item_content_description),
        modifier = modifier.then(iconModifier),
    )
}

/**
 * Gets the current swipe progress as a fraction between 0 and 1.
 *
 * @return The progress fraction, or 0 if the dismiss box is settled and not animating.
 **/
private fun SwipeToDismissBoxState.getFraction(): Float = if (
    targetValue != SwipeToDismissBoxValue.Settled ||
    // Workaround. If you move the element a bit, it will return 1f and the red background will be visible.
    progress != 1f
) {
    val fraction = progress / DELETION_THRESHOLD
    if (fraction > 1f) 1f else fraction
} else {
    0f
}

@DarkLightPreviews
@Composable
private fun Preview() = RecogniserTheme {
    Surface {
        RecognisedObjectSwipeToDeleteCard(
            model = RecognisedObjectUiModel(0, R.drawable.image_placeholder, "One", "01.01.2010"),
            isExpanded = false,
            onClick = {},
            onDelete = {},
        )
    }
}

@DarkLightPreviews
@Composable
private fun DeletedPreview() = RecogniserTheme {
    Surface {
        RecognisedObjectSwipeToDeleteCard(
            model = RecognisedObjectUiModel(0, R.drawable.image_placeholder, "One", "01.01.2010"),
            isExpanded = false,
            onClick = {},
            onDelete = {},
            initialSwipeToDismissBoxValue = SwipeToDismissBoxValue.EndToStart,
        )
    }
}
