package ru.whoame.recogniser.ui.screen.recognizer

import androidx.camera.camera2.interop.ExperimentalCamera2Interop
import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.SurfaceRequest
import androidx.camera.viewfinder.compose.MutableCoordinateTransformer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.geometry.takeOrElse
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.setFrom
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import ru.whoame.recogniser.ui.DarkLightScreenPreviews
import ru.whoame.recogniser.ui.theme.RecogniserTheme
import java.util.UUID

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

@androidx.annotation.OptIn(ExperimentalCamera2Interop::class)
@Composable
private fun CameraPreviewContent(
  viewModel: RecognizerViewModel,
  modifier: Modifier = Modifier,
  lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
  val surfaceRequest by viewModel.surfaceRequest.collectAsStateWithLifecycle()
  val sensorFaceRects by viewModel.sensorFaceRects.collectAsStateWithLifecycle()
  val transformationInfo by
  produceState<SurfaceRequest.TransformationInfo?>(null, surfaceRequest) {
    try {
      surfaceRequest?.setTransformationInfoListener(Runnable::run) { transformationInfo ->
        value = transformationInfo
      }
      awaitCancellation()
    } finally {
      surfaceRequest?.clearTransformationInfoListener()
    }
  }
  val shouldSpotlightFaces by remember {
    derivedStateOf { sensorFaceRects.isNotEmpty() && transformationInfo != null}
  }
  val spotlightColor = Color(0xDDE60991)
  //////
  val context = LocalContext.current
  LaunchedEffect(lifecycleOwner) {
    viewModel.bindToCamera(context.applicationContext, lifecycleOwner)
  }

  var autofocusRequest by remember { mutableStateOf(UUID.randomUUID() to Offset.Unspecified) }

  val autofocusRequestId = autofocusRequest.first
  // Show the autofocus indicator if the offset is specified
  val showAutofocusIndicator = autofocusRequest.second.isSpecified
  // Cache the initial coords for each autofocus request
  val autofocusCoords = remember(autofocusRequestId) { autofocusRequest.second }

  // Queue hiding the request for each unique autofocus tap
  if (showAutofocusIndicator) {
    LaunchedEffect(autofocusRequestId) {
      delay(1000)
      // Clear the offset to finish the request and hide the indicator
      autofocusRequest = autofocusRequestId to Offset.Unspecified
    }
  }

  ///

  surfaceRequest?.let { request ->
    val coordinateTransformer = remember { MutableCoordinateTransformer() }
    CameraXViewfinder(
      surfaceRequest = request,
      coordinateTransformer = coordinateTransformer,
      modifier = modifier.pointerInput(viewModel, coordinateTransformer) {
        detectTapGestures { tapCoords ->
          with(coordinateTransformer) {
            viewModel.tapToFocus(tapCoords.transform())
          }
          autofocusRequest = UUID.randomUUID() to tapCoords
        }
      }
    )

    AnimatedVisibility(
      visible = showAutofocusIndicator,
      enter = fadeIn(),
      exit = fadeOut(),
      modifier = Modifier
        .offset { autofocusCoords.takeOrElse { Offset.Zero } .round() }
        .offset((-24).dp, (-24).dp)
    ) {
      Spacer(Modifier.border(2.dp, Color.White, CircleShape).size(48.dp))
    }

    AnimatedVisibility(shouldSpotlightFaces, enter = fadeIn(), exit = fadeOut()) {
      Canvas(Modifier.fillMaxSize()) {
        val uiFaceRects = sensorFaceRects.transformToUiCoords(
          transformationInfo = transformationInfo,
          uiToBufferCoordinateTransformer = coordinateTransformer
        )

        // Fill the whole space with the color
        drawRect(spotlightColor)
        // Then extract each face and make it transparent

        uiFaceRects.forEach { faceRect ->
          drawRect(
            Brush.radialGradient(
              0.4f to Color.Black, 1f to Color.Transparent,
              center = faceRect.center,
              radius = faceRect.minDimension * 2f,
            ),
            blendMode = BlendMode.DstOut
          )
        }
      }
    }
  }
}

@Composable
private fun AskPermissionContent(
  onGrantClick: () -> Unit,
  shouldShowRationale: Boolean,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier.fillMaxSize().wrapContentSize().widthIn(max = 480.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    val textToShow = if (shouldShowRationale) {
      // If the user has denied the permission, but the rationale can be shown
      "Whoops! Looks like we need your camera to work our magic!" +
        "Don't worry, we just wanna see your awesome creation.  " +
        "Grant us permission and let's get this party started!"
    } else {
      // If it's the first time the user lands on this feature, or the user
      // doesn't want to be asked again for this permission
      "Hi there! We need your camera to work our magic! ✨\n" +
        "Grant us permission and let's get this party started! \uD83C\uDF89"
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

private fun List<Rect>.transformToUiCoords(
  transformationInfo: SurfaceRequest.TransformationInfo?,
  uiToBufferCoordinateTransformer: MutableCoordinateTransformer
): List<Rect> = this.map { sensorRect ->
  val bufferToUiTransformMatrix = Matrix().apply {
    setFrom(uiToBufferCoordinateTransformer.transformMatrix)
    invert()
  }

  val sensorToBufferTransformMatrix = Matrix().apply {
    transformationInfo?.let {
      setFrom(it.sensorToBufferTransform)
    }
  }

  val bufferRect = sensorToBufferTransformMatrix.map(sensorRect)
  val uiRect = bufferToUiTransformMatrix.map(bufferRect)

  uiRect
}