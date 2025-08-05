package com.baghdad.domain.usecase.review

import com.baghdad.domain.repository.MovieRepository
import com.baghdad.entity.media.Review
import javax.inject.Inject

class GetMovieReviewsUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(movieId: Long): List<Review> {
        return movieRepository.getMovieReviews(movieId)
    }
}