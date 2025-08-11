package com.baghdad.remoteDataSource.mapper.movie

import com.baghdad.remoteDataSource.response.movie.MovieImageResponse

fun MovieImageResponse.toImageUrl(): List<String> {
    return backdrops?.map { "https://image.tmdb.org/t/p/w500" + it.filePath.orEmpty() }.orEmpty()
}