package ru.whoame.recogniser.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity representing a single recognised object stored in Room.
 *
 * @property id Stable unique identifier of the recognised object.
 * @property title Label of the object.
 * @property timestamp Time when the object was recognised, in epoch millis.
 **/
@Entity
data class RecognisedObjectEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val timestamp: Long,
)
