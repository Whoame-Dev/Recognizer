package ru.whoame.recogniser.ui.composable.recognisedobjectcard

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ru.whoame.recogniser.R
import ru.whoame.recogniser.ui.DarkLightPreviews
import ru.whoame.recogniser.ui.composable.model.RecognisedObjectUiModel
import ru.whoame.recogniser.ui.theme.RecogniserTheme
import ru.whoame.recogniser.ui.theme.Transparent
import ru.whoame.recogniser.utils.shimmerLoading

/**
 * UI state of `RecognisedObjectCard`.
 *
 * - `FOLD`: Compact view showing image and brief info.
 * - `LOAD`: Loading state with shimmer placeholders.
 * - `EXPAND`: Expanded view showing full-size image and details.
 **/
enum class RecognisedObjectCardState {

    /** Compact view showing image and brief info **/
    FOLD,
    /** Loading state with shimmer placeholders. **/
    LOAD,
    /** Expanded view showing full-size image and details. **/
    EXPAND

}

/**
 * Card that displays a recognised object's image, title and date.
 *
 * Layout is controlled by [state]: folded, loading (shimmer) or expanded. The size changes
 * are animated and tapping the card triggers [onClick].
 *
 * @param model The UI model containing the data to display in the card.
 * @param state Current UI state, see [RecognisedObjectCardState].
 * @param onClick Invoked when the card is tapped.
 * @param modifier Optional modifier for the card.
 **/
@Composable
fun RecognisedObjectCard(
    model: RecognisedObjectUiModel,
    state: RecognisedObjectCardState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) = Card(
    shape = MaterialTheme.shapes.medium,
    elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.card_elevation)),
    modifier = modifier,
) {
    val isExpanded = state == RecognisedObjectCardState.EXPAND
    val isLoading = state == RecognisedObjectCardState.LOAD
    val columnModifier = if (isExpanded) {
        Modifier.fillMaxSize()
    } else {
        Modifier
    }
        .clickable(onClick = onClick)
        .animateContentSize(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessMedium,
            ),
        )
        .let { localModifier ->
            if (isLoading) localModifier.shimmerLoading() else localModifier
        }
    Column(modifier = columnModifier) {
        val imageBoxModifier = if (isExpanded) {
            Modifier
                .fillMaxSize()
                .weight(1f)
        } else {
            Modifier.fillMaxWidth()
        }
        Box(imageBoxModifier) {
            Image(
                painter = painterResource(model.image),
                contentDescription = null,
                contentScale = if (isExpanded) ContentScale.Fit else ContentScale.Crop,
                modifier = if (isExpanded) {
                    Modifier.matchParentSize()
                } else {
                    Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(R.dimen.folded_image_height))
                        .let {
                            if (isLoading) {
                                it.shimmerLoading()
                            } else {
                                it
                            }
                        }
                },
            )

            if (!isExpanded) {
                val gradient = Brush.verticalGradient(
                    colors = listOf(
                        Transparent,
                        MaterialTheme.colorScheme.surfaceContainerHighest,
                    ),
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(R.dimen.folded_image_foreground_height))
                        .align(Alignment.BottomCenter)
                        .background(gradient),
                )
            }
        }

        DescriptionRow(
            title = model.title,
            date = model.date,
            isExpanded = isExpanded,
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)),
        )
    }
}

/**
 * Row with title, date and an expand indicator.
 *
 * The arrow rotates to indicate the expanded state.
 *
 * @param title Title text.
 * @param date Formatted save date.
 * @param isExpanded Whether the parent card is expanded, which controls arrow rotation.
 * @param modifier Optional modifier for the row.
 **/
@Composable
private fun DescriptionRow(
    title: String,
    date: String,
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
) = Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
    Column {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
        Text(text = date, style = MaterialTheme.typography.labelSmall)
    }
    Spacer(Modifier.weight(1f))

    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = tween(500),
        label = stringResource(R.string.expand_arrow_rotation_animation_label),
    )
    Icon(
        imageVector = Icons.Default.ExpandMore,
        contentDescription = null,
        modifier = Modifier.graphicsLayer { rotationZ = rotationAngle },
    )
}

@DarkLightPreviews
@Composable
private fun RecognisedObjectCardFoldedPreview() = RecogniserTheme {
    Surface {
        RecognisedObjectCard(
            model = RecognisedObjectUiModel(0, R.drawable.image_placeholder, "One", "01.01.2010"),
            state = RecognisedObjectCardState.FOLD,
            onClick = {},
        )
    }
}

@DarkLightPreviews
@Composable
private fun RecognisedObjectCardExpandedPreview() = RecogniserTheme {
    Surface {
        RecognisedObjectCard(
            model = RecognisedObjectUiModel(0, R.drawable.image_placeholder, "One", "01.01.2010"),
            state = RecognisedObjectCardState.EXPAND,
            onClick = {},
        )
    }
}

@DarkLightPreviews
@Composable
private fun RecognisedObjectCardLoadingPreview() = RecogniserTheme {
    Surface {
        RecognisedObjectCard(
            model = RecognisedObjectUiModel(0, R.drawable.image_placeholder, "One", "01.01.2010"),
            state = RecognisedObjectCardState.LOAD,
            onClick = {},
        )
    }
}

@DarkLightPreviews
@Composable
private fun DescriptionRowFoldedPreview() = RecogniserTheme {
    Surface {
        DescriptionRow("Title", "Date", false)
    }
}

@DarkLightPreviews
@Composable
private fun DescriptionRowExpandedPreview() = RecogniserTheme {
    Surface {
        DescriptionRow("Title", "Date", true)
    }
}
