package ru.whoame.recogniser.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Room database for the app that stores data.
 *
 * - Entities: [RecognisedObjectEntity]
 * - Version: 1
 *
 * Exposes typed accessors for DAOs.
 **/
@Database(entities = [RecognisedObjectEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Provides access to CRUD operations for [RecognisedObjectEntity].
     **/
    abstract fun recognisedObjectDao(): RecognisedObjectDao

}
