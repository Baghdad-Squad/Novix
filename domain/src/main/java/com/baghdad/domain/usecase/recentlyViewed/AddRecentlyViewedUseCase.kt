package com.baghdad.domain.usecase.recentlyViewed

import com.baghdad.domain.repository.RecentlyViewedRepository

class AddRecentlyViewedUseCase(
    private val recentlyViewedRepository: RecentlyViewedRepository
) {
    suspend fun addRecentlyViewedMovie(movieId: Long) {
        return recentlyViewedRepository.addMovieToRecentlyViewed(
            movieId = movieId
        )
    }

    suspend fun addRecentlyViewedTvShow(tvShowId: Long) {
        return recentlyViewedRepository.addTvShowToRecentlyViewed(
            tvShowId = tvShowId
        )
    }
}