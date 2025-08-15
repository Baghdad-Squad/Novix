package com.baghdad.repository.model

data class PagedResultDto<T>(
    val data: List<T>,
    val nextKey: Int?,
    val prevKey: Int?
)