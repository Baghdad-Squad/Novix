package com.baghdad.novix.di

import com.baghdad.novix.logger.CrashlyticsLogger
import com.baghdad.repository.logger.Logger
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object LoggerModule {

    @Provides
    fun provideLogger(
        crashlytics: FirebaseCrashlytics
    ): Logger{
       return CrashlyticsLogger(crashlytics = crashlytics)
    }

    @Provides
    fun provideFirebaseCrashlytics(): FirebaseCrashlytics{
        return  Firebase.crashlytics
    }

}