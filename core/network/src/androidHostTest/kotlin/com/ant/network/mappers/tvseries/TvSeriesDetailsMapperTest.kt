package com.ant.network.mappers.tvseries

import com.ant.models.entities.VideoType
import com.ant.network.dto.CastDto
import com.ant.network.dto.CreditsDto
import com.ant.network.dto.CrewDto
import com.ant.network.dto.TvShowDto
import com.ant.network.dto.VideoDto
import com.ant.network.dto.VideoResultsDto
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class TvSeriesDetailsMapperTest {

    private val tvSeriesDataMapper = TvSeriesDataMapper()
    private val mapper = TvSeriesDetailsMapper(tvSeriesDataMapper)

    @Test
    fun `Should map detail fields from dto`() = runTest {
        val dto = TvShowDto(
            id = 1,
            name = "Test Show",
            numberOfEpisodes = 62,
            numberOfSeasons = 5,
            status = "Ended",
            firstAirDate = "2008-01-20",
            lastAirDate = "2013-09-29",
        )

        val result = mapper.map(dto)

        assertEquals(62, result.tvSeriesData.numberOfEpisodes)
        assertEquals(5, result.tvSeriesData.numberOfSeasons)
        assertEquals("Ended", result.tvSeriesData.status)
        assertNotNull(result.tvSeriesData._firstAirDate)
        assertNotNull(result.tvSeriesData._lastAirDate)
    }

    @Test
    fun `Should map cast from credits`() = runTest {
        val dto = TvShowDto(
            id = 10,
            credits = CreditsDto(
                id = 200,
                cast = listOf(
                    CastDto(castId = 1, name = "Bryan Cranston", order = 0, profilePath = "/bryan.jpg"),
                ),
            ),
        )

        val result = mapper.map(dto)

        assertEquals(1, result.tvSeriesCasts?.size)
        assertEquals("Bryan Cranston", result.tvSeriesCasts?.first()?.name)
        assertEquals(10L, result.tvSeriesCasts?.first()?.movieId)
    }

    @Test
    fun `Should map crew from credits`() = runTest {
        val dto = TvShowDto(
            id = 10,
            credits = CreditsDto(
                id = 200,
                crew = listOf(
                    CrewDto(name = "Vince Gilligan", job = "Creator", profilePath = "/vince.jpg"),
                ),
            ),
        )

        val result = mapper.map(dto)

        assertEquals(1, result.movieCrewList?.size)
        assertEquals("Vince Gilligan", result.movieCrewList?.first()?.name)
        assertEquals("Creator", result.movieCrewList?.first()?.job)
    }

    @Test
    fun `Should map videos with correct types`() = runTest {
        val dto = TvShowDto(
            id = 10,
            videos = VideoResultsDto(
                results = listOf(
                    VideoDto(key = "abc", type = "Trailer", name = "Official"),
                    VideoDto(key = "def", type = "Teaser", name = "Teaser"),
                ),
            ),
        )

        val result = mapper.map(dto)

        assertEquals(2, result.videos?.size)
        assertEquals(VideoType.TRAILER, result.videos!![0].type)
        assertEquals(VideoType.TEASER, result.videos!![1].type)
    }

    @Test
    fun `Should return empty lists when credits and videos are null`() = runTest {
        val dto = TvShowDto(id = 1, credits = null, videos = null)

        val result = mapper.map(dto)

        assertTrue(result.tvSeriesCasts?.isEmpty() == true)
        assertTrue(result.movieCrewList?.isEmpty() == true)
        assertTrue(result.videos?.isEmpty() == true)
    }

    @Test
    fun `Should handle null dates gracefully`() = runTest {
        val dto = TvShowDto(id = 1, firstAirDate = null, lastAirDate = null)

        val result = mapper.map(dto)

        assertNull(result.tvSeriesData._firstAirDate)
        assertNull(result.tvSeriesData._lastAirDate)
    }
}
