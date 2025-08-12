package com.baghdad.remoteDataSource.mapper.episode

import com.baghdad.remoteDataSource.response.episode.EpisodeVideosResponse
import com.baghdad.remoteDataSource.util.getTrailerUrlFromKey

fun EpisodeVideosResponse.mapToYoutubeTrailerUrl(): String {
    return results
        ?.find { it?.site == "YouTube" && it.type == "Trailer" }
        ?.key
        ?.let {
            getTrailerUrlFromKey(it)
        }.orEmpty()
}
