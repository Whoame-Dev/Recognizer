package ru.whoame.recogniser.ui.composables

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import com.jakewharton.threetenabp.AndroidThreeTen
import org.threeten.bp.LocalDateTime
import ru.whoame.recogniser.R
import ru.whoame.recogniser.model.RecognisedObject
import ru.whoame.recogniser.ui.DarkLightPreviews
import ru.whoame.recogniser.ui.theme.RecogniserTheme
import ru.whoame.recogniser.ui.theme.Transparent

@Composable
fun RecognisedObjectCard(
    recognisedObject: RecognisedObject,
    isExpanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) = Card(
    shape = MaterialTheme.shapes.medium,
    elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.card_elevation)),
    modifier = modifier,
) {
    val columnModifier = if (isExpanded) {
        Modifier.fillMaxSize()
    } else {
        Modifier
    }
        .clickable { onClick.invoke() }
        .animateContentSize(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessMedium,
            ),
        )
    Column(modifier = columnModifier) {
        val imageModifier = if (isExpanded) {
            Modifier
                .fillMaxSize()
                .weight(1f)
        } else {
            Modifier.fillMaxWidth()
        }
        Box(imageModifier) {
            Image(
                painter = painterResource(R.drawable.image_placeholder),
                contentDescription = null,
                contentScale = if (isExpanded) ContentScale.Fit else ContentScale.Crop,
                modifier = if (isExpanded) {
                    Modifier.matchParentSize()
                } else {
                    Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(R.dimen.folded_image_height))
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
            title = recognisedObject.title,
            date = recognisedObject.date.toString(),
            isExpanded = isExpanded,
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)),
        )
    }
}

@Composable
private fun DescriptionRow(
    title: String,
    date: String,
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
) = Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
    Column {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
        Text(date)
    }
    Spacer(Modifier.weight(1f))

    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = tween(500),
        label = "rotationAnimation",
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
    AndroidThreeTen.init(LocalContext.current)
    Surface {
        RecognisedObjectCard(
            recognisedObject = RecognisedObject(0, "One", LocalDateTime.now()),
            isExpanded = false,
            onClick = {},
        )
    }
}

@DarkLightPreviews
@Composable
private fun RecognisedObjectCardExpandedPreview() = RecogniserTheme {
    AndroidThreeTen.init(LocalContext.current)
    Surface {
        RecognisedObjectCard(
            recognisedObject = RecognisedObject(0, "One", LocalDateTime.now()),
            isExpanded = true,
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
