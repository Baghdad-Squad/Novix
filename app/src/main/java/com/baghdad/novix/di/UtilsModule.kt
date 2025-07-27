package com.baghdad.novix.di

import com.baghdad.domain.util.SearchFilterHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UtilsModule {

    @Provides
    fun provideSearchFilterHelper() : SearchFilterHelper{
        return SearchFilterHelper()
    }
}