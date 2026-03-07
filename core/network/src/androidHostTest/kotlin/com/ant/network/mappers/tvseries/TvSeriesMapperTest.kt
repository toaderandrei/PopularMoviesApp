package com.ant.network.mappers.tvseries

import com.ant.network.dto.GenreResultsDto
import com.ant.network.dto.TvShowDto
import com.ant.network.dto.TvShowResultsPageDto
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TvSeriesMapperTest {

    private val tvSeriesDataMapper = TvSeriesDataMapper()
    private val mapper = TvSeriesMapper(tvSeriesDataMapper)

    @Test
    fun `Should map paginated results with tv shows`() = runTest {
        val page = TvShowResultsPageDto(
            page = 1,
            totalPages = 5,
            results = listOf(
                TvShowDto(id = 1, name = "Show A"),
                TvShowDto(id = 2, name = "Show B"),
            ),
        )
        val genres = GenreResultsDto(genres = emptyList())

        val result = mapper.map(Pair(page, genres))

        assertEquals(1, result.page)
        assertEquals(5, result.totalPages)
        assertEquals(2, result.items.size)
        assertEquals("Show A", result.items[0].name)
    }

    @Test
    fun `Should return empty list when results is null`() = runTest {
        val page = TvShowResultsPageDto(page = 1, results = null)
        val genres = GenreResultsDto(genres = emptyList())

        val result = mapper.map(Pair(page, genres))

        assertTrue(result.items.isEmpty())
    }

    @Test
    fun `Should default page and totalPages to 1 when null`() = runTest {
        val page = TvShowResultsPageDto(page = null, totalPages = null, results = emptyList())
        val genres = GenreResultsDto(genres = emptyList())

        val result = mapper.map(Pair(page, genres))

        assertEquals(1, result.page)
        assertEquals(1, result.totalPages)
    }
}
