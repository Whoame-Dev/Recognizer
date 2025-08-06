package ru.whoame.recogniser.data.database

import androidx.room.Entity

@Entity(primaryKeys = ["title", "timestamp"])
data class RecognisedObjectEntity(
    val title: String,
    val timestamp: Long,
)
