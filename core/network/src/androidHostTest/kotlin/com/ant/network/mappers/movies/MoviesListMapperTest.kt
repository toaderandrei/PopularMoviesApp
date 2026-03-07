package com.ant.network.mappers.movies

import com.ant.network.dto.GenreResultsDto
import com.ant.network.dto.MovieDto
import com.ant.network.dto.MovieResultsPageDto
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MoviesListMapperTest {

    private val movieDataMapper = MovieDataMapper()
    private val mapper = MoviesListMapper(movieDataMapper)

    @Test
    fun `Should map paginated results with movies`() = runTest {
        val page = MovieResultsPageDto(
            page = 2,
            totalPages = 10,
            totalResults = 200,
            results = listOf(
                MovieDto(id = 1, title = "Movie A"),
                MovieDto(id = 2, title = "Movie B"),
            ),
        )
        val genres = GenreResultsDto(genres = emptyList())

        val result = mapper.map(Pair(page, genres))

        assertEquals(2, result.page)
        assertEquals(10, result.totalPages)
        assertEquals(2, result.items.size)
        assertEquals("Movie A", result.items[0].name)
        assertEquals("Movie B", result.items[1].name)
    }

    @Test
    fun `Should return empty list when results is null`() = runTest {
        val page = MovieResultsPageDto(page = 1, results = null)
        val genres = GenreResultsDto(genres = emptyList())

        val result = mapper.map(Pair(page, genres))

        assertTrue(result.items.isEmpty())
    }

    @Test
    fun `Should default page to 1 when null`() = runTest {
        val page = MovieResultsPageDto(page = null, totalPages = null, results = emptyList())
        val genres = GenreResultsDto(genres = emptyList())

        val result = mapper.map(Pair(page, genres))

        assertEquals(1, result.page)
        assertEquals(1, result.totalPages)
    }

    @Test
    fun `Should handle empty results list`() = runTest {
        val page = MovieResultsPageDto(page = 1, totalPages = 1, results = emptyList())
        val genres = GenreResultsDto(genres = emptyList())

        val result = mapper.map(Pair(page, genres))

        assertTrue(result.items.isEmpty())
    }
}
