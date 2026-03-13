package com.ant.shared.di

import com.ant.shared.logger.KermitLogger
import com.ant.shared.logger.Logger
import com.ant.shared.qualifiers.DispatcherQualifiers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

/** Koin module providing coroutine dispatchers and an application-scoped [CoroutineScope]. */
val coroutinesModule: Module = module {
    single<CoroutineDispatcher>(named(DispatcherQualifiers.IO)) { Dispatchers.IO }
    single<CoroutineDispatcher>(named(DispatcherQualifiers.DEFAULT)) { Dispatchers.Default }
    single<CoroutineDispatcher>(named(DispatcherQualifiers.MAIN)) { Dispatchers.Main }
    single<CoroutineScope>(named(DispatcherQualifiers.APP_SCOPE)) {
        CoroutineScope(SupervisorJob() + get<CoroutineDispatcher>(named(DispatcherQualifiers.DEFAULT)))
    }
}

/** Koin module providing a [Logger] singleton backed by Kermit. */
val loggerModule: Module = module {
    single<Logger> { KermitLogger() }
}

/** Aggregate Koin module that includes all common infrastructure dependencies. */
val commonModule: Module = module {
    includes(coroutinesModule, loggerModule)
}
