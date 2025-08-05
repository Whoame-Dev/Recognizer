package ru.whoame.recogniser.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.whoame.recogniser.ui.theme.RecogniserTheme

@DarkLightPreviews
@Composable
fun RecogniserApp() = RecogniserTheme {
    Scaffold { innerPaddings ->
        Greeting("world", Modifier.padding(innerPaddings))
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier,
    )
}

@DarkLightPreviews
@Composable
fun GreetingPreview() {
    RecogniserTheme {
        Greeting("Android")
    }
}