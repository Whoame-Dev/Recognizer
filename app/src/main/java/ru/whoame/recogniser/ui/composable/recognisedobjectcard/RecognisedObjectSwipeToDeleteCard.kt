package ru.whoame.recogniser.ui.composable.recognisedobjectcard

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
        positionalThreshold = { distance -> distance * 0.4f },
    )
    SwipeToDismissBox(
        state = swipeToDismissBoxState,
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = !isExpanded,
        modifier = modifier,
        backgroundContent = {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(R.string.delete_item_content_description),
                modifier = Modifier
                    .fillMaxSize()
                    .clip(MaterialTheme.shapes.medium)
                    .background(
                        color = lerp(
                            start = MaterialTheme.colorScheme.background,
                            stop = MaterialTheme.colorScheme.errorContainer,
                            // Workaround. If you move the element a bit, it will return 1f and the red background will be visible.
                            fraction = swipeToDismissBoxState.let { state ->
                                val progress = state.progress
                                if (state.targetValue != SwipeToDismissBoxValue.Settled || progress != 1f) {
                                    state.progress
                                } else {
                                    0f
                                }
                            },
                        ),
                    )
                    .wrapContentSize(Alignment.CenterEnd)
                    .padding(dimensionResource(R.dimen.padding_medium)),
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
