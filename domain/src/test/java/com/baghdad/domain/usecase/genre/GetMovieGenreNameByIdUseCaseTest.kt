package com.baghdad.domain.usecase.genre

import com.baghdad.domain.usecase.movie.GetMovieGenresUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetMovieGenreNameByIdUseCaseTest {

    private val getGenresUseCase = mockk<GetMovieGenresUseCase>()
    private val getMovieGenreNameByIdUseCase = GetMovieGenreNameByIdUseCase(getGenresUseCase)

    @Test
    fun `getMovieGenreNameByIdUseCase should return correct genre when called with valid ID`() =
        runTest {
            coEvery { getGenresUseCase.getMovieGenres() } returns genres

            val result = getMovieGenreNameByIdUseCase(genreId)

            assertThat(result).isEqualTo(genre)
        }

    companion object {
        val genreId = GenreMock.GENRE_ID
        val genre = GenreMock.GENRE
        val genres = GenreMock.GENRES
    }
}