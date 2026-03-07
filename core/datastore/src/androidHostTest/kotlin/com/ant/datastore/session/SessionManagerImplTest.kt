package com.ant.datastore.session

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import app.cash.turbine.turbineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class SessionManagerImplTest {

    private lateinit var testFile: File
    private lateinit var datastoreScope: CoroutineScope
    private lateinit var sessionManager: SessionManagerImpl

    @BeforeTest
    fun setup() {
        testFile = File.createTempFile("test_prefs_", ".preferences_pb")
        testFile.delete()
        datastoreScope = CoroutineScope(UnconfinedTestDispatcher() + Job())
        val dataStore = PreferenceDataStoreFactory.create(
            scope = datastoreScope,
            produceFile = { testFile },
        )
        sessionManager = SessionManagerImpl(dataStore, datastoreScope)
    }

    @AfterTest
    fun teardown() {
        datastoreScope.cancel()
        testFile.delete()
    }

    @Test
    fun `Should save and retrieve session id`() = runTest {
        val result = sessionManager.saveSessionId("test_session_123")

        assertTrue(result)
        assertEquals("test_session_123", sessionManager.getSessionId())
    }

    @Test
    fun `Should return false when saving null session id`() = runTest {
        val result = sessionManager.saveSessionId(null)

        assertFalse(result)
        assertNull(sessionManager.getSessionId())
    }

    @Test
    fun `Should save and retrieve username`() = runTest {
        val result = sessionManager.saveUsername("john_doe")

        assertTrue(result)
        assertEquals("john_doe", sessionManager.getUsername())
    }

    @Test
    fun `Should return false when saving null username`() = runTest {
        val result = sessionManager.saveUsername(null)

        assertFalse(result)
        assertNull(sessionManager.getUsername())
    }

    @Test
    fun `Should clear session and sign out when session exists`() = runTest {
        sessionManager.saveSessionId("session_to_clear")
        sessionManager.saveUsername("user_to_clear")

        val result = sessionManager.clearSessionAndSignOut()

        assertTrue(result)
        assertNull(sessionManager.getSessionId())
        assertNull(sessionManager.getUsername())
    }

    @Test
    fun `Should return false when clearing session with no existing session`() = runTest {
        val result = sessionManager.clearSessionAndSignOut()

        assertFalse(result)
    }

    @Test
    fun `Should emit true for authentication status when session is saved`() = runTest {
        turbineScope {
            val turbine = sessionManager.getUserAuthenticationStatus().testIn(this)

            turbine.skipItems(1) // skip initial null
            sessionManager.saveSessionId("auth_session")
            assertEquals(true, turbine.awaitItem())

            turbine.cancel()
        }
    }

    @Test
    fun `Should enable guest mode`() = runTest {
        val result = sessionManager.setGuestMode(true)

        assertTrue(result)

        turbineScope {
            val turbine = sessionManager.isGuestMode().testIn(this)
            assertEquals(true, turbine.awaitItem())
            turbine.cancel()
        }
    }

    @Test
    fun `Should return false for guest mode by default`() = runTest {
        turbineScope {
            val turbine = sessionManager.isGuestMode().testIn(this)
            assertEquals(false, turbine.awaitItem())
            turbine.cancel()
        }
    }

    @Test
    fun `Should disable guest mode after enabling`() = runTest {
        sessionManager.setGuestMode(true)
        sessionManager.setGuestMode(false)

        turbineScope {
            val turbine = sessionManager.isGuestMode().testIn(this)
            assertEquals(false, turbine.awaitItem())
            turbine.cancel()
        }
    }

    @Test
    fun `Should report can skip authentication when session exists`() = runTest {
        turbineScope {
            val turbine = sessionManager.canSkipAuthentication().testIn(this)

            turbine.skipItems(1) // skip initial null
            sessionManager.saveSessionId("skip_session")
            assertEquals(true, turbine.awaitItem())

            turbine.cancel()
        }
    }
}
