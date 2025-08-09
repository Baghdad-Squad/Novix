package com.baghdad.domain.usecase.movie

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.model.savedList.SavableMovie
import com.baghdad.domain.repository.MovieRepository
import javax.inject.Inject

class GetMoviesByGenreUseCase @Inject constructor(
    val movieRepository: MovieRepository
) {
    suspend operator fun invoke(
        genreId: Long,
        page: Int,
    ): PagedResult<SavableMovie> = movieRepository.getMoviesByGenre(genreId, page, PAGE_SIZE)

    companion object {
        const val PAGE_SIZE = 20
    }
}