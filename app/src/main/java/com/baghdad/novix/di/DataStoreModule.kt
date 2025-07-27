package com.baghdad.novix.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.baghdad.local_datasource.dataStore.user.UserSerializer
import com.example.application.proto.User
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun providePreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.datastore
    }

    @Provides
    @Singleton
    fun provideUserDataStore(@ApplicationContext context: Context): DataStore<User> {
        return context.userDataStore
    }

    val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "app-preferences")

    val Context.userDataStore: DataStore<User> by dataStore(
        fileName = "user.pb",
        serializer = UserSerializer
    )

}