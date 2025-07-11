package com.baghdad.remote_datasource.dataSource

import com.baghdad.remote_datasource.entity.GenreListResponse
import com.baghdad.remote_datasource.entity.SearchParameter
import com.baghdad.remote_datasource.entity.SearchResponse
import com.baghdad.remote_datasource.entity.toDto
import com.baghdad.remote_datasource.entity.toParams
import com.baghdad.remote_datasource.mapper.toDto
import com.baghdad.remote_datasource.util.handleRequest
import com.baghdad.repository.datasource.remote.RemoteDataSource
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.SearchResultDto
import io.ktor.client.HttpClient


class RemoteDataSourceImpl(
    private val httpClient: HttpClient,
    private val apiKey: String,
    private val baseUrl: String
) : RemoteDataSource {

    override suspend fun searchMultiResults(query: String, pageNumber: Int): SearchResultDto {
        val params = SearchParameter(query, pageNumber)
        return handleRequest<SearchResponse>(

            client = httpClient,
            url = "$baseUrl$SEARCH_MULTI_ENDPOINT",
            params = params.toParams(),
            apiKey = apiKey
        ).toDto()

    }

    override suspend fun getMovieGenre(language: String): List<GenreDto> {
        val params = mapOf("language" to language)
        return handleRequest<GenreListResponse>(
            client = httpClient,
            url = "$baseUrl$MOVIE_GENRE_ENDPOINT",
            params = params,
            apiKey = apiKey
        ).toDto()
    }

    override suspend fun getTvShowGenre(language: String): List<GenreDto> {
        val params = mapOf("language" to language)
        return handleRequest<GenreListResponse>(
            client = httpClient,
            url = "$baseUrl$TV_SHOW_GENRE_ENDPOINT",
            params = params,
            apiKey = apiKey
        ).toDto()
    }

    companion object {
        private const val SEARCH_MULTI_ENDPOINT = "/search/multi"
        private const val MOVIE_GENRE_ENDPOINT = "/genre/movie/list"
        private const val TV_SHOW_GENRE_ENDPOINT = "/genre/tv/list"
    }
}