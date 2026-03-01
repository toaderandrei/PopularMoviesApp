package com.ant.database.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ant.database.converters.TmdbTypeConverters
import com.ant.database.entity.MovieCastEntity
import com.ant.database.entity.MovieCrewEntity
import com.ant.database.entity.MovieDataEntity
import com.ant.database.entity.MovieReviewEntity
import com.ant.database.entity.MovieVideoEntity
import com.ant.database.entity.TvShowEntity

@Database(
    entities = [
        MovieDataEntity::class,
        TvShowEntity::class,
        MovieCastEntity::class,
        MovieReviewEntity::class,
        MovieCrewEntity::class,
        MovieVideoEntity::class,
   ],
    version = 41,
    exportSchema = false
)
@TypeConverters(
    TmdbTypeConverters::class,
)
abstract class MoviesDb : RoomDatabase(), TmdbDatabase