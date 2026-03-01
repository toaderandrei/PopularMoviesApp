package com.ant.data.repositories.movies

import com.ant.database.database.MoviesDb
import com.ant.database.mapper.toDomain
import com.ant.models.entities.MovieData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoadFavoredMovieListRepository @Inject constructor(
    private val moviesDb: MoviesDb,
) {
    suspend fun performRequest(params: Boolean): List<MovieData> {
        return moviesDb.moviesDao().loadFavoredMovies(params).map { it.toDomain() }
    }
}