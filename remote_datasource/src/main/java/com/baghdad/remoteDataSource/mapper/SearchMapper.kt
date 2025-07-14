package com.baghdad.remoteDataSource.mapper

import com.baghdad.remoteDataSource.response.SearchResponse
import com.baghdad.repository.model.ActorDto
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.SearchResultDto
import com.baghdad.repository.model.TvShowDto

internal fun SearchResponse.toDto(
    movieGenres: List<GenreDto>?,
    tvGenres: List<GenreDto>?
): SearchResultDto {
    val actors = results
        .filter { it.mediaType == "person" }
        .mapNotNull { item ->
            item.id?.let { id ->
                ActorDto(
                    id = id,
                    name = item.tvShowName ?: "Unknown",
                    imageUrl = ("https://image.tmdb.org/t/p/w500" + item.profilePath) ?: ""
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
                    genres = mapGenreIdsToGenres(item.genreIds, movieGenres),
                    imdbRating = item.voteAverage ?: 0.0,
                    userRating = null,
                    releaseDate = item.releaseDate ?: "",
                    overview = item.movieOverview ?: "",
                    posterPictureURL = ("https://image.tmdb.org/t/p/w500" + item.tvShowPosterPath),
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
                    genres = mapGenreIdsToGenres(item.genreIds, tvGenres),
                    imdbRating = item.voteAverage ?: 0.0,
                    userRating = null,
                    releaseDate = item.firstAirDate ?: "",
                    overview = item.movieOverview ?: "",
                    posterPictureURL = ("https://image.tmdb.org/t/p/w500" + item.tvShowPosterPath),
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

