package ru.whoame.recogniser.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.threeten.bp.LocalDateTime
import ru.whoame.recogniser.model.RecognisedObject
import ru.whoame.recogniser.ui.screen.RecognisedObjectListScreen
import ru.whoame.recogniser.ui.theme.RecogniserTheme

@Composable
fun RecogniserApp(
    viewModel: RecogniserAppViewModel = koinViewModel(),
) {
    val name by viewModel.stateFlow.collectAsStateWithLifecycle()
    //RecogniserAppStateless(name)
    RecognisedObjectListScreen(
        objects = listOf(
            RecognisedObject(0, "One", LocalDateTime.now()),
            RecognisedObject(1, "Two", LocalDateTime.now()),
            RecognisedObject(2, "Three", LocalDateTime.now()),
            RecognisedObject(3, "Four", LocalDateTime.now()),
            RecognisedObject(4, "Five", LocalDateTime.now()),
        ),
    )
}

@Composable
private fun RecogniserAppStateless(name: String) = Scaffold { innerPaddings ->
    Text(
        text = "Hello $name!",
        modifier = Modifier.padding(innerPaddings),
    )
}

@DarkLightPreviews
@Composable
private fun RecogniserAppPreview() = RecogniserTheme {
    RecogniserAppStateless("World")
}
