package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.mapper.search.toPagedActorDtos
import com.baghdad.remoteDataSource.mapper.search.toPagedMovieDtos
import com.baghdad.remoteDataSource.mapper.search.toPagedTvShowDtos
import com.baghdad.remoteDataSource.request.SearchParameter
import com.baghdad.remoteDataSource.request.toParams
import com.baghdad.remoteDataSource.response.search.ActorSearchResponse
import com.baghdad.remoteDataSource.response.search.MovieSearchResponse
import com.baghdad.remoteDataSource.response.search.TvShowSearchResponse
import com.baghdad.remoteDataSource.util.handleRequest
import com.baghdad.repository.datasource.remote.RemoteSearchDataSource
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.TvShowDto
import com.baghdad.repository.model.actor.ActorDto
import io.ktor.client.HttpClient

class RemoteSearchDataSourceImpl(
    private val httpClient: HttpClient,
    private val baseUrl: String
) : RemoteSearchDataSource {
    override suspend fun searchMovies(
        query: String,
        page: Int,
        genres: List<GenreDto>
    ): PagedResultDto<MovieDto> {
        val params = SearchParameter(query, page)
        val searchResponse = handleRequest<MovieSearchResponse>(
            client = httpClient,
            url = "$baseUrl$SEARCH_MOVIES_ENDPOINT",
            params = params.toParams(),
        )
        return searchResponse.toPagedMovieDtos(genres = genres)
    }

    override suspend fun searchTvShows(
        query: String,
        page: Int,
        genres: List<GenreDto>
    ): PagedResultDto<TvShowDto> {
        val params = SearchParameter(query, page)
        val searchResponse = handleRequest<TvShowSearchResponse>(
            client = httpClient,
            url = "$baseUrl$SEARCH_TV_SHOWS_ENDPOINT",
            params = params.toParams(),
        )
        return searchResponse.toPagedTvShowDtos(genres = genres)
    }

    override suspend fun searchActors(
        query: String,
        page: Int,
    ): PagedResultDto<ActorDto> {
        val params = SearchParameter(query, page)
        val searchResponse = handleRequest<ActorSearchResponse>(
            client = httpClient,
            url = "$baseUrl$SEARCH_ACTORS_ENDPOINT",
            params = params.toParams(),
        )
        return searchResponse.toPagedActorDtos()
    }

    override suspend fun getMoviesResultCount(title: String): Int {
        val params = SearchParameter(title, 1)
        val searchResponse = handleRequest<MovieSearchResponse>(
            client = httpClient,
            url = "$baseUrl$SEARCH_MOVIES_ENDPOINT",
            params = params.toParams(),
        )
        return searchResponse.totalResults ?: 0
    }

    companion object {
        private const val SEARCH_MOVIES_ENDPOINT = "/search/movie"
        private const val SEARCH_TV_SHOWS_ENDPOINT = "/search/tv"
        private const val SEARCH_ACTORS_ENDPOINT = "/search/person"
    }
}