package ru.whoame.recogniser.data.repository

import kotlinx.coroutines.flow.Flow
import ru.whoame.recogniser.model.RecognisedObject
import ru.whoame.recogniser.utils.Resource

/**
 * Repository for managing [RecognisedObject] persistence and retrieval.
 **/
interface RecognisedObjectRepository {

    /**
     * Observe all stored recognised objects.
     *
     * @return a [Flow] that emits [Resource] of [RecognisedObject]
     **/
    fun getAll(): Flow<Resource<List<RecognisedObject>>>

    /**
     * Persist the provided list of recognised objects.
     *
     * @param recognisedObjects the list of [RecognisedObject] to save
     * @return a [Flow] emitting completion status as [Resource]
     **/
    fun saveAll(recognisedObjects: List<RecognisedObject>): Flow<Resource<Unit>>

    /**
     * Persist a single recognised object.
     *
     * @param recognisedObject the item ([RecognisedObject]) to save
     * @return a [Flow] emitting completion status as [Resource]
     **/
    fun save(recognisedObject: RecognisedObject): Flow<Resource<Unit>>

    /**
     * Delete a single recognised object.
     *
     * @param recognisedObject the item ([RecognisedObject]]) to delete
     * @return a [Flow] emitting completion status as [Resource]
     **/
    fun delete(recognisedObject: RecognisedObject): Flow<Resource<Unit>>

    /**
     * Remove recognised object from local storage by id.
     *
     * @param id object id to delete
     * @return a [Flow] emitting completion status as [Resource]
     **/
    fun delete(id: Long): Flow<Resource<Unit>>

}
