package com.baghdad.local_datasource

import com.baghdad.local_datasource.roomDB.dao.GenreDao
import com.baghdad.local_datasource.roomDB.dao.TvShowDao
import com.baghdad.local_datasource.roomDB.entity.Genre
import com.baghdad.local_datasource.roomDB.entity.TvShow
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.baghdad.local_datasource.roomDB.entity.toLocalDto
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.GenreDto
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


@ExperimentalCoroutinesApi
class LocalTvShowDataSourceImplTest {

    private lateinit var tvShowDao: TvShowDao
    private lateinit var genreDao: GenreDao
    private lateinit var logger: Logger

    private lateinit var localTvShowDataSource: LocalTvShowDataSourceImpl

    @BeforeEach
    fun setUp() {
        tvShowDao = mockk()
        genreDao = mockk()
        logger = mockk()
        localTvShowDataSource = LocalTvShowDataSourceImpl(tvShowDao, genreDao, logger)
    }

    @Test
    fun `should add single tv show to database`() = runTest {
        // Given
        val tvShowDto = TV_SHOW_DTO
        val expectedEntity = tvShowDto.toLocalDto()

        coEvery { tvShowDao.upsertTvShow(expectedEntity) } just Runs

        // When
        localTvShowDataSource.addTvShow(tvShowDto)

        // Then
        coVerify { tvShowDao.upsertTvShow(expectedEntity) }
    }


    @Test
    fun `should add tv shows to database`() = runTest {
        // Given
        val tvShowsDto = TV_SHOWS
        val expectedEntity = tvShowsDto.map { it.toLocalDto() }

        coEvery { tvShowDao.upsertTvShows(expectedEntity) } just Runs

        // When
        localTvShowDataSource.addTvShows(tvShowsDto)

        // Then
        coVerify { tvShowDao.upsertTvShows(expectedEntity) }
    }

    @Test
    fun `should return Tv Show when valid Id`() = runTest {
        // Given
        val tvShowId = TV_SHOW.id
        val expectedTvShow = TV_SHOW

        // When
        coEvery { tvShowDao.getTvShowById(tvShowId) } returns expectedTvShow
        coEvery { genreDao.getGenreById(any()) } returns GENRE
        coEvery { logger.logException(any()) } just Runs

        val result = localTvShowDataSource.getTvShowById(tvShowId).toLocalDto()

        // Then
        coVerify { tvShowDao.getTvShowById(tvShowId) }
        assert(result.id == expectedTvShow.id)
    }


    @Test
    fun `should return all tv shows when called`() = runTest {
        // Given
        val expectedTvShows = TV_SHOWS.map { it.toLocalDto() }

        // When
        coEvery { tvShowDao.getAllTvShow() } returns flowOf(expectedTvShows)
        coEvery { genreDao.getGenreById(any()) } returns GENRE
        coEvery { logger.logException(any()) } just Runs
        val result = localTvShowDataSource.getAllTvShows()

        // Then
        coVerify { tvShowDao.getAllTvShow() }
        assert(result.first().size == expectedTvShows.size)

    }

    @Test
    fun `should delete tv show when tv show have the same id`() = runTest {
        // Given
        val tvShowId = TV_SHOW.id
        coEvery { tvShowDao.deleteTvShowByID(tvShowId) } just Runs
        coEvery { logger.logException(any()) } just Runs

        // When
        localTvShowDataSource.deleteTvShowById(tvShowId)

        // Then
        coVerify { tvShowDao.deleteTvShowByID(tvShowId) }
    }

    @Test
    fun `should delete all tv shows when deleteAllTvShows called`() {
        // Given
        coEvery { tvShowDao.deleteAll() } just Runs
        coEvery { logger.logException(any()) } just Runs

        // When
        runTest { localTvShowDataSource.deleteAllTvShows() }

        // Then
        coVerify { tvShowDao.deleteAll() }
    }

    @Test
    fun `should update tv show when updateTvShow called`() {
        // Given
        val tvShowDto = TV_SHOW_DTO
        val expectedEntity = tvShowDto.toLocalDto()
        coEvery { tvShowDao.upsertTvShow(expectedEntity) } just Runs
        coEvery { logger.logException(any()) } just Runs

        // When
        runTest { localTvShowDataSource.updateTvShow(tvShowDto) }

        // Then
        coVerify { tvShowDao.upsertTvShow(expectedEntity) }
    }

    @Test
    fun `should return tv shows when search with title`() = runTest {
        // Given
        val title = "Test"
        val pageSize = 10
        val page = 1
        val expectedTvShows = TV_SHOWS.map { it.toLocalDto() }
        coEvery {
            tvShowDao.getTvShowsFromSearchQuery(title, pageSize, any())
        } returns expectedTvShows
        coEvery { genreDao.getGenresByIds(any()) } returns listOf(GENRE)
        coEvery { logger.logException(any()) } just Runs

        // When
        val result = localTvShowDataSource.searchTvShowsByTitle(title, page, pageSize)

        // Then
        coVerify { tvShowDao.getTvShowsFromSearchQuery(title, pageSize, any()) }
        coVerify { genreDao.getGenresByIds(any()) }
        assert(result.size == expectedTvShows.size)
    }

    companion object {
        val GENRES = listOf(1L, 2L, 3L, 4L, 5L, 6L)
        val GENRE = Genre(
            id = 1,
            name = "Drama",
            type = "TV_SHOW"
        )
        val TV_SHOW = TvShow(
            id = 123L,
            title = "Test TV Show",
            genres = GENRES,
            imdbRating = 7.0,
            userRating = 7.7,
            releaseDate = "2002-2-22",
            overview = "Test overview",
            posterPictureURL = "Test URL",
            numberOfSeasons = 1
        )
        val TV_SHOW_DTO = TV_SHOW.toDto(GENRES.map { GenreDto(it, "", GenreDto.GenreType.TV_SHOW) })
        val TV_SHOWS = listOf(
            TV_SHOW_DTO.copy(id = 123L),
            TV_SHOW_DTO.copy(id = 1234L).copy(title = "Test TV Show 2"),
            TV_SHOW_DTO.copy(id = 12345L).copy(title = "Test TV Show 3"),
            TV_SHOW_DTO.copy(id = 123456L).copy(title = "Test TV Show 4")
        )

    }
}