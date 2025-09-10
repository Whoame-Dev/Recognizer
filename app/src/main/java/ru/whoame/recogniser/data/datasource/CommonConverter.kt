package ru.whoame.recogniser.data.datasource

import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId

/**
 * Centralized helpers for common, stateless conversions across data and domain layers.
 *
 * Intended as a single place for reusable mappers between framework/primitive types and
 * application models.
 *
 * Add additional converters here to avoid duplication and keep conversion logic consistent.
 **/
object CommonConverter {

    /**
     * Converts a [org.threeten.bp.LocalDateTime] to an epoch timestamp in milliseconds.
     *
     * The provided local date-time is interpreted in the device's default zone
     * ([ZoneId.systemDefault]) before being converted to an [Instant].
     *
     * @param date The local date-time to convert.
     * @return Epoch timestamp in milliseconds.
     **/
    fun toTimestamp(date: LocalDateTime): Long = date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

    /**
     * Converts an epoch timestamp in milliseconds to a [org.threeten.bp.LocalDateTime].
     *
     * The resulting local date-time is produced in the device's default zone
     * ([ZoneId.systemDefault]).
     *
     * @param timestamp Epoch timestamp in milliseconds.
     * @return A local date-time representing the given instant in the system default zone.
     **/
    fun toLocalDateTime(timestamp: Long): LocalDateTime = LocalDateTime.ofInstant(
        /* instant = */ Instant.ofEpochMilli(timestamp),
        /* zone = */ ZoneId.systemDefault(),
    )

}
