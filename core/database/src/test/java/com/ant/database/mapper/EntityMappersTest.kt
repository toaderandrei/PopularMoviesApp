package com.ant.database.mapper

import com.ant.database.entity.MovieCastEntity
import com.ant.database.entity.MovieCrewEntity
import com.ant.database.entity.MovieDataEntity
import com.ant.database.entity.MovieReviewEntity
import com.ant.database.entity.MovieVideoEntity
import com.ant.database.entity.TvShowEntity
import com.ant.models.entities.MovieCast
import com.ant.models.entities.MovieCrew
import com.ant.models.entities.MovieData
import com.ant.models.entities.MovieReview
import com.ant.models.entities.MovieVideo
import com.ant.models.entities.TvShow
import com.ant.models.entities.VideoType
import kotlinx.datetime.Clock
import org.junit.Assert.assertEquals
import org.junit.Test

class EntityMappersTest {

    // region MovieData round-trip

    @Test
    fun `Should map MovieDataEntity to MovieData and back`() {
        val date = Clock.System.now()
        val entity = MovieDataEntity(
            id = 42,
            name = "Test Movie",
            originalTitle = "Original",
            homepage = "https://example.com",
            voteCount = 100,
            overview = "A great movie",
            runtime = "120",
            posterPath = "/poster.jpg",
            backDropPath = "/backdrop.jpg",
            _releaseDate = date,
            voteAverage = 8.5,
            originalLanguage = "en",
            _genres = listOf("Action", "Drama"),
            _genres_ids = listOf(28, 18),
            favored = true,
            syncedToRemote = false,
        )

        val domain = entity.toDomain()
        val roundTripped = domain.toEntity()

        assertEquals(entity, roundTripped)
    }

    @Test
    fun `Should map MovieData to MovieDataEntity and back`() {
        val date = Clock.System.now()
        val domain = MovieData(
            id = 42,
            name = "Test Movie",
            originalTitle = "Original",
            homepage = "https://example.com",
            voteCount = 100,
            overview = "A great movie",
            runtime = "120",
            posterPath = "/poster.jpg",
            backDropPath = "/backdrop.jpg",
            _releaseDate = date,
            voteAverage = 8.5,
            originalLanguage = "en",
            _genres = listOf("Action", "Drama"),
            _genres_ids = listOf(28, 18),
            favored = true,
            syncedToRemote = false,
        )

        val entity = domain.toEntity()
        val roundTripped = entity.toDomain()

        assertEquals(domain.id, roundTripped.id)
        assertEquals(domain.name, roundTripped.name)
        assertEquals(domain.originalTitle, roundTripped.originalTitle)
        assertEquals(domain.homepage, roundTripped.homepage)
        assertEquals(domain.voteCount, roundTripped.voteCount)
        assertEquals(domain.overview, roundTripped.overview)
        assertEquals(domain.runtime, roundTripped.runtime)
        assertEquals(domain.posterPath, roundTripped.posterPath)
        assertEquals(domain.backDropPath, roundTripped.backDropPath)
        assertEquals(domain._releaseDate, roundTripped._releaseDate)
        assertEquals(domain.voteAverage, roundTripped.voteAverage)
        assertEquals(domain.originalLanguage, roundTripped.originalLanguage)
        assertEquals(domain._genres, roundTripped._genres)
        assertEquals(domain._genres_ids, roundTripped._genres_ids)
        assertEquals(domain.favored, roundTripped.favored)
        assertEquals(domain.syncedToRemote, roundTripped.syncedToRemote)
    }

    // endregion

    // region TvShow round-trip

