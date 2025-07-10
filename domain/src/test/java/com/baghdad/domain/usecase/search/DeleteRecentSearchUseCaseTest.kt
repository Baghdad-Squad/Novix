package com.baghdad.domain.usecase.search

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class DeleteRecentSearchUseCaseTest {
    private val deleteRecentSearchUseCase = DeleteRecentSearchUseCase()

    @Test
    fun dummyTest() = runTest {
        deleteRecentSearchUseCase(0L).let {
            assert(true)
        }
    }
}