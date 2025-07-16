package com.baghdad.repository.model

data class PagerResultDto<T>(
    val data: List<T>,
    val nextKey: Int?,
    val prevKey: Int?
)
