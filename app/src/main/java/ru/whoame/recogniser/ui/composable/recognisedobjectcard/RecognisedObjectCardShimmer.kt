package ru.whoame.recogniser.ui.composable.recognisedobjectcard

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.whoame.recogniser.R
import ru.whoame.recogniser.ui.DarkLightPreviews
import ru.whoame.recogniser.ui.composable.model.RecognisedObjectUiModel
import ru.whoame.recogniser.ui.theme.RecogniserTheme

/**
 * Shimmer variant of [RecognisedObjectCard].
 *
 * Displays the card in a loading state using a placeholder image and empty texts.
 * Internally sets [RecognisedObjectCardState.LOAD] to show the shimmer effect.
 *
 * @param modifier Optional modifier for the card container.
 **/
@Composable
fun RecognisedObjectCardShimmer(
    modifier: Modifier = Modifier,
) = RecognisedObjectCard(
    model = RecognisedObjectUiModel(0, R.drawable.image_placeholder, "", ""),
    state = RecognisedObjectCardState.LOAD,
    onClick = {},
    modifier = modifier,
)

@DarkLightPreviews
@Composable
private fun Preview() = RecogniserTheme {
    Surface {
        RecognisedObjectCardShimmer()
    }
}
