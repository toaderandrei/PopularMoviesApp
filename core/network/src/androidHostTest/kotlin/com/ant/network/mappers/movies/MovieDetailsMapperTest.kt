package com.ant.network.mappers.movies

import com.ant.models.entities.VideoType
import com.ant.network.dto.CastDto
import com.ant.network.dto.CreditsDto
import com.ant.network.dto.CrewDto
import com.ant.network.dto.MovieDto
import com.ant.network.dto.ReviewDto
import com.ant.network.dto.ReviewResultsDto
import com.ant.network.dto.VideoDto
import com.ant.network.dto.VideoResultsDto
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MovieDetailsMapperTest {

    private val movieDataMapper = MovieDataMapper()
    private val mapper = MovieDetailsMapper(movieDataMapper)

    @Test
    fun `Should map runtime and original language from dto`() = runTest {
        val dto = MovieDto(
            id = 1,
            title = "Test",
            runtime = 120,
            originalLanguage = "en",
        )

        val result = mapper.map(dto)

        assertEquals("120", result.movieData.runtime)
        assertEquals("en", result.movieData.originalLanguage)
    }

    @Test
    fun `Should map reviews from dto`() = runTest {
        val dto = MovieDto(
            id = 42,
            reviews = ReviewResultsDto(
                results = listOf(
                    ReviewDto(author = "Alice", content = "Great movie!", url = "https://review.com"),
                ),
            ),
        )

        val result = mapper.map(dto)

        assertEquals(1, result.reviews?.size)
        val review = result.reviews!!.first()
        assertEquals("Alice", review.name)
        assertEquals("Great movie!", review.content)
        assertEquals("https://review.com", review.url)
        assertEquals(42, review.tmdbId)
        assertEquals(0L, review.movieId)
    }

    @Test
    fun `Should map cast from dto`() = runTest {
        val dto = MovieDto(
            id = 1,
            credits = CreditsDto(
                id = 100,
                cast = listOf(
                    CastDto(castId = 5, name = "Brad Pitt", order = 0, profilePath = "/brad.jpg"),
                ),
            ),
        )

        val result = mapper.map(dto)

        assertEquals(1, result.movieCasts?.size)
        val cast = result.movieCasts!!.first()
        assertEquals("Brad Pitt", cast.name)
        assertEquals(5, cast.cast_id)
        assertEquals(0, cast.order)
        assertEquals("/brad.jpg", cast.profileImagePath)
        assertEquals(100, cast.creditId)
    }

    @Test
    fun `Should map crew from dto`() = runTest {
        val dto = MovieDto(
            id = 1,
            credits = CreditsDto(
                id = 100,
                crew = listOf(
                    CrewDto(name = "David Fincher", job = "Director", profilePath = "/david.jpg"),
                ),
            ),
        )

        val result = mapper.map(dto)

        assertEquals(1, result.movieCrewList?.size)
        val crew = result.movieCrewList!!.first()
        assertEquals("David Fincher", crew.name)
        assertEquals("Director", crew.job)
        assertEquals("/david.jpg", crew.profilePath)
    }

    @Test
    fun `Should map video types correctly`() = runTest {
        val dto = MovieDto(
            id = 1,
            videos = VideoResultsDto(
                results = listOf(
                    VideoDto(key = "abc", type = "Trailer", name = "Official Trailer"),
                    VideoDto(key = "def", type = "Teaser", name = "Teaser"),
                    VideoDto(key = "ghi", type = "Clip", name = "Clip"),
                    VideoDto(key = "jkl", type = "Featurette", name = "Behind the Scenes"),
                    VideoDto(key = "mno", type = "Opening_Credits", name = "Opening"),
                ),
            ),
        )

        val result = mapper.map(dto)

        assertEquals(5, result.videos?.size)
        assertEquals(VideoType.TRAILER, result.videos!![0].type)
        assertEquals(VideoType.TEASER, result.videos!![1].type)
        assertEquals(VideoType.CLIP, result.videos!![2].type)
        assertEquals(VideoType.FEATURETTE, result.videos!![3].type)
        assertEquals(VideoType.OPENING_CREDITS, result.videos!![4].type)
    }

    @Test
    fun `Should return null for unknown video type`() = runTest {
        val dto = MovieDto(
            id = 1,
            videos = VideoResultsDto(
                results = listOf(
                    VideoDto(key = "xyz", type = "BehindTheScenes"),
                ),
            ),
        )

        val result = mapper.map(dto)

        assertNull(result.videos?.first()?.type)
    }

    @Test
    fun `Should handle null video type string`() = runTest {
        val dto = MovieDto(
            id = 1,
            videos = VideoResultsDto(
                results = listOf(VideoDto(key = "abc", type = null)),
            ),
        )

        val result = mapper.map(dto)

        assertNull(result.videos?.first()?.type)
    }

    @Test
    fun `Should return empty lists when credits and reviews are null`() = runTest {
        val dto = MovieDto(id = 1, credits = null, reviews = null, videos = null)

        val result = mapper.map(dto)

        assertTrue(result.movieCasts?.isEmpty() == true)
        assertTrue(result.movieCrewList?.isEmpty() == true)
        assertTrue(result.reviews?.isEmpty() == true)
        assertTrue(result.videos?.isEmpty() == true)
    }
}
