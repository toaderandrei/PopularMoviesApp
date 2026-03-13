package com.ant.shared.qualifiers

/**
 * Named qualifier constants for Koin dependency injection of coroutine dispatchers.
 * Used with `named()` in Koin module definitions.
 */
object DispatcherQualifiers {
    /** Qualifier for [kotlinx.coroutines.Dispatchers.IO]. */
    const val IO = "IoDispatcher"
    /** Qualifier for [kotlinx.coroutines.Dispatchers.Default]. */
    const val DEFAULT = "DefaultDispatcher"
    /** Qualifier for [kotlinx.coroutines.Dispatchers.Main]. */
    const val MAIN = "MainDispatcher"
    /** Qualifier for [kotlinx.coroutines.Dispatchers.Main.immediate][kotlinx.coroutines.MainCoroutineDispatcher.immediate]. */
    const val MAIN_IMMEDIATE = "MainImmediateDispatcher"
    /** Qualifier for the application-level [kotlinx.coroutines.CoroutineScope] with a [kotlinx.coroutines.SupervisorJob]. */
    const val APP_SCOPE = "ApplicationScope"
}
