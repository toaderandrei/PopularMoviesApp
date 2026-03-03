package com.ant.ui.image

import android.content.Context
import coil.Coil
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import com.ant.common.di.Initializer
import okhttp3.OkHttpClient

@OptIn(ExperimentalCoilApi::class)
class CoilInitializer(
    private val context: Context,
    private val okHttpClient: OkHttpClient,
    private val tmdbImageCoilInterceptor: TmdbImageCoilInterceptor,
) : Initializer {
    override fun init() {
        Coil.setImageLoader(
            ImageLoader.Builder(context)
                .components {
                    add(tmdbImageCoilInterceptor)
                }
                .okHttpClient(okHttpClient)
                .build()
        )
    }
}