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
import com.baghdad.remoteDataSource.interceptor.CacheInterceptor
import com.baghdad.remoteDataSource.interceptor.HeadersSetupInterceptor
import com.baghdad.remoteDataSource.interceptor.OfflineCacheInterceptor
import com.baghdad.remoteDataSource.util.Connectivity
import com.baghdad.repository.datasource.remote.RemoteActorDataSource
import com.baghdad.repository.datasource.remote.RemoteAuthenticationDataSource
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
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit


private const val timeOut = 20L
val remoteDataSourceModule = module {
    single<HttpClient> {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 20_000
                connectTimeoutMillis = 20_000
                socketTimeoutMillis = 20_000
            }
        }
    }

    single<Cache> {
        val cacheSize = 5L * 1024 * 1024
        val cacheDir = androidContext().cacheDir
        Cache(File(cacheDir, "http-cache"), cacheSize)
    }
    single<Connectivity> {
        Connectivity(androidContext())
    }
    single<CacheInterceptor> {
        CacheInterceptor()
    }
    single<OfflineCacheInterceptor> {
        OfflineCacheInterceptor(get<Connectivity>())
    }
    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single<LanguageProvider> { AppLanguageProvider() }
    single<String>(named("AUTHORIZATION_TOKEN")) { "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyZTZkYmRkOTNjMGY5NzdkMjMwOGJjMzM3NmI3YTNmOCIsIm5iZiI6MTc1MzAyOTE3Ni45OSwic3ViIjoiNjg3ZDFhMzgzOTg0OWZmZThkZDk4ZDEzIiwic2NvcGVzIjpbImFwaV9yZWFkIl0sInZlcnNpb24iOjF9.5NDfRH9_oRVtrvQb8Bs11qWGeLzEE5US_e5IcVQWerE" }
    single<HeadersSetupInterceptor> {
        HeadersSetupInterceptor(
            languageProvider = get(),
            authorizationToken = get(named("AUTHORIZATION_TOKEN"))
        )
    }

    single {
        OkHttpClient.Builder()
            .callTimeout(timeOut, TimeUnit.SECONDS)
            .connectTimeout(timeOut, TimeUnit.SECONDS)
            .readTimeout(timeOut, TimeUnit.SECONDS)
            .writeTimeout(timeOut, TimeUnit.SECONDS)
            .cache(get())
            .addInterceptor(get<HttpLoggingInterceptor>())
            .addInterceptor(get<OfflineCacheInterceptor>())
            .addNetworkInterceptor(get<CacheInterceptor>())
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
    single<AuthenticationApiService> {
        get<Retrofit>().create(AuthenticationApiService::class.java)
    }
    single<RemoteAuthenticationDataSource> {
        RemoteAuthenticationImpl(
            authenticationApiService = get(),
            logger = get()
        )
    }
}