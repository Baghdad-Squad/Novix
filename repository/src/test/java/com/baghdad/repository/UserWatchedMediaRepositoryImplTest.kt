package com.baghdad.repository

import com.baghdad.domain.model.continueWatching.UserWatchedMedia
import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.domain.repository.AuthenticationRepository
import com.baghdad.domain.repository.UserWatchedMediaRepository
import com.baghdad.entity.user.User
import com.baghdad.repository.datasource.local.SavableMovieDataSource
import com.baghdad.repository.datasource.local.UserWatchedMediaDataSource
import com.baghdad.repository.datasource.remote.RemoteGenreDataSource
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.UserWatchedMediaDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UserWatchedMediaRepositoryImplTest {

    private val userWatchedMediaDataSource: UserWatchedMediaDataSource = mockk()
    private val authenticationRepositoryImpl: AuthenticationRepository = mockk()
    private val remoteGenreDataSource: RemoteGenreDataSource = mockk()
    private val localSavableMovieDataSource: SavableMovieDataSource = mockk()
    private val userWatchedMediaRepository: UserWatchedMediaRepository =
        UserWatchedMediaRepositoryImpl(
            userWatchedMediaDataSource = userWatchedMediaDataSource,
            authenticationRepository = authenticationRepositoryImpl,
            savableMovieDataSource = localSavableMovieDataSource,
            remoteGenreDataSource = remoteGenreDataSource
        )

    @BeforeEach
    fun setup() {
        coEvery { authenticationRepositoryImpl.getUserInfo() } returns createMockUser()
        coEvery { localSavableMovieDataSource.getSavedMovies() } returns emptyMap()
    }

    @Test
    fun `getUserWatchedMediaMovies returns paged movies result when data source returns data`() =
        runTest {
            val page = 1
            val pageSize = 10
            val mockDtoList = listOf(
                createMockUserWatchedMediaDto(1L, UserWatchedMediaDto.ContentType.MOVIE),
                createMockUserWatchedMediaDto(3L, UserWatchedMediaDto.ContentType.MOVIE)
            )
            val expectedData = mockDtoList.map {
                createMockUserWatchedMedia(it.contentId, UserWatchedMedia.ContentType.MOVIE)
            }
            val expectedResult = PagedResult(
                data = expectedData,
                nextKey = null,
                prevKey = null
            )

            coEvery {
                userWatchedMediaDataSource.getPagedUserWatchedMediaMovies(
                    1,
                    pageSize,
                    page
                )
            } returns mockDtoList

            val result = userWatchedMediaRepository.getPagedMovies(page, pageSize)

            assertThat(result).isEqualTo(expectedResult)
            coVerify {
                userWatchedMediaDataSource.getPagedUserWatchedMediaMovies(1, pageSize, page)
            }
        }

    @Test
    fun `getUserWatchedMediaTvShow returns paged tv shows result when data source returns data`() =
        runTest {
            val page = 1
            val pageSize = 10
            val mockDtoList = listOf(
                createMockUserWatchedMediaDto(1L, UserWatchedMediaDto.ContentType.TV_SHOW),
                createMockUserWatchedMediaDto(3L, UserWatchedMediaDto.ContentType.TV_SHOW)
            )
            val expectedData = mockDtoList.map {
                createMockUserWatchedMedia(it.contentId, UserWatchedMedia.ContentType.TV_SHOW)
            }
            val expectedResult = PagedResult(
                data = expectedData,
                nextKey = null,
                prevKey = null
            )

            coEvery {
                userWatchedMediaDataSource.getPagedUserWatchedMediaTvShows(
                    1,
                    pageSize,
                    page
                )
            } returns mockDtoList

            val result = userWatchedMediaRepository.getPagedTvShows(page, pageSize)

            assertThat(result).isEqualTo(expectedResult)
            coVerify {
                userWatchedMediaDataSource.getPagedUserWatchedMediaTvShows(1, pageSize, page)
            }
        }

    @Test
    fun `getUserWatchedMedia returns empty paged result when data source is empty`() = runTest {
        val page = 1
        val pageSize = 10
        val expectedResult = PagedResult<UserWatchedMedia>(
            data = emptyList(),
            nextKey = null,
            prevKey = null
        )

        coEvery {
            userWatchedMediaDataSource.getPagedUserWatchedMediaMovies(
                1,
                pageSize,
                page
            )
        } returns emptyList()

        val result = userWatchedMediaRepository.getPagedMovies(page, pageSize)

        assertThat(result).isEqualTo(expectedResult)
        coVerify {
            userWatchedMediaDataSource.getPagedUserWatchedMediaMovies(1, pageSize, page)
        }
    }

    @Test
    fun `getUserWatchedMedia should return paged result with next key when full page is returned`() =
        runTest {
            val page = 1
            val pageSize = 2
            val mockUserWatchedMediaList = listOf(
                createMockUserWatchedMediaDto(1L, UserWatchedMediaDto.ContentType.MOVIE),
                createMockUserWatchedMediaDto(2L, UserWatchedMediaDto.ContentType.MOVIE),
            )
            val expectedData = mockUserWatchedMediaList.map {
                createMockUserWatchedMedia(it.contentId, UserWatchedMedia.ContentType.MOVIE)
            }

            val expectedResult = PagedResult(
                data = expectedData,
                nextKey = 2,
                prevKey = null
            )

            coEvery {
                userWatchedMediaDataSource.getPagedUserWatchedMediaMovies(1, pageSize, page)
            } returns mockUserWatchedMediaList


            val result = userWatchedMediaRepository.getPagedMovies(page, pageSize)

            assertThat(expectedResult == result).isTrue()
            coVerify {
                userWatchedMediaDataSource.getPagedUserWatchedMediaMovies(
                    1,
                    pageSize,
                    page
                )
            }
        }

    @Test
    fun `addUserWatchedMedia should call data source when correct parameters for movie provided`() =
        runTest {
            val expectedDto =
                createMockUserWatchedMediaDto(999L, UserWatchedMediaDto.ContentType.TV_SHOW)
            val expectedData =
                createMockUserWatchedMedia(999L, UserWatchedMedia.ContentType.TV_SHOW)

            coEvery { userWatchedMediaDataSource.addUserWatchedMedia(expectedDto) } returns Unit
            userWatchedMediaRepository.addUserWatchedMedia(
                contentId = expectedData.contentId,
                genreIds = expectedData.genreIds,
                contentImageUrl = expectedData.contentImageUrl,
                contentType = expectedData.contentType
            )

            coVerify { userWatchedMediaDataSource.addUserWatchedMedia(expectedDto) }
        }

    @Test
    fun `addUserWatchedMedia should call data source when correct parameters for tv show provided`() =
        runTest {
            val expectedDto =
                createMockUserWatchedMediaDto(999L, UserWatchedMediaDto.ContentType.TV_SHOW)
            val expectedData =
                createMockUserWatchedMedia(999L, UserWatchedMedia.ContentType.TV_SHOW)

            coEvery { userWatchedMediaDataSource.addUserWatchedMedia(expectedDto) } returns Unit
            userWatchedMediaRepository.addUserWatchedMedia(
                contentId = expectedData.contentId,
                genreIds = expectedData.genreIds,
                contentImageUrl = expectedData.contentImageUrl,
                contentType = expectedData.contentType
            )

            coVerify { userWatchedMediaDataSource.addUserWatchedMedia(expectedDto) }
        }


    @Test
    fun `addUserWatchedMedia should handle large genre list when large genre is provided`() =
        runTest {
            val expectedDto =
                createMockUserWatchedMediaDto(999L, UserWatchedMediaDto.ContentType.TV_SHOW)
            val expectedData =
                createMockUserWatchedMedia(999L, UserWatchedMedia.ContentType.TV_SHOW)

            coEvery { userWatchedMediaDataSource.addUserWatchedMedia(expectedDto) } returns Unit

            userWatchedMediaRepository.addUserWatchedMedia(
                contentId = expectedData.contentId,
                genreIds = expectedData.genreIds,
                contentImageUrl = expectedData.contentImageUrl,
                contentType = expectedData.contentType
            )

            coVerify { userWatchedMediaDataSource.addUserWatchedMedia(expectedDto) }
        }

    @Test
    fun `getUsedMovieGenres should return used movies genres when data source returns data`() =
        runTest {
            val mockDtoList = listOf(
                createMockUserWatchedMediaDto(1L, UserWatchedMediaDto.ContentType.MOVIE),
                createMockUserWatchedMediaDto(3L, UserWatchedMediaDto.ContentType.MOVIE)
            )

            coEvery { userWatchedMediaDataSource.getUserWatchedMediaMovies(1) } returns flowOf(
                mockDtoList
            )
            coEvery { remoteGenreDataSource.getMovieGenre(any()) } returns createMockListOfGenreDto(
                3
            )
            val result = userWatchedMediaRepository.getUsedMovieGenres()

            assertThat(result).isNotNull()
        }

    @Test
    fun `getUsedTvShowGenres should return used movies genres when data source returns data`() =
        runTest {
            val mockDtoList = listOf(
                createMockUserWatchedMediaDto(1L, UserWatchedMediaDto.ContentType.TV_SHOW),
                createMockUserWatchedMediaDto(3L, UserWatchedMediaDto.ContentType.TV_SHOW)
            )

            coEvery { userWatchedMediaDataSource.getUserWatchedMediaTvShows(1) } returns flowOf(
                mockDtoList
            )
            coEvery { remoteGenreDataSource.getTvShowGenre(any()) } returns createMockListOfGenreDto(
                3
            )
            val result = userWatchedMediaRepository.getUsedTvShowGenres()

            assertThat(result).isNotNull()
        }

    companion object {
        private fun createMockUserWatchedMediaDto(
            contentId: Long,
            contentType: UserWatchedMediaDto.ContentType
        ) = UserWatchedMediaDto(
            contentId = contentId,
            genreIds = listOf(1L, 2L),
            contentImageUrl = "https://example.com/image$contentId.jpg",
            contentType = contentType,
            userId = 1L
        )

        private fun createMockUser() = User(
            id = 1L,
            userName = "testuser",
            imageUrl = "https://example.com/avatar.jpg"
        )

        private fun createMockUserWatchedMedia(
            contentId: Long,
            contentType: UserWatchedMedia.ContentType
        ) = UserWatchedMedia(
            contentId = contentId,
            genreIds = listOf(1L, 2L),
            contentImageUrl = "https://example.com/image$contentId.jpg",
            contentType = contentType,
            userId = 1L,
            isSaved = false,
            listId = null,
        )

        private fun createMockGenreDto(id: Long): GenreDto {
            return GenreDto(
                id = id,
                name = "Action",
                type = GenreDto.GenreType.TV_SHOW
            )
        }

        private fun createMockListOfGenreDto(count: Int): List<GenreDto> {
            return (1..count).map { id ->
                createMockGenreDto(id.toLong())
            }
        }
    }
}