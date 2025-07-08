package com.baghdad.remote_datasource.entity

data class SearchParameter(
    val query: String,
    val pageNumber: Int = 1,
    val language: String = "en-US",
    val includeAdult: Boolean = false,
)

