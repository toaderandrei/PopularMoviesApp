package com.ant.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ant.database.entity.TvShowEntity

@Dao
abstract class TvSeriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg tvSeriesData: TvShowEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(tvSeriesData: TvShowEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(tvSeriesData: List<TvShowEntity>)

    @Query("SELECT * FROM TvSeriesData WHERE id = :id")
    abstract fun findTvSeriesById(id: Int?): TvShowEntity?

    @Query("DELETE FROM TvSeriesData where id =:id")
    abstract fun deleteTvSeriesById(id: Long)

    @Query("SELECT * from TvSeriesData where favored=:favored")
    abstract fun loadFavoredTvSeriesData(favored: Boolean): List<TvShowEntity>

    @Query("UPDATE tvseriesdata SET synced_to_remote = :synced WHERE id = :id")
    abstract suspend fun updateSyncStatus(id: Long, synced: Boolean)
}