package com.baghdad.novix.di

import com.baghdad.novix.logger.CrashlyticsLogger
import com.baghdad.repository.logger.Logger
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class LoggerModule{

    @Binds
    abstract fun provideLogger(crashlyticsLogger: CrashlyticsLogger): Logger

}