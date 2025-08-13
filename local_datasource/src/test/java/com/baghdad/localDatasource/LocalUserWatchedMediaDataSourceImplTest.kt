package com.baghdad.localDatasource

import com.baghdad.localDatasource.roomDB.dao.UserWatchedMediaDao
import com.baghdad.localDatasource.roomDB.entity.toLocalDto
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.UserWatchedMediaDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LocalUserWatchedMediaDataSourceImplTest {

    private lateinit var userWatchedMediaDao: UserWatchedMediaDao
    private lateinit var logger: Logger
    private lateinit var dataSource: LocalUserWatchedMediaDataSourceImpl

    @BeforeEach
    fun setUp() {
        userWatchedMediaDao = mockk()
        logger = mockk(relaxed = true)
        dataSource = LocalUserWatchedMediaDataSourceImpl(userWatchedMediaDao, logger)
    }

    @Test
    fun `should add to user watched media when addUserWatchedMedia is called`() = runTest {
        // Given
        coEvery { userWatchedMediaDao.upsertUserWatchedMedia(any()) } returns Unit

        // When
        val result = dataSource.addUserWatchedMedia(userWatchedMediaDto)

        // Then
        assertThat(result).isEqualTo(Unit)
    }

    @Test
    fun `should return UserWatchedMedia movies list when getPagedUserWatchedMediaMovies is called`() =
        runTest {
            // Given
            coEvery {
                userWatchedMediaDao.getPagedUserWatchedMediaMovies(
                    1,
                    any(),
                    any()
                )
            } returns listOf(
                userWatchedMediaDto.toLocalDto()
        )

            // When
            val result = dataSource.getPagedUserWatchedMediaMovies(1, pageSize = 10, page = 1)

            // Then
            assertThat(result).isEqualTo(listOf(userWatchedMediaDto))
    }

    @Test
    fun `should return UserWatchedMedia tv shows list when getPagedUserWatchedMediaTvShows is called`() =
        runTest {
            // Given
            coEvery {
                userWatchedMediaDao.getPagedUserWatchedMediaTvShows(
                    1,
                    any(),
                    any()
                )
            } returns listOf(
                userWatchedMediaDto.toLocalDto()
            )

            // When
            val result = dataSource.getPagedUserWatchedMediaTvShows(1, pageSize = 10, page = 1)

            // Then
            assertThat(result).isEqualTo(listOf(userWatchedMediaDto))
        }

    @Test
    fun `should return UserWatchedMedia movies flow when getUserWatchedMediaMovies is called`() =
        runTest {
            // Given
            every { userWatchedMediaDao.getUserWatchedMediaMovies(1) } returns flowOf(
                listOf(userWatchedMediaDto.toLocalDto())
            )

            // When
            val flow = dataSource.getUserWatchedMediaMovies(1)
            val result = flow.first()

            // Then
            assertThat(result).isEqualTo(listOf(userWatchedMediaDto))
        }

    @Test
    fun `should return UserWatchedMedia tv shows flow when getUserWatchedMediaTvShows is called`() =
        runTest {
            // Given
            every { userWatchedMediaDao.getUserWatchedMediaTvShows(1) } returns flowOf(
                listOf(userWatchedMediaDto.toLocalDto())
            )

            // When
            val flow = dataSource.getUserWatchedMediaTvShows(1)
            val result = flow.first()

            // Then
            assertThat(result).isEqualTo(listOf(userWatchedMediaDto))
        }

    @Test
    fun `should observe user watched media when observeUserWatchedMedia is called`() = runTest {
        // Given
        every { userWatchedMediaDao.observeUserWatchedMedia(1) } returns flowOf(
            listOf(userWatchedMediaDto.toLocalDto())
        )

        // When
        val flow = dataSource.observeUserWatchedMedia(1)
        val result = flow.first()

        // Then
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
