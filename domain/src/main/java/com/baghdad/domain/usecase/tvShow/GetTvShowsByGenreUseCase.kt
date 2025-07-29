package com.baghdad.domain.usecase.tvShow

import com.baghdad.domain.repository.TvShowRepository
import com.baghdad.entity.media.TvShow
import javax.inject.Inject

class GetTvShowsByGenreUseCase @Inject constructor(
    private val tvShowRepository: TvShowRepository
) {
    suspend operator fun invoke(genreId: Long, page: Int): List<TvShow> {
        return tvShowRepository.getTvShowsByGenre(genreId, page)
    }
}