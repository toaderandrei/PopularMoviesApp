package com.ant.data.repositories

import com.ant.data.repositories.search.SearchMovieRepository
import com.ant.data.repositories.search.SearchTvShowRepository
import com.ant.domain.repositories.SearchRepository
import com.ant.models.entities.MovieData
import com.ant.models.entities.TvShow
import com.ant.models.request.RequestType


/**
 * Default [SearchRepository] implementation that delegates movie and TV show
 * searches to their respective repositories.
 */
class DefaultSearchRepository constructor(
    private val searchMovieRepository: SearchMovieRepository,
    private val searchTvShowRepository: SearchTvShowRepository,
) : SearchRepository {

    /** Searches TMDb for movies matching the query. */
    override suspend fun searchMovies(params: RequestType.SearchMovieRequest): List<MovieData> {
        return searchMovieRepository.performRequest(params)
    }

    /** Searches TMDb for TV shows matching the query. */
    override suspend fun searchTvShows(params: RequestType.SearchTvShowRequest): List<TvShow> {
        return searchTvShowRepository.performRequest(params)
    }
}
