package com.baghdad.domain.usecase.actor

import com.baghdad.domain.repository.ActorRepository
import com.baghdad.domain.testHelper.getMinimalTvShow
import com.baghdad.domain.testHelper.getSampleTvShow
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.TvShow
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetActorTvShowUseCaseTest {

    private lateinit var actorRepository: ActorRepository
    private lateinit var getActorTvShowUseCase: GetActorTvShowUseCase

    @BeforeEach
    fun setUp() {
        actorRepository = mockk(relaxed = true)
        getActorTvShowUseCase = GetActorTvShowUseCase(actorRepository)
    }

    @Test
    fun `getActorTvShowUseCase() should return tv shows when actor has appearances`() = runTest {
        coEvery { actorRepository.getActorTvShows(ACTOR_ID) } returns listOf(SAMPLE_TV_SHOWS)

        val result = getActorTvShowUseCase(ACTOR_ID)

        assertThat(result).isEqualTo(listOf(SAMPLE_TV_SHOWS))
    }

    @Test
    fun `getActorTvShowUseCase() should return empty list when actor has no tv show appearances`() =
        runTest {
            coEvery { actorRepository.getActorTvShows(ACTOR_ID_NO_SHOWS) } returns emptyList()

            val result = getActorTvShowUseCase(ACTOR_ID_NO_SHOWS)

            assertThat(result).isEmpty()
        }

    @Test
    fun `getActorTvShowUseCase() should return tv shows with minimal information when data is sparse`() =
        runTest {
            coEvery { actorRepository.getActorTvShows(ACTOR_ID_MINIMAL) } returns listOf(
                MINIMAL_TV_SHOW
            )

            val result = getActorTvShowUseCase(ACTOR_ID_MINIMAL)

            assertThat(result[0].title).isEqualTo("Minimal Show")
            assertThat(result[0].posterImageURL).isEmpty()
            assertThat(result[0].genres).isEmpty()
        }

    @Test
    fun `getActorTvShowUseCase() should return tv shows with multiple genres`() = runTest {
        coEvery { actorRepository.getActorTvShows(ACTOR_ID_MULTIPLE_GENRES) } returns MULTI_GENRES_SHOW

        val result = getActorTvShowUseCase(ACTOR_ID_MULTIPLE_GENRES)

        assertThat(result[0].genres).hasSize(3)
    }

    @Test
    fun `getActorTvShowUseCase() should return tv shows with header images`() = runTest {
        coEvery { actorRepository.getActorTvShows(ACTOR_ID_WITH_HEADERS) } returns listOf(
            SAMPLE_TV_SHOWS
        )

        val result = getActorTvShowUseCase(ACTOR_ID_WITH_HEADERS)

        assertThat(result[0].headerImagesURLs).hasSize(2)
    }

    @Test
    fun `getActorTvShowUseCase() should return tv shows with different season counts`() = runTest {
        coEvery { actorRepository.getActorTvShows(ACTOR_ID_WITH_SEASONS) } returns TV_SHOWS_WITH_DIFFERENT_SEASON_COUNT

        val result = getActorTvShowUseCase(ACTOR_ID_WITH_SEASONS)

        assertThat(result[0].numberOfSeasons).isEqualTo(5)
        assertThat(result[1].numberOfSeasons).isEqualTo(6)
    }


    @Test
    fun `getActorTvShowUseCase() should return tv shows with special characters in titles`() =
        runTest {
            val specialTitleShow = listOf(SAMPLE_TV_SHOWS.copy(title = TV_SHOW_TITLE))
            coEvery { actorRepository.getActorTvShows(ACTOR_ID_SPECIAL_TITLE) } returns specialTitleShow

            val result = getActorTvShowUseCase(ACTOR_ID_SPECIAL_TITLE)

            assertThat(result[0].title).isEqualTo(TV_SHOW_TITLE)
        }

    companion object {
        private val SAMPLE_TV_SHOWS = getSampleTvShow()
        private val MINIMAL_TV_SHOW = getMinimalTvShow()
        private val MULTI_GENRES_SHOW = listOf(
            SAMPLE_TV_SHOWS.copy(
                genres = listOf(
                    Genre(id = 1, name = "Drama"),
                    Genre(id = 2, name = "Thriller"),
                    Genre(id = 3, name = "Mystery")
                )
            )
        )
        private val TV_SHOWS_WITH_DIFFERENT_SEASON_COUNT = listOf(
            getSampleTvShow(numberOfSeasons = 5),
            getSampleTvShow(numberOfSeasons = 6)
        )

        private const val ACTOR_ID = 1L
        private const val ACTOR_ID_NO_SHOWS = 2L
        private const val ACTOR_ID_MINIMAL = 3L
        private const val ACTOR_ID_MULTIPLE_GENRES = 4L
        private const val ACTOR_ID_WITH_HEADERS = 5L
        private const val ACTOR_ID_WITH_SEASONS = 6L
        private const val ACTOR_ID_SPECIAL_TITLE = 7L

        private const val TV_SHOW_TITLE = "Stranger Things  戦争の藝術"
    }
}
