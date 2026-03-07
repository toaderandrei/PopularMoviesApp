package com.ant.app.application

import android.app.Application
import com.ant.app.di.allKoinModules
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class PopularMoviesApp : Application() {

    private val initializers: AppInitializers by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@PopularMoviesApp)
            modules(allKoinModules)
        }
        initializers.init()
    }
}
