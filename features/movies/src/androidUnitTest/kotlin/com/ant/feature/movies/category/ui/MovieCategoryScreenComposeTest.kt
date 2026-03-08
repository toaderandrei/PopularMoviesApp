package com.ant.feature.movies.category.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.ant.feature.movies.category.MovieCategoryUiState
import com.ant.models.entities.MovieData
import com.ant.models.request.MovieType
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.concurrent.atomic.AtomicBoolean

@RunWith(RobolectricTestRunner::class)
class MovieCategoryScreenComposeTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `Should display loading state when loading`() {
        composeTestRule.setContent {
            MaterialTheme {
                MovieCategoryScreen(
                    uiState = MovieCategoryUiState(
                        categoryType = MovieType.POPULAR,
                        isLoading = true,
                    ),
                    onMovieClick = {},
                    onNavigateBack = {},
                    onRefresh = {},
                    onLoadMore = {},
                )
            }
        }

        composeTestRule.onNodeWithTag("movie_category_loading").assertIsDisplayed()
    }

    @Test
    fun `Should display error message when error with empty list`() {
        composeTestRule.setContent {
            MaterialTheme {
                MovieCategoryScreen(
                    uiState = MovieCategoryUiState(
                        categoryType = MovieType.POPULAR,
                        error = "Failed to load movies",
                    ),
                    onMovieClick = {},
                    onNavigateBack = {},
                    onRefresh = {},
                    onLoadMore = {},
                )
            }
        }

        composeTestRule.onNodeWithTag("movie_category_error").assertIsDisplayed()
        composeTestRule.onNodeWithText("Failed to load movies").assertIsDisplayed()
    }

    @Test
    fun `Should display empty state when no movies`() {
        composeTestRule.setContent {
            MaterialTheme {
                MovieCategoryScreen(
                    uiState = MovieCategoryUiState(
                        categoryType = MovieType.POPULAR,
                    ),
                    onMovieClick = {},
                    onNavigateBack = {},
                    onRefresh = {},
                    onLoadMore = {},
                )
            }
        }

        composeTestRule.onNodeWithTag("movie_category_empty").assertIsDisplayed()
        composeTestRule.onNodeWithText("No movies found").assertIsDisplayed()
    }

    @Test
    fun `Should display movie grid when content available`() {
        composeTestRule.setContent {
            MaterialTheme {
                MovieCategoryScreen(
                    uiState = MovieCategoryUiState(
                        categoryType = MovieType.POPULAR,
                        movies = listOf(
                            MovieData(id = 1, name = "Movie 1"),
                            MovieData(id = 2, name = "Movie 2"),
                        ),
                        currentPage = 1,
                        totalPages = 1,
                    ),
                    onMovieClick = {},
                    onNavigateBack = {},
                    onRefresh = {},
                    onLoadMore = {},
                )
            }
        }

        composeTestRule.onNodeWithTag("movie_category_grid").assertIsDisplayed()
    }

    @Test
    fun `Should display correct category title for popular`() {
        composeTestRule.setContent {
            MaterialTheme {
                MovieCategoryScreen(
                    uiState = MovieCategoryUiState(
                        categoryType = MovieType.POPULAR,
                        isLoading = true,
                    ),
                    onMovieClick = {},
                    onNavigateBack = {},
                    onRefresh = {},
                    onLoadMore = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Popular Movies").assertIsDisplayed()
    }

    @Test
    fun `Should display correct category title for top rated`() {
        composeTestRule.setContent {
            MaterialTheme {
                MovieCategoryScreen(
                    uiState = MovieCategoryUiState(
                        categoryType = MovieType.TOP_RATED,
                        isLoading = true,
                    ),
                    onMovieClick = {},
                    onNavigateBack = {},
                    onRefresh = {},
                    onLoadMore = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Top Rated Movies").assertIsDisplayed()
    }

    @Test
    fun `Should invoke back callback on back button click`() {
        val backClicked = AtomicBoolean(false)

        composeTestRule.setContent {
            MaterialTheme {
                MovieCategoryScreen(
                    uiState = MovieCategoryUiState(
                        categoryType = MovieType.POPULAR,
                        isLoading = true,
                    ),
                    onMovieClick = {},
                    onNavigateBack = { backClicked.set(true) },
                    onRefresh = {},
                    onLoadMore = {},
                )
            }
        }

        composeTestRule.onNodeWithTag("movie_category_back").performClick()
        assertTrue(backClicked.get())
    }

    @Test
    fun `Should not display grid when loading`() {
        composeTestRule.setContent {
            MaterialTheme {
                MovieCategoryScreen(
                    uiState = MovieCategoryUiState(
                        categoryType = MovieType.POPULAR,
                        isLoading = true,
                    ),
                    onMovieClick = {},
                    onNavigateBack = {},
                    onRefresh = {},
                    onLoadMore = {},
                )
            }
        }

        composeTestRule.onNodeWithTag("movie_category_grid").assertDoesNotExist()
        composeTestRule.onNodeWithTag("movie_category_error").assertDoesNotExist()
        composeTestRule.onNodeWithTag("movie_category_empty").assertDoesNotExist()
    }
}
