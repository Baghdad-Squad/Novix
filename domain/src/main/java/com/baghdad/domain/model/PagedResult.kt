package com.baghdad.domain.model

data class PagedResult<T>(
    val data: List<T>,
    val nextKey: Int?,
    val prevKey: Int?
)