package com.baghdad.remoteDataSource.util

fun getPreviousKey(page: Int?): Int? {
    return if (page == null || page <= 1) null else page - 1
}

fun getNextKey(page: Int?, totalPages: Int?): Int? {
    return if (page == null || totalPages == null || page >= totalPages) null else page + 1
}