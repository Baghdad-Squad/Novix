package com.baghdad.novix.di

import com.baghdad.novix.logger.CrashlyticsLogger
import com.baghdad.repository.logger.Logger
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val loggerModule = module {
    singleOf(::CrashlyticsLogger) { bind<Logger>() }
    single { Firebase.crashlytics }
}