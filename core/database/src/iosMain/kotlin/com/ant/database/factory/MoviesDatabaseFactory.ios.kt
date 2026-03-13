package com.ant.database.factory

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.ant.database.database.MoviesDb
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

/**
 * iOS implementation of Room database factory.
 * Uses Room with BundledSQLiteDriver and iOS document directory.
 */
actual class MoviesDatabaseFactory {
    @OptIn(ExperimentalForeignApi::class)
    actual fun createDatabase(): MoviesDb {
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
        val dbPath = requireNotNull(documentDirectory?.path) + "/tmdb_movies.db"

        return Room.databaseBuilder<MoviesDb>(name = dbPath)
            .setDriver(BundledSQLiteDriver())
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }
}
