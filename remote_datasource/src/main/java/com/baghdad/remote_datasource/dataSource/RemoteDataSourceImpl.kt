package com.baghdad.remote_datasource.dataSource

import com.baghdad.remote_datasource.api.TmdbApi
import com.baghdad.remote_datasource.entity.SearchParameter
import com.baghdad.remote_datasource.mapper.toDto
import com.baghdad.repository.RemoteDataSource
import com.baghdad.repository.model.SearchResultDto
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json




class RemoteDataSourceImpl(private val api: TmdbApi) : RemoteDataSource {
    override suspend fun searchMultiResults(query: String , pageNumber:Int): SearchResultDto {
        val params = SearchParameter(query , pageNumber)
        return api.searchMulti(params).toDto()
    }
}

object KtorClient {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }
}
