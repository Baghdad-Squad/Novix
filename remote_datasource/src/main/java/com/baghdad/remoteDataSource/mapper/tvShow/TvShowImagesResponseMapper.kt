package com.baghdad.remoteDataSource.mapper.tvShow

import com.baghdad.remoteDataSource.response.tvShow.TVShowImagesResponse
import com.baghdad.remoteDataSource.util.getImageUrlFromPath

fun TVShowImagesResponse.toImageUrls(): List<String> {
    return backdrops.orEmpty().map { getImageUrlFromPath(it.filePath) }
}
