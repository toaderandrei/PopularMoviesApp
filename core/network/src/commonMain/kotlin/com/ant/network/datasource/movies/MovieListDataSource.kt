package com.ant.network.datasource.movies

import com.ant.common.exceptions.withRetry
import com.ant.models.entities.MovieData
import com.ant.models.model.PaginatedResult
import com.ant.models.request.MovieType
import com.ant.models.request.RequestType
import com.ant.network.api.TmdbGenreApi
import com.ant.network.api.TmdbMoviesApi
import com.ant.network.mappers.movies.MoviesListMapper

/**
 * Fetches a paginated list of movies by category (popular, top-rated, etc.) from the TMDb API
 * and maps the results to domain [MovieData] models.
 */
class MovieListDataSource(
    private val moviesApi: TmdbMoviesApi,
    private val genreApi: TmdbGenreApi,
    private val moviesListMapper: MoviesListMapper,
) {
    /**
     * Fetches movies for the category and page specified in [params].
     *
     * @return paginated result of mapped [MovieData] domain models.
     */
    suspend operator fun invoke(params: RequestType.MovieRequest): PaginatedResult<MovieData> {
        val movieResults = withRetry {
            when (params.movieType) {
                MovieType.POPULAR -> moviesApi.getPopular(params.page)
                MovieType.TOP_RATED -> moviesApi.getTopRated(params.page)
                MovieType.NOW_PLAYING -> moviesApi.getNowPlaying(params.page)
                MovieType.UPCOMING -> moviesApi.getUpcoming(params.page)
            }
        }

        val genres = genreApi.getMovieGenres()
        return moviesListMapper.map(Pair(movieResults, genres))
    }
}
