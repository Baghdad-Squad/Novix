package com.baghdad.domain.usecase.genre

import com.baghdad.domain.usecase.tvShow.GetTvShowGenresUseCase
import com.baghdad.entity.media.Genre
import javax.inject.Inject

class GetTvShowGenreNameByIdUseCase @Inject constructor(
    private val getGenresUseCase: GetTvShowGenresUseCase
) {
    suspend operator fun invoke(categoryId: Long): Genre {
        return getGenresUseCase.getTvShowGenres().first { it.id == categoryId }
    }
}