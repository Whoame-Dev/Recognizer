package ru.whoame.recogniser.data.datasource

import ru.whoame.recogniser.data.database.RecognisedObjectEntity
import ru.whoame.recogniser.model.RecognisedObject

fun List<RecognisedObjectEntity>.toUiModel(): List<RecognisedObject> = map(RecognisedObjectEntity::toUiModel)

fun RecognisedObjectEntity.toUiModel() = RecognisedObject(
    id = id,
    title = title,
    date = CommonConverter.toLocalDateTime(timestamp),
)

fun List<RecognisedObject>.toEntity() = map(RecognisedObject::toEntity)

fun RecognisedObject.toEntity() = RecognisedObjectEntity(
    id = id,
    title = title,
    timestamp = CommonConverter.toTimestamp(date),
)
