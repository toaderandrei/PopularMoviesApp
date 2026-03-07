package com.ant.network.mappers.movies

import com.ant.models.entities.MovieData
import com.ant.network.dto.MovieDto
import com.ant.network.extensions.parseDate
import com.ant.network.mappers.Mapper

/** Maps a single [MovieDto] to a [MovieData] domain model. */
class MovieDataMapper : Mapper<MovieDto, MovieData> {
    override suspend fun map(from: MovieDto): MovieData {
        return MovieData(
            id = from.id?.toLong() ?: 0L,
            name = from.title,
            originalTitle = from.originalTitle,
            voteCount = from.voteCount,
            overview = from.overview,
            backDropPath = from.backdropPath,
            posterPath = from.posterPath,
            voteAverage = from.voteAverage,
            _releaseDate = from.releaseDate?.parseDate(),
            _genres_ids = from.genreIds,
            _genres = emptyList(),
        )
    }
}
