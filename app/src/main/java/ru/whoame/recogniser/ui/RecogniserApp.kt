package ru.whoame.recogniser.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.whoame.recogniser.ui.theme.RecogniserTheme

@Composable
fun RecogniserApp(
    viewModel: RecogniserAppViewModel = koinViewModel(),
) {
    val name by viewModel.stateFlow.collectAsStateWithLifecycle()
    RecogniserAppStateless(name)
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
