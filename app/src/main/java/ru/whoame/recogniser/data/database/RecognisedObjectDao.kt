package ru.whoame.recogniser.data.database

import androidx.room.*

@Dao
interface RecognisedObjectDao {

    @Query("SELECT * FROM recognisedobjectentity")
    suspend fun getAll(): List<RecognisedObjectEntity>

    @Insert
    suspend fun insertAll(list: List<RecognisedObjectEntity>)

    @Insert
    suspend fun insert(entity: RecognisedObjectEntity)

    @Delete
    suspend fun delete(entity: RecognisedObjectEntity)

}
