package com.ant.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ant.database.entity.TvShowEntity

/** Data access object for [TvShowEntity] operations. */
@Dao
abstract class TvSeriesDao {

    /** Inserts one or more TV series entities, replacing on conflict. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(vararg tvSeriesData: TvShowEntity)

    /** Inserts a single TV series entity, replacing on conflict. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(tvSeriesData: TvShowEntity)

    /** Inserts a list of TV series entities, replacing on conflict. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(tvSeriesData: List<TvShowEntity>)

    /** Finds a TV series by its [id], or returns null if not found. */
    @Query("SELECT * FROM tvseriesdata WHERE id = :id")
    abstract suspend fun findTvSeriesById(id: Int?): TvShowEntity?

    /** Deletes the TV series with the given [id]. */
    @Query("DELETE FROM tvseriesdata where id =:id")
    abstract suspend fun deleteTvSeriesById(id: Long)

    /** Loads all TV series matching the given [favored] status. */
    @Query("SELECT * from tvseriesdata where favored=:favored")
    abstract suspend fun loadFavoredTvSeriesData(favored: Boolean): List<TvShowEntity>

    /** Updates the remote sync status for the TV series with the given [id]. */
    @Query("UPDATE tvseriesdata SET synced_to_remote = :synced WHERE id = :id")
    abstract suspend fun updateSyncStatus(id: Long, synced: Boolean)
}