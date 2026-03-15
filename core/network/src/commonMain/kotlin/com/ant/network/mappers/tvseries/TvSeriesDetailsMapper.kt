package com.ant.network.mappers.tvseries

import com.ant.models.entities.MovieCast
import com.ant.models.entities.MovieCrew
import com.ant.models.entities.MovieVideo
import com.ant.models.entities.TvShowDetails
import com.ant.models.entities.VideoType
import com.ant.network.dto.TvShowDto
import com.ant.network.extensions.parseDate
import com.ant.network.mappers.Mapper

/**
 * Maps a detailed [TvShowDto] (with credits, videos) to a [TvShowDetails] domain model.
 *
 * Delegates base TV show data mapping to [TvSeriesDataMapper] and additionally maps
 * cast, crew, and videos.
 */
class TvSeriesDetailsMapper(
    private val tvSeriesDataMapper: TvSeriesDataMapper,
) : Mapper<TvShowDto, TvShowDetails> {
    override suspend fun map(from: TvShowDto): TvShowDetails {
        var tvSeries = tvSeriesDataMapper.map(from)
        tvSeries = tvSeries.copy(
            numberOfEpisodes = from.numberOfEpisodes,
            _firstAirDate = from.firstAirDate?.parseDate(),
            _lastAirDate = from.lastAirDate?.parseDate(),
            numberOfSeasons = from.numberOfSeasons,
            status = from.status,
        )

        val castList = from.credits?.cast?.map { cast ->
            MovieCast(
                movieId = tvSeries.id,
                creditId = from.credits.id,
                cast_id = cast.castId,
                name = cast.name,
                order = cast.order,
                profileImagePath = cast.profilePath,
            )
        } ?: emptyList()

        val crewList = from.credits?.crew?.map { crew ->
            MovieCrew(
                movieId = tvSeries.id,
                creditsId = from.credits.id,
                name = crew.name,
                job = crew.job,
                profilePath = crew.profilePath,
            )
        } ?: emptyList()

        val videos = from.videos?.results?.map { video ->
            MovieVideo(
                movieId = tvSeries.id,
                key = video.key,
                iso_639_1 = video.iso6391,
                iso_3166_1 = video.iso31661,
                size = video.size,
                name = video.name,
                type = video.type?.toVideoType(),
                site = video.site,
            )
        } ?: emptyList()

        return TvShowDetails(tvSeries, videos, castList, crewList)
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
