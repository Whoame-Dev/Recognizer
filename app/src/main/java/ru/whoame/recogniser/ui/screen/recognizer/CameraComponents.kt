package ru.whoame.recogniser.ui.screen.recognizer

import androidx.annotation.OptIn
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.geometry.takeOrElse
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.delay
import java.util.UUID

@OptIn(ExperimentalCamera2Interop::class)
@Composable
fun CameraPreviewContent(
  viewModel: RecognizerViewModel,
  modifier: Modifier = Modifier,
  lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
  val surfaceRequest by viewModel.surfaceRequest.collectAsStateWithLifecycle()
  val sensorFaceRects by viewModel.sensorFaceRects.collectAsStateWithLifecycle()
  var autofocusRequest by remember { mutableStateOf(UUID.randomUUID() to Offset.Unspecified) }
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

  val context = LocalContext.current
  LaunchedEffect(lifecycleOwner) {
    viewModel.bindToCamera(context.applicationContext, lifecycleOwner)
  }

  val autofocusRequestId = autofocusRequest.first
  val showAutofocusIndicator = autofocusRequest.second.isSpecified
  val autofocusCoords = remember(autofocusRequestId) { autofocusRequest.second }

  // Queue hiding the request for each unique autofocus tap
  if (showAutofocusIndicator) {
    LaunchedEffect(autofocusRequestId) {
      delay(1000)
      // Clear the offset to finish the request and hide the indicator
      autofocusRequest = autofocusRequestId to Offset.Unspecified
    }
  }

  surfaceRequest?.let { request ->
    val coordinateTransformer = remember { MutableCoordinateTransformer() }
    CameraFinder(
      surfaceRequest = request,
      viewModel = viewModel,
      coordinateTransformer = coordinateTransformer,
      onDetectedTap = { tapCoords -> autofocusRequest = UUID.randomUUID() to tapCoords },
      modifier = modifier
    )
    FocusIndicator(
      showAutofocusIndicator = showAutofocusIndicator,
      autofocusCoords = autofocusCoords
    )
    FaceIndicator(
      shouldSpotlightFaces = shouldSpotlightFaces,
      sensorFaceRects = sensorFaceRects,
      transformationInfo = transformationInfo,
      coordinateTransformer = coordinateTransformer
    )
  }
}

@OptIn(ExperimentalCamera2Interop::class)
@Composable
fun CameraFinder(
  surfaceRequest: SurfaceRequest,
  viewModel: RecognizerViewModel,
  coordinateTransformer: MutableCoordinateTransformer,
  onDetectedTap: (Offset) -> Unit,
  modifier: Modifier = Modifier,
) {
  CameraXViewfinder(
    surfaceRequest = surfaceRequest,
    coordinateTransformer = coordinateTransformer,
    modifier = modifier.pointerInput(viewModel, coordinateTransformer) {
      detectTapGestures { tapCoords ->
        with(coordinateTransformer) {
          viewModel.tapToFocus(tapCoords.transform())
        }
        onDetectedTap.invoke(tapCoords)
      }
    }
  )
}

@Composable
fun FocusIndicator(
  showAutofocusIndicator: Boolean,
  autofocusCoords: Offset,
) {
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
}

@Composable
fun FaceIndicator(
  shouldSpotlightFaces: Boolean,
  sensorFaceRects: List<Rect>,
  transformationInfo: SurfaceRequest.TransformationInfo?,
  coordinateTransformer: MutableCoordinateTransformer
) {
  val spotlightColor = Color(0xDDE60991)
  AnimatedVisibility(shouldSpotlightFaces, enter = fadeIn(), exit = fadeOut()) {
    Canvas(Modifier.fillMaxSize()) {
      val uiFaceRects: List<Rect> = sensorFaceRects.transformToUiCoords(
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