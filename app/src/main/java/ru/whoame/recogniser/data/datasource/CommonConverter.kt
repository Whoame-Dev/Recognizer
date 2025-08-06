package ru.whoame.recogniser.data.datasource

import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset

object CommonConverter {

    fun toTimestamp(date: LocalDateTime): Long = date.atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli()

    fun toLocalDateTime(timestamp: Long): LocalDateTime = LocalDateTime.ofInstant(
        /* instant = */ Instant.ofEpochSecond(timestamp),
        /* zone = */ ZoneOffset.systemDefault(),
    )

}
