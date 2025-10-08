package ru.whoame.recogniser.ui.screen.recognizer

import androidx.camera.core.SurfaceRequest
import androidx.camera.viewfinder.compose.MutableCoordinateTransformer
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.setFrom

fun List<Rect>.transformToUiCoords(
  transformationInfo: SurfaceRequest.TransformationInfo?,
  uiToBufferCoordinateTransformer: MutableCoordinateTransformer,
): List<Rect> = this.map { sensorRect ->

  val bufferRect = Matrix().apply {
    transformationInfo?.let {
      setFrom(matrix = it.sensorToBufferTransform)
    }
  }
    .map(sensorRect)

  val uiRect = Matrix().apply {
    setFrom(uiToBufferCoordinateTransformer.transformMatrix)
    invert()
  }
    .map(bufferRect)

  uiRect
}