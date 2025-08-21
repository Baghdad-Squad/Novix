package com.baghdad.domain.usecase.tvShow

import com.baghdad.domain.repository.TvShowRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetTvShowsByGenreUseCaseTest {

    private val tvShowRepository = mockk<TvShowRepository>()
    private val getTvShowsByGenreUseCase = GetTvShowsByGenreUseCase(tvShowRepository)

    @Test
    fun `getTvShowsByGenreUseCase should return list of tv shows when repository returns data`() =
        runTest {
            coEvery {
                tvShowRepository.getTvShowsByGenre(
                    genreId,
                    page,
                    pageSize
                )
            } returns tvShowResult

            val result = getTvShowsByGenreUseCase(genreId, page, pageSize)

            assertThat(result).isEqualTo(tvShowResult)
        }

    private companion object {
        val tvShowResult = TvShowMock.TV_SHOW_RESULT
        val genreId = TvShowMock.GENRE_ID
        val page = 1
        val pageSize = 20
    }
}