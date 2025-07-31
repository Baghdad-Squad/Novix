package com.baghdad.viewmodel.trendingActors

import androidx.paging.PagingData
import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.usecase.actor.GetTrendingActorsUseCase
import com.baghdad.entity.person.Actor
import com.baghdad.viewmodel.base.BaseUiState
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
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
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TrendingActorsViewModelTest {

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
    fun `should emit OnBackClick when onBackClick is called`() = runTest {
        var result: TrendingActorsUiEffect? = null
        val job = launch { trendingActorViewModel.uiEffect.collect { result = it } }

        trendingActorViewModel.onBackClick()
        testDispatcher.scheduler.advanceUntilIdle()

        assertThat(result).isEqualTo(TrendingActorsUiEffect.OnBackClick)
        job.cancel()
    }

    @Test
    fun `should emit NavigateToActorsDetails when onTrendingActorClick is called`() = runTest {
        val actorId = 123L
        var result: TrendingActorsUiEffect? = null
        val job = launch { trendingActorViewModel.uiEffect.collect { result = it } }

        trendingActorViewModel.onTrendingActorClick(actorId)
        testDispatcher.scheduler.advanceUntilIdle()

        assertThat(result).isEqualTo(TrendingActorsUiEffect.NavigateToActorsDetails(actorId))
        job.cancel()
    }

    @Test
    fun `should emit multiple NavigateToActorsDetails effects for multiple clicks`() = runTest {
        val ids = listOf(123L, 456L, 789L)
        val results = mutableListOf<TrendingActorsUiEffect>()
        val job = launch { trendingActorViewModel.uiEffect.collect { results.add(it) } }

        ids.forEach {
            trendingActorViewModel.onTrendingActorClick(it)
            testDispatcher.scheduler.advanceUntilIdle()
        }

        assertThat(results).hasSize(3)
        assertThat(results[0]).isEqualTo(TrendingActorsUiEffect.NavigateToActorsDetails(123L))
        assertThat(results[1]).isEqualTo(TrendingActorsUiEffect.NavigateToActorsDetails(456L))
        assertThat(results[2]).isEqualTo(TrendingActorsUiEffect.NavigateToActorsDetails(789L))
        job.cancel()
    }

    @Test
    fun `should have default state values`() {
        val state = TrendingActorsUiState()
        assertThat(state.isLoading).isFalse()
    }

    @Test
    fun `should apply isLoading correctly through constructor`() {
        val flow = flowOf(PagingData.from(listOf(createTrendingActor())))
        val state = TrendingActorsUiState(trendingActor = flow, isLoading = true)
        assertThat(state.trendingActor).isEqualTo(flow)
        assertThat(state.isLoading).isTrue()
    }

    @Test
    fun `should expose isLoading from BaseUiState`() {
        val state: BaseUiState = TrendingActorsUiState(isLoading = true)
        assertThat(state.isLoading).isTrue()
    }

    @Test
    fun `should create copy with updated isLoading`() {
        val original = TrendingActorsUiState(isLoading = false)
        val copy = original.copy(isLoading = true)
        assertThat(copy.isLoading).isTrue()
        assertThat(original.isLoading).isFalse()
    }

    @Test
    fun `should create copy with updated trendingActor flow`() {
        val original = TrendingActorsUiState(trendingActor = emptyFlow())
        val newFlow = flowOf(PagingData.from(listOf(createTrendingActor())))
        val copy = original.copy(trendingActor = newFlow)
        assertThat(copy.trendingActor).isEqualTo(newFlow)
    }

    @Test
    fun `should equal when states are identical`() {
        val flow = emptyFlow<PagingData<TrendingActorsUiState.TrendingActor>>()
        val a = TrendingActorsUiState(trendingActor = flow, isLoading = true)
        val b = TrendingActorsUiState(trendingActor = flow, isLoading = true)
        assertThat(a).isEqualTo(b)
    }

    @Test
    fun `should not equal when isLoading differs`() {
        val a = TrendingActorsUiState(isLoading = true)
        val b = TrendingActorsUiState(isLoading = false)
        assertThat(a).isNotEqualTo(b)
    }

    @Test
    fun `should have same hashCode when states are equal`() {
        val flow = emptyFlow<PagingData<TrendingActorsUiState.TrendingActor>>()
        val a = TrendingActorsUiState(trendingActor = flow, isLoading = true)
        val b = TrendingActorsUiState(trendingActor = flow, isLoading = true)
        assertThat(a.hashCode()).isEqualTo(b.hashCode())
    }

    @Test
    fun `should correctly create TrendingActor`() {
        val actor = TrendingActorsUiState.TrendingActor(ACTOR_ID, PROFILE_PICTURE_URL, ACTOR_NAME)
        assertThat(actor.id).isEqualTo(ACTOR_ID)
        assertThat(actor.name).isEqualTo(ACTOR_NAME)
        assertThat(actor.profilePictureURL).isEqualTo(PROFILE_PICTURE_URL)
    }

    @Test
    fun `should copy TrendingActor with new name`() {
        val original = TrendingActorsUiState.TrendingActor(ACTOR_ID, PROFILE_PICTURE_URL, "Old")
        val copy = original.copy(name = "New")
        assertThat(copy.name).isEqualTo("New")
        assertThat(original.name).isEqualTo("Old")
    }

    @Test
    fun `should equal when TrendingActors have same values`() {
        val a = TrendingActorsUiState.TrendingActor(ACTOR_ID, PROFILE_PICTURE_URL, ACTOR_NAME)
        val b = TrendingActorsUiState.TrendingActor(ACTOR_ID, PROFILE_PICTURE_URL, ACTOR_NAME)
        assertThat(a).isEqualTo(b)
    }

    @Test
    fun `should not equal when TrendingActors have different ids`() {
        val a = TrendingActorsUiState.TrendingActor(1, "", "")
        val b = TrendingActorsUiState.TrendingActor(2, "", "")
        assertThat(a).isNotEqualTo(b)
    }

    @Test
    fun `should return consistent hashCode for TrendingActor`() {
        val a = TrendingActorsUiState.TrendingActor(ACTOR_ID, PROFILE_PICTURE_URL, ACTOR_NAME)
        val b = TrendingActorsUiState.TrendingActor(ACTOR_ID, PROFILE_PICTURE_URL, ACTOR_NAME)
        assertThat(a.hashCode()).isEqualTo(b.hashCode())
    }

    @Test
    fun `should deconstruct TrendingActor correctly`() {
        val actor = TrendingActorsUiState.TrendingActor(ACTOR_ID, PROFILE_PICTURE_URL, ACTOR_NAME)
        val (id, url, name) = actor
        assertThat(id).isEqualTo(ACTOR_ID)
        assertThat(url).isEqualTo(PROFILE_PICTURE_URL)
        assertThat(name).isEqualTo(ACTOR_NAME)
    }

    @Test
    fun `should return OnBackClick singleton correctly`() {
        val a = TrendingActorsUiEffect.OnBackClick
        val b = TrendingActorsUiEffect.OnBackClick
        assertThat(a).isSameInstanceAs(b)
    }

    @Test
    fun `should equal NavigateToActorsDetails with same id`() {
        val a = TrendingActorsUiEffect.NavigateToActorsDetails(ACTOR_ID)
        val b = TrendingActorsUiEffect.NavigateToActorsDetails(ACTOR_ID)
        assertThat(a).isEqualTo(b)
    }

    @Test
    fun `should not equal NavigateToActorsDetails with different id`() {
        val a = TrendingActorsUiEffect.NavigateToActorsDetails(1L)
        val b = TrendingActorsUiEffect.NavigateToActorsDetails(2L)
        assertThat(a).isNotEqualTo(b)
    }

    @Test
    fun `should map Actor to TrendingActor using extension`() {
        val actor = createMockActor()
        val ui = actor.toTrendingActorsUi()
        assertThat(ui.id).isEqualTo(actor.id)
        assertThat(ui.name).isEqualTo(actor.name)
        assertThat(ui.profilePictureURL).isEqualTo(actor.profilePictureURL)
    }

    @Test
    fun `should update isLoading to false after finally`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()
        val state = trendingActorViewModel.uiState.value
        assertThat(state.isLoading).isFalse()
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
        placeOfBirth = "New York",
        deathDate = null,
        biography = "Bio",
        headerPictures = listOf("/header.jpg"),
        department = "Acting"
    )

    companion object {
        private const val ACTOR_ID = 123L
        private const val ACTOR_NAME = "Robert Downey Jr."
        private const val PROFILE_PICTURE_URL = "https://example.com/robert_downey_jr.jpg"
        private fun createMockActors() = listOf(
            Actor(
                id = 1L,
                name = "Robert Downey Jr.",
                profilePictureURL = "/robert_downey_jr.jpg",
                birthDate = LocalDate.parse("1965-04-04"),
                placeOfBirth = "NYC",
                deathDate = null,
                biography = "Actor",
                headerPictures = listOf("/header1.jpg"),
                department = "Acting"
            ),
            Actor(
                id = 2L,
                name = "Scarlett Johansson",
                profilePictureURL = "/scarlett_johansson.jpg",
                birthDate = LocalDate.parse("1984-11-22"),
                placeOfBirth = "NYC",
                deathDate = null,
                biography = "Actress",
                headerPictures = listOf("/header2.jpg"),
                department = "Acting"
            )
        )
    }
}