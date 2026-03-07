package com.ant.models.extensions

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Formats this [Instant] as a human-readable date string.
 *
 * @param useDifferentFormat when true, formats as "dd-MM-yyyy"; otherwise uses ISO-8601.
 * @return the formatted date string, or null if formatting fails.
 */
fun Instant.toFormattedDateString(useDifferentFormat: Boolean = false): String? {
    return try {
        if (useDifferentFormat) {
            val localDateTime = this.toLocalDateTime(TimeZone.currentSystemDefault())
            "${localDateTime.dayOfMonth.toString().padStart(2, '0')}-" +
                "${localDateTime.monthNumber.toString().padStart(2, '0')}-" +
                "${localDateTime.year}"
        } else {
            this.toString()
        }
    } catch (e: Exception) {
        null
    }
}

/**
 * Parses this string as an [Instant], returning null if parsing fails.
 */
fun String.toInstant(): Instant? {
    return try {
        Instant.parse(this)
    } catch (e: Exception) {
        null
    }
}
