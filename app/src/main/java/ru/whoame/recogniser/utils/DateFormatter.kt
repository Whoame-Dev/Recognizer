package ru.whoame.recogniser.utils

import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import ru.whoame.recogniser.utils.DateFormatter.DEFAULT_FORMAT

/**
 * Utility object for formatting [LocalDateTime] into string
 * and back with custom or default formats.
 **/
object DateFormatter {

    private const val DEFAULT_FORMAT = "dd.MM.yyyy HH:mm:ss"

    /**
     * Formats the given [date] using the provided [format],
     * or uses the default format if not specified.
     *
     * @param date The [LocalDateTime] to format.
     * @param format The string format pattern. Defaults to [DEFAULT_FORMAT].
     *
     * @return A formatted date-time string.
     **/
    fun defaultFormat(date: LocalDateTime, format: String = DEFAULT_FORMAT): String {
        val customFormatter = DateTimeFormatter.ofPattern(format)
        return date.format(customFormatter)
    }

}
