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
import org.junit.jupiter.api.assertThrows

class FavoriteGenreRepositoryImplTest {

    private lateinit var localFavoriteGenreDataSource: LocalFavoriteGenreDataSource
    private lateinit var favoriteGenreRepositoryImpl: FavoriteGenreRepositoryImpl

    @BeforeEach
    fun setUp() {
        localFavoriteGenreDataSource = mockk()
        favoriteGenreRepositoryImpl = FavoriteGenreRepositoryImpl(localFavoriteGenreDataSource)
    }

    @Test
    fun `getFavoriteGenres should return map of genre names to counts when data source returns data`() = runTest {

        val mockFavoriteGenreList = listOf(
            createMockFavoriteGenreDto(1L, "Action", 15),
            createMockFavoriteGenreDto(2L, "Comedy", 8),
            createMockFavoriteGenreDto(3L, "Drama", 12)
        )

        val expectedMap = mapOf(
            "Action" to 15,
            "Comedy" to 8,
            "Drama" to 12
        )

        coEvery { localFavoriteGenreDataSource.getFavoriteGenres() } returns mockFavoriteGenreList

        val result = favoriteGenreRepositoryImpl.getFavoriteGenres()

        assertEquals(expectedMap, result)
        coVerify { localFavoriteGenreDataSource.getFavoriteGenres() }
    }

    @Test
    fun `getFavoriteGenres should return empty map when data source returns empty list`() = runTest {

        coEvery { localFavoriteGenreDataSource.getFavoriteGenres() } returns emptyList()

        val result = favoriteGenreRepositoryImpl.getFavoriteGenres()

        assertEquals(emptyMap<String, Int>(), result)
        coVerify { localFavoriteGenreDataSource.getFavoriteGenres() }
    }

    @Test
    fun `getFavoriteGenres should throw exception when data source call fails`() = runTest {

        val exception = RuntimeException("Database error")
        coEvery { localFavoriteGenreDataSource.getFavoriteGenres() } throws exception

        assertThrows<Exception> {
            favoriteGenreRepositoryImpl.getFavoriteGenres()
        }
    }

    @Test
    fun `getFavoriteGenres should return map with single entry when data source returns single item`() = runTest {

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
    fun `getFavoriteGenres should handle genres with zero count`() = runTest {

        val genreListWithZeroCount = listOf(
            createMockFavoriteGenreDto(1L, "Thriller", 0),
            createMockFavoriteGenreDto(2L, "Romance", 5)
        )
        val expectedMap = mapOf(
            "Thriller" to 0,
            "Romance" to 5
        )

        coEvery { localFavoriteGenreDataSource.getFavoriteGenres() } returns genreListWithZeroCount

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

    @Test
    fun `getFavoriteGenres should handle duplicate genre names by using last occurrence`() = runTest {

        val genreListWithDuplicates = listOf(
            createMockFavoriteGenreDto(1L, "Action", 10),
            createMockFavoriteGenreDto(2L, "Action", 15), // Duplicate name
            createMockFavoriteGenreDto(3L, "Comedy", 5)
        )
        val expectedMap = mapOf(
            "Action" to 15,
            "Comedy" to 5
        )

        coEvery { localFavoriteGenreDataSource.getFavoriteGenres() } returns genreListWithDuplicates

        val result = favoriteGenreRepositoryImpl.getFavoriteGenres()

        assertEquals(expectedMap, result)
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