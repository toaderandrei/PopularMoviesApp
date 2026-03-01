package com.ant.models.extensions

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

private const val REGEX_DATE_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy"
private const val REGEX_DATE_FORMAT_OUTPUT = "dd-MM-yyyy"

fun Date.toDateString(useDifferentFormat: Boolean = false): String? {
    return try {
        val formatter = DateTimeFormatter.ofPattern(REGEX_DATE_FORMAT, Locale.getDefault())
        val zonedDateTime = ZonedDateTime.parse(this.toString(), formatter)
        val dateTimeFormatterOutput: DateTimeFormatter = if (useDifferentFormat) {
            DateTimeFormatter.ofPattern(REGEX_DATE_FORMAT_OUTPUT)
        } else {
            DateTimeFormatter.ofPattern(REGEX_DATE_FORMAT)
        }
        zonedDateTime.format(dateTimeFormatterOutput)
    } catch (e: Exception) {
        null
    }
}

fun String.toDate(): Date? {
    return try {
        val formatter = DateTimeFormatter.ofPattern(REGEX_DATE_FORMAT)
        val dateTime = ZonedDateTime.parse(this, formatter)
        Date.from(dateTime.toInstant())
    } catch (e: Exception) {
        null
    }
}
