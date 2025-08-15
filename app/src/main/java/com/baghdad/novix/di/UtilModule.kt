package com.baghdad.novix.di

import android.content.Context
import com.baghdad.localDatasource.util.AppLanguageController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UtilModule {

    @Provides
    @Singleton
    fun provideAppLanguageController(
        @ApplicationContext context: Context
    ): AppLanguageController {
        return AppLanguageController(context)
    }

    @Provides
    @Singleton
    fun provideAppContext(
        @ApplicationContext context: Context
    ): Context = context
}