package com.baghdad.localDatasource

import com.baghdad.localDatasource.mapper.toLocalDto
import com.baghdad.localDatasource.roomDB.dao.RecentlyViewedDao
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.RecentlyViewedDto
import com.baghdad.repository.model.RecentlyViewedDto.ContentType
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class LocalRecentlyViewedDataSourceImplTest {
    private var recentlyViewedDao: RecentlyViewedDao = mockk()
    private var logger: Logger = mockk(relaxed = true)
    private var localRecentlyViewedDataSourceImpl: LocalRecentlyViewedDataSourceImpl =
        LocalRecentlyViewedDataSourceImpl(recentlyViewedDao = recentlyViewedDao, logger = logger)

    @Test
    fun `getAllRecentlyViewed should return flow of recently viewed items when dao returns data`() =
        runTest {
            val recentlyViewedTvShow = recentlyViewedTvShow.toLocalDto()
            coEvery { recentlyViewedDao.getAllRecentlyViewed() } returns flowOf(
                listOf(
                    recentlyViewedTvShow
                )
            )

            val result = localRecentlyViewedDataSourceImpl.getAllRecentlyViewed().first()

            assertThat(result[0].contentId).isEqualTo(recentlyViewedTvShow.contentId)
        }

    @Test
    fun `getAllRecentlyViewed should return empty flow when dao returns empty list`() = runTest {
        coEvery { recentlyViewedDao.getAllRecentlyViewed() } returns flowOf(emptyList())

        val result = localRecentlyViewedDataSourceImpl.getAllRecentlyViewed().first()

        assertThat(result).isEmpty()
    }

    @Test
    fun `deleteAllRecentlyViewed should delete all recently viewed when invoked`() = runTest {
        coEvery { recentlyViewedDao.deleteAllRecentlyViewed() } returns Unit

        localRecentlyViewedDataSourceImpl.deleteAllRecentlyViewed()

        coVerify(exactly = 1) { recentlyViewedDao.deleteAllRecentlyViewed() }
    }

    @Test
    fun `addMediaToRecentlyViewed should add media to recently viewed when invoked`() = runTest {
        coEvery { recentlyViewedDao.upsertRecentlyViewed(any()) } returns Unit

        localRecentlyViewedDataSourceImpl.addMediaToRecentlyViewed(recentlyViewedMovie)

        coVerify(exactly = 1) { recentlyViewedDao.upsertRecentlyViewed(recentlyViewedMovie.toLocalDto()) }
    }

    private companion object {
        val recentlyViewedTvShow = RecentlyViewedDto(
            contentId = 1L,
            contentImageUrl = "https://example.com/poster.jpg",
            contentType = ContentType.TV_SHOW,
            viewedAtEpochMillis = 123456789L
        )

        val recentlyViewedMovie = RecentlyViewedDto(
            contentId = 2L,
            contentImageUrl = "https://example.com/movie_poster.jpg",
            contentType = ContentType.MOVIE,
            viewedAtEpochMillis = 987654321L
        )
    }
}