package ru.whoame.recogniser.ui.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import ru.whoame.recogniser.R
import ru.whoame.recogniser.ui.DarkLightPreviews
import ru.whoame.recogniser.ui.theme.RecogniserTheme

@Composable
fun ScreenTopBar(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) = Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
    IconButton(onBackClick) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(R.string.back_content_description),
        )
    }
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
    )
    Spacer(Modifier.width(dimensionResource(R.dimen.padding_medium)))
}

@Composable
@DarkLightPreviews
private fun ScreenTopBarPreview() = RecogniserTheme {
    Surface {
        ScreenTopBar(title = "Title", onBackClick = {})
    }
}
