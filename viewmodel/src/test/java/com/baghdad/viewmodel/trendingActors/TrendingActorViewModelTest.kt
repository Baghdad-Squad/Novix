package com.baghdad.viewmodel.trendingActors

import androidx.paging.PagingData
import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.usecase.actor.GetTrendingActorsUseCase
import com.baghdad.entity.person.Actor
import com.baghdad.viewmodel.base.BaseUiState
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyAll
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TrendingActorsCompleteTest {

    private lateinit var getTrendingActorsUseCase: GetTrendingActorsUseCase
    private lateinit var trendingActorViewModel: TrendingActorViewModel
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getTrendingActorsUseCase = mockk()
        val mockActors = createMockActors()
        val pagedResult = PagedResult(
            data = mockActors,
            nextKey = 2,
            prevKey = null
        )
        coEvery { getTrendingActorsUseCase(any()) } returns pagedResult
        trendingActorViewModel = TrendingActorViewModel(
            getTrendingActorsUseCase = getTrendingActorsUseCase
        )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onBackClick should send OnBackClick effect`() = runTest {
        var receivedEffect: TrendingActorsUiEffect? = null
        val job = launch {
            trendingActorViewModel.uiEffect.collect { effect ->
                receivedEffect = effect
            }
        }

        trendingActorViewModel.onBackClick()
        testDispatcher.scheduler.advanceUntilIdle()

        Assertions.assertEquals(TrendingActorsUiEffect.OnBackClick, receivedEffect)
        job.cancel()
    }

    @Test
    fun `onTrendingActorClick should send NavigateToActorsDetails effect`() = runTest {
        val actorId = 123L
        var receivedEffect: TrendingActorsUiEffect? = null
        val job = launch {
            trendingActorViewModel.uiEffect.collect { effect ->
                receivedEffect = effect
            }
        }

        trendingActorViewModel.onTrendingActorClick(actorId)
        testDispatcher.scheduler.advanceUntilIdle()

        Assertions.assertEquals(TrendingActorsUiEffect.NavigateToActorsDetails(actorId), receivedEffect)
        job.cancel()
    }

    @Test
    fun `multiple onTrendingActorClick calls should send multiple effects`() = runTest {
        val receivedEffects = mutableListOf<TrendingActorsUiEffect>()
        val actorIds = listOf(123L, 456L, 789L)
        val job = launch {
            trendingActorViewModel.uiEffect.collect { effect ->
                receivedEffects.add(effect)
            }
        }

        actorIds.forEach { actorId ->
            trendingActorViewModel.onTrendingActorClick(actorId)
            testDispatcher.scheduler.advanceUntilIdle()
        }

        Assertions.assertEquals(3, receivedEffects.size)
        Assertions.assertEquals(TrendingActorsUiEffect.NavigateToActorsDetails(123L), receivedEffects[0])
        Assertions.assertEquals(TrendingActorsUiEffect.NavigateToActorsDetails(456L), receivedEffects[1])
        Assertions.assertEquals(TrendingActorsUiEffect.NavigateToActorsDetails(789L), receivedEffects[2])
        job.cancel()
    }

    @Test
    fun `default constructor should create state with default values`() {
        val state = TrendingActorsUiState()
        Assertions.assertEquals(emptyFlow<PagingData<TrendingActorsUiState.TrendingActor>>().toString(), state.trendingActor.toString())
        Assertions.assertFalse(state.isLoading)
    }

    @Test
    fun `constructor with parameters should set values correctly`() {
        val mockPagingData = PagingData.from(listOf(createTrendingActor()))
        val trendingActorFlow = flowOf(mockPagingData)
        val state = TrendingActorsUiState(
            trendingActor = trendingActorFlow,
            isLoading = true
        )

        Assertions.assertEquals(trendingActorFlow, state.trendingActor)
        Assertions.assertTrue(state.isLoading)
    }

    @Test
    fun `isLoading property should be accessible from BaseUiState interface`() {
        val state: BaseUiState = TrendingActorsUiState(isLoading = true)
        Assertions.assertTrue(state.isLoading)
    }

    @Test
    fun `copy function should work correctly with isLoading change`() {
        val originalState = TrendingActorsUiState(isLoading = false)
        val newState = originalState.copy(isLoading = true)
        Assertions.assertTrue(newState.isLoading)
        Assertions.assertFalse(originalState.isLoading)
    }

    @Test
    fun `copy function should work correctly with trendingActor change`() {
        val originalFlow = emptyFlow<PagingData<TrendingActorsUiState.TrendingActor>>()
        val newFlow = flowOf(PagingData.from(listOf(createTrendingActor())))
        val originalState = TrendingActorsUiState(trendingActor = originalFlow)
        val newState = originalState.copy(trendingActor = newFlow)

        Assertions.assertEquals(newFlow, newState.trendingActor)
        Assertions.assertEquals(originalFlow, originalState.trendingActor)
    }

    @Test
    fun `equals should work correctly for identical states`() {
        val flow = emptyFlow<PagingData<TrendingActorsUiState.TrendingActor>>()
        val state1 = TrendingActorsUiState(trendingActor = flow, isLoading = true)
        val state2 = TrendingActorsUiState(trendingActor = flow, isLoading = true)
        Assertions.assertEquals(state1, state2)
    }

    @Test
    fun `equals should work correctly for different states`() {
        val state1 = TrendingActorsUiState(isLoading = true)
        val state2 = TrendingActorsUiState(isLoading = false)
        Assertions.assertNotEquals(state1, state2)
    }

    @Test
    fun `hashCode should be consistent for identical states`() {
        val flow = emptyFlow<PagingData<TrendingActorsUiState.TrendingActor>>()
        val state1 = TrendingActorsUiState(trendingActor = flow, isLoading = true)
        val state2 = TrendingActorsUiState(trendingActor = flow, isLoading = true)
        Assertions.assertEquals(state1.hashCode(), state2.hashCode())
    }

    @Test
    fun `TrendingActor constructor should set values correctly`() {
        val trendingActor = TrendingActorsUiState.TrendingActor(
            id = ACTOR_ID,
            profilePictureURL = PROFILE_PICTURE_URL,
            name = ACTOR_NAME
        )
        Assertions.assertEquals(ACTOR_ID, trendingActor.id)
        Assertions.assertEquals(PROFILE_PICTURE_URL, trendingActor.profilePictureURL)
        Assertions.assertEquals(ACTOR_NAME, trendingActor.name)
    }

    @Test
    fun `TrendingActor copy function should work correctly`() {
        val originalActor = TrendingActorsUiState.TrendingActor(
            id = ACTOR_ID,
            profilePictureURL = PROFILE_PICTURE_URL,
            name = "Original Name"
        )
        val copiedActor = originalActor.copy(name = "New Name")
        Assertions.assertEquals("New Name", copiedActor.name)
        Assertions.assertEquals("Original Name", originalActor.name)
        Assertions.assertEquals(ACTOR_ID, copiedActor.id)
        Assertions.assertEquals(PROFILE_PICTURE_URL, copiedActor.profilePictureURL)
    }

    @Test
    fun `TrendingActor equals should work correctly`() {
        val actor1 = TrendingActorsUiState.TrendingActor(
            id = ACTOR_ID,
            profilePictureURL = PROFILE_PICTURE_URL,
            name = ACTOR_NAME
        )
        val actor2 = TrendingActorsUiState.TrendingActor(
            id = ACTOR_ID,
            profilePictureURL = PROFILE_PICTURE_URL,
            name = ACTOR_NAME
        )
        Assertions.assertEquals(actor1, actor2)
    }

    @Test
    fun `TrendingActor with different ids should not be equal`() {
        val actor1 = TrendingActorsUiState.TrendingActor(id = 123L, profilePictureURL = "", name = "")
        val actor2 = TrendingActorsUiState.TrendingActor(id = 456L, profilePictureURL = "", name = "")
        Assertions.assertNotEquals(actor1, actor2)
    }

    @Test
    fun `TrendingActor hashCode should be consistent`() {
        val actor1 = TrendingActorsUiState.TrendingActor(
            id = ACTOR_ID,
            profilePictureURL = PROFILE_PICTURE_URL,
            name = ACTOR_NAME
        )
        val actor2 = TrendingActorsUiState.TrendingActor(
            id = ACTOR_ID,
            profilePictureURL = PROFILE_PICTURE_URL,
            name = ACTOR_NAME
        )
        Assertions.assertEquals(actor1.hashCode(), actor2.hashCode())
    }

    @Test
    fun `TrendingActor component functions should work correctly`() {
        val actor = TrendingActorsUiState.TrendingActor(
            id = ACTOR_ID,
            profilePictureURL = PROFILE_PICTURE_URL,
            name = ACTOR_NAME
        )
        val (id, profileURL, name) = actor
        Assertions.assertEquals(ACTOR_ID, id)
        Assertions.assertEquals(PROFILE_PICTURE_URL, profileURL)
        Assertions.assertEquals(ACTOR_NAME, name)
    }

    @Test
    fun `OnBackClick should be singleton object`() {
        val effect1 = TrendingActorsUiEffect.OnBackClick
        val effect2 = TrendingActorsUiEffect.OnBackClick
        Assertions.assertEquals(effect1, effect2)
        Assertions.assertTrue(effect1 === effect2)
    }

    @Test
    fun `NavigateToActorsDetails with same actorId should be equal`() {
        val effect1 = TrendingActorsUiEffect.NavigateToActorsDetails(ACTOR_ID)
        val effect2 = TrendingActorsUiEffect.NavigateToActorsDetails(ACTOR_ID)
        Assertions.assertEquals(effect1, effect2)
    }

    @Test
    fun `NavigateToActorsDetails with different actorIds should not be equal`() {
        val effect1 = TrendingActorsUiEffect.NavigateToActorsDetails(123L)
        val effect2 = TrendingActorsUiEffect.NavigateToActorsDetails(456L)
        Assertions.assertNotEquals(effect1, effect2)
    }

    @Test
    fun `NavigateToActorsDetails should set actorId correctly`() {
        val effect = TrendingActorsUiEffect.NavigateToActorsDetails(ACTOR_ID)
        Assertions.assertEquals(ACTOR_ID, effect.actorId)
    }

    @Test
    fun `NavigateToActorsDetails copy function should work correctly`() {
        val originalEffect = TrendingActorsUiEffect.NavigateToActorsDetails(123L)
        val copiedEffect = originalEffect.copy(actorId = 456L)
        Assertions.assertEquals(456L, copiedEffect.actorId)
        Assertions.assertEquals(123L, originalEffect.actorId)
    }

    @Test
    fun `all effects should be usable in collections`() {
        val effects = listOf<TrendingActorsUiEffect>(
            TrendingActorsUiEffect.OnBackClick,
            TrendingActorsUiEffect.NavigateToActorsDetails(ACTOR_ID),
            TrendingActorsUiEffect.NavigateToActorsDetails(456L)
        )
        Assertions.assertEquals(3, effects.size)
        Assertions.assertTrue(effects.all { it is TrendingActorsUiEffect })
    }

    @Test
    fun `component functions should work for data classes`() {
        val navigateToActorsDetails = TrendingActorsUiEffect.NavigateToActorsDetails(123L)
        val (actorIdComponent) = navigateToActorsDetails
        Assertions.assertEquals(123L, actorIdComponent)
    }

    @Test
    fun `toString should work correctly for all effects`() {
        val onBackClick = TrendingActorsUiEffect.OnBackClick
        val navigateToActorsDetails = TrendingActorsUiEffect.NavigateToActorsDetails(123L)
        Assertions.assertEquals("OnBackClick", onBackClick.toString())
        Assertions.assertTrue(navigateToActorsDetails.toString().contains("NavigateToActorsDetails"))
        Assertions.assertTrue(navigateToActorsDetails.toString().contains("123"))
    }

    @Test
    fun `effects should work correctly in when expressions`() {
        val effects = listOf<TrendingActorsUiEffect>(
            TrendingActorsUiEffect.OnBackClick,
            TrendingActorsUiEffect.NavigateToActorsDetails(123L)
        )
        val results = effects.map { effect ->
            when (effect) {
                is TrendingActorsUiEffect.OnBackClick -> "back"
                is TrendingActorsUiEffect.NavigateToActorsDetails -> "navigate_${effect.actorId}"
            }
        }
        Assertions.assertEquals(listOf("back", "navigate_123"), results)
    }

    @Test
    fun `toTrendingActorsUi should map Actor to TrendingActor correctly`() {
        val actor = createMockActor()
        val trendingActor = actor.toTrendingActorsUi()
        Assertions.assertEquals(actor.id, trendingActor.id)
        Assertions.assertEquals(actor.profilePictureURL, trendingActor.profilePictureURL)
        Assertions.assertEquals(actor.name, trendingActor.name)
    }

    @Test
    fun `multiple TrendingActors should be comparable in collections`() {
        val actors = listOf(
            TrendingActorsUiState.TrendingActor(1L, "url1", "Actor 1"),
            TrendingActorsUiState.TrendingActor(2L, "url2", "Actor 2"),
            TrendingActorsUiState.TrendingActor(3L, "url3", "Actor 3")
        )
        val sortedById = actors.sortedBy { it.id }
        val sortedByName = actors.sortedBy { it.name }
        Assertions.assertEquals(3, sortedById.size)
        Assertions.assertEquals(1L, sortedById[0].id)
        Assertions.assertEquals(2L, sortedById[1].id)
        Assertions.assertEquals(3L, sortedById[2].id)
        Assertions.assertEquals("Actor 1", sortedByName[0].name)
        Assertions.assertEquals("Actor 2", sortedByName[1].name)
        Assertions.assertEquals("Actor 3", sortedByName[2].name)
    }

    @Test
    fun `onFinally should update isLoading to false`() = runTest {

        testDispatcher.scheduler.advanceUntilIdle()
        val state = trendingActorViewModel.uiState.value
        Assertions.assertFalse(state.isLoading)
    }

    private fun createTrendingActor() = TrendingActorsUiState.TrendingActor(
        id = ACTOR_ID,
        profilePictureURL = PROFILE_PICTURE_URL,
        name = ACTOR_NAME
    )

    private fun createMockActor() = Actor(
        id = ACTOR_ID,
        name = ACTOR_NAME,
        profilePictureURL = PROFILE_PICTURE_URL,
        birthDate = LocalDate.parse("1980-01-01"),
        placeOfBirth = "New York, USA",
        deathDate = null,
        biography = "Famous actor",
        headerPictures = listOf("/header.jpg"),
        department = "Acting"
    )

    companion object {
        private fun createMockActors() = listOf(
            Actor(
                id = 1L,
                name = "Robert Downey Jr.",
                profilePictureURL = "/robert_downey_jr.jpg",
                birthDate = LocalDate.parse("1965-04-04"),
                placeOfBirth = "New York City, New York, USA",
                deathDate = null,
                biography = "American actor and producer",
                headerPictures = listOf("/header1.jpg"),
                department = "Acting"
            ),
            Actor(
                id = 2L,
                name = "Scarlett Johansson",
                profilePictureURL = "/scarlett_johansson.jpg",
                birthDate = LocalDate.parse("1984-11-22"),
                placeOfBirth = "New York City, New York, USA",
                deathDate = null,
                biography = "American actress and singer",
                headerPictures = listOf("/header2.jpg"),
                department = "Acting"
            ),
            Actor(
                id = 3L,
                name = "Chris Evans",
                profilePictureURL = "/chris_evans.jpg",
                birthDate = LocalDate.parse("1981-06-13"),
                placeOfBirth = "Boston, Massachusetts, USA",
                deathDate = null,
                biography = "American actor and filmmaker",
                headerPictures = listOf("/header3.jpg"),
                department = "Acting"
            )
        )

        private const val ACTOR_ID = 123L
        private const val ACTOR_NAME = "Robert Downey Jr."
        private const val PROFILE_PICTURE_URL = "https://example.com/robert_downey_jr.jpg"
    }
}