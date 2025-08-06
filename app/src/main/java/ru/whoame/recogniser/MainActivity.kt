package ru.whoame.recogniser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ru.whoame.recogniser.ui.RecogniserApp
import ru.whoame.recogniser.ui.theme.RecogniserTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecogniserTheme {
                RecogniserApp()
            }
        }
    }

}
