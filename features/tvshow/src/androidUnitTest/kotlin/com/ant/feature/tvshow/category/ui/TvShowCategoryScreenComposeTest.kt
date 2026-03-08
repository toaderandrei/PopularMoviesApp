package com.ant.feature.tvshow.category.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.ant.feature.tvshow.category.TvShowCategoryUiState
import com.ant.models.entities.TvShow
import com.ant.models.request.TvShowType
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.concurrent.atomic.AtomicBoolean

@RunWith(RobolectricTestRunner::class)
class TvShowCategoryScreenComposeTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun testTvShow(id: Long = 1, name: String = "Test Show") = TvShow(
        id = id,
        name = name,
        originalTitle = null,
        voteCount = null,
        overview = null,
        voteAverage = 8.0,
        backDropPath = null,
        posterPath = null,
        originalLanguage = null,
    )

    @Test
    fun `Should display loading state when loading`() {
        composeTestRule.setContent {
            MaterialTheme {
                TvShowCategoryScreen(
                    uiState = TvShowCategoryUiState(
                        categoryType = TvShowType.POPULAR,
                        isLoading = true,
                    ),
                    onTvShowClick = {},
                    onNavigateBack = {},
                    onRefresh = {},
                    onLoadMore = {},
                )
            }
        }

        composeTestRule.onNodeWithTag("tvshow_category_loading").assertIsDisplayed()
    }

    @Test
    fun `Should display error message when error with empty list`() {
        composeTestRule.setContent {
            MaterialTheme {
                TvShowCategoryScreen(
                    uiState = TvShowCategoryUiState(
                        categoryType = TvShowType.POPULAR,
                        error = "Failed to load TV shows",
                    ),
                    onTvShowClick = {},
                    onNavigateBack = {},
                    onRefresh = {},
                    onLoadMore = {},
                )
            }
        }

        composeTestRule.onNodeWithTag("tvshow_category_error").assertIsDisplayed()
        composeTestRule.onNodeWithText("Failed to load TV shows").assertIsDisplayed()
    }

    @Test
    fun `Should display empty state when no tv shows`() {
        composeTestRule.setContent {
            MaterialTheme {
                TvShowCategoryScreen(
                    uiState = TvShowCategoryUiState(
                        categoryType = TvShowType.POPULAR,
                    ),
                    onTvShowClick = {},
                    onNavigateBack = {},
                    onRefresh = {},
                    onLoadMore = {},
                )
            }
        }

        composeTestRule.onNodeWithTag("tvshow_category_empty").assertIsDisplayed()
        composeTestRule.onNodeWithText("No TV shows found").assertIsDisplayed()
    }

    @Test
    fun `Should display tv show grid when content available`() {
        composeTestRule.setContent {
            MaterialTheme {
                TvShowCategoryScreen(
                    uiState = TvShowCategoryUiState(
                        categoryType = TvShowType.POPULAR,
                        tvShows = listOf(
                            testTvShow(id = 1, name = "Show 1"),
                            testTvShow(id = 2, name = "Show 2"),
                        ),
                        currentPage = 1,
                        totalPages = 1,
                    ),
                    onTvShowClick = {},
                    onNavigateBack = {},
                    onRefresh = {},
                    onLoadMore = {},
                )
            }
        }

        composeTestRule.onNodeWithTag("tvshow_category_grid").assertIsDisplayed()
    }

    @Test
    fun `Should display correct category title for popular`() {
        composeTestRule.setContent {
            MaterialTheme {
                TvShowCategoryScreen(
                    uiState = TvShowCategoryUiState(
                        categoryType = TvShowType.POPULAR,
                        isLoading = true,
                    ),
                    onTvShowClick = {},
                    onNavigateBack = {},
                    onRefresh = {},
                    onLoadMore = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Popular TV Shows").assertIsDisplayed()
    }

    @Test
    fun `Should display correct category title for airing today`() {
        composeTestRule.setContent {
            MaterialTheme {
                TvShowCategoryScreen(
                    uiState = TvShowCategoryUiState(
                        categoryType = TvShowType.AIRING_TODAY,
                        isLoading = true,
                    ),
                    onTvShowClick = {},
                    onNavigateBack = {},
                    onRefresh = {},
                    onLoadMore = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Airing Today").assertIsDisplayed()
    }

    @Test
    fun `Should invoke back callback on back button click`() {
        val backClicked = AtomicBoolean(false)

        composeTestRule.setContent {
            MaterialTheme {
                TvShowCategoryScreen(
                    uiState = TvShowCategoryUiState(
                        categoryType = TvShowType.POPULAR,
                        isLoading = true,
                    ),
                    onTvShowClick = {},
                    onNavigateBack = { backClicked.set(true) },
                    onRefresh = {},
                    onLoadMore = {},
                )
            }
        }

        composeTestRule.onNodeWithTag("tvshow_category_back").performClick()
        assertTrue(backClicked.get())
    }

    @Test
    fun `Should not display grid when loading`() {
        composeTestRule.setContent {
            MaterialTheme {
                TvShowCategoryScreen(
                    uiState = TvShowCategoryUiState(
                        categoryType = TvShowType.POPULAR,
                        isLoading = true,
                    ),
                    onTvShowClick = {},
                    onNavigateBack = {},
                    onRefresh = {},
                    onLoadMore = {},
                )
            }
        }

        composeTestRule.onNodeWithTag("tvshow_category_grid").assertDoesNotExist()
        composeTestRule.onNodeWithTag("tvshow_category_error").assertDoesNotExist()
        composeTestRule.onNodeWithTag("tvshow_category_empty").assertDoesNotExist()
    }
}
