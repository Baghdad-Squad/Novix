package com.baghdad.domain.usecase.tvShow

import com.baghdad.domain.model.pagination.PagedResult
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
        val result = tvShowRepository.getTrendingTvShows(page = page)
        return result.copy(
            data = result.data.filterByGenre(genreId)
        )
    }

    private fun List<TvShow>.filterByGenre(
        genreId: Long?
    ): List<TvShow> {
        return if (genreId != null) {
            filter { tvShow ->
                tvShow.genres.any { genre -> genre.id == genreId }
            }
        } else {
            this
        }
    }
}