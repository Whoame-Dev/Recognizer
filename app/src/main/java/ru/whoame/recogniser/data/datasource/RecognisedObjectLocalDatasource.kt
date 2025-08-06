package ru.whoame.recogniser.data.datasource

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.whoame.recogniser.data.database.RecognisedObjectDao
import ru.whoame.recogniser.model.RecognisedObject
import ru.whoame.recogniser.utils.flowOf

class RecognisedObjectLocalDatasourceImpl(
    private val dao: RecognisedObjectDao,
) : RecognisedObjectLocalDatasource {

    override fun getAll(): Flow<List<RecognisedObject>> = flowOf { dao.getAll() }
        .flowOn(Dispatchers.IO)
        .map { list -> list.toUiModel() }

    override fun saveAll(
        recognisedObjects: List<RecognisedObject>,
    ): Flow<Unit> = flowOf { recognisedObjects.toEntity() }
        .map { list -> dao.insertAll(list) }
        .flowOn(Dispatchers.IO)

    override fun save(recognisedObject: RecognisedObject): Flow<Unit> = flowOf { recognisedObject.toEntity() }
        .map { entity -> dao.insert(entity) }
        .flowOn(Dispatchers.IO)

    override fun delete(recognisedObject: RecognisedObject): Flow<Unit> = flowOf { recognisedObject.toEntity() }
        .map { entity -> dao.delete(entity) }
        .flowOn(Dispatchers.IO)

}

interface RecognisedObjectLocalDatasource {

    fun getAll(): Flow<List<RecognisedObject>>

    fun saveAll(recognisedObjects: List<RecognisedObject>): Flow<Unit>

    fun save(recognisedObject: RecognisedObject): Flow<Unit>

    fun delete(recognisedObject: RecognisedObject): Flow<Unit>

}
