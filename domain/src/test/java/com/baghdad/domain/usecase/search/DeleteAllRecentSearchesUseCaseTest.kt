package com.baghdad.domain.usecase.search

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class DeleteAllRecentSearchesUseCaseTest {
    private val deleteAllRecentSearchesUseCase = DeleteAllRecentSearchesUseCase()

    @Test
    fun dummyTest() = runTest {
        deleteAllRecentSearchesUseCase().let {
            Assertions.assertTrue(true)
        }
    }
}