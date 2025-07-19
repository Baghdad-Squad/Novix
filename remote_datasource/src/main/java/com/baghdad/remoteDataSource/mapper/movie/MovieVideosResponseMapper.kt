package com.baghdad.remoteDataSource.mapper.movie

import com.baghdad.remoteDataSource.response.movie.MovieVideosResponse

fun MovieVideosResponse.mapToYoutubeURL(): String {
    return results?.firstOrNull { it?.site == "YouTube" && it.type == "Trailer" }?.key?.let {
        "https://www.youtube.com/watch?v=$it"
    } ?: ""
}