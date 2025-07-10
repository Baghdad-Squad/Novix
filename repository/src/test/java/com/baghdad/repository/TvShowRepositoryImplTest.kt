package com.baghdad.repository

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

class TvShowRepositoryImplTest {
    val tvShowRepository = TvShowRepositoryImpl()

    @Test
    fun getGenres() = runTest {
        assertDoesNotThrow {
            tvShowRepository.getGenres()
        }

    }

}