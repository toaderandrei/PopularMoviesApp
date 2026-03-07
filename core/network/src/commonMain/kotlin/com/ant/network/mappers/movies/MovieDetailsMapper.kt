package com.ant.network.mappers.movies

import com.ant.models.entities.MovieCast
import com.ant.models.entities.MovieCrew
import com.ant.models.entities.MovieDetails
import com.ant.models.entities.MovieReview
import com.ant.models.entities.MovieVideo
import com.ant.models.entities.VideoType
import com.ant.network.dto.MovieDto
import com.ant.network.mappers.Mapper

/**
 * Maps a detailed [MovieDto] (with credits, reviews, videos) to a [MovieDetails] domain model.
 *
 * Delegates base movie data mapping to [MovieDataMapper] and additionally maps
 * cast, crew, reviews, and videos.
 */
class MovieDetailsMapper(
    private val movieDataMapper: MovieDataMapper,
) : Mapper<MovieDto, MovieDetails> {
    override suspend fun map(from: MovieDto): MovieDetails {
        var movieData = movieDataMapper.map(from)
        movieData = movieData.copy(
            runtime = from.runtime?.toString(),
            originalLanguage = from.originalLanguage,
        )

        val movieReviews = from.reviews?.results?.map { review ->
            MovieReview(
                tmdbId = from.id,
                content = review.content,
                url = review.url,
                movieId = 0,
                name = review.author,
            )
        } ?: emptyList()

        val castList = from.credits?.cast?.map { cast ->
            MovieCast(
                movieId = movieData.id,
                creditId = from.credits?.id,
                cast_id = cast.castId,
                name = cast.name,
                order = cast.order,
                profileImagePath = cast.profilePath,
            )
        } ?: emptyList()

        val crewList = from.credits?.crew?.map { crew ->
            MovieCrew(
                movieId = movieData.id,
                creditsId = from.credits?.id,
                name = crew.name,
                job = crew.job,
                profilePath = crew.profilePath,
            )
        } ?: emptyList()

        val videos = from.videos?.results?.map { video ->
            MovieVideo(
                movieId = movieData.id,
                key = video.key,
                iso_639_1 = video.iso6391,
                iso_3166_1 = video.iso31661,
                size = video.size,
                name = video.name,
                type = video.type?.toVideoType(),
                site = video.site,
            )
        } ?: emptyList()

        return MovieDetails(movieData, videos, castList, movieReviews, crewList)
    }
}

private fun String.toVideoType(): VideoType? {
    return when (this.lowercase()) {
        "clip" -> VideoType.CLIP
        "featurette" -> VideoType.FEATURETTE
        "opening_credits" -> VideoType.OPENING_CREDITS
        "teaser" -> VideoType.TEASER
        "trailer" -> VideoType.TRAILER
        else -> null
    }
}
