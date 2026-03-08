package com.ant.app.application

import com.ant.app.BuildConfig
import com.ant.common.di.Initializer
import com.ant.common.logger.TmdbLogger

class TimberInitializer(
    private val tmdbLogger: TmdbLogger
) : Initializer {
    override fun init() {
        tmdbLogger.init(BuildConfig.DEBUG)
    }
}
