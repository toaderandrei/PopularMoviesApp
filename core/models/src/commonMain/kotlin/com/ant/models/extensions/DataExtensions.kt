package com.ant.models.extensions

import kotlin.time.Instant

/**
 * Formats this [Instant] as a human-readable date string.
 *
 * @param useDifferentFormat when true, formats as "dd-MM-yyyy"; otherwise uses ISO-8601.
 * @return the formatted date string, or null if formatting fails.
 */
expect fun Instant.toFormattedDateString(useDifferentFormat: Boolean = false): String?

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
