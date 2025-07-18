package com.baghdad.remoteDataSource.request


data class SearchParameter(
    val query: String,
    val pageNumber: Int = 1,
    val includeAdult: Boolean = false,
)
fun SearchParameter.toParams(): Map<String, String> = mapOf(
    "query" to query,
    "page" to pageNumber.toString(),
    "include_adult" to includeAdult.toString()
)

