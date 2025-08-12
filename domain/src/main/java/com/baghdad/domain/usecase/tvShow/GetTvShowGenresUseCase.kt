package com.baghdad.domain.usecase.tvShow

import com.baghdad.domain.repository.TvShowRepository
import com.baghdad.entity.media.Genre
import javax.inject.Inject

class GetTvShowGenresUseCase @Inject constructor(
    private val tvShowRepository: TvShowRepository,
) {
    suspend fun getTvShowGenres(): List<Genre> {
        return tvShowRepository.getGenres()
    }
}