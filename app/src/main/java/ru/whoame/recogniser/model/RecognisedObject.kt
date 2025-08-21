package ru.whoame.recogniser.model

import androidx.compose.runtime.Immutable
import org.threeten.bp.LocalDateTime

@Immutable
data class RecognisedObject(
    val id: Int,
    val title: String,
    val date: LocalDateTime,
)
