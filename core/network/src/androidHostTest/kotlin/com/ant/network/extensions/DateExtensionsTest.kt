package com.ant.network.extensions

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DateExtensionsTest {

    @Test
    fun `Should parse valid ISO-8601 date`() {
        val result = "2024-03-15".parseDate()

        val expected = LocalDate(2024, 3, 15).atStartOfDayIn(TimeZone.UTC)
        assertEquals(expected, result)
    }

    @Test
    fun `Should parse date at year boundary`() {
        val result = "2000-01-01".parseDate()

        val expected = LocalDate(2000, 1, 1).atStartOfDayIn(TimeZone.UTC)
        assertEquals(expected, result)
    }

    @Test
    fun `Should parse leap year date`() {
        val result = "2024-02-29".parseDate()

        val expected = LocalDate(2024, 2, 29).atStartOfDayIn(TimeZone.UTC)
        assertEquals(expected, result)
    }

    @Test
    fun `Should return null for invalid date string`() {
        assertNull("not-a-date".parseDate())
    }

    @Test
    fun `Should return null for empty string`() {
        assertNull("".parseDate())
    }

    @Test
    fun `Should return null for partial date`() {
        assertNull("2024-03".parseDate())
    }

    @Test
    fun `Should return null for invalid month`() {
        assertNull("2024-13-01".parseDate())
    }

    @Test
    fun `Should return null for invalid day`() {
        assertNull("2024-02-30".parseDate())
    }
}