    @Test
    fun `Should map TvShowEntity to TvShow and back`() {
        val firstAirDate = Clock.System.now()
        val lastAirDate = Clock.System.now()
        val entity = TvShowEntity(
            id = 10,
            name = "Test Show",
            originalTitle = "Original Show",
            voteCount = 50,
            overview = "A great show",
            voteAverage = 9.0,
            backDropPath = "/backdrop.jpg",
            posterPath = "/poster.jpg",
            originalLanguage = "en",
            status = "Returning Series",
            _firstAirDate = firstAirDate,
            _lastAirDate = lastAirDate,
            numberOfSeasons = 3,
            numberOfEpisodes = 30,
            _genres_ids = listOf(10, 20),
            _genres = listOf("Comedy", "Drama"),
            favored = false,
            syncedToRemote = true,
        )

        val domain = entity.toDomain()
        val roundTripped = domain.toEntity()

        assertEquals(entity, roundTripped)
    }

    @Test
    fun `Should map TvShow to TvShowEntity and back`() {
        val domain = TvShow(
            id = 10,
            name = "Test Show",
            originalTitle = "Original Show",
            voteCount = 50,
            overview = "A great show",
            voteAverage = 9.0,
            backDropPath = "/backdrop.jpg",
            posterPath = "/poster.jpg",
            originalLanguage = "en",
            status = "Returning Series",
            numberOfSeasons = 3,
            numberOfEpisodes = 30,
            _genres_ids = listOf(10, 20),
            _genres = listOf("Comedy", "Drama"),
            favored = false,
            syncedToRemote = true,
        )

        val entity = domain.toEntity()
        val roundTripped = entity.toDomain()

        assertEquals(domain.id, roundTripped.id)
        assertEquals(domain.name, roundTripped.name)
        assertEquals(domain.voteAverage, roundTripped.voteAverage)
        assertEquals(domain.favored, roundTripped.favored)
        assertEquals(domain.syncedToRemote, roundTripped.syncedToRemote)
    }

    // endregion

    // region MovieCast round-trip

    @Test
    fun `Should map MovieCastEntity to MovieCast and back`() {
        val entity = MovieCastEntity(
            id = 1,
            creditId = 100,
            cast_id = 200,
            movieId = 42,
            name = "John Doe",
            order = 1,
            profileImagePath = "/profile.jpg",
        )

        val domain = entity.toDomain()
        val roundTripped = domain.toEntity()

        assertEquals(entity, roundTripped)
    }

    // endregion

    // region MovieCrew round-trip

    @Test
    fun `Should map MovieCrewEntity to MovieCrew and back`() {
        val entity = MovieCrewEntity(
            id = 5,
            creditsId = 300,
            movieId = 42,
            name = "Directing",
            job = "Director",
            profilePath = "/crew.jpg",
        )

        val domain = entity.toDomain()
        val roundTripped = domain.toEntity()

        assertEquals(entity, roundTripped)
    }

    // endregion

    // region MovieReview round-trip

    @Test
    fun `Should map MovieReviewEntity to MovieReview and back`() {
        val entity = MovieReviewEntity(
            id = 7,
            name = "Reviewer",
            content = "Great movie!",
            url = "https://review.com",
            movieId = 42,
            tmdbId = 999,
        )

        val domain = entity.toDomain()
        val roundTripped = domain.toEntity()

        assertEquals(entity, roundTripped)
    }

    // endregion

    // region MovieVideo round-trip

    @Test
    fun `Should map MovieVideoEntity to MovieVideo and back`() {
        val entity = MovieVideoEntity(
            id = 3,
            key = "abc123",
            name = "Official Trailer",
            iso_639_1 = "en",
            iso_3166_1 = "US",
            site = "YouTube",
            size = 1080,
            type = VideoType.TRAILER,
            movieId = 42,
        )

        val domain = entity.toDomain()
        val roundTripped = domain.toEntity()

        assertEquals(entity, roundTripped)
    }

    // endregion

    // region Null handling

    @Test
    fun `Should map MovieDataEntity with null fields to domain and back`() {
        val entity = MovieDataEntity()
        val domain = entity.toDomain()
        val roundTripped = domain.toEntity()

        assertEquals(entity, roundTripped)
    }

    @Test
    fun `Should map TvShowEntity with default fields to domain and back`() {
        val entity = TvShowEntity()
        val domain = entity.toDomain()
        val roundTripped = domain.toEntity()

        assertEquals(entity, roundTripped)
    }

    // endregion
}
