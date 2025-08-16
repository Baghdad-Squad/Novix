package com.baghdad.domain.model.pagination

data class PagedResult<T>(
    val data: List<T>,
    val nextPage: Int?,
    val prevPage: Int?
)