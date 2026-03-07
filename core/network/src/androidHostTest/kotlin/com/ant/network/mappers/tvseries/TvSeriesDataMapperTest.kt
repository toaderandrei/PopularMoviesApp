package com.ant.network.mappers.tvseries

import com.ant.network.dto.TvShowDto
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class TvSeriesDataMapperTest {

    private val mapper = TvSeriesDataMapper()

    @Test
    fun `Should map all fields from TvShowDto to TvShow`() = runTest {
        val dto = TvShowDto(
            id = 1399,
            name = "Breaking Bad",
            originalName = "Breaking Bad",
            voteCount = 12000,
            overview = "A chemistry teacher...",
            backdropPath = "/bb_backdrop.jpg",
            posterPath = "/bb_poster.jpg",
            originalLanguage = "en",
            voteAverage = 9.5,
            genreIds = listOf(18, 80),
            firstAirDate = "2008-01-20",
        )

        val result = mapper.map(dto)

        assertEquals(1399L, result.id)
        assertEquals("Breaking Bad", result.name)
        assertEquals("Breaking Bad", result.originalTitle)
        assertEquals(12000, result.voteCount)
        assertEquals("A chemistry teacher...", result.overview)
        assertEquals("/bb_backdrop.jpg", result.backDropPath)
        assertEquals("/bb_poster.jpg", result.posterPath)
        assertEquals("en", result.originalLanguage)
        assertEquals(9.5, result.voteAverage)
        assertEquals(listOf(18, 80), result._genres_ids)
        assertNotNull(result._firstAirDate)
    }

    @Test
    fun `Should default id to 0 when null`() = runTest {
        val dto = TvShowDto(id = null)

        val result = mapper.map(dto)

        assertEquals(0L, result.id)
    }

    @Test
    fun `Should handle null first air date`() = runTest {
        val dto = TvShowDto(id = 1, firstAirDate = null)

        val result = mapper.map(dto)

        assertNull(result._firstAirDate)
    }

    @Test
    fun `Should handle invalid date string`() = runTest {
        val dto = TvShowDto(id = 1, firstAirDate = "invalid")

        val result = mapper.map(dto)

        assertNull(result._firstAirDate)
    }
}
