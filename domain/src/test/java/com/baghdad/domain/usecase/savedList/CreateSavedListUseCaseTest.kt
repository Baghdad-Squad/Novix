package com.baghdad.domain.usecase.savedList

import com.baghdad.domain.repository.SavedListRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class CreateSavedListUseCaseTest {

    private val savedListRepository = mockk<SavedListRepository>()
    private val savedListUseCase = CreateSavedListUseCase(savedListRepository)

    @Test
    fun `savedListUseCase should call createSavedList when the title is passed`() = runTest {
        coEvery { savedListRepository.createSavedList(title) } returns Unit

        savedListUseCase.invoke(title)

        coVerify(exactly = 1) { savedListRepository.createSavedList(title) }
    }

    companion object {
        val title = "favorite"
    }
}