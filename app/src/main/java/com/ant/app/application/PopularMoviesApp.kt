package com.ant.app.application

import android.app.Application
import com.ant.app.di.appModule
import com.ant.ui.di.initKoin
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class PopularMoviesApp : Application() {

    private val initializers: AppInitializers by inject()

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidLogger()
            androidContext(this@PopularMoviesApp)
            modules(appModule)  // Add app-specific module
        }
        initializers.init()
    }
}
