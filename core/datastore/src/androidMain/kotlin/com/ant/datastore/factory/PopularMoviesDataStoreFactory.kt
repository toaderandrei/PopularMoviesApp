package com.ant.datastore.factory

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.ant.datastore.createDataStore
import com.ant.datastore.dataStoreFileName

actual class PopularMoviesDataStoreFactory(
    private val context: Context
) {
    actual fun createDataStore(): DataStore<Preferences> = createDataStore(
        producePath = { context.filesDir.resolve(dataStoreFileName).absolutePath }
    )
}
