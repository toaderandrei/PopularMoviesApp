package com.ant.models.entities

/**
 * Base contract for all TMDb domain entities, providing a unique identifier and display name.
 */
interface TmdbEntity {
    val id: Long

    val name: String?
}