//package com.baghdad.local_datasource
//
//import com.baghdad.repository.logger.Logger
//import io.mockk.coEvery
//import io.mockk.mockk
//import kotlinx.coroutines.test.runTest
//import org.junit.jupiter.api.Assertions.assertNotNull
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//
//class LocalFavoriteGenreDataSourceImplTest {
//
//    lateinit var favoriteGenreDao: FavoriteGenreDao
//    lateinit var logger: Logger
//
//    lateinit var localFavoriteGenreDataSourceImpl: LocalFavoriteGenreDataSourceImpl
//
//    @BeforeEach
//    fun setUp() {
//        favoriteGenreDao = mockk()
//        logger = mockk()
//
//        localFavoriteGenreDataSourceImpl =
//            LocalFavoriteGenreDataSourceImpl(favoriteGenreDao, logger)
//    }
//
//    @Test
//    fun `should update Favorite genre count when a new one added`() = runTest {
//        // Given
//        val id = 1L
//        val name = "Action"
//        coEvery { favoriteGenreDao.updateFavoriteGenreCount(id, name) } returns Unit
//        coEvery { logger.logException(any()) } returns Unit
//
//        // When
//        val result = localFavoriteGenreDataSourceImpl.updateFavoriteGenreCount(id, name)
//
//        // Then
//        assertNotNull(result)
//    }
//
//    @Test
//    fun `should get all favorite genres when call getFavoriteGenres`() = runTest {
//        // Given
//        coEvery { favoriteGenreDao.getFavoriteGenres() } returns emptyList()
//        coEvery { logger.logException(any()) } returns Unit
//
//        // When
//        val result = localFavoriteGenreDataSourceImpl.getFavoriteGenres()
//
//        // Then
//        assertNotNull(result)
//    }
//
//}