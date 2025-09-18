package ru.whoame.recogniser.ui.screen.menu

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.whoame.recogniser.R
import ru.whoame.recogniser.ui.DarkLightPreviews
import ru.whoame.recogniser.ui.DarkLightScreenPreviews
import ru.whoame.recogniser.ui.theme.RecogniserTheme

@Composable
fun MenuScreen(
  recognisedObjectListClick: () -> Unit,
  modifier: Modifier = Modifier,
) = Column(
  modifier = modifier,
  verticalArrangement = Arrangement.Center,
) {
  MenuItem(
    text = stringResource(R.string.recognised_object_list_title),
    onClick = recognisedObjectListClick,
    modifier = Modifier
      .fillMaxWidth()
      .padding(dimensionResource(R.dimen.padding_medium)),
  )
}

@Composable
private fun MenuItem(
  text: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val padding = dimensionResource(R.dimen.padding_medium)
  val innerModifier = modifier
    .border(1.dp, MaterialTheme.colorScheme.onBackground)
    .padding(padding)
    .clickable(onClick = onClick)

  Text(
    text = text,
    modifier = innerModifier,
    style = MaterialTheme.typography.headlineSmall,
  )
}

@Composable
@DarkLightScreenPreviews
private fun MenuScreenPreview() = RecogniserTheme {
  Scaffold { innerPadding ->
    MenuScreen(recognisedObjectListClick = {}, modifier = Modifier.padding(innerPadding))
  }
}

@Composable
@DarkLightPreviews
private fun MenuItemPreview() = RecogniserTheme {
  Surface {
    MenuItem(text = "Text", {})
  }
}
