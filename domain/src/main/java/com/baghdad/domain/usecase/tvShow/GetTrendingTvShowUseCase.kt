package com.baghdad.domain.usecase.tvShow

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.repository.TvShowRepository
import com.baghdad.entity.media.TvShow
import javax.inject.Inject

class GetTrendingTvShowUseCase @Inject constructor(
    private val tvShowRepository: TvShowRepository
) {
    suspend operator fun invoke(
        page: Int,
        genreId: Long?,
    ): PagedResult<TvShow> {
        val result = tvShowRepository.getTrendingTvShows(page)
        val filteredTvShows =
            if (genreId != null) {
                result.data.filter { tvShow ->
                tvShow.genres.any { genre -> genre.id == genreId }
            }
        } else {
            result.data
        }
        return result.copy(data = filteredTvShows)
    }
}