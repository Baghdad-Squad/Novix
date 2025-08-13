package com.baghdad.remoteDataSource.util

fun getTrailerUrlFromKey(key: String?): String = key?.let { "https://www.youtube.com/watch?v=$it" } ?: ""
