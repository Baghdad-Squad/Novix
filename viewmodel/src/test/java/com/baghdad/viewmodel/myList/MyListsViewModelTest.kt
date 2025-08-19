package com.baghdad.viewmodel.myList

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import app.cash.turbine.test
import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.domain.usecase.login.IsUserLoggedInUseCase
import com.baghdad.domain.usecase.savedList.CreateSavedListUseCase
import com.baghdad.domain.usecase.savedList.GetSavedListsUseCase
import com.baghdad.entity.savedList.SavedList
import com.baghdad.viewmodel.myLists.MyListsScreenEffect
import com.baghdad.viewmodel.myLists.MyListsScreenState.SavedListUiState
import com.baghdad.viewmodel.myLists.MyListsViewModel
import com.baghdad.viewmodel.myLists.toUiState
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


@OptIn(ExperimentalCoroutinesApi::class)
class MyListsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private var getSavedListsUseCase: GetSavedListsUseCase = mockk(relaxed = true)
    private var createSavedListUseCase: CreateSavedListUseCase = mockk()
    private var isUserLoggedInUseCase: IsUserLoggedInUseCase = mockk()

    private lateinit var viewModel: MyListsViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private val savedStateHandle = SavedStateHandle(
        mapOf(
            "isDeleteSuccess" to false
        )
    )

    private fun createViewModel(): MyListsViewModel {
        return MyListsViewModel(
            getSavedListsUseCase = getSavedListsUseCase,
            createSavedListUseCase = createSavedListUseCase,
            isUserLoggedInUseCase = isUserLoggedInUseCase,
            savedStateHandle = savedStateHandle,
            defaultDispatcher = testDispatcher
        )
    }

    @Test
    fun `should update logged in state when initialize viewmodel and user is logged in`() =
        runTest {
            coEvery { isUserLoggedInUseCase() } returns true

            viewModel = createViewModel()
            advanceUntilIdle()

            viewModel.uiState.test {
                val state = awaitItem()
                assertThat(state.isUsedLoggedIn).isTrue()
            }
        }

    @Test
    fun `should update logged in state when initialize viewmodel and user is not logged in`() =
        runTest {
            coEvery { isUserLoggedInUseCase() } returns false

            viewModel = createViewModel()
            advanceUntilIdle()

            viewModel.uiState.test {
                val state = awaitItem()
                assertThat(state.isUsedLoggedIn).isFalse()
            }
        }

    @Test
    fun `should update saved lists when initialize viewmodel and user is logged in`() = runTest {
        coEvery { isUserLoggedInUseCase() } returns true
        coEvery { getSavedListsUseCase(any(), any()) } returns PagedResult(
            data = listOf(savedListSample), nextPage = null, prevPage = null
        )
        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.savedLists).isNotEqualTo(flowOf<PagingData<SavedListUiState>>())
        }
    }

    @Test
    fun `should show add list bottom sheet when onAddListFabClick`() = runTest {

        val viewModel = createViewModel()

        viewModel.onAddListFabClick()

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.addListBottomSheetState.isVisible).isTrue()
        }
    }

    @Test
    fun `should emit NavigateToLogin when onLoginClick`() = runTest {
        val viewModel = createViewModel()

        viewModel.onLoginClick()

        viewModel.uiEffect.test {
            val effect = awaitItem()
            assertThat(effect).isEqualTo(MyListsScreenEffect.NavigateToLogin)
        }
    }

    @Test
    fun `should update list name correctly when onAddedListNameChange`() = runTest {
        val viewModel = createViewModel()

        viewModel.onAddedListNameChange(LIST_NAME)

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.addListBottomSheetState.listName).isEqualTo(LIST_NAME)
        }
    }

    @Test
    fun `should hide add list bottom sheet when onAddListBottomSheetDismiss`() = runTest {
        val viewModel = createViewModel()

        viewModel.onAddListBottomSheetDismiss()

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.addListBottomSheetState.isVisible).isFalse()
        }
    }

    @Test
    fun `should add call  when onAddListBottomSheetAddClick`() = runTest {
        val viewModel = createViewModel()

        viewModel.onAddListBottomSheetAddClick()
        advanceUntilIdle()

        coVerify { createSavedListUseCase(any()) }
    }

    @Test
    fun `should return correct save list ui state when mapping correctly`() = runTest {
        val savedListUiState = savedListSample.toUiState()

        assertThat(savedListUiState).isEqualTo(savedListUiStateSample)
    }

    private companion object {
        const val LIST_NAME = "Test List"
        val savedListSample = SavedList(
            id = 1L, name = "my list", itemCount = 5
        )
        val savedListUiStateSample = SavedListUiState(
            id = 1L, name = "my list", itemCount = 5
        )
    }
}
