package com.baghdad.domain.usecase.genre

import com.baghdad.domain.usecase.movie.GetMovieGenresUseCase
import com.baghdad.entity.media.Genre
import javax.inject.Inject

class GetMovieGenreNameByIdUseCase @Inject constructor(
    private val getGenresUseCase: GetMovieGenresUseCase
) {
    suspend operator fun invoke(genreId: Long): Genre {
        return getGenresUseCase.getMovieGenres().first { it.id == genreId }
    }

}