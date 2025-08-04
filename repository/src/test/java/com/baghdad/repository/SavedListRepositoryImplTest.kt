import com.baghdad.entity.savedList.SavedList
import com.baghdad.repository.SavedListRepositoryImpl
import com.baghdad.repository.datasource.local.LocalSessionDataStore
import com.baghdad.repository.datasource.local.LocalUserDataStore
import com.baghdad.repository.datasource.remote.RemoteSavedListDataSource
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.SavedListDto
import com.baghdad.repository.model.UserDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SavedListRepositoryImplTest {
    private lateinit var remoteSource: RemoteSavedListDataSource
    private lateinit var localSessionDataStore: LocalSessionDataStore
    private lateinit var localUserDataStore: LocalUserDataStore
    private lateinit var repository: SavedListRepositoryImpl

    @BeforeEach
    fun setUp() {
        remoteSource = mockk(relaxed = true)
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


    companion object {
        private const val PAGE = 1
        private const val PAGE_SIZE = 20
        private const val SESSION_ID = "test_session_id"
        private const val DEFAULT_ACCOUNT_ID = 0L
        private const val TEST_ACCOUNT_ID = 12345L
        private val TEST_USER = UserDto(id = TEST_ACCOUNT_ID, userName = "testuser")

        private val SAMPLE_SAVED_LIST_DTOS = listOf(
            SavedListDto(id = 1L, name = "My Favorites", itemCount = 10),
            SavedListDto(id = 2L, name = "Watch Later", itemCount = 5)
        )
    }

}
