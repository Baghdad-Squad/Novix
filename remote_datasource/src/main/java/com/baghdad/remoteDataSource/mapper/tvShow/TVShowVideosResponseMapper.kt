package com.baghdad.remoteDataSource.mapper.tvShow

import com.baghdad.remoteDataSource.response.tvShow.TVShowVideosResponse
import com.baghdad.remoteDataSource.util.getTrailerUrlFromKey

fun TVShowVideosResponse.mapToYoutubeURL(): String {
    return results
        ?.find { it?.site == "YouTube" && it.type == "Trailer" }
        ?.key
        ?.let {
            getTrailerUrlFromKey(
                it,
            )
        }.orEmpty()
}
