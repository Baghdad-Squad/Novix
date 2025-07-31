package com.baghdad.repository

import com.baghdad.repository.datasource.local.LocalFavoriteGenreDataSource
import com.baghdad.repository.model.FavoriteGenreDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FavoriteGenreRepositoryImplTest {
    private lateinit var localFavoriteGenreDataSource: LocalFavoriteGenreDataSource
    private lateinit var favoriteGenreRepositoryImpl: FavoriteGenreRepositoryImpl

    @BeforeEach
    fun setUp() {
        localFavoriteGenreDataSource = mockk()
        favoriteGenreRepositoryImpl = FavoriteGenreRepositoryImpl(localFavoriteGenreDataSource)
    }

    @Test
    fun `getFavoriteGenres should return empty map when data source returns empty list`() =
        runTest {

            coEvery { localFavoriteGenreDataSource.getFavoriteGenres() } returns emptyList()

            val result = favoriteGenreRepositoryImpl.getFavoriteGenres()

            assertEquals(emptyMap<String, Int>(), result)
            coVerify { localFavoriteGenreDataSource.getFavoriteGenres() }
        }

    @Test
    fun `getFavoriteGenres should return map with single entry when data source returns single item`() =
        runTest {

            val singleGenreList = listOf(
                createMockFavoriteGenreDto(5L, "Horror", 3)
            )
            val expectedMap = mapOf("Horror" to 3)

            coEvery { localFavoriteGenreDataSource.getFavoriteGenres() } returns singleGenreList

            val result = favoriteGenreRepositoryImpl.getFavoriteGenres()

            assertEquals(expectedMap, result)
            coVerify { localFavoriteGenreDataSource.getFavoriteGenres() }
        }

    @Test
    fun `getFavoriteGenres should handle large dataset correctly`() = runTest {

        val largeGenreList = (1..100).map { index ->
            createMockFavoriteGenreDto(index.toLong(), "Genre$index", index * 2)
        }
        val expectedMap = (1..100).associate { "Genre$it" to (it * 2) }

        coEvery { localFavoriteGenreDataSource.getFavoriteGenres() } returns largeGenreList

        val result = favoriteGenreRepositoryImpl.getFavoriteGenres()

        assertEquals(expectedMap, result)
        assertEquals(100, result.size)
        coVerify { localFavoriteGenreDataSource.getFavoriteGenres() }
    }

    companion object {

        private fun createMockFavoriteGenreDto(
            genreId: Long,
            name: String,
            count: Int
        ) = FavoriteGenreDto(
            genreId = genreId,
            name = name,
            count = count,
            timeStamp = System.currentTimeMillis()
        )
    }
}