package com.baghdad.remoteDataSource.mapper.movie

import com.baghdad.remoteDataSource.response.movie.MovieVideosResponse
import com.baghdad.remoteDataSource.util.getTrailerUrlFromKey

fun MovieVideosResponse.mapToYoutubeURL(): String =
    results
        ?.find { it?.site == "YouTube" && it.type == "Trailer" }
        ?.key
        ?.let {
            getTrailerUrlFromKey(
                it,
            )
        }.orEmpty()
