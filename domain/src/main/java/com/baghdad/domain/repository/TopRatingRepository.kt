package com.baghdad.domain.repository

import com.baghdad.domain.model.PagedResult
import com.baghdad.entity.media.Movie

interface TopRatingRepository {
    suspend fun getTopRatedMovies(page: Int): PagedResult<Movie>
}