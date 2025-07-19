package com.baghdad.domain.model.search

import com.baghdad.entity.media.Genre

data class SearchFilter(
    val minimumYear: Int?,
    val maximumYear: Int?,
    val minimumRating: Int,
    val selectedGenres: List<Genre>
)