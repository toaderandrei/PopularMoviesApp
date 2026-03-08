package com.ant.network.datasource.search

import com.ant.common.exceptions.withRetry
import com.ant.models.entities.MovieData
import com.ant.models.request.RequestType
import com.ant.network.api.TmdbGenreApi
import com.ant.network.api.TmdbSearchApi
import com.ant.network.mappers.movies.MoviesListMapper

/**
 * Searches for movies on the TMDb API by query string
 * and maps the results to domain [MovieData] models.
 */
class SearchMovieDataSource(
    private val searchApi: TmdbSearchApi,
    private val genreApi: TmdbGenreApi,
    private val moviesListMapper: MoviesListMapper,
) {
    /**
     * Executes a movie search for the query and page specified in [params].
     *
     * @return list of mapped [MovieData] domain models.
     */
    suspend operator fun invoke(params: RequestType.SearchMovieRequest): List<MovieData> {
        val searchResults = withRetry {
            searchApi.searchMovies(params.query, params.page)
        }
        val genres = genreApi.getMovieGenres()
        return moviesListMapper.map(Pair(searchResults, genres)).items
    }
}
