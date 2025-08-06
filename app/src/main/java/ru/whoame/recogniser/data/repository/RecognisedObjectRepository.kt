package ru.whoame.recogniser.data.repository

import kotlinx.coroutines.flow.Flow
import ru.whoame.recogniser.data.datasource.RecognisedObjectLocalDatasource
import ru.whoame.recogniser.model.RecognisedObject
import ru.whoame.recogniser.utils.Resource
import ru.whoame.recogniser.utils.asResource

class RecognisedObjectRepositoryImpl(
    private val localDatasource: RecognisedObjectLocalDatasource
) : RecognisedObjectRepository {

    override fun getAll(): Flow<Resource<List<RecognisedObject>>> = localDatasource.getAll()
        .asResource()

    override fun saveAll(
        recognisedObjects: List<RecognisedObject>,
    ): Flow<Resource<Unit>> = localDatasource.saveAll(recognisedObjects)
        .asResource()

    override fun insert(
        recognisedObject: RecognisedObject,
    ): Flow<Resource<Unit>> = localDatasource.save(recognisedObject)
        .asResource()

    override fun delete(
        recognisedObject: RecognisedObject,
    ): Flow<Resource<Unit>> = localDatasource.delete(recognisedObject)
        .asResource()

}

interface RecognisedObjectRepository {

    fun getAll(): Flow<Resource<List<RecognisedObject>>>

    fun saveAll(recognisedObjects: List<RecognisedObject>): Flow<Resource<Unit>>

    fun insert(recognisedObject: RecognisedObject): Flow<Resource<Unit>>

    fun delete(recognisedObject: RecognisedObject): Flow<Resource<Unit>>

}
