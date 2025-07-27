package com.baghdad.novix.di

import com.baghdad.local_datasource.language.AppLanguageProvider
import com.baghdad.novix.BuildConfig
import com.baghdad.remoteDataSource.RemoteActorDataSourceImpl
import com.baghdad.remoteDataSource.RemoteAuthenticationImpl
import com.baghdad.remoteDataSource.RemoteEpisodeDataSourceImpl
import com.baghdad.remoteDataSource.RemoteGenreDataSourceImpl
import com.baghdad.remoteDataSource.RemoteMovieDataSourceImpl
import com.baghdad.remoteDataSource.RemoteSearchDataSourceImpl
import com.baghdad.remoteDataSource.RemoteTvShowDataSourceImpl
import com.baghdad.remoteDataSource.apiService.ActorApiService
import com.baghdad.remoteDataSource.apiService.AuthenticationApiService
import com.baghdad.remoteDataSource.apiService.EpisodeApiService
import com.baghdad.remoteDataSource.apiService.GenreApiService
import com.baghdad.remoteDataSource.apiService.MovieApiService
import com.baghdad.remoteDataSource.apiService.SearchApiService
import com.baghdad.remoteDataSource.apiService.TvShowApiService
import com.baghdad.remoteDataSource.interceptor.HeadersSetupInterceptor
import com.baghdad.remoteDataSource.interceptor.KtorApiInterceptor
import com.baghdad.repository.datasource.remote.RemoteActorDataSource
import com.baghdad.repository.datasource.remote.RemoteAuthenticationDataSource
import com.baghdad.repository.datasource.remote.RemoteEpisodeDataSource
import com.baghdad.repository.datasource.remote.RemoteGenreDataSource
import com.baghdad.repository.datasource.remote.RemoteMovieDataSource
import com.baghdad.repository.datasource.remote.RemoteSearchDataSource
import com.baghdad.repository.datasource.remote.RemoteTvShowDataSource
import com.baghdad.repository.language.LanguageProvider
import com.baghdad.repository.logger.Logger
import com.google.firebase.sessions.dagger.Module
import com.google.firebase.sessions.dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import jakarta.inject.Named
import jakarta.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val timeOut = 20L

    @Provides
    fun provideHttpClient(): HttpClient {
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            install(KtorApiInterceptor) {
                apiKey = BuildConfig.API_KEY
                languageProvider = provideLanguageProvider()
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 20_000
                connectTimeoutMillis = 20_000
                socketTimeoutMillis = 20_000
            }
        }
    }

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    fun provideLanguageProvider(): LanguageProvider {
        return AppLanguageProvider()
    }

    @Provides
    @Singleton
    @Named("AUTHORIZATION_TOKEN")
    fun provideAuthorizationToken(): String {
        return "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyZTZkYmRkOTNjMGY5NzdkMjMwOGJjMzM3NmI3YTNmOCIsIm5iZiI6MTc1MzAyOTE3Ni45OSwic3ViIjoiNjg3ZDFhMzgzOTg0OWZmZThkZDk4ZDEzIiwic2NvcGVzIjpbImFwaV9yZWFkIl0sInZlcnNpb24iOjF9.5NDfRH9_oRVtrvQb8Bs11qWGeLzEE5US_e5IcVQWerE"
    }

    @Provides
    fun provideHeadersSetupInterceptor(
        languageProvider: LanguageProvider,
        @Named("AUTHORIZATION_TOKEN") authorizationToken: String
    ): HeadersSetupInterceptor {
        return HeadersSetupInterceptor(languageProvider, authorizationToken)
    }

    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        headersSetupInterceptor: HeadersSetupInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .callTimeout(timeOut, TimeUnit.SECONDS)
            .connectTimeout(timeOut, TimeUnit.SECONDS)
            .readTimeout(timeOut, TimeUnit.SECONDS)
            .writeTimeout(timeOut, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(headersSetupInterceptor)
            .build()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Named("BASE_URL")
    fun provideBaseUrl(): String {
        return "https://api.themoviedb.org/3"
    }

    @Provides
    @Named("API_KEY")
    fun provideApiKey(): String {
        return BuildConfig.API_KEY
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
    fun provideSearchApiService(retrofit: Retrofit): SearchApiService {
        return retrofit.create(SearchApiService::class.java)
    }

    @Provides
    fun provideTvShowApiService(retrofit: Retrofit): TvShowApiService {
        return retrofit.create(TvShowApiService::class.java)
    }

    @Provides
    fun provideAuthenticationApiService(retrofit: Retrofit): AuthenticationApiService {
        return retrofit.create(AuthenticationApiService::class.java)
    }

    @Provides
    fun provideRemoteSearchDataSource(
        searchApiService: SearchApiService,
        logger: Logger
    ): RemoteSearchDataSource {
        return RemoteSearchDataSourceImpl(searchApiService, logger)
    }

    @Provides
    fun provideRemoteGenreDataSource(
        genreApiService: GenreApiService,
        logger: Logger
    ): RemoteGenreDataSource {
        return RemoteGenreDataSourceImpl(genreApiService, logger)
    }

    @Provides
    fun provideRemoteMovieDataSource(
        movieApiService: MovieApiService,
        logger: Logger
    ): RemoteMovieDataSource {
        return RemoteMovieDataSourceImpl(movieApiService, logger)
    }

    @Provides
    fun provideRemoteActorDataSource(
        actorApiService: ActorApiService,
        logger: Logger
    ): RemoteActorDataSource {
        return RemoteActorDataSourceImpl(actorApiService, logger)
    }

    @Provides
    fun provideRemoteTvShowDataSource(
        tvShowApiService: TvShowApiService,
        logger: Logger
    ): RemoteTvShowDataSource {
        return RemoteTvShowDataSourceImpl(tvShowApiService, logger)
    }

    @Provides
    fun provideRemoteEpisodeDataSource(
        episodeApiService: EpisodeApiService,
        logger: Logger
    ): RemoteEpisodeDataSource {
        return RemoteEpisodeDataSourceImpl(episodeApiService, logger)
    }

    @Provides
    fun provideRemoteAuthenticationDataSource(
        authenticationApiService: AuthenticationApiService,
        logger: Logger
    ): RemoteAuthenticationDataSource {
        return RemoteAuthenticationImpl(authenticationApiService, logger)
    }
}