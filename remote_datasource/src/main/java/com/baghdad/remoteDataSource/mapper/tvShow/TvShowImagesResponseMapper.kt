package com.baghdad.remoteDataSource.mapper.tvShow

import com.baghdad.remoteDataSource.response.tvShow.TVShowImagesResponse

fun TVShowImagesResponse.toImageUrls(): List<String> {
    return backdrops.orEmpty().map { "https://image.tmdb.org/t/p/w500" + it.filePath }
}