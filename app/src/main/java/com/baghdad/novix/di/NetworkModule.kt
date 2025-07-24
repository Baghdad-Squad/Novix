package com.baghdad.novix.di

import com.baghdad.local_datasource.language.AppLanguageProvider
import com.baghdad.novix.BuildConfig
import com.baghdad.remoteDataSource.RemoteActorDataSourceImpl
import com.baghdad.remoteDataSource.RemoteEpisodeDataSourceImpl
import com.baghdad.remoteDataSource.RemoteGenreDataSourceImpl
import com.baghdad.remoteDataSource.RemoteMovieDataSourceImpl
import com.baghdad.remoteDataSource.RemoteSearchDataSourceImpl
import com.baghdad.remoteDataSource.RemoteTvShowDataSourceImpl
import com.baghdad.remoteDataSource.apiService.ActorApiService
import com.baghdad.remoteDataSource.apiService.EpisodeApiService
import com.baghdad.remoteDataSource.apiService.GenreApiService
import com.baghdad.remoteDataSource.apiService.MovieApiService
import com.baghdad.remoteDataSource.apiService.SearchApiService
import com.baghdad.remoteDataSource.apiService.TvShowApiService
import com.baghdad.remoteDataSource.interceptor.HeadersSetupInterceptor
import com.baghdad.remoteDataSource.interceptor.KtorApiInterceptor
import com.baghdad.repository.datasource.remote.RemoteActorDataSource
import com.baghdad.repository.datasource.remote.RemoteEpisodeDataSource
import com.baghdad.repository.datasource.remote.RemoteGenreDataSource
import com.baghdad.repository.datasource.remote.RemoteMovieDataSource
import com.baghdad.repository.datasource.remote.RemoteSearchDataSource
import com.baghdad.repository.datasource.remote.RemoteTvShowDataSource
import com.baghdad.repository.language.LanguageProvider
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val timeOut = 20L
val remoteDataSourceModule = module {
    single<HttpClient> {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            install(KtorApiInterceptor) {
                apiKey = BuildConfig.API_KEY
                languageProvider = get()
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 20_000
                connectTimeoutMillis = 20_000
                socketTimeoutMillis = 20_000
            }
        }
    }

    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single {
        HeadersSetupInterceptor(get())
    }

    single {
        OkHttpClient.Builder()
            .callTimeout(timeOut, TimeUnit.SECONDS)
            .connectTimeout(timeOut, TimeUnit.SECONDS)
            .readTimeout(timeOut, TimeUnit.SECONDS)
            .writeTimeout(timeOut, TimeUnit.SECONDS)
            .addInterceptor(get<HttpLoggingInterceptor>())
            .addInterceptor(get<HeadersSetupInterceptor>())
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single(named("BASE_URL")) {
        "https://api.themoviedb.org/3"
    }

    single(named("API_KEY")) {
        BuildConfig.API_KEY
    }

    single<RemoteSearchDataSource> {
        RemoteSearchDataSourceImpl(
            searchApiService = get(),
            logger = get()
        )
    }

    single<RemoteGenreDataSource> {
        RemoteGenreDataSourceImpl(
            genreApiService = get(),
            logger = get()
        )
    }

    single<RemoteMovieDataSource> {
        RemoteMovieDataSourceImpl(
            movieApiService = get(),
            logger = get(),
        )
    }

    single<RemoteActorDataSource> {
        RemoteActorDataSourceImpl(
            actorApiService = get(),
            logger = get()
        )
    }

    single<RemoteTvShowDataSource> {
        RemoteTvShowDataSourceImpl(
            tvShowApiService = get(),
            logger = get()
        )
    }

    single<RemoteEpisodeDataSource> {
        RemoteEpisodeDataSourceImpl(
            episodeApiService = get(),
            logger = get()
        )
    }

    single<LanguageProvider> { AppLanguageProvider() }

    single<ActorApiService> {
        get<Retrofit>().create(ActorApiService::class.java)
    }

    single<EpisodeApiService> {
        get<Retrofit>().create(EpisodeApiService::class.java)
    }

    single<GenreApiService> {
        get<Retrofit>().create(GenreApiService::class.java)
    }

    single<MovieApiService> {
        get<Retrofit>().create(MovieApiService::class.java)
    }

    single<SearchApiService> {
        get<Retrofit>().create(SearchApiService::class.java)
    }

    single<TvShowApiService> {
        get<Retrofit>().create(TvShowApiService::class.java)
    }
}