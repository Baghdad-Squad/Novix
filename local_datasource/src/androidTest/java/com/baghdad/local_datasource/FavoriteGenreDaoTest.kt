package com.baghdad.local_datasource

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.baghdad.local_datasource.roomDB.dao.FavoriteGenreDao
import com.baghdad.local_datasource.roomDB.database.NovixDatabase
import com.baghdad.local_datasource.roomDB.entity.FavoriteGenre
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class FavoriteGenreDaoTest {

    private lateinit var database: NovixDatabase
    private lateinit var favoriteGenreDao: FavoriteGenreDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NovixDatabase::class.java
        ).allowMainThreadQueries().build()
        favoriteGenreDao = database.favoriteGenreDao()
    }

    @After
    fun closeDataBase() {
        database.clearAllTables()
        database.close()
    }

    @Test
    fun shouldInsertGenre_whenAddFavoriteGenreIsCalled() = runBlocking {
        // Given
        val genre = FAKE_GENRE_1

        // When
        favoriteGenreDao.addFavoriteGenre(genre)
        val result = favoriteGenreDao.getFavoriteGenreById(genre.genreId)

        // Then
        assertThat(result).isNotNull()
        assertThat(result?.name).isEqualTo(genre.name)
        assertThat(result?.count).isEqualTo(genre.count)
    }

    @Test
    fun shouldReturnCorrectGenre_whenGetFavoriteGenreByIdIsCalled() = runBlocking {
        // Given
        favoriteGenreDao.addFavoriteGenre(FAKE_GENRE_2)

        // When
        val result = favoriteGenreDao.getFavoriteGenreById(FAKE_GENRE_2.genreId)

        // Then
        assertThat(result?.name).isEqualTo(FAKE_GENRE_2.name)
        assertThat(result?.count).isEqualTo(FAKE_GENRE_2.count)
    }

    @Test
    fun shouldReturnGenresOrderedByCountAndTimestamp_whenGetFavoriteGenresIsCalled() = runBlocking {
        // Given
        FAKE_GENRES.forEach { favoriteGenreDao.addFavoriteGenre(it) }

        // When
        val result = favoriteGenreDao.getFavoriteGenres()

        // Then
        assertThat(result.size).isEqualTo(3)
        assertThat(result[0].name).isEqualTo("Sci-Fi")
        assertThat(result[1].name).isEqualTo("Drama")
        assertThat(result[2].name).isEqualTo("Comedy")
    }

    @Test
    fun shouldInsertNewGenreWithCountOne_whenUpdateFavoriteGenreCountCalledForNonExistingGenre() =
        runBlocking {
        // Given
            val newGenreId = 100L
            val newGenreName = "Thriller"

        // When
            favoriteGenreDao.updateFavoriteGenreCount(newGenreId, newGenreName)
            val result = favoriteGenreDao.getFavoriteGenreById(newGenreId)

        // Then
            assertThat(result).isNotNull()
            assertThat(result?.count).isEqualTo(1)
            assertThat(result?.name).isEqualTo("Thriller")
    }

    @Test
    fun shouldIncrementCount_whenUpdateFavoriteGenreCountCalledForExistingGenre() = runBlocking {
        // Given
        favoriteGenreDao.addFavoriteGenre(FAKE_GENRE_3)

        // When
        favoriteGenreDao.updateFavoriteGenreCount(FAKE_GENRE_3.genreId, FAKE_GENRE_3.name)
        val result = favoriteGenreDao.getFavoriteGenreById(FAKE_GENRE_3.genreId)

        // Then
        assertThat(result?.count).isEqualTo(FAKE_GENRE_3.count + 1)
    }

    companion object {
        val FAKE_GENRE_1 = FavoriteGenre(
            genreId = 1L,
            name = "Action",
            count = 1,
            timeStamp = 1000L
        )

        val FAKE_GENRE_2 = FavoriteGenre(
            genreId = 2L,
            name = "Drama",
            count = 2,
            timeStamp = 2000L
        )

        val FAKE_GENRE_3 = FavoriteGenre(
            genreId = 3L,
            name = "Horror",
            count = 2,
            timeStamp = 3000L
        )

        val FAKE_GENRES = listOf(
            FavoriteGenre(1L, "Comedy", 3, 1L),
            FavoriteGenre(2L, "Drama", 3, 2L),
            FavoriteGenre(3L, "Sci-Fi", 2, 3L)
        )
    }
}
