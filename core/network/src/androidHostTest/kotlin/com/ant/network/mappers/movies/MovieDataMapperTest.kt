package com.ant.network.mappers.movies

import com.ant.network.dto.MovieDto
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class MovieDataMapperTest {

    private val mapper = MovieDataMapper()

    @Test
    fun `Should map all fields from MovieDto to MovieData`() = runTest {
        val dto = MovieDto(
            id = 550,
            title = "Fight Club",
            originalTitle = "Fight Club",
            voteCount = 25000,
            overview = "An insomniac office worker...",
            backdropPath = "/backdrop.jpg",
            posterPath = "/poster.jpg",
            voteAverage = 8.4,
            releaseDate = "1999-10-15",
            genreIds = listOf(18, 53),
            runtime = 139,
            originalLanguage = "en",
        )

        val result = mapper.map(dto)

        assertEquals(550L, result.id)
        assertEquals("Fight Club", result.name)
        assertEquals("Fight Club", result.originalTitle)
        assertEquals(25000, result.voteCount)
        assertEquals("An insomniac office worker...", result.overview)
        assertEquals("/backdrop.jpg", result.backDropPath)
        assertEquals("/poster.jpg", result.posterPath)
        assertEquals(8.4, result.voteAverage)
        assertEquals(listOf(18, 53), result._genres_ids)
        assertNotNull(result._releaseDate)
    }

    @Test
    fun `Should default id to 0 when null`() = runTest {
        val dto = MovieDto(id = null, title = "No ID")

        val result = mapper.map(dto)

        assertEquals(0L, result.id)
    }

    @Test
    fun `Should handle null release date`() = runTest {
        val dto = MovieDto(id = 1, releaseDate = null)

        val result = mapper.map(dto)

        assertNull(result._releaseDate)
    }

    @Test
    fun `Should handle invalid release date string`() = runTest {
        val dto = MovieDto(id = 1, releaseDate = "not-a-date")

        val result = mapper.map(dto)

        assertNull(result._releaseDate)
    }

    @Test
    fun `Should set genres to empty list`() = runTest {
        val dto = MovieDto(id = 1)

        val result = mapper.map(dto)

        assertEquals(emptyList(), result._genres)
    }

    @Test
    fun `Should handle all null fields gracefully`() = runTest {
        val dto = MovieDto()

        val result = mapper.map(dto)

        assertEquals(0L, result.id)
        assertNull(result.name)
        assertNull(result.overview)
        assertNull(result.backDropPath)
        assertNull(result.posterPath)
        assertNull(result.voteAverage)
    }
}
