package com.baghdad.domain.usecase.recentlyViewed

import com.baghdad.domain.model.search.RecentlyViewed.ContentType
import com.baghdad.domain.repository.RecentlyViewedRepository
import com.baghdad.domain.testHelper.getRecentlyViewedItem
import com.baghdad.entity.media.Genre
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

    private val dummyGenres = listOf(
        Genre(id = 1, name = "Action"),
        Genre(id = 2, name = "Drama")
    )

    @BeforeEach
    fun setUp() {
        recentlyViewedRepository = mockk(relaxed = true)
        addRecentlyViewedUseCase = AddRecentlyViewedUseCase(recentlyViewedRepository)
    }

    @Test
    fun `invoke() should add movie to recently viewed when contentType is MOVIE`() = runTest {
        val movie = getRecentlyViewedItem(contentType = ContentType.MOVIE)

        addRecentlyViewedUseCase(
            contentId = movie.contentId,
            contentImageUrl = movie.contentImageUrl,
            mediaGenres = dummyGenres,
            contentType = movie.contentType
        )

        coVerify(exactly = 1) {
            recentlyViewedRepository.addRecentlyViewed(
                withArg {
                    assertThat(it.contentId).isEqualTo(movie.contentId)
                    assertThat(it.contentImageUrl).isEqualTo(movie.contentImageUrl)
                    assertThat(it.contentType).isEqualTo(ContentType.MOVIE)
                    assertThat(it.viewedAt).isNotNull()
                },
                mediaGenres = dummyGenres
            )
        }
    }

    @Test
    fun `invoke() should add tv show to recently viewed when contentType is TV_SHOW`() = runTest {
        val tvShow = getRecentlyViewedItem(contentType = ContentType.TV_SHOW)

        addRecentlyViewedUseCase(
            contentId = tvShow.contentId,
            contentImageUrl = tvShow.contentImageUrl,
            mediaGenres = dummyGenres,
            contentType = tvShow.contentType
        )

        coVerify(exactly = 1) {
            recentlyViewedRepository.addRecentlyViewed(
                withArg {
                    assertThat(it.contentId).isEqualTo(tvShow.contentId)
                    assertThat(it.contentImageUrl).isEqualTo(tvShow.contentImageUrl)
                    assertThat(it.contentType).isEqualTo(ContentType.TV_SHOW)
                    assertThat(it.viewedAt).isNotNull()
                },
                mediaGenres = dummyGenres
            )
        }
    }

    @Test
    fun `invoke() should propagate exception when repository throws`() = runTest {
        val recentlyViewed = getRecentlyViewedItem()

        coEvery {
            recentlyViewedRepository.addRecentlyViewed(any(), any())
        } throws RuntimeException("Database error")

        val exception = runCatching {
            addRecentlyViewedUseCase(
                contentId = recentlyViewed.contentId,
                contentImageUrl = recentlyViewed.contentImageUrl,
                mediaGenres = dummyGenres,
                contentType = recentlyViewed.contentType
            )
        }.exceptionOrNull()

        assertThat(exception).isNotNull()
        assertThat(exception).isInstanceOf(RuntimeException::class.java)
        assertThat(exception?.message).isEqualTo("Database error")
    }
}
