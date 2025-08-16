package com.baghdad.localDatasource

import com.baghdad.localDatasource.mapper.toLocalDto
import com.baghdad.localDatasource.roomDB.dao.UserWatchedMediaDao
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.UserWatchedMediaDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class UserWatchedMediaDataSourceImplTest {

    private val userWatchedMediaDao: UserWatchedMediaDao = mockk()
    private val logger: Logger = mockk(relaxed = true)
    private val dataSource: UserWatchedMediaDataSourceImpl =
        UserWatchedMediaDataSourceImpl(userWatchedMediaDao, logger)

    @Test
    fun `should add to user watched media when addUserWatchedMedia is called`() = runTest {
        coEvery { userWatchedMediaDao.upsertUserWatchedMedia(any()) } returns Unit

        val result = dataSource.addUserWatchedMedia(userWatchedMediaDto)

        assertThat(result).isEqualTo(Unit)
    }

    @Test
    fun `should return UserWatchedMedia movies list when getPagedUserWatchedMediaMovies is called`() =
        runTest {
            coEvery {
                userWatchedMediaDao.getPagedUserWatchedMediaMovies(
                    1,
                    any(),
                    any()
                )
            } returns listOf(
                userWatchedMediaDto.toLocalDto()
            )

            val result = dataSource.getPagedUserWatchedMediaMovies(1, pageSize = 10, page = 1)

            assertThat(result).isEqualTo(listOf(userWatchedMediaDto))
        }

    @Test
    fun `should return UserWatchedMedia tv shows list when getPagedUserWatchedMediaTvShows is called`() =
        runTest {
            coEvery {
                userWatchedMediaDao.getPagedUserWatchedMediaTvShows(
                    1,
                    any(),
                    any()
                )
            } returns listOf(
                userWatchedMediaDto.toLocalDto()
            )

            val result = dataSource.getPagedUserWatchedMediaTvShows(1, pageSize = 10, page = 1)

            assertThat(result).isEqualTo(listOf(userWatchedMediaDto))
        }

    @Test
    fun `should return UserWatchedMedia movies flow when getUserWatchedMediaMovies is called`() =
        runTest {
            every { userWatchedMediaDao.getUserWatchedMediaMovies(1) } returns flowOf(
                listOf(userWatchedMediaDto.toLocalDto())
            )

            val flow = dataSource.getUserWatchedMediaMovies(1)
            val result = flow.first()

            assertThat(result).isEqualTo(listOf(userWatchedMediaDto))
        }

    @Test
    fun `should return UserWatchedMedia tv shows flow when getUserWatchedMediaTvShows is called`() =
        runTest {

        every { userWatchedMediaDao.getUserWatchedMediaTvShows(1) } returns flowOf(
                listOf(userWatchedMediaDto.toLocalDto())
            )

            val flow = dataSource.getUserWatchedMediaTvShows(1)
            val result = flow.first()

            assertThat(result).isEqualTo(listOf(userWatchedMediaDto))
        }

    @Test
    fun `should observe user watched media when observeUserWatchedMedia is called`() = runTest {

    every { userWatchedMediaDao.observeUserWatchedMedia(1) } returns flowOf(
            listOf(userWatchedMediaDto.toLocalDto())
        )

        val flow = dataSource.observeUserWatchedMedia(1)
        val result = flow.first()

        assertThat(result).isEqualTo(listOf(userWatchedMediaDto))
    }

    companion object {
        val userWatchedMediaDto = UserWatchedMediaDto(
            contentId = 1,
            genreIds = listOf(1L, 2L),
            contentImageUrl = "test",
            contentType = UserWatchedMediaDto.ContentType.MOVIE,
            userId = 1,
        )
    }
}
