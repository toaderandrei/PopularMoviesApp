package com.ant.network.mappers.login

import com.ant.network.dto.SessionDto
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class LoginSessionMapperTest {

    private val mapper = LoginSessionMapper()

    @Test
    fun `Should map session id from SessionDto`() = runTest {
        val dto = SessionDto(success = true, sessionId = "abc123")

        val result = mapper.map(dto)

        assertEquals("abc123", result.sessionId)
    }

    @Test
    fun `Should handle null session id`() = runTest {
        val dto = SessionDto(success = false, sessionId = null)

        val result = mapper.map(dto)

        assertNull(result.sessionId)
    }

    @Test
    fun `Should not set username`() = runTest {
        val dto = SessionDto(success = true, sessionId = "session1")

        val result = mapper.map(dto)

        assertNull(result.username)
    }
}
