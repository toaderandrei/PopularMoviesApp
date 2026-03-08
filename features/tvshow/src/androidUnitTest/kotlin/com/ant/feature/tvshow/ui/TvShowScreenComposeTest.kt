package com.ant.feature.tvshow.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.ant.feature.tvshow.TvShowSection
import com.ant.feature.tvshow.TvShowUiState
import com.ant.models.entities.TvShow
import com.ant.models.request.TvShowType
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TvShowScreenComposeTest {

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
    fun `Should display loading state when loading with empty sections`() {
        composeTestRule.setContent {
            MaterialTheme {
                TvShowScreen(
                    uiState = TvShowUiState(isLoading = true),
                    onTvShowClick = {},
                    onMoreClick = {},
                    onRefresh = {},
                )
            }
        }

        composeTestRule.onNodeWithTag("tvshow_loading").assertIsDisplayed()
    }

    @Test
    fun `Should not display sections when loading`() {
        composeTestRule.setContent {
            MaterialTheme {
                TvShowScreen(
                    uiState = TvShowUiState(isLoading = true),
                    onTvShowClick = {},
                    onMoreClick = {},
                    onRefresh = {},
                )
            }
        }

        composeTestRule.onNodeWithTag("tvshow_sections").assertDoesNotExist()
    }

    @Test
    fun `Should display error state when error with empty sections`() {
        composeTestRule.setContent {
            MaterialTheme {
                TvShowScreen(
                    uiState = TvShowUiState(error = "Connection failed"),
                    onTvShowClick = {},
                    onMoreClick = {},
                    onRefresh = {},
                )
            }
        }

        composeTestRule.onNodeWithTag("tvshow_error").assertIsDisplayed()
        composeTestRule.onNodeWithText("Connection failed").assertIsDisplayed()
    }

    @Test
    fun `Should display empty state when no sections`() {
        composeTestRule.setContent {
            MaterialTheme {
                TvShowScreen(
                    uiState = TvShowUiState(),
                    onTvShowClick = {},
                    onMoreClick = {},
                    onRefresh = {},
                )
            }
        }

        composeTestRule.onNodeWithTag("tvshow_empty").assertIsDisplayed()
        composeTestRule.onNodeWithText("No TV shows found").assertIsDisplayed()
    }

    @Test
    fun `Should display sections when content available`() {
        composeTestRule.setContent {
            MaterialTheme {
                TvShowScreen(
                    uiState = TvShowUiState(
                        tvShowSections = mapOf(
                            TvShowType.POPULAR to TvShowSection(
                                category = TvShowType.POPULAR,
                                tvShows = listOf(testTvShow()),
                            ),
                        ),
                    ),
                    onTvShowClick = {},
                    onMoreClick = {},
                    onRefresh = {},
                )
            }
        }

        composeTestRule.onNodeWithTag("tvshow_sections").assertIsDisplayed()
    }

    @Test
    fun `Should display section title for popular tv shows`() {
        composeTestRule.setContent {
            MaterialTheme {
                TvShowScreen(
                    uiState = TvShowUiState(
                        tvShowSections = mapOf(
                            TvShowType.POPULAR to TvShowSection(
                                category = TvShowType.POPULAR,
                                tvShows = listOf(testTvShow()),
                            ),
                        ),
                    ),
                    onTvShowClick = {},
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
                TvShowScreen(
                    uiState = TvShowUiState(
                        tvShowSections = mapOf(
                            TvShowType.POPULAR to TvShowSection(
                                category = TvShowType.POPULAR,
                                tvShows = listOf(testTvShow()),
                            ),
                        ),
                    ),
                    onTvShowClick = {},
                    onMoreClick = {},
                    onRefresh = {},
                )
            }
        }

        composeTestRule.onNodeWithTag("tvshow_error").assertDoesNotExist()
        composeTestRule.onNodeWithTag("tvshow_loading").assertDoesNotExist()
        composeTestRule.onNodeWithTag("tvshow_empty").assertDoesNotExist()
    }
}
