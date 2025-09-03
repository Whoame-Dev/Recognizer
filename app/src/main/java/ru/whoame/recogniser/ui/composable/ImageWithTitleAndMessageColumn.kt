package ru.whoame.recogniser.ui.composable

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.dimensionResource
import ru.whoame.recogniser.R
import ru.whoame.recogniser.ui.DarkLightPreviews
import ru.whoame.recogniser.ui.theme.RecogniserTheme

/**
 * Displays an icon, a title, and a message stacked vertically and centered.
 *
 * The column centers its content both horizontally and vertically within the given [modifier].
 *
 * @param painter Icon [Painter] to render at the top of the column.
 * @param title The primary text displayed under the icon, styled as `titleLarge`.
 * @param message The secondary text displayed under the title, styled as `bodyMedium`.
 * @param modifier Optional [Modifier] for the root column.
 **/
@Composable
fun ImageWithTitleAndMessageColumn(
    painter: Painter,
    title: String,
    message: String,
    modifier: Modifier = Modifier,
) = Column(
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = modifier,
) {
    Icon(
        painter = painter,
        contentDescription = null,
        modifier = Modifier.size(dimensionResource(R.dimen.empty_icon_size)),
    )
    Spacer(Modifier.height(dimensionResource(R.dimen.padding_medium)))
    Text(text = title, style = MaterialTheme.typography.titleLarge)
    Text(text = message, style = MaterialTheme.typography.bodyMedium)
}

@DarkLightPreviews
@Composable
private fun Preview() = RecogniserTheme {
    Surface {
        ImageWithTitleAndMessageColumn(
            painter = rememberVectorPainter(Icons.Default.ErrorOutline),
            title = "Title",
            message = "Message",
        )
    }
}
