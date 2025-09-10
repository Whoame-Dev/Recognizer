package ru.whoame.recogniser.data.database

import androidx.room.*

/**
 * Data access object for [RecognisedObjectEntity].
 *
 * Provides suspendable CRUD operations backed by Room.
 **/
@Dao
interface RecognisedObjectDao {

    /**
     * Returns all recognised objects in insertion order defined by Room.
     **/
    @Query("SELECT * FROM recognisedobjectentity")
    suspend fun getAll(): List<RecognisedObjectEntity>

    /**
     * Inserts a list of entities.
     *
     * @param list Entities to insert.
     **/
    @Insert
    suspend fun insertAll(list: List<RecognisedObjectEntity>)

    /**
     * Inserts a single entity.
     *
     * @param entity Entity to insert.
     **/
    @Insert
    suspend fun insert(entity: RecognisedObjectEntity)

    /**
     * Deletes a single entity.
     *
     * @param entity Entity to delete.
     **/
    @Delete
    suspend fun delete(entity: RecognisedObjectEntity)

    /**
     * Deletes a single entity by id.
     *
     * @param id Entity id to delete.
     **/
    @Query("DELETE FROM recognisedobjectentity WHERE id = :id")
    suspend fun deleteById(id: Long)

}
