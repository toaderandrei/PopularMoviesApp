package com.ant.datastore.factory

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

/**
 * Factory for creating DataStore instances following KMP pattern.
 * Platform-specific implementations handle context/path requirements.
 *
 * See: https://developer.android.com/kotlin/multiplatform/datastore
 */
expect class PopularMoviesDataStoreFactory {
    fun createDataStore(): DataStore<Preferences>
}
