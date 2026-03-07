package com.ant.network.api

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin

/** iOS [HttpClientEngine] backed by the Darwin (URLSession) networking stack. */
actual fun createPlatformEngine(): HttpClientEngine = Darwin.create()
