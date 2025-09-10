package ru.whoame.recogniser.data.repository

import kotlinx.coroutines.flow.Flow
import ru.whoame.recogniser.data.datasource.RecognisedObjectLocalDatasource
import ru.whoame.recogniser.model.RecognisedObject
import ru.whoame.recogniser.utils.Resource
import ru.whoame.recogniser.utils.asResource

/**
 * Implementation of the repository interface that uses a local data source for
 * storing and retrieving recognised objects.
 *
 * @property localDatasource The local data source ([RecognisedObjectLocalDatasource]) responsible for
 * handling [RecognisedObject] persistence operations.
 **/
class RecognisedObjectRepositoryImpl(
    private val localDatasource: RecognisedObjectLocalDatasource,
) : RecognisedObjectRepository {

    override fun getAll(): Flow<Resource<List<RecognisedObject>>> = localDatasource.getAll()
        .asResource()

    override fun saveAll(
        recognisedObjects: List<RecognisedObject>,
    ): Flow<Resource<Unit>> = localDatasource.saveAll(recognisedObjects)
        .asResource()

    override fun save(
        recognisedObject: RecognisedObject,
    ): Flow<Resource<Unit>> = localDatasource.save(recognisedObject)
        .asResource()

    override fun delete(
        recognisedObject: RecognisedObject,
    ): Flow<Resource<Unit>> = localDatasource.delete(recognisedObject)
        .asResource()

    override fun delete(id: Long): Flow<Resource<Unit>> = localDatasource.delete(id)
        .asResource()

}
