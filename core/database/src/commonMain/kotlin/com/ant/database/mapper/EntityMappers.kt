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

// region MovieData <-> MovieDataEntity

/** Converts a [MovieDataEntity] to its domain model [MovieData]. */
fun MovieDataEntity.toDomain(): MovieData = MovieData(
    id = id,
    name = name,
    originalTitle = originalTitle,
    homepage = homepage,
    voteCount = voteCount,
    overview = overview,
    runtime = runtime,
    posterPath = posterPath,
    backDropPath = backDropPath,
    _releaseDate = _releaseDate,
    voteAverage = voteAverage,
    originalLanguage = originalLanguage,
    _genres = _genres,
    _genres_ids = _genres_ids,
    favored = favored,
    syncedToRemote = syncedToRemote,
)

/** Converts a [MovieData] domain model to its database entity [MovieDataEntity]. */
fun MovieData.toEntity(): MovieDataEntity = MovieDataEntity(
    id = id,
    name = name,
    originalTitle = originalTitle,
    homepage = homepage,
    voteCount = voteCount,
    overview = overview,
    runtime = runtime,
    posterPath = posterPath,
    backDropPath = backDropPath,
    _releaseDate = _releaseDate,
    voteAverage = voteAverage,
    originalLanguage = originalLanguage,
    _genres = _genres,
    _genres_ids = _genres_ids,
    favored = favored,
    syncedToRemote = syncedToRemote,
)

// endregion

// region TvShow <-> TvShowEntity

/** Converts a [TvShowEntity] to its domain model [TvShow]. */
fun TvShowEntity.toDomain(): TvShow = TvShow(
    id = id,
    name = name,
    originalTitle = originalTitle,
    voteCount = voteCount,
    overview = overview,
    voteAverage = voteAverage,
    backDropPath = backDropPath,
    posterPath = posterPath,
    originalLanguage = originalLanguage,
    status = status,
    _firstAirDate = _firstAirDate,
    _lastAirDate = _lastAirDate,
    numberOfSeasons = numberOfSeasons,
    numberOfEpisodes = numberOfEpisodes,
    _genres_ids = _genres_ids,
    _genres = _genres,
    favored = favored,
    syncedToRemote = syncedToRemote,
)

/** Converts a [TvShow] domain model to its database entity [TvShowEntity]. */
fun TvShow.toEntity(): TvShowEntity = TvShowEntity(
    id = id,
    name = name,
    originalTitle = originalTitle,
    voteCount = voteCount,
    overview = overview,
    voteAverage = voteAverage,
    backDropPath = backDropPath,
    posterPath = posterPath,
    originalLanguage = originalLanguage,
    status = status,
    _firstAirDate = _firstAirDate,
    _lastAirDate = _lastAirDate,
    numberOfSeasons = numberOfSeasons,
    numberOfEpisodes = numberOfEpisodes,
    _genres_ids = _genres_ids,
    _genres = _genres,
    favored = favored,
    syncedToRemote = syncedToRemote,
)

// endregion

// region MovieCast <-> MovieCastEntity

/** Converts a [MovieCastEntity] to its domain model [MovieCast]. */
fun MovieCastEntity.toDomain(): MovieCast = MovieCast(
    id = id,
    creditId = creditId,
    cast_id = cast_id,
    movieId = movieId,
    name = name,
    order = order,
    profileImagePath = profileImagePath,
)

/** Converts a [MovieCast] domain model to its database entity [MovieCastEntity]. */
fun MovieCast.toEntity(): MovieCastEntity = MovieCastEntity(
    id = id,
    creditId = creditId,
    cast_id = cast_id,
    movieId = movieId,
    name = name,
    order = order,
    profileImagePath = profileImagePath,
)

// endregion

// region MovieCrew <-> MovieCrewEntity

/** Converts a [MovieCrewEntity] to its domain model [MovieCrew]. */
fun MovieCrewEntity.toDomain(): MovieCrew = MovieCrew(
    id = id,
    creditsId = creditsId,
    movieId = movieId,
    name = name,
    job = job,
    profilePath = profilePath,
)

/** Converts a [MovieCrew] domain model to its database entity [MovieCrewEntity]. */
fun MovieCrew.toEntity(): MovieCrewEntity = MovieCrewEntity(
    id = id,
    creditsId = creditsId,
    movieId = movieId,
    name = name,
    job = job,
    profilePath = profilePath,
)

// endregion

// region MovieReview <-> MovieReviewEntity

/** Converts a [MovieReviewEntity] to its domain model [MovieReview]. */
fun MovieReviewEntity.toDomain(): MovieReview = MovieReview(
    id = id,
    name = name,
    content = content,
    url = url,
    movieId = movieId,
    tmdbId = tmdbId,
)

/** Converts a [MovieReview] domain model to its database entity [MovieReviewEntity]. */
fun MovieReview.toEntity(): MovieReviewEntity = MovieReviewEntity(
    id = id,
    name = name,
    content = content,
    url = url,
    movieId = movieId,
    tmdbId = tmdbId,
)

// endregion

// region MovieVideo <-> MovieVideoEntity

/** Converts a [MovieVideoEntity] to its domain model [MovieVideo]. */
fun MovieVideoEntity.toDomain(): MovieVideo = MovieVideo(
    id = id,
    key = key,
    name = name,
    iso_639_1 = iso_639_1,
    iso_3166_1 = iso_3166_1,
    site = site,
    size = size,
    type = type,
    movieId = movieId,
)

/** Converts a [MovieVideo] domain model to its database entity [MovieVideoEntity]. */
fun MovieVideo.toEntity(): MovieVideoEntity = MovieVideoEntity(
    id = id,
    key = key,
    name = name,
    iso_639_1 = iso_639_1,
    iso_3166_1 = iso_3166_1,
    site = site,
    size = size,
    type = type,
    movieId = movieId,
)

// endregion
