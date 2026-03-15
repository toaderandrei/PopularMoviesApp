package com.ant.network.datasource.search

import com.ant.models.entities.MovieData
import com.ant.models.request.RequestType
import com.ant.network.dto.GenreResultsDto
import com.ant.network.dto.MovieResultsPageDto
import com.ant.network.ktx.safeResourceGet
import com.ant.network.mappers.movies.MoviesListMapper
import com.ant.network.resources.GenreResources
import com.ant.network.resources.SearchResources
import io.ktor.client.HttpClient

class SearchMovieDataSource(
    private val client: HttpClient,
    private val moviesListMapper: MoviesListMapper,
) {
    suspend operator fun invoke(params: RequestType.SearchMovieRequest): List<MovieData> {
        val searchResults: MovieResultsPageDto = client.safeResourceGet(
            SearchResources.Movies(query = params.query, page = params.page)
        )
        val genres: GenreResultsDto = client.safeResourceGet(GenreResources.MovieGenres())
        return moviesListMapper.map(Pair(searchResults, genres)).items
    }
}
