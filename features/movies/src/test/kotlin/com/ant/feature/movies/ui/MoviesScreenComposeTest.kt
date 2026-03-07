package com.ant.feature.movies.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.ant.feature.movies.MovieSection
import com.ant.feature.movies.MoviesUiState
import com.ant.models.entities.MovieData
import com.ant.models.request.MovieType
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MoviesScreenComposeTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `Should display loading state when loading with empty sections`() {
        composeTestRule.setContent {
            MaterialTheme {
                MoviesScreen(
                    uiState = MoviesUiState(isLoading = true),
                    onMovieClick = {},
                    onMoreClick = {},
                    onRefresh = {},
                )
            }
        }

        composeTestRule.onNodeWithTag("movies_loading").assertIsDisplayed()
    }

    @Test
    fun `Should not display sections when loading`() {
        composeTestRule.setContent {
            MaterialTheme {
                MoviesScreen(
                    uiState = MoviesUiState(isLoading = true),
                    onMovieClick = {},
                    onMoreClick = {},
                    onRefresh = {},
                )
            }
        }

        composeTestRule.onNodeWithTag("movies_sections").assertDoesNotExist()
    }

    @Test
    fun `Should display error state when error with empty sections`() {
        composeTestRule.setContent {
            MaterialTheme {
                MoviesScreen(
                    uiState = MoviesUiState(error = "Network error"),
                    onMovieClick = {},
                    onMoreClick = {},
                    onRefresh = {},
                )
            }
        }

        composeTestRule.onNodeWithTag("movies_error").assertIsDisplayed()
        composeTestRule.onNodeWithText("Network error").assertIsDisplayed()
    }

    @Test
    fun `Should display empty state when no sections`() {
        composeTestRule.setContent {
            MaterialTheme {
                MoviesScreen(
                    uiState = MoviesUiState(),
                    onMovieClick = {},
                    onMoreClick = {},
                    onRefresh = {},
                )
            }
        }

        composeTestRule.onNodeWithTag("movies_empty").assertIsDisplayed()
        composeTestRule.onNodeWithText("No movies found").assertIsDisplayed()
    }

    @Test
    fun `Should display sections when content available`() {
        composeTestRule.setContent {
            MaterialTheme {
                MoviesScreen(
                    uiState = MoviesUiState(
                        movieSections = mapOf(
                            MovieType.POPULAR to MovieSection(
                                category = MovieType.POPULAR,
                                movies = listOf(
                                    MovieData(id = 1, name = "Test Movie"),
                                ),
                            ),
                        ),
                    ),
                    onMovieClick = {},
                    onMoreClick = {},
                    onRefresh = {},
                )
            }
        }

        composeTestRule.onNodeWithTag("movies_sections").assertIsDisplayed()
    }

    @Test
    fun `Should display section title for popular movies`() {
        composeTestRule.setContent {
            MaterialTheme {
                MoviesScreen(
                    uiState = MoviesUiState(
                        movieSections = mapOf(
                            MovieType.POPULAR to MovieSection(
                                category = MovieType.POPULAR,
                                movies = listOf(
                                    MovieData(id = 1, name = "Test Movie"),
                                ),
                            ),
                        ),
                    ),
                    onMovieClick = {},
                    onMoreClick = {},
                    onRefresh = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Popular").assertIsDisplayed()
    }

    @Test
    fun `Should not display error when sections have content`() {
        composeTestRule.setContent {
            MaterialTheme {
                MoviesScreen(
                    uiState = MoviesUiState(
                        movieSections = mapOf(
                            MovieType.POPULAR to MovieSection(
                                category = MovieType.POPULAR,
                                movies = listOf(
                                    MovieData(id = 1, name = "Test Movie"),
                                ),
                            ),
                        ),
                    ),
                    onMovieClick = {},
                    onMoreClick = {},
                    onRefresh = {},
                )
            }
        }

        composeTestRule.onNodeWithTag("movies_error").assertDoesNotExist()
        composeTestRule.onNodeWithTag("movies_loading").assertDoesNotExist()
        composeTestRule.onNodeWithTag("movies_empty").assertDoesNotExist()
    }
}
