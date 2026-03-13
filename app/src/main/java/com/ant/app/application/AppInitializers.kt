package com.ant.app.application

import com.ant.shared.di.Initializer

class AppInitializers(
    private val initializers: Set<Initializer>
) {
    fun init() {
        initializers.forEach {
            it.init()
        }
    }
}
