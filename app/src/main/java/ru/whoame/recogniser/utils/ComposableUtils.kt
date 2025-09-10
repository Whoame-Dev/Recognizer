package ru.whoame.recogniser.utils

import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import ru.whoame.recogniser.R

/**
 * Applies a skeleton-like loading animation to the component.
 *
 * @param durationMillis Duration of the shimmer animation in milliseconds.
 *
 * @receiver Modifier to extend with the shimmer effect.
 *
 * @return A Modifier with the shimmer loading animation applied.
 **/
@Composable
fun Modifier.shimmerLoading(
    durationMillis: Int = 1500,
): Modifier {
    val transition = rememberInfiniteTransition(label = stringResource(R.string.shimmer_transition_label))

    val translateAnimation by transition.animateFloat(
        initialValue = -2500f,
        targetValue = 2500f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = stringResource(R.string.shimmer_loading_label),
    )

    val backgroundColor = Color.LightGray

    return drawWithContent {
        drawRect(backgroundColor)
        drawRect(
            brush = Brush.linearGradient(
                colors = listOf(
                    backgroundColor,
                    Color.White.copy(alpha = 0.5f),
                    backgroundColor,
                ),
                start = Offset(x = translateAnimation, y = translateAnimation),
                end = Offset(x = translateAnimation + 1000f, y = translateAnimation + 1000f),
            ),
        )
    }
}
