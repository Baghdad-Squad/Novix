//package com.baghdad.local_datasource
//
//import androidx.room.Room
//import androidx.test.core.app.ApplicationProvider
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import androidx.test.filters.SmallTest
//import com.baghdad.local_datasource.roomDB.database.NovixDatabase
//import com.baghdad.local_datasource.roomDB.entity.Genre
//import com.google.common.truth.Truth.assertThat
//import org.junit.After
//import org.junit.Before
//import org.junit.Test
//import org.junit.runner.RunWith
//
//@RunWith(AndroidJUnit4::class)
//@SmallTest
//class GenreDaoTest {
//
//    private lateinit var database: NovixDatabase
//    private lateinit var genreDao: GenreDao
//
//    @Before
//    fun setup() {
//        database = Room.inMemoryDatabaseBuilder(
//            ApplicationProvider.getApplicationContext(),
//            NovixDatabase::class.java
//        ).allowMainThreadQueries().build()
//        genreDao = database.genreDao()
//    }
//
//    @After
//    fun closeDatabase() {
//        database.clearAllTables()
//        database.close()
//    }
//
//    @Test
//    fun shouldInsertGenreCorrectly_whenAddGenreIsCalled() {
//        // Given
//        val genre = FAKE_GENRE_1
//
//        // When
//        genreDao.addGenre(genre)
//
//        // Then
//        val result = genreDao.getGenreById(genre.id)
//        assertThat(result).isNotNull()
//        assertThat(result.id).isEqualTo(genre.id)
//        assertThat(result.name).isEqualTo(genre.name)
//    }
//
//    @Test
//    fun shouldReturnCorrectGenre_whenGetGenreByIdIsCalled() {
//        // Given
//        val genre = FAKE_GENRE_2
//        genreDao.addGenre(genre)
//
//        // When
//        val result = genreDao.getGenreById(genre.id)
//
//        // Then
//        assertThat(result).isEqualTo(genre)
//    }
//
//    @Test
//    fun shouldReturnAllGenres_whenGetAllGenresIsCalled() {
//        // Given
//        genreDao.addGenre(FAKE_GENRE_1)
//        genreDao.addGenre(FAKE_GENRE_2)
//
//        // When
//        val result = genreDao.getAllGenres()
//
//        // Then
//        assertThat(result).hasSize(2)
//        assertThat(result).containsAtLeast(FAKE_GENRE_1, FAKE_GENRE_2)
//    }
//
//    @Test
//    fun shouldRemoveSpecificGenre_whenDeleteGenreByIdIsCalled() {
//        // Given
//        val genre = FAKE_GENRE_3
//        genreDao.addGenre(genre)
//
//        // When
//        genreDao.deleteGenreById(genre.id)
//
//        // Then
//        val result = genreDao.getAllGenres()
//        assertThat(result).doesNotContain(genre)
//    }
//
//    @Test
//    fun shouldRemoveAllGenres_whenDeleteAllGenresIsCalled() {
//        // Given
//        genreDao.addGenre(FAKE_GENRE_1)
//        genreDao.addGenre(FAKE_GENRE_2)
//
//        // When
//        genreDao.deleteAllGenres()
//
//        // Then
//        val result = genreDao.getAllGenres()
//        assertThat(result).isEmpty()
//    }
//
//    @Test
//    fun shouldReturnCorrectName_whenGetGenreByIdIsCalled() {
//        // Given
//        val genre = FAKE_GENRE_4
//        genreDao.addGenre(genre)
//
//        // When
//        val result = genreDao.getGenreById(genre.id)
//
//        // Then
//        assertThat(result.name).isEqualTo("name7")
//    }
//
//    companion object {
//        val FAKE_GENRE_1 = Genre(id = 1L, name = "name1", type = "type1")
//        val FAKE_GENRE_2 = Genre(id = 2L, name = "name2", type = "type2")
//        val FAKE_GENRE_3 = Genre(id = 3L, name = "name3", type = "type3")
//        val FAKE_GENRE_4 = Genre(id = 6L, name = "name7", type = "type7")
//    }
//}
