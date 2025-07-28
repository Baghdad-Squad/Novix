package com.baghdad.local_datasource

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.baghdad.local_datasource.roomDB.dao.TvShowDao
import com.baghdad.local_datasource.roomDB.database.NovixDatabase
import com.baghdad.local_datasource.roomDB.entity.SearchQuery
import com.baghdad.local_datasource.roomDB.entity.TvShow
import com.baghdad.repository.model.SearchQueryDto
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class TvShowDaoTest {
    private lateinit var database: NovixDatabase
    private lateinit var dao: TvShowDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NovixDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.tvShowDao()
    }

    @After
    fun closeDataBase() {
        database.close()
    }

    @Test
    fun upsertTvShow_savesDataCorrectly() = runBlocking {
        // Given
        dao.upsertTvShow(fakeTvShow1)

        // When
        val result = dao.getAllTvShow().first()

        // Then
        assertEquals(1, result.size)
        assertEquals("Quantum Echo", result.first().title)
    }

    @Test
    fun getAllTvShow_returnsAllInsertedItems() = runBlocking {
        // Given
        dao.upsertTvShow(fakeTvShow1)
        dao.upsertTvShow(fakeTvShow2)

        // When
        val result = dao.getAllTvShow().first()

        // Then
        assertEquals(2, result.size)
    }

    @Test
    fun deleteTvShowById_removesTargetShow() = runBlocking {
        // Given
        dao.upsertTvShow(fakeTvShow1)
        dao.deleteTvShowByID(fakeTvShow1.id)

        // When
        val result = dao.getAllTvShow().first()

        // Then
        assertTrue(result.none { it.id == fakeTvShow1.id })
    }

    @Test
    fun deleteAll_removesAllShows() = runBlocking {
        // Given
        dao.upsertTvShow(fakeTvShow1)
        dao.upsertTvShow(fakeTvShow2)

        // When
        dao.deleteAll()
        val result = dao.getAllTvShow().first()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun getTvShowById_returnsCorrectData() = runBlocking {
        // Given
        dao.upsertTvShow(fakeTvShow2)

        // When
        val result = dao.getTvShowById(fakeTvShow2.id)

        // Then
        assertEquals(fakeTvShow2.title, result.title)
        assertEquals(fakeTvShow2.numberOfSeasons, result.numberOfSeasons)
    }

    @Test
    fun getTvShowsFromSearchQuery_returnsPaginatedMatchingTvShows() = runBlocking {
        // Given
        dao.upsertTvShows(listOf(fakeTvShow1, fakeTvShow2))
        database.searchQueryDao().addSearchQuery(
            SearchQuery(
                queryName = "trending_tv",
                mediaId = fakeTvShow1.id,
                mediaType = SearchQueryDto.MediaType.TV_SHOW.name
            )
        )
        database.searchQueryDao().addSearchQuery(
            SearchQuery(
                queryName = "trending_tv",
                mediaId = fakeTvShow2.id,
                mediaType = SearchQueryDto.MediaType.TV_SHOW.name
            )
        )
        val firstPage = dao.getTvShowsFromSearchQuery(
            queryName = "trending_tv",
            pageSize = 1,
            offset = 0
        )
        val secondPage = dao.getTvShowsFromSearchQuery(
            queryName = "trending_tv",
            pageSize = 1,
            offset = 1
        )

        // Then
        assertEquals(1, firstPage.size)
        assertEquals(1, secondPage.size)
        assertNotEquals(firstPage.first().id, secondPage.first().id)
        assertTrue(listOf(fakeTvShow1.id, fakeTvShow2.id).contains(firstPage.first().id))
        assertTrue(listOf(fakeTvShow1.id, fakeTvShow2.id).contains(secondPage.first().id))
    }

    private val fakeTvShow1 = TvShow(
        id = 100L,
        title = "Quantum Echo",
        genres = listOf(1L, 2L, 3L),
        imdbRating = 8.6,
        userRating = 9.2,
        releaseDate = "2025-01-15",
        overview = "Echoes of quantum futures disrupt timelines.",
        posterPictureURL = "https://example.com/quantum.jpg",
        numberOfSeasons = 2
    )

    private val fakeTvShow2 = TvShow(
        id = 101L,
        title = "Shadow Empire",
        genres = listOf(4L, 5L, 6L),
        imdbRating = 7.9,
        userRating = 8.5,
        releaseDate = "2023-11-22",
        overview = "A mythical war for the lost throne of Arak.",
        posterPictureURL = "https://example.com/shadow.jpg",
        numberOfSeasons = 4
    )
}