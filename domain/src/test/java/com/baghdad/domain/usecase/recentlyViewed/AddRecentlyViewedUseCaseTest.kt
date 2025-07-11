package com.baghdad.domain.usecase.recentlyViewed

import com.baghdad.domain.repository.RecentlyViewedRepository
import com.baghdad.domain.testHelper.getTestMovie
import com.baghdad.domain.testHelper.getTestTvShow
import com.baghdad.entity.media.Media.MediaType
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
    fun `should add movie to recently viewed when media is movie`() = runTest {
        val testMovie = getTestMovie()
        addRecentlyViewedUseCase(testMovie)

        coVerify {
            recentlyViewedRepository.addMediaToRecentlyViewed(
                mediaId = testMovie.id,
                mediaType = MediaType.MOVIE
            )
        }
    }

    @Test
    fun `should add tv show to recently viewed when media is tv show`() = runTest {
        val testTvShow = getTestTvShow()

        addRecentlyViewedUseCase(getTestTvShow())

        coVerify {
            recentlyViewedRepository.addMediaToRecentlyViewed(
                mediaId = testTvShow.id,
                mediaType = MediaType.TV_SHOW
            )
        }
    }

    @Test
    fun `should propagate exception when repository throws exception`() = runTest {
        val testMovie = getTestMovie()
        coEvery {
            recentlyViewedRepository.addMediaToRecentlyViewed(
                any(),
                any()
            )
        } throws RuntimeException()

        val resultException = runCatching { addRecentlyViewedUseCase(testMovie) }.exceptionOrNull()

        assertThat(resultException).isNotNull()
        assertThat(resultException).isInstanceOf(RuntimeException::class.java)
    }
}