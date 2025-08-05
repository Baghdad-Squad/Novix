package com.baghdad.domain.usecase.savedList

import com.baghdad.domain.repository.SavedListRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CreateSavedListUseCaseTest {

    lateinit var savedListUseCase: CreateSavedListUseCase
    lateinit var savedListRepository: SavedListRepository

    @BeforeEach
    fun setUp() {
        savedListRepository = mockk()
        savedListUseCase = CreateSavedListUseCase(savedListRepository)
    }


    @Test
    fun `invoke() should call createSavedList when the title is passed`() = runTest {
        // Given
        val title = "favorite"
        coEvery { savedListRepository.createSavedList(title) } returns Unit
        // When
        savedListUseCase.invoke(title)

        // Then
        coVerify(exactly = 1) { savedListRepository.createSavedList(title) }
    }

    @Test
    fun `invoke() should propagate exception when repository throws exception`() = runTest {
        // Given
        val exception = RuntimeException("Database failure")
        coEvery { savedListRepository.createSavedList(any()) } throws exception

        // When
        val resultException = runCatching { savedListUseCase("Weekend Plans") }.exceptionOrNull()

        // Then
        assertThat(resultException).isNotNull()
        assertThat(resultException).isInstanceOf(RuntimeException::class.java)
    }

    @Test
    fun `invoke should handle when special characters in title`() = runTest {
        // Given
        val title = "@List#2025!"
        coEvery { savedListRepository.createSavedList(title) } returns Unit

        // When
        savedListUseCase.invoke(title)

        // Then
        coVerify(exactly = 1) { savedListRepository.createSavedList(title) }
    }
}