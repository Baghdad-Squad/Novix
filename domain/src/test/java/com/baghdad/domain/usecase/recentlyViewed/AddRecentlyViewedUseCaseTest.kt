package com.baghdad.domain.usecase.recentlyViewed

import com.baghdad.domain.repository.RecentlyViewedRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AddRecentlyViewedUseCaseTest {

    private lateinit var recentlyViewedRepository: RecentlyViewedRepository
    private lateinit var addRecentlyViewedUseCase: AddRecentlyViewedUseCase

    @BeforeEach
    fun setUp() {
        recentlyViewedRepository = mockk(relaxed = true)
        addRecentlyViewedUseCase = AddRecentlyViewedUseCase(recentlyViewedRepository)
    }

    @Test
    fun `should add movie to recently viewed`() = runTest {
        val movieId = 123L

        addRecentlyViewedUseCase.addRecentlyViewedMovie(movieId)

        coVerify {
            recentlyViewedRepository.addMovieToRecentlyViewed(movieId)
        }
    }

    @Test
    fun `should add tv show to recently viewed`() = runTest {
        val tvShowId = 456L

        addRecentlyViewedUseCase.addRecentlyViewedTvShow(tvShowId)

        coVerify {
            recentlyViewedRepository.addTvShowToRecentlyViewed(tvShowId)
        }
    }

    @Test
    fun `should propagate exception when repository throws exception for movie`() = runTest {
        val movieId = 789L
        coEvery { recentlyViewedRepository.addMovieToRecentlyViewed(movieId) } throws RuntimeException()

        val resultException = runCatching {
            addRecentlyViewedUseCase.addRecentlyViewedMovie(movieId)
        }.exceptionOrNull()

        assertThat(resultException).isNotNull()
        assertThat(resultException).isInstanceOf(RuntimeException::class.java)
    }

    @Test
    fun `should propagate exception when repository throws exception for tv show`() = runTest {
        val tvShowId = 321L
        coEvery { recentlyViewedRepository.addTvShowToRecentlyViewed(tvShowId) } throws RuntimeException()

        val resultException = runCatching {
            addRecentlyViewedUseCase.addRecentlyViewedTvShow(tvShowId)
        }.exceptionOrNull()

        assertThat(resultException).isNotNull()
        assertThat(resultException).isInstanceOf(RuntimeException::class.java)
    }
}