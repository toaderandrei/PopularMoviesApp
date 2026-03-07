package com.ant.network.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Creates a platform-specific [HttpClientEngine] for Ktor.
 *
 * Android uses OkHttp; iOS uses Darwin.
 */
expect fun createPlatformEngine(): HttpClientEngine

/**
 * Creates a pre-configured [HttpClient] for TMDb API requests.
 *
 * Includes JSON content negotiation (lenient, ignores unknown keys),
 * request logging, and the TMDb base URL with the API key appended to every request.
 *
 * @param apiKey TMDb API key added as a query parameter to all requests.
 * @param engine platform-specific HTTP engine; defaults to [createPlatformEngine].
 */
fun createTmdbHttpClient(apiKey: String, engine: HttpClientEngine = createPlatformEngine()): HttpClient {
    return HttpClient(engine) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                coerceInputValues = true
            })
        }

        install(Logging) {
            level = LogLevel.BODY
        }

        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = "api.themoviedb.org"
                parameters.append("api_key", apiKey)
            }
        }
    }
}
