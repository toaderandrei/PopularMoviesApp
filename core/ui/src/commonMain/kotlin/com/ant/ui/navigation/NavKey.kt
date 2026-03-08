package com.ant.ui.navigation

import kotlinx.serialization.Serializable

/**
 * Base interface for all navigation routes in the app.
 *
 * All route classes must:
 * 1. Implement this interface
 * 2. Be annotated with @Serializable
 * 3. Be registered in the app's savedStateConfiguration
 *
 * Example:
 * ```
 * @Serializable
 * data object MoviesRoute : NavKey
 *
 * @Serializable
 * data class MovieDetailsRoute(val movieId: Int) : NavKey
 * ```
 */
@Serializable
sealed interface NavKey
