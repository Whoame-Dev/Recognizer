package ru.whoame.recogniser.data.datasource

import ru.whoame.recogniser.data.database.RecognisedObjectEntity
import ru.whoame.recogniser.model.RecognisedObject

/**
 * Maps a list of persisted entities to a list of domain models.
 *
 * @return List of [RecognisedObject] with elements in the same order as the receiver.
 * @see RecognisedObjectEntity.toDomainModel
 **/
fun List<RecognisedObjectEntity>.toDomainModel(): List<RecognisedObject> = map(RecognisedObjectEntity::toDomainModel)

/**
 * Maps a single persisted entity to its domain model counterpart.
 *
 * Converts the epoch-based `timestamp` to [java.time.LocalDateTime] using [CommonConverter].
 *
 * @return A [RecognisedObject] representing this entity.
 * @see CommonConverter.toLocalDateTime
 **/
fun RecognisedObjectEntity.toDomainModel() = RecognisedObject(
    id = id,
    title = title,
    date = CommonConverter.toLocalDateTime(timestamp),
)

/**
 * Maps a list of domain models to a list of entities suitable for persistence.
 *
 * @return List of [RecognisedObjectEntity] with elements in the same order as the receiver.
 * @see RecognisedObject.toEntity
 **/
fun List<RecognisedObject>.toEntity(): List<RecognisedObjectEntity> = map(RecognisedObject::toEntity)

/**
 * Maps a single domain model to its persistence entity counterpart.
 *
 * Converts the [java.time.LocalDateTime] `date` to a timestamp using [CommonConverter].
 *
 * @return A [RecognisedObjectEntity] representing this domain object.
 * @see CommonConverter.toTimestamp
 **/
fun RecognisedObject.toEntity() = RecognisedObjectEntity(
    id = id,
    title = title,
    timestamp = CommonConverter.toTimestamp(date),
)
