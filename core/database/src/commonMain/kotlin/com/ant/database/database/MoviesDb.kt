package com.ant.database.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import com.ant.database.converters.TmdbTypeConverters
import com.ant.database.entity.MovieCastEntity
import com.ant.database.entity.MovieCrewEntity
import com.ant.database.entity.MovieDataEntity
import com.ant.database.entity.MovieReviewEntity
import com.ant.database.entity.MovieVideoEntity
import com.ant.database.entity.TvShowEntity

/**
 * Room database definition for the TMDb application.
 *
 * Contains all entity tables for movies, TV series, casts, crews, reviews, and videos.
 * Uses [TmdbTypeConverters] for custom type serialization and the KMP
 * [ConstructedBy] pattern for cross-platform database instantiation.
 */
@Database(
    entities = [
        MovieDataEntity::class,
        TvShowEntity::class,
        MovieCastEntity::class,
        MovieReviewEntity::class,
        MovieCrewEntity::class,
        MovieVideoEntity::class,
   ],
    version = 42,
    exportSchema = false
)
@TypeConverters(
    TmdbTypeConverters::class,
)
@ConstructedBy(MoviesDbConstructor::class)
abstract class MoviesDb : RoomDatabase(), TmdbDatabase

/**
 * Expected constructor for [MoviesDb] used by Room KMP code generation.
 *
 * The actual implementation is generated per platform by the Room KSP processor.
 */
// Room KMP generates the actual implementations
@Suppress("KotlinNoActualForExpect", "EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object MoviesDbConstructor : RoomDatabaseConstructor<MoviesDb> {
    override fun initialize(): MoviesDb
}