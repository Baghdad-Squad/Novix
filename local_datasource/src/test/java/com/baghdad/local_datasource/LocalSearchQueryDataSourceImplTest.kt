//package com.baghdad.local_datasource
//
//import com.baghdad.local_datasource.roomDB.dao.ActorDao
//import com.baghdad.local_datasource.roomDB.dao.MovieDao
//import com.baghdad.local_datasource.roomDB.dao.TvShowDao
//import com.baghdad.local_datasource.roomDB.entity.SearchQuery
//import com.baghdad.repository.logger.Logger
//import com.baghdad.repository.model.SearchQueryDto
//import com.baghdad.repository.model.SearchQueryDto.MediaType
//import io.mockk.Runs
//import io.mockk.coEvery
//import io.mockk.coVerify
//import io.mockk.coVerifySequence
//import io.mockk.just
//import io.mockk.mockk
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.test.runTest
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//
//@ExperimentalCoroutinesApi
//class LocalSearchQueryDataSourceImplTest {
//
//    lateinit var searchQueryDao: SearchQueryDao
//    lateinit var movieDao: MovieDao
//    lateinit var tvShowDao: TvShowDao
//    lateinit var actorDao: ActorDao
//    lateinit var logger: Logger
//
//    lateinit var localSearchQueryDataSource: LocalSearchQueryDataSourceImpl
//
//    @BeforeEach
//    fun setUp() {
//        searchQueryDao = mockk()
//        movieDao = mockk()
//        tvShowDao = mockk()
//        actorDao = mockk()
//        logger = mockk()
//
//        localSearchQueryDataSource = LocalSearchQueryDataSourceImpl(
//            searchQueryDao,
//            movieDao,
//            tvShowDao,
//            actorDao,
//            logger
//        )
//    }
//
//    @Test
//    fun `addSearchQuery should insert a single query into database`() = runTest {
//        // Given
//        coEvery {
//            searchQueryDao.addSearchQuery(
//                match { it.queryName == "test" && it.mediaId == 1L && it.mediaType == "MOVIE" })
//        } just Runs
//
//        coEvery { logger.logException(any()) } just Runs
//
//        // When
//        localSearchQueryDataSource.addSearchQuery(SEARCH_QUERY_DTO)
//
//        // Then
//        coVerify {
//            searchQueryDao.addSearchQuery(match {
//                it.queryName == SEARCH_QUERY_DTO.queryName &&
//                        it.mediaId == SEARCH_QUERY_DTO.mediaId &&
//                        it.mediaType == SEARCH_QUERY_DTO.mediaType.name
//            })
//        }
//    }
//
//
//    @Test
//    fun `addSearchQueries should insert multiple queries into database`() = runTest {
//        // Given
//        coEvery {
//            searchQueryDao.addSearchQueries(match {
//                it.size == SEARCH_QUERIES_DTO.size &&
//                        it.zip(SEARCH_QUERIES_DTO).all { (local, original) ->
//                            local.queryName == original.queryName &&
//                                    local.mediaId == original.mediaId &&
//                                    local.mediaType == original.mediaType.name
//                        }
//            })
//        } just Runs
//        coEvery { logger.logException(any()) } just Runs
//
//        // When
//        localSearchQueryDataSource.addSearchQueries(SEARCH_QUERIES_DTO)
//
//        // Then
//        coVerify { searchQueryDao.addSearchQueries(any()) }
//    }
//
//    @Test
//    fun `getInvalidSearchQueries should return list of outdated queries`() = runTest {
//        // Given
//        coEvery { searchQueryDao.getInvalidSearchQueries(TIME_STAMP) } returns INVALID_SEARCH_QUERIES
//        coEvery { logger.logException(any()) } just Runs
//
//        // When
//        val result = localSearchQueryDataSource.getInvalidSearchQueries(TIME_STAMP)
//
//        // Then
//        coVerify { searchQueryDao.getInvalidSearchQueries(TIME_STAMP) }
//        assert(result.size == INVALID_SEARCH_QUERIES.size)
//    }
//
//    @Test
//    fun `getAllSearchQueries should return all queries from database`() = runTest {
//        // Given
//        coEvery { searchQueryDao.getAllSearchQueries() } returns INVALID_SEARCH_QUERIES
//        coEvery { logger.logException(any()) } just Runs
//
//        // When
//        val result = localSearchQueryDataSource.getAllSearchQueries()
//
//        // Then
//        coVerify { searchQueryDao.getAllSearchQueries() }
//        assert(result.size == INVALID_SEARCH_QUERIES.size)
//    }
//
//    @Test
//    fun `getSearchByQuery should return matching queries by name and type`() = runTest {
//        // Given
//        coEvery {
//            searchQueryDao.getSearchByQuery(
//                queryName = "test",
//                mediaType = "MOVIE"
//            )
//        } returns INVALID_SEARCH_QUERIES
//        coEvery { logger.logException(any()) } just Runs
//
//        // When
//        localSearchQueryDataSource.getSearchByQuery(
//            query = "test",
//            type = MediaType.MOVIE
//        )
//
//        // Then
//        coVerify {
//            searchQueryDao.getSearchByQuery(
//                queryName = "test",
//                mediaType = "MOVIE"
//            )
//        }
//    }
//
//    @Test
//    fun `deleteInvalidSearchQueries should remove related media and outdated queries`() = runTest {
//        // Given
//        coEvery { searchQueryDao.getInvalidSearchQueries(TIME_STAMP) } returns INVALID_SEARCH_QUERIES
//        coEvery { movieDao.deleteMovieById(any()) } just Runs
//        coEvery { tvShowDao.deleteTvShowByID(any()) } just Runs
//        coEvery { actorDao.deleteActorById(any()) } just Runs
//        coEvery { searchQueryDao.deleteInvalidSearchQueries(TIME_STAMP) } just Runs
//        coEvery { logger.logException(any()) } just Runs
//
//        // When
//        localSearchQueryDataSource.deleteInvalidSearchQueries(TIME_STAMP)
//
//        // Then
//        coVerifySequence {
//            searchQueryDao.getInvalidSearchQueries(TIME_STAMP)
//            movieDao.deleteMovieById(2)
//            movieDao.deleteMovieById(3)
//            movieDao.deleteMovieById(4)
//            movieDao.deleteMovieById(5)
//            movieDao.deleteMovieById(6)
//            movieDao.deleteMovieById(7)
//            searchQueryDao.deleteInvalidSearchQueries(TIME_STAMP)
//        }
//    }
//
//
//    @Test
//    fun `deleteAllSearchQueries should clear the database`() = runTest {
//        // Given
//        coEvery { searchQueryDao.deleteAllSearchQueries() } just Runs
//        coEvery { logger.logException(any()) } just Runs
//
//        // When
//        localSearchQueryDataSource.deleteAllSearchQueries()
//
//        // Then
//        coVerify(exactly = 1) { searchQueryDao.deleteAllSearchQueries() }
//    }
//
//    @Test
//    fun `deleteInvalidSearchQueries should handle TV_SHOW and ACTOR`() = runTest {
//        // Given
//        val queries = listOf(
//            INVALID_SEARCH_QUERY.copy(mediaType = "TV_SHOW", mediaId = 100L),
//            INVALID_SEARCH_QUERY.copy(mediaType = "ACTOR", mediaId = 200L)
//        )
//        coEvery { searchQueryDao.getInvalidSearchQueries(TIME_STAMP) } returns queries
//        coEvery { tvShowDao.deleteTvShowByID(any()) } just Runs
//        coEvery { actorDao.deleteActorById(any()) } just Runs
//        coEvery { searchQueryDao.deleteInvalidSearchQueries(any()) } just Runs
//        coEvery { logger.logException(any()) } just Runs
//
//        // When
//        localSearchQueryDataSource.deleteInvalidSearchQueries(TIME_STAMP)
//
//        // Then
//        coVerifySequence {
//            searchQueryDao.getInvalidSearchQueries(TIME_STAMP)
//            tvShowDao.deleteTvShowByID(100)
//            actorDao.deleteActorById(200)
//            searchQueryDao.deleteInvalidSearchQueries(TIME_STAMP)
//        }
//    }
//
//
//    companion object {
//
//        val SEARCH_QUERY_DTO = SearchQueryDto(
//            queryName = "test",
//            mediaId = 1L,
//            mediaType = MediaType.MOVIE
//        )
//
//        val SEARCH_QUERIES_DTO = listOf(
//            SEARCH_QUERY_DTO.copy(queryName = "test2", mediaId = 2L),
//            SEARCH_QUERY_DTO.copy(queryName = "test3", mediaId = 3L),
//            SEARCH_QUERY_DTO.copy(queryName = "test4", mediaId = 4L),
//            SEARCH_QUERY_DTO.copy(queryName = "test5", mediaId = 5L),
//            SEARCH_QUERY_DTO.copy(queryName = "test6", mediaId = 6L),
//            SEARCH_QUERY_DTO.copy(queryName = "test7", mediaId = 7L),
//        )
//
//        const val TIME_STAMP = 1L
//        val INVALID_SEARCH_QUERY =
//            SearchQuery(
//                queryName = "test",
//                mediaId = 1L,
//                mediaType = "MOVIE",
//                timeStamp = TIME_STAMP
//            )
//        val INVALID_SEARCH_QUERIES = listOf(
//            INVALID_SEARCH_QUERY.copy(queryName = "test2", mediaId = 2L),
//            INVALID_SEARCH_QUERY.copy(queryName = "test3", mediaId = 3L),
//            INVALID_SEARCH_QUERY.copy(queryName = "test4", mediaId = 4L),
//            INVALID_SEARCH_QUERY.copy(queryName = "test5", mediaId = 5L),
//            INVALID_SEARCH_QUERY.copy(queryName = "test6", mediaId = 6L),
//            INVALID_SEARCH_QUERY.copy(queryName = "test7", mediaId = 7L)
//        )
//
//    }
//}