package com.ant.network.api

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import java.util.concurrent.TimeUnit

/** Android [HttpClientEngine] backed by OkHttp with 35-second connect/read timeouts. */
actual fun createPlatformEngine(): HttpClientEngine = OkHttp.create {
    config {
        connectTimeout(35L, TimeUnit.SECONDS)
        readTimeout(35L, TimeUnit.SECONDS)
        retryOnConnectionFailure(false)
    }
}