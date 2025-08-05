//package com.baghdad.local_datasource
//
//import com.baghdad.local_datasource.roomDB.dao.GenreDao
//import com.baghdad.local_datasource.roomDB.entity.Genre
//import com.baghdad.local_datasource.roomDB.entity.Movie
//import com.baghdad.local_datasource.roomDB.entity.toDto
//import com.baghdad.local_datasource.roomDB.entity.toDtos
//import com.baghdad.repository.logger.Logger
//import com.baghdad.repository.model.GenreDto
//import com.google.common.truth.Truth.assertThat
//import io.mockk.coEvery
//import io.mockk.mockk
//import kotlinx.coroutines.flow.first
//import kotlinx.coroutines.flow.flowOf
//import kotlinx.coroutines.test.runTest
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//
//class LocalMovieDataSourceImplTest {
//
//    lateinit var movieDao: MovieDao
//    lateinit var genreDao: GenreDao
//    lateinit var logger: Logger
//
//    lateinit var localMovieDataSourceImpl: LocalMovieDataSourceImpl
//
//    @BeforeEach
//    fun setUp() {
//        movieDao = mockk()
//        genreDao = mockk()
//        logger = mockk()
//
//        localMovieDataSourceImpl = LocalMovieDataSourceImpl(movieDao, genreDao, logger)
//    }
//
//    @Test
//    fun `should add new single movie when call addMovie`() = runTest {
//        // Given
//        coEvery { movieDao.upsertMovie(any()) } returns Unit
//        coEvery { logger.logException(any()) } returns Unit
//
//        // When
//        val result = localMovieDataSourceImpl.addMovie(MOVIE.toDto(listOf(GENRE.toDto())))
//
//        // Then
//        assertThat(result).isEqualTo(Unit)
//    }
//
//    @Test
//    fun `should add new movies when call addMovies`() = runTest {
//        // Given
//        coEvery { movieDao.upsertMovies(any()) } returns Unit
//        coEvery { logger.logException(any()) } returns Unit
//
//        // When
//        val result = localMovieDataSourceImpl.addMovies(MOVIES.toDtos(mapOf()))
//
//        // Then
//        assertThat(result).isEqualTo(Unit)
//    }
//
//    @Test
//    fun `should get specific movie when call getMovieById`() = runTest {
//        // Given
//        coEvery { movieDao.getMovieById(1) } returns MOVIE
//        coEvery { genreDao.getGenreById(any()) } returns GENRE
//        coEvery { logger.logException(any()) } returns Unit
//
//        // When
//        val result = localMovieDataSourceImpl.getMovieById(1)
//
//        // Then
//        assertThat(result.id).isEqualTo(MOVIE.id)
//    }
//
//    @Test
//    fun `should get movies by ids when call getMoviesByIds`() = runTest {
//        // Given
//        coEvery { movieDao.getMoviesByIds(any()) } returns MOVIES
//        coEvery { genreDao.getAllGenres() } returns listOf(GENRE)
//        coEvery { logger.logException(any()) } returns Unit
//
//        // When
//        val result = localMovieDataSourceImpl.getMoviesByIds(listOf(1, 2, 3))
//
//        // Then
//        assertThat(result.size).isEqualTo(MOVIES.size)
//        assertThat(result[0].id).isEqualTo(MOVIES[0].id)
//    }
//
//    @Test
//    fun `should get all movies when call getAllMovies`() = runTest {
//        // Given
//        coEvery { movieDao.getAllMovies() } returns flowOf(MOVIES)
//        coEvery { genreDao.getGenreById(any()) } returns GENRE
//        coEvery { logger.logException(any()) } returns Unit
//
//        // When
//        val result = localMovieDataSourceImpl.getAllMovies().first()
//
//        // Then
//        assertThat(result.size).isEqualTo(MOVIES.size)
//        assertThat(result[0].id).isEqualTo(MOVIES[0].id)
//    }
//
//
//    @Test
//    fun `should delete specific movie when send that movie id`() = runTest {
//        // Given
//        coEvery { movieDao.deleteMovieById(1) } returns Unit
//        coEvery { logger.logException(any()) } returns Unit
//
//        // When
//        val result = localMovieDataSourceImpl.deleteMovieById(1)
//
//        // Then
//        assertThat(result).isEqualTo(Unit)
//    }
//
//    @Test
//    fun `should clear database form movies when call deleteAllMovies`() = runTest {
//        // Given
//        coEvery { movieDao.deleteAll() } returns Unit
//        coEvery { logger.logException(any()) } returns Unit
//
//        // When
//        val result = localMovieDataSourceImpl.deleteAllMovies()
//
//        // Then
//        assertThat(result).isEqualTo(Unit)
//    }
//
//    @Test
//    fun `should update specific movie when call updateMovie`() = runTest {
//        // Given
//        coEvery { movieDao.upsertMovie(any()) } returns Unit
//        coEvery { logger.logException(any()) } returns Unit
//
//        // When
//        val result = localMovieDataSourceImpl.updateMovie(MOVIE.toDto(listOf(GENRE.toDto())))
//
//        // Then
//        assert(result == Unit)
//    }
//
//    @Test
//    fun `should get movies when search by title and title is valid`() = runTest {
//        // Given
//        coEvery { movieDao.getMoviesFromSearchQuery(any(), any(), any()) } returns MOVIES
//        coEvery { genreDao.getGenresByIds(any()) } returns listOf(GENRE)
//        coEvery { logger.logException(any()) } returns Unit
//
//        // When
//        val result = localMovieDataSourceImpl.searchMoviesByTitle("title", 1, 10)
//
//        // Then
//        assertThat(result.size).isEqualTo(MOVIES.size)
//        assertThat(result[0].id).isEqualTo(MOVIES[0].id)
//    }
//
//
//    companion object {
//
//        val GENRE = Genre(
//            id = 1,
//            name = "Action",
//            type = GenreDto.GenreType.MOVIE.name
//        )
//
//        val MOVIE = Movie(
//            id = 1,
//            title = "Movie 1",
//            overview = "Overview 1",
//            releaseDate = "releaseDate 1",
//            imdbRating = 1.7,
//            userRating = 5.5,
//            posterPictureURL = "Test",
//            runtimeMinutes = 5,
//            genres = listOf(
//                1, 2
//            )
//        )
//
//        val MOVIES = listOf(
//            MOVIE,
//            MOVIE.copy(id = 2, title = "Movie 2"),
//            MOVIE.copy(id = 3, title = "Movie 3"),
//            MOVIE.copy(id = 4, title = "Movie 4"),
//            MOVIE.copy(id = 5, title = "Movie 5")
//        )
//    }
//
//}