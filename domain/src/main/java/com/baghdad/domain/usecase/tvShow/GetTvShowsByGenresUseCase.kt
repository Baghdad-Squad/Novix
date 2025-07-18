package com.baghdad.domain.usecase.tvShow

import com.baghdad.domain.repository.TvShowRepository
import com.baghdad.entity.media.TvShow

class GetTvShowsByGenresUseCase(private val tvShowRepository: TvShowRepository) {
    suspend operator fun invoke(genreId: Long, page: Int): List<TvShow> {
        return tvShowRepository.getTvShowsByGenre(genreId, page)
    }
}