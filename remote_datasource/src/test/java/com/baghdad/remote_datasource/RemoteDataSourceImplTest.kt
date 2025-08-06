//package com.baghdad.remote_datasource
//
//import com.baghdad.remote_datasource.entity.SearchParameter
//import com.baghdad.remote_datasource.entity.SearchResponse
//import com.baghdad.remote_datasource.entity.toParams
//import com.baghdad.remote_datasource.util.configureRequest
//import com.baghdad.remote_datasource.util.handleRequest
//import com.baghdad.repository.datasource.remote.RemoteSearchDataSource
//import com.baghdad.repository.model.ActorDto
//import com.baghdad.repository.model.MovieDto
//import com.baghdad.repository.model.TvShowDto
//import io.ktor.client.HttpClient
//import io.ktor.client.request.get
//import io.ktor.client.statement.HttpResponse
//import io.ktor.http.HttpStatusCode
//import io.mockk.coEvery
//import io.mockk.coVerify
//import io.mockk.every
//import io.mockk.mockk
//import io.mockk.mockkStatic
//import io.mockk.unmockkAll
//import kotlinx.coroutines.test.runTest
//import org.junit.jupiter.api.AfterEach
//import org.junit.jupiter.api.Assertions.assertEquals
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//
//class RemoteDataSourceImplTest {
//
//    private lateinit var httpClient: HttpClient
//    private lateinit var remoteDataSource: RemoteSearchDataSource
//    private val apiKey = "api_key"
//    private val baseUrl = "base_url"
//
//    @BeforeEach
//    fun setup() {
//        httpClient = mockk(relaxed = true)
//        remoteDataSource = RemoteSearchDataSource(httpClient, apiKey, baseUrl, mockk())
//    }
//
//    @AfterEach
//    fun tearDown() {
//        unmockkAll()
//    }
//
//    @Test
//    fun `searchMultiResults should handle empty query`() = runTest {
//        // Given
//        val query = "aboud"
//        val pageNumber = 1
//        val expectedParams = SearchParameter(
//            query = query,
//            pageNumber = pageNumber,
//            language = "en-US",
//            includeAdult = false
//        )
//        val mockSearchResponse = SearchResponse(
//            pageNumber = 1,
//            results = emptyList(),
//            totalPages = 0,
//            totalResults = 0
//        )
//        val statusCode = HttpStatusCode.OK
//        coEvery {
//            handleRequest<SearchResponse>(any(), any(), any(), any())
//        } returns mockSearchResponse
//        coEvery {
//            httpClient.get(any<String>()) {
//                configureRequest(expectedParams.toParams(), apiKey)
//            }
//        }
//
//
//        // When
//        val result = remoteDataSource.searchMultiResults(query, pageNumber)
//
//        // Then
//        assertEquals(emptyList<ActorDto>(), result.actors)
//        assertEquals(emptyList<MovieDto>(), result.movies)
//        assertEquals(emptyList<TvShowDto>(), result.tvShows)
//        coVerify {
//            handleRequest<SearchResponse>(
//                client = httpClient,
//                url = "$baseUrl/search/multi",
//                params = expectedParams.toParams(),
//                apiKey = apiKey
//            )
//        }
//    }
//}
//
