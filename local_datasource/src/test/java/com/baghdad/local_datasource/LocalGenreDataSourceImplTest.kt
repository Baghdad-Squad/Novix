package com.baghdad.local_datasource

import com.baghdad.local_datasource.database.dao.GenreDao
import com.baghdad.local_datasource.database.dto.LocalGenreDto
import com.baghdad.local_datasource.database.dto.toDto
import com.baghdad.repository.model.GenreDto
import com.google.common.truth.Truth
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LocalGenreDataSourceImplTest {

    private lateinit var genreDao: GenreDao
    private lateinit var dataSource: LocalGenreDataSource

    @BeforeEach
    fun setup() {
        genreDao = mockk(relaxed = true)
        dataSource = LocalGenreDataSource(genreDao)
    }

    @Test
    fun `getMovieGenre returns only movie genres mapped to dto`() = runTest {
        val genres = listOf(
            LocalGenreDto(id = 1L, name = "Action", type = "MOVIE"),
            LocalGenreDto(id = 2L, name = "Drama", type = "TV_SHOW")
        )
        every { genreDao.getAllGenres() } returns genres

        val result = dataSource.getMovieGenre("en")

        Truth.assertThat(listOf(genres[0].toDto())).isEqualTo(result)
    }

    @Test
    fun `getTvShowGenre returns only tv genres mapped to dto`() = runTest {
        val genres = listOf(
            LocalGenreDto(id = 1L, name = "Comedy", type = "TV_SHOW"),
            LocalGenreDto(id = 2L, name = "Sci-Fi", type = "MOVIE")
        )
        every { genreDao.getAllGenres() } returns genres

        val result = dataSource.getTvShowGenre("en")

        Truth.assertThat(listOf(genres[0].toDto())).isEqualTo(result)
    }

    @Test
    fun `addGenre inserts genre mapped to local entity`() = runTest {
        val genre = GenreDto(id = 0L, name = "Fantasy", type = GenreDto.GenreType.TV_SHOW)
        val type = GenreDto.GenreType.TV_SHOW
        val expectedEntity = LocalGenreDto(type = "TV_SHOW", name = "Fantasy")

        every { genreDao.addGenre(expectedEntity) } just Runs

        dataSource.addGenre(genre, type)

        verify { genreDao.addGenre(expectedEntity) }
    }

    @Test
    fun `getAllGenres returns all genres mapped to dto`() = runTest {
        val entities = listOf(
            LocalGenreDto(id = 1L, name = "Drama", type = "MOVIE"),
            LocalGenreDto(id = 2L, name = "Thriller", type = "TV_SHOW")
        )
        every { genreDao.getAllGenres() } returns entities

        val result = dataSource.getAllGenres()

        Truth.assertThat(entities.map { it.toDto() }).isEqualTo(result)
    }

    @Test
    fun `getGenreById returns mapped dto for valid id`() = runTest {
        val entity = LocalGenreDto(id = 5L, name = "Adventure", type = "MOVIE")
        every { genreDao.getGenreById(5L) } returns entity

        val result = dataSource.getGenreById(5L)

        Truth.assertThat(entity.toDto()).isEqualTo(result)
    }

    @Test
    fun `deleteGenreById calls dao delete method with correct id`() = runTest {
        val genreId = 9L
        every { genreDao.deleteGenreById(genreId) } just Runs

        dataSource.deleteGenreById(genreId)

        verify { genreDao.deleteGenreById(genreId) }
    }

    @Test
    fun `deleteAllGenres clears all genres`() = runTest {
        every { genreDao.deleteAllGenres() } just Runs

        dataSource.deleteAllGenres()

        verify { genreDao.deleteAllGenres() }

    }


}