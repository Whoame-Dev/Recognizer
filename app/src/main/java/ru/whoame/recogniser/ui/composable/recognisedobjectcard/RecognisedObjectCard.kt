package ru.whoame.recogniser.ui.composable.recognisedobjectcard

import androidx.annotation.DrawableRes
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
import androidx.compose.ui.unit.IntSize
import ru.whoame.recogniser.R
import ru.whoame.recogniser.ui.DarkLightPreviews
import ru.whoame.recogniser.ui.composable.model.RecognisedObjectUiModel
import ru.whoame.recogniser.ui.theme.RecogniserTheme
import ru.whoame.recogniser.ui.theme.Transparent
import ru.whoame.recogniser.utils.shimmerLoading

/**
 * UI state of `RecognisedObjectCard`.
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

    val springSpec = spring<IntSize>(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessMedium,
    )
    val columnModifier = when {
        isLoading -> Modifier.shimmerLoading()
        isExpanded -> Modifier.fillMaxSize()
        else -> Modifier
    }
        .clickable(onClick = onClick)
        .animateContentSize(animationSpec = springSpec)
    Column(modifier = columnModifier) {
        val imageWithGradientModifier = if (isExpanded) {
            Modifier
                .fillMaxSize()
                .weight(1f)
        } else {
            Modifier.fillMaxWidth()
        }

        RecognisedObjectImage(
            image = model.image,
            isExpanded = isExpanded,
            modifier = imageWithGradientModifier,
        )

        DescriptionRow(
            title = model.title,
            date = model.date,
            isExpanded = isExpanded,
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)),
        )
    }
}

/**
 * Displays an image with an optional gradient overlay at the bottom.
 *
 * The image adapts its size and scaling behavior based on the expansion state:
 * - When expanded: fills the parent size with fit scaling to maintain aspect ratio
 * - When folded: has fixed height with crop scaling and a gradient overlay at bottom
 *
 * @param image Drawable resource ID for the image to display.
 * @param isExpanded Whether the parent card is in expanded state, affecting image size and scaling.
 * @param modifier Optional modifier for the container Box.
 **/
@Composable
private fun RecognisedObjectImage(
    @DrawableRes image: Int,
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
) = Box(modifier) {
    val imageModifier = if (isExpanded) {
        Modifier.matchParentSize()
    } else {
        Modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.folded_image_height))
    }
    Image(
        painter = painterResource(image),
        contentDescription = null,
        contentScale = if (isExpanded) ContentScale.Fit else ContentScale.Crop,
        modifier = imageModifier,
    )

    if (!isExpanded) {
        val gradient = Brush.verticalGradient(
            colors = listOf(
                Transparent,
                MaterialTheme.colorScheme.surfaceContainerHighest,
            ),
        )
        val spacerModifier = Modifier
            .fillMaxWidth()
            .height(height = dimensionResource(R.dimen.folded_image_foreground_height))
            .align(Alignment.BottomCenter)
            .background(brush = gradient)
        Spacer(modifier = spacerModifier)
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
private fun RecognisedObjectImageFoldedPreview() = RecogniserTheme {
    Surface {
        RecognisedObjectImage(
            image = R.drawable.image_placeholder,
            isExpanded = false,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@DarkLightPreviews
@Composable
private fun RecognisedObjectImageExpandedPreview() = RecogniserTheme {
    Surface {
        RecognisedObjectImage(
            image = R.drawable.image_placeholder,
            isExpanded = true,
            modifier = Modifier.fillMaxSize(),
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
