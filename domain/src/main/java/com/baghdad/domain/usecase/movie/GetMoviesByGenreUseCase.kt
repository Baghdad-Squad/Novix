package com.baghdad.domain.usecase.movie

import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.domain.model.savedList.SavedMovie
import com.baghdad.domain.repository.MovieRepository
import javax.inject.Inject

class GetMoviesByGenreUseCase @Inject constructor(
    val movieRepository: MovieRepository
) {
    suspend operator fun invoke(
        genreId: Long,
        page: Int,
        pageSize: Int
    ): PagedResult<SavedMovie> {
        return movieRepository.getMoviesByGenre(
            genreId = genreId,
            page = page,
            pageSize = pageSize
        )
    }
}