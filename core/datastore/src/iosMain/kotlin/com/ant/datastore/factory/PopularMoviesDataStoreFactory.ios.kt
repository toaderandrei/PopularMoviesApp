package com.ant.datastore.factory

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path.Companion.toPath
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

/**
 * iOS implementation of DataStore factory.
 * Uses file path pattern for DataStore creation - no context needed.
 */
actual class PopularMoviesDataStoreFactory {
    @OptIn(ExperimentalForeignApi::class)
    actual fun createDataStore(): DataStore<Preferences> {
        return PreferenceDataStoreFactory.createWithPath(
            produceFile = {
                val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
                    directory = NSDocumentDirectory,
                    inDomain = NSUserDomainMask,
                    appropriateForURL = null,
                    create = false,
                    error = null,
                )
                val path = requireNotNull(documentDirectory?.path)
                "$path/app_preferences.preferences_pb".toPath()
            }
        )
    }
}
