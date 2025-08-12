package com.baghdad.remoteDataSource.mapper.actor

import com.baghdad.remoteDataSource.response.actor.ActorTvShowsResponse
import com.baghdad.remoteDataSource.util.getImageUrlFromPath
import com.baghdad.repository.model.TvShowDto

fun ActorTvShowsResponse.toActorTvShowList(): List<TvShowDto> = cast?.filter { it?.id != null }?.map { it.toDto() }.orEmpty()

private fun ActorTvShowsResponse.ActorTvShowDto.toDto(): TvShowDto =
    TvShowDto(
        id = id ?: -1,
        title = name ?: originalName.orEmpty(),
        genres = emptyList(),
        imdbRating = voteAverage ?: 0.0,
        userRating = null,
        releaseDate = firstAirDate.takeUnless { it.isNullOrEmpty() } ?: "0001-01-01",
        overview = overview.orEmpty(),
        posterPictureURL = getImageUrlFromPath(posterPath),
        numberOfSeasons = 0,
        trailerURL = "",
        headerImagesURLs = emptyList()
    )