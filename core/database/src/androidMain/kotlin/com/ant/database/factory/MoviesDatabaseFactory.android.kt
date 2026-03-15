package com.ant.database.factory

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ant.database.database.MoviesDb

/**
 * Migration from version 40 to 41.
 * Adds synced_to_remote column to track remote synchronization status.
 */
private val MIGRATION_40_41 = object : Migration(40, 41) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE moviedata ADD COLUMN synced_to_remote INTEGER DEFAULT 0")
        db.execSQL("ALTER TABLE tvseriesdata ADD COLUMN synced_to_remote INTEGER DEFAULT 0")
    }
}

/**
 * Android implementation of Room database factory.
 * Uses Room.databaseBuilder with Android Context and migrations.
 */
actual class MoviesDatabaseFactory(
    private val context: Context
) {
    actual fun createDatabase(): MoviesDb {
        return Room.databaseBuilder(
            context = context,
            klass = MoviesDb::class.java,
            name = "tmdb_movies.db"
        )
            .addMigrations(MIGRATION_40_41)
            .fallbackToDestructiveMigration(false)
            .build()
    }
}
