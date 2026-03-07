package com.ant.network.mappers.tvseries

import com.ant.models.entities.TvShow
import com.ant.network.dto.TvShowDto
import com.ant.network.extensions.parseDate
import com.ant.network.mappers.Mapper

/** Maps a single [TvShowDto] to a [TvShow] domain model. */
class TvSeriesDataMapper : Mapper<TvShowDto, TvShow> {
    override suspend fun map(from: TvShowDto): TvShow {
        return TvShow(
            id = from.id?.toLong() ?: 0L,
            name = from.name,
            originalTitle = from.originalName,
            voteCount = from.voteCount,
            overview = from.overview,
            backDropPath = from.backdropPath,
            posterPath = from.posterPath,
            originalLanguage = from.originalLanguage,
            voteAverage = from.voteAverage,
            _genres_ids = from.genreIds,
            _firstAirDate = from.firstAirDate?.parseDate(),
        )
    }
}
