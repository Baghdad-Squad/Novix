package com.baghdad.remote_datasource.dataSource

import com.baghdad.repository.RemoteDataSource
import com.baghdad.repository.model.SearchResult
import com.baghdad.remote_datasource.entity.SearchResponse
import com.baghdad.repository.model.ActorDto
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.SearchResultDto
import com.baghdad.repository.model.TvShowDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object KtorClient {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }
}


class RemoteDataSourceImpl(
    private val client: HttpClient,
    private val apiKey: String
) : RemoteDataSource {

    override suspend fun searchResults(query: String, page: Int, language: String, isAdult: Boolean): SearchResultDto {
        val response: SearchResponse = client.get("https://api.themoviedb.org/3/search/multi") {
            url {
                parameters.append("api_key", apiKey)
                parameters.append("query", query)
                parameters.append("page", page.toString())
                parameters.append("language", language)
                parameters.append("include_adult", isAdult.toString())
            }
        }.body()

        val movies = mutableListOf<MovieDto>()
        val tvShows = mutableListOf<TvShowDto>()
        val actors = mutableListOf<ActorDto>()

//        id = item.id ?: -1,
//        title = item.movieTitle.orEmpty(),
//        overview = item.movieOverview.orEmpty(),
//        releaseDate = item.releaseDate.orEmpty(),
//        posterPath = item.tvShowPosterPath.orEmpty()
//        id = item.id?.toLong() ?: -1,
//        title = item.movieTitle.orEmpty(),
//        genres = emptyList(),
//        imdbRating = 0.0,
//        userRating = null,
//        releaseDate = item.releaseDate.orEmpty(),
//        overview = item.movieOverview.orEmpty(),
//        cast = emptyList(),
//        posterPictureURL = item.tvShowPosterPath.orEmpty(),
//        backdropPicturesURLs = emptyList(),
//        runtimeMinutes = 0
        response.results.forEach { item ->
            when (item.mediaType) {
                "movie" -> {
                    movies.add(
                        MovieDto(
                            id = item.id ?: -1,
                            title = item.movieTitle.orEmpty(),
                            genres = emptyList(),
                            imdbRating = 0.0,
                            userRating = 0.0,
                            releaseDate = item.releaseDate.orEmpty(),
                            overview = item.movieOverview.orEmpty(),
                            cast = emptyList(),
                            posterPictureURL = item.tvShowPosterPath.orEmpty(),
                            backdropPicturesURLs = emptyList(),
                            runtimeMinutes = 0
                        )
                    )
                }

                "tv" -> {
                    tvShows.add(
                        TvShowDto(
                            id = item.id?.toLong() ?: -1L,
                            title = item.tvShowName ?: item.tvShowOriginalName.orEmpty(),
                            genres = emptyList(), // search/multi doesn't return genres
                            imdbRating = 0.0, // not included in /search/multi
                            userRating = null, // can be set later by user
                            releaseDate = item.firstAirDate.orEmpty(),
                            overview = item.movieOverview.orEmpty(),
                            cast = emptyList(), // also not included in /search/multi
                            posterPictureURL = item.tvShowPosterPath.orEmpty(),
                            backdropPicturesURLs = emptyList(), // not included in /search/multi
                            numberOfSeasons = 0 // not available in this endpoint
                        )
                    )
                }

                "person" -> {
                    actors.add(
                        ActorDto(
                            id = item.id?.toLong() ?: -1L,
                            name = item.tvShowName.orEmpty(), // or item.movieTitle.orEmpty()
                            imageUrl = item.profilePath.orEmpty()
                        )
                    )
                }
            }
        }

        return SearchResultDto(
            actors = actors,
            movies = movies,
            tvShows = tvShows
        )
    }
}