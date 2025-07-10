package com.baghdad.remote_datasource.dataSource

import com.baghdad.remote_datasource.entity.GenreListResponse
import com.baghdad.remote_datasource.entity.SearchParameter
import com.baghdad.remote_datasource.entity.SearchResponse
import com.baghdad.remote_datasource.entity.toDto
import com.baghdad.remote_datasource.entity.toParams
import com.baghdad.remote_datasource.mapper.toDto
import com.baghdad.repository.datasource.remote.RemoteDataSource
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.SearchResultDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlin.collections.component1
import kotlin.collections.component2


class RemoteDataSourceImpl(
    private val httpClient: HttpClient,
    private val apiKey: String,
    private val baseUrl: String
) : RemoteDataSource {
    override suspend fun searchMultiResults(query: String, pageNumber: Int): SearchResultDto {
        val params = SearchParameter(query, pageNumber)
        val result : SearchResponse = httpClient.get("$baseUrl/search/multi") {
            parameter("api_key", apiKey)
            params.toParams().forEach { (key, value) ->
                parameter(key, value)
            }
        }.body()
        return result.toDto()
    }

    override suspend fun getMovieGenre(language: String): List<GenreDto> {
        val result : GenreListResponse = httpClient.get(("$baseUrl/genre/movie/list")) {
            parameter("api_key", apiKey)
            parameter("language", language)
        }.body()
        return result.toDto()
    }

    override suspend fun getTvShowGenre(language: String): List<GenreDto> {
        val result : GenreListResponse = httpClient.get(("$baseUrl/genre/tv/list")) {
            parameter("api_key", apiKey)
            parameter("language", language)
        }.body()
        return result.toDto()
    }

}
