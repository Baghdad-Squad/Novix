package com.baghdad.domain.usecase.tvShow

import com.baghdad.domain.repository.TvShowRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetTvShowGenresUseCaseTest {

    private val tvShowRepository = mockk<TvShowRepository>()
    private val getTvShowGenresUseCase = GetTvShowGenresUseCase(tvShowRepository)

    @Test
    fun `getTvShowGenresUseCase should return list of genres when repository returns data`() =
        runTest {
            coEvery { tvShowRepository.getGenres() } returns genres

            val result = getTvShowGenresUseCase.getTvShowGenres()

            assertThat(result).isEqualTo(genres)
        }

    companion object {
        val genres = TvShowMock.GENRES
    }
}