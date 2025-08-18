package com.baghdad.domain.usecase.tvShow

import com.baghdad.domain.repository.TvShowRepository
import com.baghdad.entity.media.TvShow
import javax.inject.Inject

class GetPopularTvShowsUseCase @Inject constructor(
    private val tvShowRepository: TvShowRepository
) {
    suspend operator fun invoke() : List<TvShow> {
        return tvShowRepository.getPopularTvShows()
    }
}