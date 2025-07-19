package com.baghdad.remoteDataSource.mapper.tvShow

import com.baghdad.remoteDataSource.response.tvShow.TVShowVideosResponse

fun TVShowVideosResponse.mapToYoutubeURL(): String {
    return results?.firstOrNull { it?.site == "YouTube" && it.type == "Trailer" }?.key?.let {
        "https://www.youtube.com/watch?v=$it"
    } ?: ""
}