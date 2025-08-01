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
import com.google.common.truth.Truth.assertThat
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
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
    fun shouldSaveTvShowCorrectly_whenUpsertTvShowIsCalled() = runBlocking {
        // When
        dao.upsertTvShow(FAKE_TV_SHOW_1)
        val result = dao.getAllTvShow().first()

        // Then
        assertThat(result.size).isEqualTo(1)
        assertThat(result.first().title).isEqualTo(FAKE_TV_SHOW_1.title)
    }

    @Test
    fun shouldReturnAllTvShows_whenGetAllTvShowIsCalled() = runBlocking {
        // When
        dao.upsertTvShow(FAKE_TV_SHOW_1)
        dao.upsertTvShow(FAKE_TV_SHOW_2)
        val result = dao.getAllTvShow().first()

        // Then
        assertThat(result.size).isEqualTo(2)
    }

    @Test
    fun shouldRemoveSpecificTvShow_whenDeleteTvShowByIdIsCalled() = runBlocking {
        // When
        dao.upsertTvShow(FAKE_TV_SHOW_1)
        dao.deleteTvShowByID(FAKE_TV_SHOW_1.id)
        val result = dao.getAllTvShow().first()

        // Then
        assertTrue(result.none { it.id == FAKE_TV_SHOW_1.id })
    }

    @Test
    fun shouldRemoveAllTvShows_whenDeleteAllIsCalled() = runBlocking {
        // When
        dao.upsertTvShow(FAKE_TV_SHOW_1)
        dao.upsertTvShow(FAKE_TV_SHOW_2)
        dao.deleteAll()
        val result = dao.getAllTvShow().first()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun shouldReturnCorrectTvShow_whenGetTvShowByIdIsCalled() = runBlocking {
        // When
        dao.upsertTvShow(FAKE_TV_SHOW_2)
        val result = dao.getTvShowById(FAKE_TV_SHOW_2.id)

        // Then
        assertThat(result.title).isEqualTo(FAKE_TV_SHOW_2.title)
        assertThat(result.numberOfSeasons).isEqualTo(FAKE_TV_SHOW_2.numberOfSeasons)
    }

    @Test
    fun shouldReturnPaginatedTvShows_whenGetTvShowsFromSearchQueryIsCalled() = runBlocking {
        // Given
        dao.upsertTvShows(listOf(FAKE_TV_SHOW_1, FAKE_TV_SHOW_2))
        database.searchQueryDao().addSearchQuery(
            SearchQuery(
                queryName = "trending_tv",
                mediaId = FAKE_TV_SHOW_1.id,
                mediaType = SearchQueryDto.MediaType.TV_SHOW.name
            )
        )
        database.searchQueryDao().addSearchQuery(
            SearchQuery(
                queryName = "trending_tv",
                mediaId = FAKE_TV_SHOW_2.id,
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
        assertThat(firstPage.size).isEqualTo(1)
        assertThat(secondPage.size).isEqualTo(1)
        assertThat(firstPage.first().id).isNotEqualTo(secondPage.first().id)
        assertThat(
            listOf(
                FAKE_TV_SHOW_1.id,
                FAKE_TV_SHOW_2.id
            ).contains(firstPage.first().id)
        ).isTrue()
        assertThat(
            listOf(
                FAKE_TV_SHOW_1.id,
                FAKE_TV_SHOW_2.id
            ).contains(secondPage.first().id)
        ).isTrue()
    }

    companion object {
        val FAKE_TV_SHOW_1 = TvShow(
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

        val FAKE_TV_SHOW_2 = TvShow(
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
}
