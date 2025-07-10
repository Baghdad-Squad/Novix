package com.baghdad.novix.di

import com.baghdad.remote_datasource.dataSource.RemoteSearchDataSourceImpl
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
        }
    }
    
    single(named("BASE_URL")) {
        "https://api.themoviedb.org/3/search/multi"
    }

    single(named("API_KEY")) {
        "abb0a1c5a5c6ac93cbfe7b94186a67c9"
    }

    single<RemoteSearchDataSource> {
        RemoteSearchDataSourceImpl(
            httpClient = get(),
            apiKey = get(named("API_KEY")),
            baseUrl = get(named("BASE_URL"))
        )
    }
}