package com.baghdad.domain.usecase.review

import com.baghdad.domain.repository.MovieRepository
import com.baghdad.entity.media.Review

class GetMovieReviewsUseCase(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(movieId: Long): List<Review> {
        return movieRepository.getMovieReviews(movieId)
    }
}