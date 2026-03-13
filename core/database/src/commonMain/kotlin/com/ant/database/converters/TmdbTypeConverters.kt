package com.ant.database.converters

import androidx.room.TypeConverter
import com.ant.models.extensions.toInstant
import kotlinx.datetime.Instant
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


/**
 * Room type converters for custom types used across TMDb database entities.
 *
 * Handles conversion between [Instant], [List]<[Int]>, [List]<[String]> and their
 * SQLite-compatible [String] representations using kotlinx.serialization.
 */
class TmdbTypeConverters {

    /** Converts an ISO-8601 string to an [Instant]. */
    @TypeConverter
    fun toLocalTime(value: String?): Instant? = value?.toInstant()

    /** Converts an [Instant] to its ISO-8601 string representation. */
    @TypeConverter
    fun fromLocalTime(value: Instant?): String? = value?.toString()

    /** Deserializes a JSON string into a list of integer IDs. */
    @TypeConverter
    fun fromStringToList(value: String?): List<Int> {
        if (value == null) {
            return emptyList()
        }
        return Json.decodeFromString(value)
    }

    /** Serializes a list of integer IDs into a JSON string. */
    @TypeConverter
    fun fromArrayToList(list: List<Int>?): String? {
        if (list == null) {
            return null
        }
        return Json.encodeToString(list)
    }

    /** Deserializes a JSON string into a list of genre name strings. */
    @TypeConverter
    fun fromStringToListOfStrings(genreIds: String?): List<String> {
        if (genreIds == null) {
            return emptyList()
        }
        return Json.decodeFromString(genreIds)
    }

    /** Serializes a list of strings into a JSON string. */
    @TypeConverter
    fun fromListToString(list: List<String>?): String? {
        if (list.isNullOrEmpty()) {
            return null
        }
        return Json.encodeToString(list)
    }
}
