package ru.whoame.recogniser.ui.dialog.state.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import ru.whoame.recogniser.ui.theme.SuccessGreen
import ru.whoame.recogniser.ui.theme.errorLight
import ru.whoame.recogniser.utils.StringArgs

@Immutable
data class DeleteRecognisedItemUiState(
  val icon: IconModel?,
  val title: StringArgs,
  val message: StringArgs?,
  val dismissText: StringArgs?,
  val isConfirmVisible: Boolean,
  val isLoading: Boolean,
  val isItemDeleted: Boolean,
) {

  sealed interface IconModel {

    val vector: ImageVector
    val tint: Color

    data class Done(
      override val vector: ImageVector = Icons.Default.Done,
      override val tint: Color = SuccessGreen,
    ) : IconModel

    data class Error(
      override val vector: ImageVector = Icons.Default.Close,
      override val tint: Color = errorLight,
    ) : IconModel

  }

}
