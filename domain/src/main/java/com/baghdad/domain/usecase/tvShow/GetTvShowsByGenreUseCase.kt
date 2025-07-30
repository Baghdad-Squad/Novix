package com.baghdad.domain.usecase.tvShow

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.repository.TvShowRepository
import com.baghdad.entity.media.TvShow

class GetTvShowsByGenreUseCase(
    private val tvShowRepository: TvShowRepository
) {
    suspend operator fun invoke(genreId: Long, page: Int): PagedResult<TvShow> {
        return tvShowRepository.getTvShowsByGenre(genreId, page, pageSize = PAGE_SIZE)
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}