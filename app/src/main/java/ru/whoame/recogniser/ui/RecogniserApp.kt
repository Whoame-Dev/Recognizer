package ru.whoame.recogniser.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import ru.whoame.recogniser.ui.screen.menu.MenuScreen
import ru.whoame.recogniser.ui.screen.recognisedlist.RecognisedListScreen
import ru.whoame.recogniser.ui.theme.RecogniserTheme

@Composable
fun RecogniserApp() = RecogniserTheme {
  Scaffold { innerPaddings ->
    val backStack =
      rememberNavBackStack(Destination.MenuScreen)

    val screenModifier = Modifier
      .fillMaxSize()
      .padding(innerPaddings)

    NavDisplay(
      backStack = backStack,
      modifier = screenModifier,
      onBack = { backStack.removeLastOrNull() },
    ) { key ->
      navigate(key as? Destination, backStack)
    }
  }
}

private fun navigate(
  key: Destination?,
  backStack: NavBackStack<NavKey>,
): NavEntry<NavKey> = when (key) {
  is Destination.MenuScreen -> NavEntry(key) {
    MenuScreen(recognisedObjectListClick = {
      backStack.add(Destination.RecognisedListScreen)
    })
  }

  is Destination.RecognisedListScreen -> NavEntry(key) {
    RecognisedListScreen(onBackClick = {
      backStack.removeLastOrNull()
    })
  }

  else -> throw IllegalArgumentException("Unknown navigation key: $key")
}
