package com.baghdad.domain.usecase.recentlyViewed

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class DeleteAllRecentlyViewedUseCaseTest {
    private val deleteAllRecentlyViewedUseCase = DeleteAllRecentlyViewedUseCase()

    @Test
    fun dummyTest() = runTest {
        deleteAllRecentlyViewedUseCase()
        Assertions.assertTrue(true)
    }
}