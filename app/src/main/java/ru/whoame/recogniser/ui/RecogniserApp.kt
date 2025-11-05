package ru.whoame.recogniser.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.whoame.recogniser.ui.screen.recognisedlist.RecognisedListScreen

@Composable
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
fun RecogniserApp() {
    Scaffold { innerPaddings ->
        val screenModifier = Modifier
            .fillMaxSize()
            .padding(innerPaddings)

        RecognisedListScreen({ /* Need to implement with navigation */ }, screenModifier)
    }
}
