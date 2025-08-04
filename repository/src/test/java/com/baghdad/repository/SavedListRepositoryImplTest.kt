package com.baghdad.repository

import com.baghdad.domain.exception.UnAuthorizedException
import com.baghdad.repository.datasource.local.LocalSessionDataStore
import com.baghdad.entity.savedList.SavedList
import com.baghdad.repository.datasource.local.LocalUserDataStore
import com.baghdad.repository.datasource.remote.RemoteSavedListDataSource
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.SavedListDto
import com.baghdad.repository.model.UserDto
import io.mockk.Runs
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class SavedListRepositoryImplTest {
    private lateinit var remoteSource: RemoteSavedListDataSource
    private lateinit var localSessionDataStore: LocalSessionDataStore
    private lateinit var localUserDataStore: LocalUserDataStore
    private lateinit var repository: SavedListRepositoryImpl

    val listId = 22L
    val movieId = 22002L
    val sessionId = "session_id"
    val title = "Favorite"

    @BeforeEach
    fun setUp() {
        remoteSource = mockk(relaxed = true)
        localSessionDataStore = mockk(relaxed = true)
        localUserDataStore = mockk(relaxed = true)
        repository = SavedListRepositoryImpl(
            remoteSavedListSource = remoteSource,
            localSessionDataStore = localSessionDataStore,
            localUserDataStore = localUserDataStore
        )
    }


    @Test
    fun `should createSavedList call remote source with correct session ID`() = runTest {
        // Given
        coEvery { localSessionDataStore.getSessionId() } returns sessionId
        coEvery { remoteSource.createSavedList(title, sessionId) } returns Unit

        // When
        repository.createSavedList(title)

        // Then
        coVerify(exactly = 1) { localSessionDataStore.getSessionId() }
        coVerify(exactly = 1) { remoteSource.createSavedList(title, sessionId) }
        localSessionDataStore = mockk(relaxed = true)
        localUserDataStore = mockk(relaxed = true)
        repository =
            SavedListRepositoryImpl(remoteSource, localSessionDataStore, localUserDataStore)
    }

    @Test
    fun `getSavedLists should return mapped paged result when remote source returns data successfully`() =
        runTest {
            // Given
            val pagedResultDto = PagedResultDto(
                data = SAMPLE_SAVED_LIST_DTOS,
                nextKey = 2,
                prevKey = null
            )

            coEvery { localSessionDataStore.getSessionId() } returns SESSION_ID
            coEvery { localUserDataStore.getUser() } returns TEST_USER
            coEvery {
                remoteSource.getSavedLists(
                    PAGE,
                    PAGE_SIZE,
                    TEST_ACCOUNT_ID,
                    SESSION_ID
                )
            } returns pagedResultDto

            // When
            val result = repository.getSavedLists(PAGE, PAGE_SIZE)

            // Then
            assertThat(result.data).hasSize(2)
            assertThat(result.data[0]).isEqualTo(
                SavedList(
                    id = 1L,
                    name = "My Favorites",
                    itemCount = 10
                )
            )
            assertThat(result.data[1]).isEqualTo(
                SavedList(
                    id = 2L,
                    name = "Watch Later",
                    itemCount = 5
                )
            )
            assertThat(result.nextKey).isEqualTo(2)
            assertThat(result.prevKey).isNull()

            coVerify(exactly = 1) { localSessionDataStore.getSessionId() }
            coVerify(exactly = 1) { localUserDataStore.getUser() }
            coVerify(exactly = 1) {
                remoteSource.getSavedLists(
                    PAGE,
                    PAGE_SIZE,
                    TEST_ACCOUNT_ID,
                    SESSION_ID
                )
            }
        }

    @Test
    fun `getSavedLists should return empty result when remote source returns empty data`() =
        runTest {
            // Given
            val pagedResultDto = PagedResultDto<SavedListDto>(
                data = emptyList(),
                nextKey = null,
                prevKey = null
            )

            coEvery { localSessionDataStore.getSessionId() } returns SESSION_ID
            coEvery { localUserDataStore.getUser() } returns TEST_USER
            coEvery {
                remoteSource.getSavedLists(
                    PAGE,
                    PAGE_SIZE,
                    TEST_ACCOUNT_ID,
                    SESSION_ID
                )
            } returns pagedResultDto

            // When
            val result = repository.getSavedLists(PAGE, PAGE_SIZE)

            // Then
            assertThat(result.data).isEmpty()
            assertThat(result.nextKey).isNull()
            assertThat(result.prevKey).isNull()

            coVerify(exactly = 1) { localSessionDataStore.getSessionId() }
            coVerify(exactly = 1) { localUserDataStore.getUser() }
            coVerify(exactly = 1) {
                remoteSource.getSavedLists(
                    PAGE,
                    PAGE_SIZE,
                    TEST_ACCOUNT_ID,
                    SESSION_ID
                )
            }
        }

    @Test
    fun `should return success response when adding a movie to saved list`() = runTest {
        // Given
        coEvery { localSessionDataStore.getSessionId() } returns sessionId
        coEvery { remoteSource.addMovieToSavedList(listId, movieId, sessionId) } just Runs

        // When
        repository.addMovieToSavedList(listId, movieId)

        // Then
        coVerify { remoteSource.addMovieToSavedList(listId, movieId, sessionId) }
    }

    @Test
    fun `should return success response when adding a tv show to saved list`() = runTest {
        // Given
        coEvery { localSessionDataStore.getSessionId() } returns sessionId
        coEvery { remoteSource.addTvShowToSavedList(listId, movieId, sessionId) } just Runs

        // When
        repository.addTvShowToSavedList(listId, movieId)

        // Then
        coVerify { remoteSource.addTvShowToSavedList(listId, movieId, sessionId) }
    }

    @Test
    fun `should throw exception when api returns error while adding a movie to saved list`() =
        runTest {
            // Given
            coEvery { localSessionDataStore.getSessionId() } returns sessionId
            coEvery {
                remoteSource.addMovieToSavedList(listId, movieId, sessionId)
            } throws Exception()

            // When & Then
            assertThrows<Exception> { repository.addMovieToSavedList(listId, movieId) }

            coVerify { remoteSource.addMovieToSavedList(listId, movieId, sessionId) }
        }

    @Test
    fun `should throw exception when api returns error while adding a tv show to saved list`() =
        runTest {
            // Given
            coEvery { localSessionDataStore.getSessionId() } returns sessionId
            coEvery {
                remoteSource.addTvShowToSavedList(listId, movieId, sessionId)
            } throws Exception()

            // When & Then
            assertThrows<Exception> { repository.addTvShowToSavedList(listId, movieId) }

            coVerify { remoteSource.addTvShowToSavedList(listId, movieId, sessionId) }
        }

    @Test
    fun `should createSavedList not crash when session ID is null`() = runBlocking {
        // Given
        val title = "Empty Session Test"
        coEvery { localSessionDataStore.getSessionId() } returns null
        coEvery { remoteSource.createSavedList(title, any()) } returns Unit

        // When
        val exception = runCatching { repository.createSavedList(title) }.exceptionOrNull()

        // Then
        assertThat(exception).isInstanceOf(UnAuthorizedException::class.java)
        coVerify { localSessionDataStore.getSessionId() }
    }

    @Test
    fun `createSavedList() should propagate exception when remote source fails`() = runTest {
        // Given
        val exception = RuntimeException("Network failure")

        coEvery { localSessionDataStore.getSessionId() } returns sessionId
        coEvery { remoteSource.createSavedList(title, sessionId) } throws exception

        // When
        val resultException = runCatching { repository.createSavedList(title) }.exceptionOrNull()

        // Then
        coVerify(exactly = 1) { localSessionDataStore.getSessionId() }
        assertThat(resultException).isNotNull()
    }

    companion object {
        private const val PAGE = 1
        private const val PAGE_SIZE = 20
        private const val SESSION_ID = "test_session_id"
        private const val TEST_ACCOUNT_ID = 12345L
        private val TEST_USER = UserDto(id = TEST_ACCOUNT_ID, userName = "testuser")

        private val SAMPLE_SAVED_LIST_DTOS = listOf(
            SavedListDto(id = 1L, name = "My Favorites", itemCount = 10),
            SavedListDto(id = 2L, name = "Watch Later", itemCount = 5)
        )
    }
}
