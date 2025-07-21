package com.baghdad.repository

import com.baghdad.entity.media.Genre
import com.baghdad.repository.datasource.remote.RemoteGenreDataSource
import com.baghdad.repository.datasource.remote.RemoteTvShowDataSource
import com.baghdad.repository.model.GenreDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Locale

class TvShowRepositoryImplTest {

    private lateinit var remoteGenreDataSource: RemoteGenreDataSource
    private lateinit var tvShowRemoteDataSource: RemoteTvShowDataSource
    private lateinit var tvShowRepositoryImpl: TvShowRepositoryImpl

    @BeforeEach
    fun setUp() {
        remoteGenreDataSource = mockk()
        tvShowRemoteDataSource = mockk() // Fixed: Initialize this mock
        tvShowRepositoryImpl = TvShowRepositoryImpl(
            remoteGenreDataSource = remoteGenreDataSource,
            tvShowRemoteDataSource = tvShowRemoteDataSource
        )
    }

    @Test
    fun `getGenres should return mapped genres when remote data source succeeds`() = runTest {

        val mockGenreDtos = listOf(
            createMockGenreDto(1L, "Action", GenreDto.GenreType.TV_SHOW),
            createMockGenreDto(2L, "Comedy", GenreDto.GenreType.TV_SHOW),
            createMockGenreDto(3L, "Drama", GenreDto.GenreType.TV_SHOW)
        )
        val expectedGenres = listOf(
            createMockGenre(1L, "Action"),
            createMockGenre(2L, "Comedy"),
            createMockGenre(3L, "Drama")
        )
        val currentLanguage = Locale.getDefault().language

        coEvery { remoteGenreDataSource.getTvShowGenre(language = currentLanguage) } returns mockGenreDtos

        val result = tvShowRepositoryImpl.getGenres()

        assertEquals(expectedGenres.size, result.size)
        assertEquals(expectedGenres[0].id, result[0].id)
        assertEquals(expectedGenres[0].name, result[0].name)
        assertEquals(expectedGenres[1].id, result[1].id)
        assertEquals(expectedGenres[1].name, result[1].name)
        assertEquals(expectedGenres[2].id, result[2].id)
        assertEquals(expectedGenres[2].name, result[2].name)
        coVerify { remoteGenreDataSource.getTvShowGenre(language = currentLanguage) }
    }

    @Test
    fun `getGenres should return empty list when remote data source returns empty list`() = runTest {

        val currentLanguage = Locale.getDefault().language
        coEvery { remoteGenreDataSource.getTvShowGenre(language = currentLanguage) } returns emptyList()

        val result = tvShowRepositoryImpl.getGenres()

        assertEquals(emptyList<Genre>(), result)
        coVerify { remoteGenreDataSource.getTvShowGenre(language = currentLanguage) }
    }

    @Test
    fun `getGenres should use default locale language when calling remote data source`() = runTest {

        val mockGenreDtos = listOf(
            createMockGenreDto(1L, "Action", GenreDto.GenreType.TV_SHOW)
        )
        val currentLanguage = Locale.getDefault().language

        coEvery { remoteGenreDataSource.getTvShowGenre(language = currentLanguage) } returns mockGenreDtos

        tvShowRepositoryImpl.getGenres()

        coVerify { remoteGenreDataSource.getTvShowGenre(language = currentLanguage) }
    }

    @Test
    fun `getGenres should handle single genre correctly`() = runTest {

        val mockGenreDto = createMockGenreDto(1L, "Thriller", GenreDto.GenreType.TV_SHOW)
        val expectedGenre = createMockGenre(1L, "Thriller")
        val currentLanguage = Locale.getDefault().language

        coEvery { remoteGenreDataSource.getTvShowGenre(language = currentLanguage) } returns listOf(mockGenreDto)

        val result = tvShowRepositoryImpl.getGenres()

        assertEquals(1, result.size)
        assertEquals(expectedGenre.id, result[0].id)
        assertEquals(expectedGenre.name, result[0].name)
        coVerify { remoteGenreDataSource.getTvShowGenre(language = currentLanguage) }
    }

    @Test
    fun `getGenres should handle multiple genres with different types correctly`() = runTest {

        val mockGenreDtos = listOf(
            createMockGenreDto(10L, "Horror", GenreDto.GenreType.TV_SHOW),
            createMockGenreDto(20L, "Romance", GenreDto.GenreType.TV_SHOW),
            createMockGenreDto(30L, "Sci-Fi", GenreDto.GenreType.TV_SHOW)
        )
        val expectedGenres = listOf(
            createMockGenre(10L, "Horror"),
            createMockGenre(20L, "Romance"),
            createMockGenre(30L, "Sci-Fi")
        )
        val currentLanguage = Locale.getDefault().language

        coEvery { remoteGenreDataSource.getTvShowGenre(language = currentLanguage) } returns mockGenreDtos

        val result = tvShowRepositoryImpl.getGenres()

        assertEquals(expectedGenres.size, result.size)
        for (i in expectedGenres.indices) {
            assertEquals(expectedGenres[i].id, result[i].id)
            assertEquals(expectedGenres[i].name, result[i].name)
        }
        coVerify { remoteGenreDataSource.getTvShowGenre(language = currentLanguage) }
    }
    companion object {

        private fun createMockGenreDto(
            id: Long,
            name: String,
            genreType: GenreDto.GenreType
        ) = GenreDto(
            id = id,
            name = name,
            type = genreType
        )
        private fun createMockGenre(
            id: Long,
            name: String
        ) = Genre(
            id = id,
            name = name
        )
    }
}