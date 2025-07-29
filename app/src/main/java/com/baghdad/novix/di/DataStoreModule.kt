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
import javax.inject.Named



@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    val Context.datastore: DataStore<Preferences> by preferencesDataStore(
        name = "app-preferences"
    )
    val Context.userDataStore: DataStore<User> by dataStore(
        fileName = "user.pb",
        serializer = UserSerializer
    )



    @Provides
    @Named("preferences")
    fun providePreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.datastore
    }

    @Provides
    @Named("user")
    fun provideUserDataStore(@ApplicationContext context: Context): DataStore<User> {
        return context.userDataStore
    }


}