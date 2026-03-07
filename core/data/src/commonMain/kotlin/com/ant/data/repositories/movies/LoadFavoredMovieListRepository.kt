package com.ant.data.repositories.movies

import com.ant.database.database.MoviesDb
import com.ant.database.mapper.toDomain
import com.ant.models.entities.MovieData


/**
 * Loads movies from the local database filtered by their favored status.
 */
class LoadFavoredMovieListRepository constructor(
    private val moviesDb: MoviesDb,
) {
    /**
     * Queries the local database for movies matching the favored flag.
     *
     * @param params `true` to load favored movies, `false` for non-favored.
     */
    suspend fun performRequest(params: Boolean): List<MovieData> {
        return moviesDb.moviesDao().loadFavoredMovies(params).map { it.toDomain() }
    }
}