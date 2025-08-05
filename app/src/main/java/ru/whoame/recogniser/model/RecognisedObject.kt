package ru.whoame.recogniser.model

import org.threeten.bp.LocalDate

data class RecognisedObject(
    val title: String,
    val date: LocalDate,
)
