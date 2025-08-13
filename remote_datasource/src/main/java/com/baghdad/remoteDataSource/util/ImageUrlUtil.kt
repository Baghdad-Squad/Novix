package com.baghdad.remoteDataSource.util

fun getImageUrlFromPath(path: String?): String =
    path
        ?.takeIf {
            it.isNotBlank()
        }?.let {
            "https://image.tmdb.org/t/p/w500$it"
        } ?: ""
