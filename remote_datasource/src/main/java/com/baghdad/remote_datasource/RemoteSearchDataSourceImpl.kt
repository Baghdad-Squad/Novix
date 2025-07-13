package com.baghdad.remote_datasource

import com.baghdad.remote_datasource.entity.SearchParameter
import com.baghdad.remote_datasource.entity.SearchResponse
import com.baghdad.remote_datasource.entity.toParams
import com.baghdad.remote_datasource.mapper.toDto
import com.baghdad.remote_datasource.util.handleRequest
import com.baghdad.repository.datasource.remote.RemoteSearchDataSource
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.SearchResultDto
import io.ktor.client.HttpClient

class RemoteSearchDataSourceImpl(
    private val httpClient: HttpClient,
    private val baseUrl: String
) : RemoteSearchDataSource {
    override suspend fun searchMultiMedia(
        query: String, pageNumber: Int,
        movieGenres: List<GenreDto>?,
        tvGenres: List<GenreDto>?
    ): SearchResultDto {
        val params = SearchParameter(query, pageNumber)
        val searchResponse = handleRequest<SearchResponse>(
            client = httpClient,
            url = "$baseUrl$SEARCH_MULTI_ENDPOINT",
            params = params.toParams(),
        )

        return searchResponse.toDto(movieGenres, tvGenres)
    }

    companion object {
        private const val SEARCH_MULTI_ENDPOINT = "/search/multi"
    }
}
