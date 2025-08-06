package ru.whoame.recogniser.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RecognisedObjectEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun recognisedObjectDao(): RecognisedObjectDao

}
