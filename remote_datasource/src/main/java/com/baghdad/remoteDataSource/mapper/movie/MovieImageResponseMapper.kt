package com.baghdad.remoteDataSource.mapper.movie

import com.baghdad.remoteDataSource.response.movie.MovieImageResponse
import com.baghdad.remoteDataSource.util.getImageUrlFromPath

fun MovieImageResponse.toImageUrl(): List<String> {
    return backdrops?.map { getImageUrlFromPath(it.filePath) }.orEmpty()
}
