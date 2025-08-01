package com.baghdad.local_datasource

import com.baghdad.local_datasource.roomDB.dao.GenreDao
import com.baghdad.local_datasource.roomDB.entity.Genre
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.GenreDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class LocalGenreDataSourceImplTest {

    lateinit var genreDao: GenreDao
    lateinit var logger: Logger

    lateinit var localGenreDataSourceImpl: LocalGenreDataSourceImpl

    @BeforeEach
    fun setUp() {
        genreDao = mockk()
        logger = mockk()

        localGenreDataSourceImpl = LocalGenreDataSourceImpl(genreDao, logger)
    }

    @Test
    fun `should get movie genres when call getMovieGenre`() = runTest {
        //Given
        val movieGenres = MOVIE_GENRES.map { it.toEntity() }
        coEvery { genreDao.getAllGenres() } returns movieGenres
        coEvery { logger.logException(any()) } returns Unit

        //When
        val result = localGenreDataSourceImpl.getMovieGenre("en")

        //Then
        assertThat(result).isEqualTo(MOVIE_GENRES)
    }

    @Test
    fun `get tv show genres when call getTvShowGenre`() = runTest {
        // Given
        val tvShowGenres = TV_SHOW_GENRES.map { it.toEntity() }
        coEvery { genreDao.getAllGenres() } returns tvShowGenres
        coEvery { logger.logException(any()) } returns Unit

        // When
        val result = localGenreDataSourceImpl.getTvShowGenre("en")

        // Then
        assertThat(result).isEqualTo(TV_SHOW_GENRES)
    }

    @Test
    fun `should add a new Genre when call addGenre`() = runTest {
        // Given
        coEvery { genreDao.addGenre(GENRE.toEntity()) } returns Unit
        coEvery { logger.logException(any()) } returns Unit

        // When
        val result = localGenreDataSourceImpl.addGenre(GENRE)

        // Then
        assertThat(result).isEqualTo(Unit)
    }

    @Test
    fun `should get all Genres from database when call getAllGenres`() = runTest {
        // Given
        val allGenres = MOVIE_GENRES.map { it.toEntity() } + TV_SHOW_GENRES.map { it.toEntity() }
        coEvery { genreDao.getAllGenres() } returns allGenres
        coEvery { logger.logException(any()) } returns Unit

        // When
        val result = localGenreDataSourceImpl.getAllGenres()

        // Then
        assertThat(result).isEqualTo(MOVIE_GENRES + TV_SHOW_GENRES)
    }

    @Test
    fun `should get Genre by id when search send a valid id`() = runTest {
        // Given
        val genre = GENRE.toEntity()
        coEvery { genreDao.getGenreById(1) } returns genre
        coEvery { logger.logException(any()) } returns Unit

        // When
        val result = localGenreDataSourceImpl.getGenreById(1)

        // Then
        assertThat(result).isEqualTo(GENRE)
    }

    @Test
    fun `should delete Genre by id when send a valid id and call`() = runTest {
        // Given
        coEvery { genreDao.deleteGenreById(1) } returns Unit
        coEvery { logger.logException(any()) } returns Unit

        // When
        val result = localGenreDataSourceImpl.deleteGenreById(1)

        // Then
        assertThat(result).isEqualTo(Unit)
    }

    @Test
    fun `should clear all Genres from database when call deleteAllGenres`() = runTest {
        // Given
        coEvery { genreDao.deleteAllGenres() } returns Unit
        coEvery { logger.logException(any()) } returns Unit

        // When
        val result = localGenreDataSourceImpl.deleteAllGenres()

        // Then
        assertThat(result).isEqualTo(Unit)
    }

    companion object {
        val GENRE = GenreDto(
            id = 1,
            name = "Action",
            type = GenreDto.GenreType.MOVIE
        )
        val MOVIE_GENRES = listOf(
            GENRE.copy(id = 2, name = "Adventure"),
            GENRE.copy(id = 3, name = "Comedy"),
            GENRE.copy(id = 4, name = "Drama"),
            GENRE.copy(id = 5, name = "Fantasy"),
            GENRE.copy(id = 6, name = "Horror"),
            GENRE.copy(id = 7, name = "Thriller")
        )
        val TV_SHOW_GENRES = listOf(
            GENRE.copy(id = 2, name = "Adventure", type = GenreDto.GenreType.TV_SHOW),
            GENRE.copy(id = 3, name = "Comedy", type = GenreDto.GenreType.TV_SHOW),
            GENRE.copy(id = 4, name = "Drama", type = GenreDto.GenreType.TV_SHOW),
            GENRE.copy(id = 5, name = "Fantasy", type = GenreDto.GenreType.TV_SHOW),
            GENRE.copy(id = 6, name = "Horror", type = GenreDto.GenreType.TV_SHOW),
            GENRE.copy(id = 7, name = "Thriller", type = GenreDto.GenreType.TV_SHOW)
        )


    }

    fun GenreDto.toEntity(): Genre {
        return Genre(id = id, name = name, type = type.name)
    }


}