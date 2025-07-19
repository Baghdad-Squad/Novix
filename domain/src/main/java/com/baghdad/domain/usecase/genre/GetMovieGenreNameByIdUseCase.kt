package com.baghdad.domain.usecase.genre

import com.baghdad.entity.media.Genre

class GetMovieGenreNameByIdUseCase(
    private val getGenresUseCase: GetGenresUseCase
) {
    suspend operator fun invoke(genreId: Long): Genre {
        return getGenresUseCase.getMovieGenres().first { it.id == genreId }
    }

}