package ru.whoame.recogniser.ui.screen.recognizer

import android.content.Context
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.CaptureResult
import android.hardware.camera2.TotalCaptureResult
import androidx.camera.camera2.interop.Camera2Interop
import androidx.camera.camera2.interop.ExperimentalCamera2Interop
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceOrientedMeteringPointFactory
import androidx.camera.core.SurfaceRequest
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.camera.viewfinder.compose.MutableCoordinateTransformer
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.setFrom
import androidx.compose.ui.graphics.toComposeRect
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@ExperimentalCamera2Interop
class RecognizerViewModel() : ViewModel(){
  private val _surfaceRequest = MutableStateFlow<SurfaceRequest?>(null)
  val surfaceRequest: StateFlow<SurfaceRequest?> = _surfaceRequest
  private var surfaceMeteringPointFactory: SurfaceOrientedMeteringPointFactory? = null
  private var cameraControl: CameraControl? = null
  private val _sensorFaceRects = MutableStateFlow(listOf<Rect>())
  val sensorFaceRects: StateFlow<List<Rect>> = _sensorFaceRects.asStateFlow()

  private val cameraPreviewUseCase = Preview.Builder()
    .apply {
      Camera2Interop.Extender(this)
        .setCaptureRequestOption(
          CaptureRequest.STATISTICS_FACE_DETECT_MODE,
          CaptureRequest.STATISTICS_FACE_DETECT_MODE_FULL
        )
        .setSessionCaptureCallback(object : CameraCaptureSession.CaptureCallback() {
          override fun onCaptureCompleted(
            session: CameraCaptureSession,
            request: CaptureRequest,
            result: TotalCaptureResult
          ) {
            super.onCaptureCompleted(session, request, result)
            result.get(CaptureResult.STATISTICS_FACES)
              ?.map { face -> face.bounds.toComposeRect() }
              ?.toList()
              ?.let { faces -> _sensorFaceRects.update { faces } }
          }
        })
    }
    .build().apply {
    setSurfaceProvider { newSurfaceRequest ->
      _surfaceRequest.update { newSurfaceRequest }
      surfaceMeteringPointFactory = SurfaceOrientedMeteringPointFactory(
        newSurfaceRequest.resolution.width.toFloat(),
        newSurfaceRequest.resolution.height.toFloat()
      )
    }
  }

  suspend fun bindToCamera(appContext: Context, lifecycleOwner: LifecycleOwner) {
    val processCameraProvider = ProcessCameraProvider.awaitInstance(appContext)
    val camera = processCameraProvider.bindToLifecycle(
      lifecycleOwner, DEFAULT_BACK_CAMERA, cameraPreviewUseCase
    )
    cameraControl = camera.cameraControl

    // Cancellation signals we're done with the camera
    try { awaitCancellation() } finally {
      processCameraProvider.unbindAll()
      cameraControl = null
    }
  }

  // Note: We could improve the interaction between UI and CameraControl somewhat by using
  // a more sophisticated coroutines setup, but this is outside the scope of this blog post.
  // If you’re interested in learning more about such an implementation, check out
  // the Jetpack Camera App sample (https://github.com/google/jetpack-camera-app),
  // which implements camera interactions through the CameraXCameraUseCase
  // (https://github.com/google/jetpack-camera-app/blob/41f953fde14bca11fed9534d074a9f5f67e8eaac/core/camera/src/main/java/com/google/jetpackcamera/core/camera/CameraXCameraUseCase.kt#L108).
  fun tapToFocus(tapCoords: Offset) {
    val point = surfaceMeteringPointFactory?.createPoint(tapCoords.x, tapCoords.y)
    if (point != null) {
      val meteringAction = FocusMeteringAction.Builder(point).build()
      cameraControl?.startFocusAndMetering(meteringAction)
    }
  }
}