package com.ant.models.extensions

import kotlin.time.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Formats this [Instant] as a human-readable date string.
 *
 * @param useDifferentFormat when true, formats as "dd-MM-yyyy"; otherwise uses ISO-8601.
 * @return the formatted date string, or null if formatting fails.
 */
actual fun Instant.toFormattedDateString(useDifferentFormat: Boolean): String? {
    return try {
        if (useDifferentFormat) {
            val localDateTime = this.toLocalDateTime(TimeZone.currentSystemDefault())
            "${localDateTime.day.toString().padStart(2, '0')}-" +
                "${localDateTime.month.toString().padStart(2, '0')}-" +
                "${localDateTime.year}"
        } else {
            this.toString()
        }
    } catch (e: Exception) {
        null
    }
}
