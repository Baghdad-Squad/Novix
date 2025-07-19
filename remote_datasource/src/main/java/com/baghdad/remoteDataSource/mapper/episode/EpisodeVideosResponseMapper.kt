package com.baghdad.remoteDataSource.mapper.episode

import com.baghdad.remoteDataSource.response.episode.EpisodeVideosResponse

fun EpisodeVideosResponse.mapToYoutubeTrailerUrl(): String {
    return results?.firstOrNull { it?.site == "YouTube" && it.type == "Trailer" }?.key
        ?: results?.firstOrNull { it?.site == "YouTube" }?.key?.let {
            "https://www.youtube.com/watch?v=$it"
        } ?: ""
}