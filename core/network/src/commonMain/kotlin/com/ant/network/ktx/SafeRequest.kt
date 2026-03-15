package com.ant.network.ktx

import com.ant.common.exceptions.NetworkError
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.delete
import io.ktor.client.plugins.resources.get
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.coroutines.delay
import kotlinx.serialization.SerializationException
import kotlin.coroutines.cancellation.CancellationException

/**
 * Type-safe GET using @Resource class with automatic retry and typed error mapping.
 */
suspend inline fun <reified T, reified R : Any> HttpClient.safeResourceGet(
    resource: R,
    maxAttempts: Int = 3,
    initialDelay: Long = 1000L,
    maxDelay: Long = 10000L,
    factor: Double = 2.0,
    shouldRetry: (NetworkError) -> Boolean = { it is NetworkError.ServerError || it is NetworkError.Unknown },
): T {
    return safeRequest(maxAttempts, initialDelay, maxDelay, factor, shouldRetry) {
        get(resource)
    }
}

/**
 * Type-safe POST using @Resource class with automatic retry and typed error mapping.
 */
suspend inline fun <reified T, reified R : Any> HttpClient.safeResourcePost(
    resource: R,
    maxAttempts: Int = 3,
    initialDelay: Long = 1000L,
    maxDelay: Long = 10000L,
    factor: Double = 2.0,
    shouldRetry: (NetworkError) -> Boolean = { it is NetworkError.ServerError || it is NetworkError.Unknown },
    crossinline block: HttpRequestBuilder.() -> Unit = {},
): T {
    return safeRequest(maxAttempts, initialDelay, maxDelay, factor, shouldRetry) {
        post(resource, block)
    }
}

/**
 * Type-safe DELETE using @Resource class with automatic retry and typed error mapping.
 */
suspend inline fun <reified T, reified R : Any> HttpClient.safeResourceDelete(
    resource: R,
    maxAttempts: Int = 3,
    initialDelay: Long = 1000L,
    maxDelay: Long = 10000L,
    factor: Double = 2.0,
    shouldRetry: (NetworkError) -> Boolean = { it is NetworkError.ServerError || it is NetworkError.Unknown },
    crossinline block: HttpRequestBuilder.() -> Unit = {},
): T {
    return safeRequest(maxAttempts, initialDelay, maxDelay, factor, shouldRetry) {
        delete(resource, block)
    }
}

/**
 * Core: executes HTTP request with retry + typed error mapping.
 * Throws [NetworkError] subtypes on failure.
 */
@PublishedApi
internal suspend inline fun <reified T> HttpClient.safeRequest(
    maxAttempts: Int,
    initialDelay: Long,
    maxDelay: Long,
    factor: Double,
    shouldRetry: (NetworkError) -> Boolean,
    crossinline execute: suspend HttpClient.() -> HttpResponse,
): T {
    var lastError: NetworkError = NetworkError.Unknown(message = "Request failed after $maxAttempts attempts")
    var currentDelay = initialDelay

    repeat(maxAttempts) { attempt ->
        try {
            val response = execute()
            if (response.status.isSuccess()) {
                return response.body<T>()
            }
            lastError = response.status.toNetworkError(response.bodyAsText())
        } catch (e: CancellationException) {
            throw e
        } catch (e: SerializationException) {
            lastError = NetworkError.Serialization(e)
        } catch (e: Exception) {
            // Ktor wraps SerializationException in JsonConvertException
            lastError = if (e.cause is SerializationException) {
                NetworkError.Serialization(e.cause)
            } else {
                NetworkError.Unknown(e, e.message)
            }
        }

        if (attempt == maxAttempts - 1 || !shouldRetry(lastError)) throw lastError

        delay(currentDelay)
        currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
    }

    throw lastError
}

/**
 * Maps HTTP status codes to [NetworkError] subtypes.
 */
fun HttpStatusCode.toNetworkError(body: String? = null): NetworkError = when (value) {
    401, 403 -> NetworkError.Unauthorized(message = body ?: "Unauthorized")
    in 500..599 -> NetworkError.ServerError(message = body ?: "Server error")
    else -> NetworkError.Unknown(message = body ?: "HTTP $value")
}
