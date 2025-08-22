package com.baghdad.domain.usecase.genre

import com.baghdad.domain.usecase.tvShow.GetTvShowGenresUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetTvShowGenreNameByIdUseCaseTest {

    private val getGenresUseCase = mockk<GetTvShowGenresUseCase>()
    private val getTvShowGenreNameByIdUseCase = GetTvShowGenreNameByIdUseCase(getGenresUseCase)

    @Test
    fun `getTvShowGenreNameByIdUseCase should return correct genre when called with valid ID`() =
        runTest {
            coEvery { getGenresUseCase.getTvShowGenres() } returns genres

            val result = getTvShowGenreNameByIdUseCase(categoryId = genreId)

            assertThat(result).isEqualTo(genre)
        }

    companion object {
        val genreId = GenreMock.GENRE_ID
        val genre = GenreMock.GENRE
        val genres = GenreMock.GENRES
    }
}