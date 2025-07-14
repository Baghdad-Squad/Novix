package com.baghdad.remoteDataSource.util.entity

data class SearchParameter(
    val query: String,
    val pageNumber: Int = 1,
    val language: String = "en-US",
    val includeAdult: Boolean = false,
)
fun SearchParameter.toParams(): Map<String, String> = mapOf(
    "query" to query,
    "page" to pageNumber.toString(),
    "language" to language,
    "include_adult" to includeAdult.toString()
)

