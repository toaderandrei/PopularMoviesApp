package com.ant.network.extensions

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn

/**
 * Parses an ISO-8601 date string (e.g. "2024-03-15") into an [Instant] at the start of day in UTC.
 *
 * @return the parsed [Instant], or `null` if the string is not a valid date.
 */
fun String.parseDate(): Instant? {
    return try {
        val localDate = LocalDate.parse(this)
        localDate.atStartOfDayIn(TimeZone.UTC)
    } catch (_: Exception) {
        null
    }
}
