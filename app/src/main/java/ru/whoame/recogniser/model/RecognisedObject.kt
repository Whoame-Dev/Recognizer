package ru.whoame.recogniser.model

import org.threeten.bp.LocalDateTime

/**
 * Domain model of an item recognised by the application.
 *
 * @property id Stable unique identifier of the recognised object in local storage.
 * @property title Human-readable label of the recognised item.
 * @property date Timestamp when the item was recognised ([LocalDateTime]).
 **/
data class RecognisedObject(
    val id: Long,
    val title: String,
    val date: LocalDateTime,
)
