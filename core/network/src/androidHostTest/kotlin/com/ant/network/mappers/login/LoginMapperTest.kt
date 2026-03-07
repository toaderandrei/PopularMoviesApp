package com.ant.network.mappers.login

import com.ant.network.dto.AccountDto
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class LoginMapperTest {

    private val mapper = LoginMapper()

    @Test
    fun `Should map account name to username`() = runTest {
        val dto = AccountDto(id = 1, name = "John Doe", username = "johndoe")

        val result = mapper.map(dto)

        assertEquals("John Doe", result.username)
    }

    @Test
    fun `Should handle null name`() = runTest {
        val dto = AccountDto(id = 1, name = null)

        val result = mapper.map(dto)

        assertNull(result.username)
    }

    @Test
    fun `Should not set session id`() = runTest {
        val dto = AccountDto(id = 1, name = "Test")

        val result = mapper.map(dto)

        assertNull(result.sessionId)
    }
}
