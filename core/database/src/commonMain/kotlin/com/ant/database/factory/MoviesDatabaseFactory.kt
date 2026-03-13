package com.ant.database.factory

import com.ant.database.database.MoviesDb

/**
 * Factory for creating Room database instances following KMP pattern.
 * Platform-specific implementations handle context/driver requirements.
 */
expect class MoviesDatabaseFactory {
    fun createDatabase(): MoviesDb
}
