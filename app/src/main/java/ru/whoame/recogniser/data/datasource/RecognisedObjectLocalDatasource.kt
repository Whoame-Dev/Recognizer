package ru.whoame.recogniser.data.datasource

import kotlinx.coroutines.flow.Flow
import ru.whoame.recogniser.model.RecognisedObject

/**
 * Local data source for persisting and observing recognised objects.
 *
 * Implementations are expected to interact with on-device storage
 * such as a database or shared preferences.
 **/
interface RecognisedObjectLocalDatasource {

    /**
     * Stream all recognised objects stored locally.
     *
     * @return a [Flow] that emits the current list of [RecognisedObject].
     **/
    fun getAll(): Flow<List<RecognisedObject>>

    /**
     * Persist a collection of recognised objects.
     *
     * @param recognisedObjects the objects to save
     * @return a [Flow] that completes when the operation finishes.
     **/
    fun saveAll(recognisedObjects: List<RecognisedObject>): Flow<Unit>

    /**
     * Persist a single recognised object.
     *
     * @param recognisedObject the object to save
     * @return a [Flow] that completes when the operation finishes.
     **/
    fun save(recognisedObject: RecognisedObject): Flow<Unit>

    /**
     * Remove the given recognised object from local storage.
     *
     * @param recognisedObject the object to delete
     * @return a [Flow] that completes when the operation finishes.
     **/
    fun delete(recognisedObject: RecognisedObject): Flow<Unit>

    /**
     * Remove recognised object from local storage by id.
     *
     * @param id object id to delete
     * @return a [Flow] that completes when the operation finishes.
     **/
    fun delete(id: Long): Flow<Unit>

}
