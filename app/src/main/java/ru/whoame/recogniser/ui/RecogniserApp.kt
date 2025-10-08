package ru.whoame.recogniser.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.whoame.recogniser.ui.screen.recognisedlist.RecognisedListScreen
import ru.whoame.recogniser.ui.screen.recognizer.RecognizerScreen

@Composable
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
fun RecogniserApp(
    viewModel: RecogniserAppViewModel = koinViewModel(),
) {
    val name by viewModel.stateFlow.collectAsStateWithLifecycle()

    Scaffold { innerPaddings ->
        val screenModifier = Modifier
            .fillMaxSize()
            .padding(innerPaddings)

//        RecognisedListScreen({ /* Need to implement with navigation */ }, screenModifier)
        RecognizerScreen({})
    }
}