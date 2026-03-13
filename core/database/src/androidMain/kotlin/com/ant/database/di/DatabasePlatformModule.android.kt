package com.ant.database.di

import org.koin.core.module.Module

/**
 * Deprecated: Use databaseFactoryModule + databaseModule instead.
 * Kept for backwards compatibility during migration.
 */
@Deprecated("Use databaseFactoryModule + databaseModule", ReplaceWith("databaseModule"))
actual val databasePlatformModule: Module = databaseModule
