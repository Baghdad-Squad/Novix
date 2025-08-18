package com.baghdad.domain.usecase.search

import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.domain.repository.SearchRepository
import com.baghdad.entity.person.Actor
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SearchActorsUseCaseTest {

    @BeforeEach
    fun setUp() {
        searchRepository = mockk(relaxed = true)
        searchActorsUseCase = SearchActorsUseCase(searchRepository)
    }

    @Test
    fun `invoke() should return actors matching query with pagination keys`() = runTest {
        // Given
        val query = "Tom"
        val page = 1
        coEvery { searchRepository.searchActorsByName(query, page) } returns sampleActors

        // When
        val result = searchActorsUseCase(query, page)

        // Then
        assertThat(result.data).hasSize(2)
        assertThat(result.data[0].name).contains("Tom")
        assertThat(result.nextPage).isEqualTo(2)
        assertThat(result.prevPage).isNull()
    }

    @Test
    fun `searchActorsUseCase() should return empty result with null keys when no matches found`() = runTest {
        // Given
        val query = "Unknown Actor"
        val page = 1
        coEvery { searchRepository.searchActorsByName(query, page) } returns emptyResult

        // When
        val result = searchActorsUseCase(query, page)

        // Then
        assertThat(result.data).isEmpty()
        assertThat(result.nextPage).isNull()
        assertThat(result.prevPage).isNull()
    }

    @Test
    fun `searchActorsUseCase() should return case insensitive matches with pagination`() = runTest {
        // Given
        val query = "meryl"
        val page = 1
        val singleActorResult = sampleActors.copy(
            data = listOf(sampleActors.data[1]),
            nextPage = null,
            prevPage = 1
        )
        coEvery { searchRepository.searchActorsByName(query, page) } returns singleActorResult

        // When
        val result = searchActorsUseCase(query, page)

        // Then
        assertThat(result.data).hasSize(1)
        assertThat(result.data[0].name).isEqualTo("Meryl Streep")
        assertThat(result.nextPage).isNull()
        assertThat(result.prevPage).isEqualTo(1)
    }

    @Test
    fun `searchActorsUseCase() should return different pages with correct pagination keys`() = runTest {
        // Given
        val query = "actor"
        val page1Result = sampleActors.copy(
            prevPage = null,
            nextPage = 2
        )
        val page2Result = sampleActors.copy(
            data = listOf(
                Actor(
                    id = 3L,
                    name = "Denzel Washington",
                    profilePictureURL = "https://example.com/denzel.jpg",
                    birthDate = LocalDate(1990, 12, 28),
                    placeOfBirth = "Mount Vernon, New York",
                    deathDate = null,
                    biography = "Two-time Academy Award-winning actor",
                    headerPictures = listOf(
                        "https://example.com/denzel_header.jpg",
                        "https://example.com/denzel_header2.jpg"
                    ),
                    department = "Acting",
                )
            ),
            prevPage = 1,
            nextPage = null
        )
        coEvery { searchRepository.searchActorsByName(query, 1) } returns page1Result
        coEvery { searchRepository.searchActorsByName(query, 2) } returns page2Result

        // When
        val result1 = searchActorsUseCase(query, 1)
        val result2 = searchActorsUseCase(query, 2)

        // Then
        assertThat(result1.data).hasSize(2)
        assertThat(result2.data).hasSize(1)
        assertThat(result1.nextPage).isEqualTo(2)
        assertThat(result2.prevPage).isEqualTo(1)
    }

    @Test
    fun `searchActorsUseCase() should handle special characters in query`() = runTest {
        // Given
        val query = "Édgar"
        val page = 1
        val specialActorResult = sampleActors.copy(
            data = listOf(
                Actor(
                    id = 4L,
                    name = "Édgar Ramírez",
                    profilePictureURL = "https://example.com/denzel.jpg",
                    birthDate = LocalDate(1990, 12, 28),
                    placeOfBirth = "Mount Vernon, New York",
                    deathDate = null,
                    biography = "Two-time Academy Award-winning actor",
                    headerPictures = listOf(
                        "https://example.com/denzel_header.jpg",
                        "https://example.com/denzel_header2.jpg"
                    ),
                    department = "Acting",
                )
            ),
            nextPage = null,
            prevPage = null
        )
        coEvery { searchRepository.searchActorsByName(query, page) } returns specialActorResult

        // When
        val result = searchActorsUseCase(query, page)

        // Then
        assertThat(result.data[0].name).isEqualTo("Édgar Ramírez")
    }

    @Test
    fun `invoke() should make exactly one repository call`() = runTest {
        // Given
        val query = "search"
        val page = 1
        coEvery { searchRepository.searchActorsByName(query, page) } returns sampleActors

        // When
        searchActorsUseCase(query, page)

        // Then
        coVerify(exactly = 1) { searchRepository.searchActorsByName(query, page) }
    }

    @Test
    fun `invoke() should return correct pagination keys for first page`() = runTest {
        // Given
        val query = "first"
        val page = 1
        val firstPageResult = sampleActors.copy(
            prevPage = null,
            nextPage = 2
        )
        coEvery { searchRepository.searchActorsByName(query, page) } returns firstPageResult

        // When
        val result = searchActorsUseCase(query, page)

        // Then
        assertThat(result.prevPage).isNull()
        assertThat(result.nextPage).isEqualTo(2)
    }

    @Test
    fun `searchActorsUseCase() should return correct pagination keys for middle page`() = runTest {
        // Given
        val query = "middle"
        val page = 3
        val middlePageResult = sampleActors.copy(
            prevPage = 2,
            nextPage = 4
        )
        coEvery { searchRepository.searchActorsByName(query, page) } returns middlePageResult

        // When
        val result = searchActorsUseCase(query, page)

        // Then
        assertThat(result.prevPage).isEqualTo(2)
        assertThat(result.nextPage).isEqualTo(4)
    }

    @Test
    fun `searchActorsUseCase() should return correct pagination keys for last page`() = runTest {
        // Given
        val query = "last"
        val page = 5
        val lastPageResult = sampleActors.copy(
            prevPage = 4,
            nextPage = null
        )
        coEvery { searchRepository.searchActorsByName(query, page) } returns lastPageResult

        // When
        val result = searchActorsUseCase(query, page)

        // Then
        assertThat(result.prevPage).isEqualTo(4)
        assertThat(result.nextPage).isNull()
    }

    companion object {
        private lateinit var searchRepository: SearchRepository
        private lateinit var searchActorsUseCase: SearchActorsUseCase

        private val sampleActors = PagedResult(
            prevPage = null,
            nextPage = 2,
            data = listOf(
                Actor(
                    id = 1L,
                    name = "Tom Hanks",
                    profilePictureURL = "https://example.com/tom.jpg",
                    birthDate = LocalDate(1956, 7, 9),
                    placeOfBirth = "Concord, California",
                    deathDate = null,
                    biography = "Academy Award-winning actor",
                    headerPictures = listOf("https://example.com/tom_header.jpg"),
                    department = "Acting"
                ),
                Actor(
                    id = 2L,
                    name = "Meryl Streep",
                    profilePictureURL = "https://example.com/meryl.jpg",
                    birthDate = LocalDate(1949, 6, 22),
                    placeOfBirth = "Summit, New Jersey",
                    deathDate = null,
                    biography = "Most Academy Award-nominated actor",
                    headerPictures = listOf("https://example.com/meryl_header.jpg"),
                    department = "Acting"
                )
            )
        )

        private val emptyResult = PagedResult<Actor>(
            prevPage = null,
            nextPage = null,
            data = emptyList()
        )
    }
}