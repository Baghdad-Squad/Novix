package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.mapper.people.toDto
import com.baghdad.remoteDataSource.response.people.PopularPeopleResponse
import com.baghdad.remoteDataSource.util.handleRequest
import com.baghdad.repository.datasource.remote.RemotePeopleDataSource
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.PeopleDto
import io.ktor.client.HttpClient

class RemotePeopleDataSourceImpl(
    private val httpClient: HttpClient,
    private val logger: Logger,
    private val baseUrl: String
) : RemotePeopleDataSource {

    override suspend fun getPopularPeople(page: Int): PagedResultDto<PeopleDto> {

        val response = handleRequest<PopularPeopleResponse>(
            client = httpClient,
            url = "$baseUrl$POPULAR_PEOPLE_ENDPOINT",
            logger = logger,
            params = mapOf("page" to "$page")
        )

        val nextKey = if (page < response.totalPages!!) page + 1 else null
        val prevKey = if (page > 1) page - 1 else null

        return PagedResultDto(
            data = response.results.map { it.toDto() },
            nextKey = nextKey,
            prevKey = prevKey
        )
    }

    companion object {
        private const val POPULAR_PEOPLE_ENDPOINT = "/trending/person/week"


    }
}