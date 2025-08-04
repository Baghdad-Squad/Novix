package com.baghdad.novix.di

import com.baghdad.remoteDataSource.apiService.ActorApiService
import com.baghdad.remoteDataSource.apiService.AuthenticationApiService
import com.baghdad.remoteDataSource.apiService.EpisodeApiService
import com.baghdad.remoteDataSource.apiService.GenreApiService
import com.baghdad.remoteDataSource.apiService.MovieApiService
import com.baghdad.remoteDataSource.apiService.SearchApiService
import com.baghdad.remoteDataSource.apiService.TvShowApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object ApiServiceModule {

    @Provides
    fun provideAuthenticationApiService(retrofit: Retrofit): AuthenticationApiService {
        return retrofit.create(AuthenticationApiService::class.java)
    }

    @Provides
    fun provideSearchApiService(retrofit: Retrofit): SearchApiService {
        return retrofit.create(SearchApiService::class.java)
    }

    @Provides
    fun provideActorApiService(retrofit: Retrofit): ActorApiService {
        return retrofit.create(ActorApiService::class.java)
    }

    @Provides
    fun provideEpisodeApiService(retrofit: Retrofit): EpisodeApiService {
        return retrofit.create(EpisodeApiService::class.java)
    }

    @Provides
    fun provideGenreApiService(retrofit: Retrofit): GenreApiService {
        return retrofit.create(GenreApiService::class.java)
    }

    @Provides
    fun provideMovieApiService(retrofit: Retrofit): MovieApiService {
        return retrofit.create(MovieApiService::class.java)
    }

    @Provides
    fun provideTvShowApiService(retrofit: Retrofit): TvShowApiService {
        return retrofit.create(TvShowApiService::class.java)
    }
}