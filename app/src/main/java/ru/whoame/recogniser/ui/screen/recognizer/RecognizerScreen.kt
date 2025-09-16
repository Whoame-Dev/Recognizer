package ru.whoame.recogniser.ui.screen.recognizer

import androidx.camera.camera2.interop.ExperimentalCamera2Interop
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import org.koin.androidx.compose.koinViewModel
import ru.whoame.recogniser.R
import ru.whoame.recogniser.ui.DarkLightScreenPreviews
import ru.whoame.recogniser.ui.theme.RecogniserTheme

@androidx.annotation.OptIn(ExperimentalCamera2Interop::class)
@Composable
fun RecognizerScreen(
  onBackClick: () -> Unit,
  modifier: Modifier = Modifier,
  viewModel: RecognizerViewModel = koinViewModel(),
) {
  RecognizerContent(
    modifier = modifier,
    viewModel = viewModel
  )
}

@androidx.annotation.OptIn(ExperimentalCamera2Interop::class)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RecognizerContent(
  viewModel: RecognizerViewModel,
  modifier: Modifier = Modifier,
) {
  val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
  if (cameraPermissionState.status.isGranted) {
    CameraPreviewContent(
      modifier = modifier,
      viewModel = viewModel
      )
  } else {
    AskPermissionContent(
      modifier = modifier,
      shouldShowRationale = cameraPermissionState.status.shouldShowRationale,
      onGrantClick = { cameraPermissionState.launchPermissionRequest() }
    )

  }
}

@Composable
private fun AskPermissionContent(
  onGrantClick: () -> Unit,
  shouldShowRationale: Boolean,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .wrapContentSize()
      .widthIn(max = 480.dp)
      .padding(dimensionResource(R.dimen.padding_medium)),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    val textToShow = if (shouldShowRationale) {
      stringResource(R.string.camera_permission_ask_rationale)
    } else {
      stringResource(R.string.camera_permission_ask)
    }
    Text(textToShow, textAlign = TextAlign.Center)
    Spacer(Modifier.height(16.dp))
    Button(onClick = onGrantClick) {
      Text("Unleash the Camera!")
    }
  }
}

@DarkLightScreenPreviews
@Composable
private fun PermissionRequiredWithRationalePreview() = RecogniserTheme {
  Scaffold { innerPadding ->
    AskPermissionContent(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding),
      shouldShowRationale = true,
      onGrantClick = {}
    )
  }
}

@DarkLightScreenPreviews
@Composable
private fun PermissionRequiredWithoutRationalePreview() = RecogniserTheme {
  Scaffold { innerPadding ->
    AskPermissionContent(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding),
      shouldShowRationale = false,
      onGrantClick = {}
    )
  }
}