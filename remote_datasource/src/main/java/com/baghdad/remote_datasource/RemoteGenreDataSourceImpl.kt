package com.baghdad.remote_datasource

import com.baghdad.remote_datasource.entity.GenreListResponse
import com.baghdad.remote_datasource.mapper.toDto
import com.baghdad.remote_datasource.util.handleRequest
import com.baghdad.repository.datasource.remote.RemoteGenreDataSource
import com.baghdad.repository.model.GenreDto
import io.ktor.client.HttpClient

class RemoteGenreDataSourceImpl(
    private val httpClient: HttpClient,
    private val apiKey: String,
    private val baseUrl: String
): RemoteGenreDataSource  {
    override suspend fun getMovieGenre(language: String): List<GenreDto> {
        val params = mapOf("language" to language)
        return handleRequest<GenreListResponse>(
            client = httpClient,
            url = "$baseUrl$MOVIE_GENRE_ENDPOINT",
            params = params,
            apiKey = apiKey
        ).toDto(genreType = GenreDto.GenreType.MOVIE)
    }

    override suspend fun getTvShowGenre(language: String): List<GenreDto> {
        val params = mapOf("language" to language)
        return handleRequest<GenreListResponse>(
            client = httpClient,
            url = "$baseUrl$TV_SHOW_GENRE_ENDPOINT",
            params = params,
            apiKey = apiKey
        ).toDto(genreType = GenreDto.GenreType.TV_SHOW)
    }
    companion object {
        private const val MOVIE_GENRE_ENDPOINT = "/genre/movie/list"
        private const val TV_SHOW_GENRE_ENDPOINT = "/genre/tv/list"
    }
}
