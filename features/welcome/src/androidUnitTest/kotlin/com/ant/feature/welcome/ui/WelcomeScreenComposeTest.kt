package com.ant.feature.welcome.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.ant.feature.welcome.WelcomeUiState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.junit.Assert.assertTrue
import java.util.concurrent.atomic.AtomicBoolean

@RunWith(RobolectricTestRunner::class)
class WelcomeScreenComposeTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `Should display loading indicator when loading`() {
        composeTestRule.setContent {
            MaterialTheme {
                WelcomeScreen(
                    uiState = WelcomeUiState(isLoading = true),
                    onLoginClick = {},
                    onContinueAsGuestClick = {},
                )
            }
        }

        composeTestRule.onNodeWithTag("welcome_loading").assertIsDisplayed()
    }

    @Test
    fun `Should not display content when loading`() {
        composeTestRule.setContent {
            MaterialTheme {
                WelcomeScreen(
                    uiState = WelcomeUiState(isLoading = true),
                    onLoginClick = {},
                    onContinueAsGuestClick = {},
                )
            }
        }

        composeTestRule.onNodeWithTag("welcome_content").assertDoesNotExist()
    }

    @Test
    fun `Should display welcome content when not loading`() {
        composeTestRule.setContent {
            MaterialTheme {
                WelcomeScreen(
                    uiState = WelcomeUiState(isLoading = false),
                    onLoginClick = {},
                    onContinueAsGuestClick = {},
                )
            }
        }

        composeTestRule.onNodeWithTag("welcome_content").assertIsDisplayed()
    }

    @Test
    fun `Should display login button when not loading`() {
        composeTestRule.setContent {
            MaterialTheme {
                WelcomeScreen(
                    uiState = WelcomeUiState(isLoading = false),
                    onLoginClick = {},
                    onContinueAsGuestClick = {},
                )
            }
        }

        composeTestRule.onNodeWithTag("welcome_login_button").assertIsDisplayed()
    }

    @Test
    fun `Should display guest button when not loading`() {
        composeTestRule.setContent {
            MaterialTheme {
                WelcomeScreen(
                    uiState = WelcomeUiState(isLoading = false),
                    onLoginClick = {},
                    onContinueAsGuestClick = {},
                )
            }
        }

        composeTestRule.onNodeWithTag("welcome_guest_button").assertIsDisplayed()
    }

    @Test
    fun `Should invoke login callback on login button click`() {
        val loginClicked = AtomicBoolean(false)

        composeTestRule.setContent {
            MaterialTheme {
                WelcomeScreen(
                    uiState = WelcomeUiState(isLoading = false),
                    onLoginClick = { loginClicked.set(true) },
                    onContinueAsGuestClick = {},
                )
            }
        }

        composeTestRule.onNodeWithTag("welcome_login_button").performClick()
        assertTrue(loginClicked.get())
    }

    @Test
    fun `Should invoke guest callback on guest button click`() {
        val guestClicked = AtomicBoolean(false)

        composeTestRule.setContent {
            MaterialTheme {
                WelcomeScreen(
                    uiState = WelcomeUiState(isLoading = false),
                    onLoginClick = {},
                    onContinueAsGuestClick = { guestClicked.set(true) },
                )
            }
        }

        composeTestRule.onNodeWithTag("welcome_guest_button").performClick()
        assertTrue(guestClicked.get())
    }
}
