package com.ant.network.resources

import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

@Serializable
@Resource("3/movie")
class MovieResources {
    @Serializable
    @Resource("popular")
    class Popular(val parent: MovieResources = MovieResources(), val page: Int)

    @Serializable
    @Resource("top_rated")
    class TopRated(val parent: MovieResources = MovieResources(), val page: Int)

    @Serializable
    @Resource("now_playing")
    class NowPlaying(val parent: MovieResources = MovieResources(), val page: Int)

    @Serializable
    @Resource("upcoming")
    class Upcoming(val parent: MovieResources = MovieResources(), val page: Int)

    @Serializable
    @Resource("{id}")
    class Details(
        val parent: MovieResources = MovieResources(),
        val id: Int,
        val append_to_response: String? = null,
    )
}

@Serializable
@Resource("3/tv")
class TvSeriesResources {
    @Serializable
    @Resource("popular")
    class Popular(val parent: TvSeriesResources = TvSeriesResources(), val page: Int)

    @Serializable
    @Resource("top_rated")
    class TopRated(val parent: TvSeriesResources = TvSeriesResources(), val page: Int)

    @Serializable
    @Resource("on_the_air")
    class OnTheAir(val parent: TvSeriesResources = TvSeriesResources(), val page: Int)

    @Serializable
    @Resource("airing_today")
    class AiringToday(val parent: TvSeriesResources = TvSeriesResources(), val page: Int)

    @Serializable
    @Resource("{id}")
    class Details(
        val parent: TvSeriesResources = TvSeriesResources(),
        val id: Int,
        val append_to_response: String? = null,
    )
}

@Serializable
@Resource("3/genre")
class GenreResources {
    @Serializable
    @Resource("movie/list")
    class MovieGenres(val parent: GenreResources = GenreResources())

    @Serializable
    @Resource("tv/list")
    class TvGenres(val parent: GenreResources = GenreResources())
}

@Serializable
@Resource("3/search")
class SearchResources {
    @Serializable
    @Resource("movie")
    class Movies(
        val parent: SearchResources = SearchResources(),
        val query: String,
        val page: Int = 1,
        val include_adult: Boolean = false,
    )

    @Serializable
    @Resource("tv")
    class TvShows(
        val parent: SearchResources = SearchResources(),
        val query: String,
        val page: Int = 1,
        val include_adult: Boolean = false,
    )
}

@Serializable
@Resource("3/authentication")
class AuthResources {
    @Serializable
    @Resource("token/new")
    class CreateToken(val parent: AuthResources = AuthResources())

    @Serializable
    @Resource("token/validate_with_login")
    class ValidateToken(val parent: AuthResources = AuthResources())

    @Serializable
    @Resource("session/new")
    class CreateSession(val parent: AuthResources = AuthResources())

    @Serializable
    @Resource("session")
    class DeleteSession(val parent: AuthResources = AuthResources())
}

@Serializable
@Resource("3/account")
class AccountResources(val session_id: String? = null) {
    @Serializable
    @Resource("{accountId}/favorite")
    class MarkFavorite(
        val parent: AccountResources = AccountResources(),
        val accountId: Int,
        val session_id: String,
    )

    @Serializable
    @Resource("{accountId}/favorite/movies")
    class FavoriteMovies(
        val parent: AccountResources = AccountResources(),
        val accountId: Int,
        val session_id: String,
        val page: Int = 1,
    )

    @Serializable
    @Resource("{accountId}/favorite/tv")
    class FavoriteTvShows(
        val parent: AccountResources = AccountResources(),
        val accountId: Int,
        val session_id: String,
        val page: Int = 1,
    )
}
