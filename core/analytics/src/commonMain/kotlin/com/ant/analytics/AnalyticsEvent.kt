package com.ant.analytics

/**
 * Represents a single analytics event to be logged.
 *
 * @param type The screen or action type this event represents.
 * @param extras Optional key-value parameters attached to the event.
 */
data class AnalyticsEvent(
    val type: Types,
    val extras: List<Param> = emptyList(),
) {

    /** Predefined analytics event types corresponding to app screens. */
    enum class Types {
        MAIN_SCREEN,
        ACCOUNT_PROFILE,
        LOGIN_SCREEN,
        LOGOUT_SCREEN,
        MOVIES_DETAILS_SCREEN,
        MOVIE_LIST_SCREEN,
        TVSHOWS_LIST_SCREEN,
        TVSHOWS_SCREEN,
    }

    /** A single key-value parameter for an analytics event. */
    data class Param(val key: String, val value: String?)

    /** Predefined parameter keys used across analytics events. */
    enum class ParamKeys {
        MAIN_SCREEN,
        LOGIN_NAME,
        LOGOUT_USER,
        MOVIES_NAME,
        MOVIE_LIST_TYPE,
        TVSHOWS_NAME,
        TVSHOWS_LIST_TYPE,
    }
}
