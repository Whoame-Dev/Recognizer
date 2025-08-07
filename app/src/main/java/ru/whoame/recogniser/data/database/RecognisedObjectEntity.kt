package ru.whoame.recogniser.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecognisedObjectEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val timestamp: Long,
)
