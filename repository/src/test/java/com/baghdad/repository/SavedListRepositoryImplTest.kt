package com.baghdad.repository

import com.baghdad.entity.savedList.SavedList
import com.baghdad.repository.datasource.local.SavableMovieDataSource
import com.baghdad.repository.datasource.local.UserDataSource
import com.baghdad.repository.datasource.remote.RemoteSavedListDataSource
import com.baghdad.repository.dummyData.DummyDataFactory.DummyDataFactory.SAVABLE_MOVIE_DTO
import com.baghdad.repository.exception.NetworkException
import com.baghdad.repository.exception.UnknownNetworkException
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.SavedListDto
import com.baghdad.repository.model.UserDto
import com.baghdad.repository.model.savedList.SavedListDetailsDto
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class SavedListRepositoryImplTest {
    private val remoteSource: RemoteSavedListDataSource = mockk()
    private val localUserDataSource: UserDataSource = mockk(relaxed = true)
    private val savableMovieDataSource: SavableMovieDataSource = mockk(relaxed = true)
    private val repository: SavedListRepositoryImpl =
        SavedListRepositoryImpl(
            remoteSavedListSource = remoteSource,
            userDataSource = localUserDataSource,
            savableMovieDataSource = savableMovieDataSource
        )

    @Test
    fun `should createSavedList call remote source with correct session ID`() =
        runTest {
            coEvery { remoteSource.createSavedList(TITLE) } returns Unit

            repository.createSavedList(TITLE)

            coVerify(exactly = 1) { remoteSource.createSavedList(TITLE) }
        }

    @Test
    fun `getSavedLists should return mapped paged result when remote source returns data successfully`() =
        runTest {
            // Given
            val pagedResultDto =
                PagedResultDto(
                    data = SAMPLE_SAVED_LIST_DTOS,
                    nextKey = 2,
                    prevKey = null,
                )

            coEvery { localUserDataSource.getUser() } returns TEST_USER
            coEvery {
                remoteSource.getSavedLists(
                    PAGE,
                    PAGE_SIZE,
                    TEST_ACCOUNT_ID,
                )
            } returns pagedResultDto

            val result = repository.getSavedLists(PAGE, PAGE_SIZE)

            assertThat(result.data).hasSize(2)
            assertThat(result.data[0]).isEqualTo(
                SavedList(
                    id = 1L,
                    name = "My Favorites",
                    itemCount = 10,
                ),
            )
            assertThat(result.data[1]).isEqualTo(
                SavedList(
                    id = 2L,
                    name = "Watch Later",
                    itemCount = 5,
                ),
            )
            assertThat(result.nextPage).isEqualTo(2)
            assertThat(result.prevPage).isNull()

            coVerify(exactly = 1) { localUserDataSource.getUser() }
            coVerify(exactly = 1) {
                remoteSource.getSavedLists(
                    page = PAGE,
                    pageSize = PAGE_SIZE,
                    accountId = TEST_ACCOUNT_ID,
                )
            }
        }

    @Test
    fun `getSavedLists should return empty result when remote source returns empty data`() =
        runTest {
            val pagedResultDto =
                PagedResultDto<SavedListDto>(
                    data = emptyList(),
                    nextKey = null,
                    prevKey = null,
                )

            coEvery { localUserDataSource.getUser() } returns TEST_USER
            coEvery {
                remoteSource.getSavedLists(
                    page = PAGE,
                    pageSize = PAGE_SIZE,
                    accountId = TEST_ACCOUNT_ID,
                )
            } returns pagedResultDto

            val result = repository.getSavedLists(PAGE, PAGE_SIZE)

            assertThat(result.data).isEmpty()
            assertThat(result.nextPage).isNull()
            assertThat(result.prevPage).isNull()

            coVerify(exactly = 1) { localUserDataSource.getUser() }
            coVerify(exactly = 1) {
                remoteSource.getSavedLists(
                    page = PAGE,
                    pageSize = PAGE_SIZE,
                    accountId = TEST_ACCOUNT_ID,
                )
            }
        }

    @Test
    fun `should return success response when adding a movie to saved list`() =
        runTest {
            coEvery { remoteSource.addMovieToSavedList(LIST_ID, MOVIE_ID) } just Runs

            repository.addMovieToSavedList(LIST_ID, MOVIE_ID)

            coVerify { remoteSource.addMovieToSavedList(LIST_ID, MOVIE_ID) }
        }

    @Test
    fun `should throw exception when api returns error while adding a movie to saved list`() =
        runTest {
            coEvery {
                remoteSource.addMovieToSavedList(LIST_ID, MOVIE_ID)
            } throws Exception()

            assertThrows<Exception> { repository.addMovieToSavedList(LIST_ID, MOVIE_ID) }

            coVerify { remoteSource.addMovieToSavedList(LIST_ID, MOVIE_ID) }
        }

    @Test
    fun `should remove movie from saved list when the movie removed successfully`() = runTest {
        coEvery { remoteSource.removeMovieFromSavedList(LIST_ID, MOVIE_ID) } just Runs

        repository.removeMovieFromSavedList(LIST_ID, MOVIE_ID)

        coVerify { remoteSource.removeMovieFromSavedList(LIST_ID, MOVIE_ID) }
    }

    @Test
    fun `createSavedList() should propagate exception when remote source fails`() =
        runTest {
            val exception = RuntimeException("Network failure")

            coEvery { remoteSource.createSavedList(TITLE) } throws exception

            val resultException =
                runCatching { repository.createSavedList(TITLE) }.exceptionOrNull()

            assertThat(resultException).isNotNull()
        }

    @Test
    fun `getSavedListDetails should return mapped SavedListDetails from remote source when data is retrieved successfully`() =
        runTest {
            val listId = 1L
            val dto =
                SavedListDetailsDto(
                    savedList = SavedListDto(1, "My Watchlist", 2),
                    pagedItems =
                        PagedResultDto(
                            data =
                                listOf(
                                    SAVABLE_MOVIE_DTO,
                                    SAVABLE_MOVIE_DTO,
                                ),
                            nextKey = null,
                            prevKey = 1
                        ),
                )
            val expectedEntity = dto.toEntity()

            coEvery { remoteSource.getSavedListDetails(listId, PAGE, PAGE_SIZE) } returns dto

            val result = repository.getSavedListDetails(listId, PAGE, PAGE_SIZE)

            assertThat(result).isEqualTo(expectedEntity)
            coVerify(exactly = 1) { remoteSource.getSavedListDetails(listId, PAGE, PAGE_SIZE) }
        }

    @Test
    fun `getSavedListDetails throws when remote source throws`() =
        runTest {
            val listId = 999L
            val exception = UnknownNetworkException()
            coEvery { remoteSource.getSavedListDetails(listId, PAGE, PAGE_SIZE) } throws exception

            val thrown =
                runCatching {
                    repository.getSavedListDetails(
                        listId = listId,
                        page = PAGE,
                        pageSize = PAGE_SIZE,
                    )
                }.exceptionOrNull()

            assertThat(thrown).isInstanceOf(NetworkException::class.java)
            coVerify(exactly = 1) { remoteSource.getSavedListDetails(listId, PAGE, PAGE_SIZE) }
        }

    @Test
    fun `should delete saved list when remote delete is successful`() =
        runTest {
            coEvery { remoteSource.deleteSavedListById(LIST_ID) } just Runs

            repository.deleteSavedListById(LIST_ID)

            coVerify { remoteSource.deleteSavedListById(LIST_ID) }
        }

    @Test
    fun `should throw exception when remote delete fails`() =
        runTest {
            coEvery {
                remoteSource.deleteSavedListById(LIST_ID)
            } throws RuntimeException("Remote failure")

            assertThrows<RuntimeException> {
                runTest { repository.deleteSavedListById(LIST_ID) }
            }
        }


    companion object {
        private const val LIST_ID = 1L
        private const val PAGE = 1
        private const val PAGE_SIZE = 20
        private const val TEST_ACCOUNT_ID = 12345L
        private val TEST_USER =
            UserDto(id = TEST_ACCOUNT_ID, userName = "testuser", imageUrl = null)
        private const val MOVIE_ID = 22002L
        private const val TITLE = "Favorite"

        private val SAMPLE_SAVED_LIST_DTOS =
            listOf(
                SavedListDto(id = 1L, name = "My Favorites", itemCount = 10),
                SavedListDto(id = 2L, name = "Watch Later", itemCount = 5),
            )
    }
}
