package ru.whoame.recogniser.ui

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Destination : NavKey {

  @Serializable
  data object MenuScreen : Destination

  @Serializable
  data object RecognisedListScreen : Destination

}
