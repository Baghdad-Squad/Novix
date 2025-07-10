package com.baghdad.remote_datasource.mapper

import com.baghdad.remote_datasource.entity.SearchResponse
import com.baghdad.repository.model.ActorDto
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.SearchResultDto
import com.baghdad.repository.model.TvShowDto

internal fun SearchResponse.toDto(): SearchResultDto {
    val actors = results
        .filter { it.mediaType == "person" }
        .mapNotNull { item ->
            item.id?.let { id ->
                ActorDto(
                    id = id,
                    name = item.tvShowName ?: "Unknown",
                    imageUrl = item.profilePath ?: ""
                )
            }
        }

    val movies = results
        .filter { it.mediaType == "movie" }
        .mapNotNull { item ->
            item.id?.let { id ->
                MovieDto(
                    id = id,
                    title = item.movieTitle ?: "Unknown Title",
                    genres = genreIdsToGenreDto(item.genreIds),
                    imdbRating = item.voteAverage ?: 0.0,
                    userRating = null,
                    releaseDate = item.releaseDate ?: "",
                    overview = item.movieOverview ?: "",
                    cast = getCastMembers(results),
                    posterPictureURL = item.tvShowPosterPath ?: "",
                    backdropPicturesURLs = item.backdropPath?.let { listOf(it) } ?: emptyList(),
                    runtimeMinutes = 0
                )
            }
        }

    val tvShows = results
        .filter { it.mediaType == "tv" }
        .mapNotNull { item ->
            item.id?.let { id ->
                TvShowDto(
                    id = id,
                    title = item.tvShowName ?: item.tvShowOriginalName ?: "Unknown TV Show",
                    genres = genreIdsToGenreDto(item.genreIds),
                    imdbRating = item.voteAverage ?: 0.0,
                    userRating = null,
                    releaseDate = item.firstAirDate ?: "",
                    overview = item.movieOverview ?: "",
                    cast = getCastMembers(results),
                    posterPictureURL = item.tvShowPosterPath ?: "",
                    backdropPicturesURLs = item.backdropPath?.let { listOf(it) } ?: emptyList(),
                    numberOfSeasons = 0
                )
            }
        }

    return SearchResultDto(
        actors = actors,
        movies = movies,
        tvShows = tvShows
    )
}


