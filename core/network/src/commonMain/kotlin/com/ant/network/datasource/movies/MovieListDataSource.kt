package com.ant.network.datasource.movies

import com.ant.models.entities.MovieData
import com.ant.models.model.PaginatedResult
import com.ant.models.request.MovieType
import com.ant.models.request.RequestType
import com.ant.network.dto.GenreResultsDto
import com.ant.network.dto.MovieResultsPageDto
import com.ant.network.ktx.safeResourceGet
import com.ant.network.mappers.movies.MoviesListMapper
import com.ant.network.resources.GenreResources
import com.ant.network.resources.MovieResources
import io.ktor.client.HttpClient

class MovieListDataSource(
    private val client: HttpClient,
    private val moviesListMapper: MoviesListMapper,
) {
    suspend operator fun invoke(params: RequestType.MovieRequest): PaginatedResult<MovieData> {
        val movieResults: MovieResultsPageDto = when (params.movieType) {
            MovieType.POPULAR -> client.safeResourceGet(MovieResources.Popular(page = params.page))
            MovieType.TOP_RATED -> client.safeResourceGet(MovieResources.TopRated(page = params.page))
            MovieType.NOW_PLAYING -> client.safeResourceGet(MovieResources.NowPlaying(page = params.page))
            MovieType.UPCOMING -> client.safeResourceGet(MovieResources.Upcoming(page = params.page))
        }

        val genres: GenreResultsDto = client.safeResourceGet(GenreResources.MovieGenres())
        return moviesListMapper.map(Pair(movieResults, genres))
    }
}
