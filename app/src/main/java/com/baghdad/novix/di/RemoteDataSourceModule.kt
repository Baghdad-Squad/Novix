package com.baghdad.novix.di

import com.baghdad.novix.BuildConfig
import com.baghdad.remote_datasource.ApiInterceptor
import com.baghdad.remote_datasource.RemoteGenreDataSourceImpl
import com.baghdad.remote_datasource.RemoteSearchDataSourceImpl
import com.baghdad.repository.datasource.remote.RemoteGenreDataSource
import com.baghdad.repository.datasource.remote.RemoteSearchDataSource
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module

val remoteDataSourceModule = module {
    single<HttpClient> {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            install(ApiInterceptor) {
                apiKey = BuildConfig.API_KEY
            }
        }
    }

    single(named("BASE_URL")) {
        "https://api.themoviedb.org/3"
    }

    single(named("API_KEY")) {
        BuildConfig.API_KEY
    }

    single<RemoteSearchDataSource> {
        RemoteSearchDataSourceImpl(
            httpClient = get(),
            baseUrl = get(named("BASE_URL"))
        )
    }

    single<RemoteGenreDataSource> {
        RemoteGenreDataSourceImpl(
            httpClient = get(),
            baseUrl = get(named("BASE_URL"))
        )
    }
}