package com.baghdad.domain.usecase.movie

import com.baghdad.domain.model.MediaAccountStates
import com.baghdad.domain.repository.MovieRepository
import javax.inject.Inject

class GetMovieAccountStatesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(movieId: Long): MediaAccountStates {
        return movieRepository.getMovieStates(movieId)
    }
}