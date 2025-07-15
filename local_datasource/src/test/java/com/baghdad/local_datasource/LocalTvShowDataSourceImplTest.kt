package com.baghdad.local_datasource

import com.baghdad.local_datasource.database.dao.TvShowDao
import com.baghdad.local_datasource.database.dto.LocalTvShowDto
import com.baghdad.local_datasource.database.dto.toDto
import com.baghdad.local_datasource.database.dto.toEntity
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LocalTvShowDataSourceImplTest {

    private lateinit var tvShowDao: TvShowDao
    private lateinit var dataSource: LocalTvShowDataSourceImpl

    @BeforeEach
    fun setup() {
        tvShowDao = mockk(relaxed = true)
        dataSource = LocalTvShowDataSourceImpl(tvShowDao)
    }

    @Test
    fun `addTvShow maps and inserts entity`() = runTest {
        val dto = fakeTvShow1.toDto()
        coEvery { tvShowDao.upsertTvShow(dto.toEntity()) } just Runs

        dataSource.addTvShow(dto)

        coVerify { tvShowDao.upsertTvShow(dto.toEntity()) }
    }

    @Test
    fun `getTvShowById returns mapped dto`() = runTest {
        coEvery { tvShowDao.getTvShowById(10L) } returns fakeTvShow1

        val result = dataSource.getTvShowById(10L)

        Assertions.assertEquals(fakeTvShow1.toDto(), result)
        coVerify { tvShowDao.getTvShowById(10L) }
    }

    @Test
    fun `getAllTvShows returns mapped flow`() = runTest {
        val flow = flowOf(listOf(fakeTvShow1, fakeTvShow2))
        coEvery { tvShowDao.getAllTvShow() } returns flow

        val result = dataSource.getAllTvShows().first()

        Assertions.assertEquals(2, result.size)
        Assertions.assertEquals(listOf(fakeTvShow1.toDto(), fakeTvShow2.toDto()), result)

    }

    @Test
    fun `deleteTvShowById calls dao correctly`() = runTest {
        coEvery { tvShowDao.deleteTvShowByID(10L) } just Runs

        dataSource.deleteTvShowById(10L)

        coVerify { tvShowDao.deleteTvShowByID(10L) }
    }

    @Test
    fun `deleteAllTvShows clears database`() = runTest {
        coEvery { tvShowDao.deleteAll() } just Runs

        dataSource.deleteAllTvShows()

        coVerify { tvShowDao.deleteAll() }
    }

    @Test
    fun `updateTvShow maps and upserts new entity`() = runTest {
        val updated = fakeTvShow2.toDto()
        coEvery { tvShowDao.upsertTvShow(updated.toEntity()) } just Runs

        dataSource.updateTvShow(updated)

        coVerify { tvShowDao.upsertTvShow(updated.toEntity()) }
    }

    @Test
    fun `searchTvShowsByTitle returns filtered mapped results`() = runTest {
        coEvery { tvShowDao.searchTvShowsByTitle("Shadow") } returns listOf(fakeTvShow2)

        val result = dataSource.searchTvShowsByTitle("Shadow")

        Assertions.assertEquals(listOf(fakeTvShow2.toDto()), result)
        coVerify { tvShowDao.searchTvShowsByTitle("Shadow") }
    }

    val fakeTvShow1 = LocalTvShowDto(
        id = 10L,
        title = "Neon Rewind",
        genres = listOf("Sci-Fi", "Thriller"),
        imdbRating = 8.3,
        userRating = 9.2,
        releaseDate = "2025-03-12",
        overview = "Remixing timelines through retro VHS tapes.",
        posterPictureURL = "https://example.com/neon.jpg",
        numberOfSeasons = 2
    )

    val fakeTvShow2 = LocalTvShowDto(
        id = 11L,
        title = "Shadow Empire",
        genres = listOf("Fantasy", "Drama"),
        imdbRating = 7.9,
        userRating = 8.5,
        releaseDate = "2024-09-01",
        overview = "An empire rises in the echoes of betrayal.",
        posterPictureURL = "https://example.com/shadow.jpg",
        numberOfSeasons = 4
    )

}