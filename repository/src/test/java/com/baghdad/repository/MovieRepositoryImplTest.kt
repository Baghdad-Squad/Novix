package com.baghdad.repository

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow


class MovieRepositoryImplTest {
    val movieRepository = MovieRepositoryImpl()
    @Test
    fun getGenres() = runTest {
        assertDoesNotThrow {
            movieRepository.getGenres()
        }
    }

}