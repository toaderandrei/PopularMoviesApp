package com.ant.network.ktx

import com.ant.common.exceptions.NetworkError
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.engine.mock.respondOk
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.resources.Resources
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.resources.Resource
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs

@Serializable
@Resource("/test")
class TestResource

@Serializable
data class TestResponse(val message: String)

class SafeRequestTest {

    private val json = Json { ignoreUnknownKeys = true }
    private val jsonHeaders = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())

    private fun createClient(engine: MockEngine): HttpClient = HttpClient(engine) {
        install(ContentNegotiation) { json(json) }
        install(Resources)
        expectSuccess = false
    }

    // -------------------------------------------------------------------------
    // Success
    // -------------------------------------------------------------------------

    @Test
    fun `Should return success on HTTP 200 response`() = runTest {
        val engine = MockEngine {
            respond(
                content = """{"message":"ok"}""",
                status = HttpStatusCode.OK,
                headers = jsonHeaders,
            )
        }
        val client = createClient(engine)

        val result = client.safeResourceGet<TestResponse, TestResource>(
            resource = TestResource(),
            maxAttempts = 1,
            initialDelay = 0L,
        )

        assertEquals(TestResponse("ok"), result)
    }

    // -------------------------------------------------------------------------
    // Unauthorized errors (parameterized via helper)
    // -------------------------------------------------------------------------

    @Test
    fun `Should throw NetworkError Unauthorized on HTTP 401`() = runTest {
        assertUnauthorizedForStatus(HttpStatusCode.Unauthorized)
    }

    @Test
    fun `Should throw NetworkError Unauthorized on HTTP 403`() = runTest {
        assertUnauthorizedForStatus(HttpStatusCode.Forbidden)
    }

    private suspend fun assertUnauthorizedForStatus(status: HttpStatusCode) {
        val engine = MockEngine {
            respondError(status = status)
        }
        val client = createClient(engine)

        assertFailsWith<NetworkError.Unauthorized> {
            client.safeResourceGet<TestResponse, TestResource>(
                resource = TestResource(),
                maxAttempts = 1,
                initialDelay = 0L,
            )
        }
    }

    // -------------------------------------------------------------------------
    // Server errors (parameterized via helper)
    // -------------------------------------------------------------------------

    @Test
    fun `Should throw NetworkError ServerError on HTTP 500`() = runTest {
        assertServerErrorForStatus(HttpStatusCode.InternalServerError)
    }

    @Test
    fun `Should throw NetworkError ServerError on HTTP 503`() = runTest {
        assertServerErrorForStatus(HttpStatusCode.ServiceUnavailable)
    }

    private suspend fun assertServerErrorForStatus(status: HttpStatusCode) {
        val engine = MockEngine {
            respondError(status = status)
        }
        val client = createClient(engine)

        assertFailsWith<NetworkError.ServerError> {
            client.safeResourceGet<TestResponse, TestResource>(
                resource = TestResource(),
                maxAttempts = 1,
                initialDelay = 0L,
            )
        }
    }

    // -------------------------------------------------------------------------
    // Unknown error
    // -------------------------------------------------------------------------

    @Test
    fun `Should throw NetworkError Unknown on HTTP 404`() = runTest {
        val engine = MockEngine {
            respondError(status = HttpStatusCode.NotFound)
        }
        val client = createClient(engine)

        assertFailsWith<NetworkError.Unknown> {
            client.safeResourceGet<TestResponse, TestResource>(
                resource = TestResource(),
                maxAttempts = 1,
                initialDelay = 0L,
            )
        }
    }

    // -------------------------------------------------------------------------
    // Serialization error
    // -------------------------------------------------------------------------

    @Test
    fun `Should throw NetworkError Serialization on malformed JSON`() = runTest {
        val engine = MockEngine {
            respond(
                content = """not valid json""",
                status = HttpStatusCode.OK,
                headers = jsonHeaders,
            )
        }
        val client = createClient(engine)

        assertFailsWith<NetworkError.Serialization> {
            client.safeResourceGet<TestResponse, TestResource>(
                resource = TestResource(),
                maxAttempts = 1,
                initialDelay = 0L,
            )
        }
    }

    // -------------------------------------------------------------------------
    // Network exception
    // -------------------------------------------------------------------------

    @Test
    fun `Should throw NetworkError Unknown on network exception`() = runTest {
        val engine = MockEngine {
            throw java.io.IOException("Connection refused")
        }
        val client = createClient(engine)

        val error = assertFailsWith<NetworkError.Unknown> {
            client.safeResourceGet<TestResponse, TestResource>(
                resource = TestResource(),
                maxAttempts = 1,
                initialDelay = 0L,
            )
        }
        assertEquals("Connection refused", error.cause?.message)
    }

    // -------------------------------------------------------------------------
    // Retry: ServerError then success
    // -------------------------------------------------------------------------

    @Test
    fun `Should retry on ServerError and succeed on second attempt`() = runTest {
        var attempt = 0
        val engine = MockEngine {
            attempt++
            if (attempt == 1) {
                respondError(status = HttpStatusCode.InternalServerError)
            } else {
                respond(
                    content = """{"message":"recovered"}""",
                    status = HttpStatusCode.OK,
                    headers = jsonHeaders,
                )
            }
        }
        val client = createClient(engine)

        val result = client.safeResourceGet<TestResponse, TestResource>(
            resource = TestResource(),
            maxAttempts = 3,
            initialDelay = 0L,
        )

        assertEquals(TestResponse("recovered"), result)
        assertEquals(2, attempt)
    }

    // -------------------------------------------------------------------------
    // Retry: Unknown error then success
    // -------------------------------------------------------------------------

    @Test
    fun `Should retry on Unknown error and succeed on second attempt`() = runTest {
        var attempt = 0
        val engine = MockEngine {
            attempt++
            if (attempt == 1) {
                respondError(status = HttpStatusCode.NotFound)
            } else {
                respond(
                    content = """{"message":"found"}""",
                    status = HttpStatusCode.OK,
                    headers = jsonHeaders,
                )
            }
        }
        val client = createClient(engine)

        val result = client.safeResourceGet<TestResponse, TestResource>(
            resource = TestResource(),
            maxAttempts = 3,
            initialDelay = 0L,
        )

        assertEquals(TestResponse("found"), result)
        assertEquals(2, attempt)
    }

    // -------------------------------------------------------------------------
    // No retry on Unauthorized
    // -------------------------------------------------------------------------

    @Test
    fun `Should NOT retry on Unauthorized error`() = runTest {
        var attempt = 0
        val engine = MockEngine {
            attempt++
            respondError(status = HttpStatusCode.Unauthorized)
        }
        val client = createClient(engine)

        assertFailsWith<NetworkError.Unauthorized> {
            client.safeResourceGet<TestResponse, TestResource>(
                resource = TestResource(),
                maxAttempts = 3,
                initialDelay = 0L,
            )
        }

        assertEquals(1, attempt)
    }

    // -------------------------------------------------------------------------
    // Respect maxAttempts
    // -------------------------------------------------------------------------

    @Test
    fun `Should respect maxAttempts parameter`() = runTest {
        var attempt = 0
        val engine = MockEngine {
            attempt++
            respondError(status = HttpStatusCode.InternalServerError)
        }
        val client = createClient(engine)

        assertFailsWith<NetworkError.ServerError> {
            client.safeResourceGet<TestResponse, TestResource>(
                resource = TestResource(),
                maxAttempts = 5,
                initialDelay = 0L,
            )
        }

        assertEquals(5, attempt)
    }

    // -------------------------------------------------------------------------
    // Exponential backoff (verify attempt count)
    // -------------------------------------------------------------------------

    @Test
    fun `Should use exponential backoff delays`() = runTest {
        var attempt = 0
        val engine = MockEngine {
            attempt++
            if (attempt < 4) {
                respondError(status = HttpStatusCode.InternalServerError)
            } else {
                respond(
                    content = """{"message":"finally"}""",
                    status = HttpStatusCode.OK,
                    headers = jsonHeaders,
                )
            }
        }
        val client = createClient(engine)

        val result = client.safeResourceGet<TestResponse, TestResource>(
            resource = TestResource(),
            maxAttempts = 5,
            initialDelay = 100L,
            factor = 2.0,
            maxDelay = 10000L,
        )

        assertEquals(TestResponse("finally"), result)
        assertEquals(4, attempt)
    }
}
