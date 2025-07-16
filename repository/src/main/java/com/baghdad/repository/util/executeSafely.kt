package com.baghdad.repository.util

import com.baghdad.domain.exception.LocalDataBaseException
import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.exception.UnKnownNetworkException
import com.baghdad.domain.model.PagedResult
import com.baghdad.repository.exception.NetworkException
import com.baghdad.repository.exception.NoInternetNetworkException
import com.baghdad.repository.exception.RequestTimeoutNetworkException
import com.baghdad.repository.exception.SerializationNetworkException
import com.baghdad.repository.exception.ServerNetworkException
import com.baghdad.repository.exception.StorageFullException
import com.baghdad.repository.exception.TooManyRequestsNetworkException
import com.baghdad.repository.model.PagedResultDto
import kotlinx.coroutines.flow.Flow
import com.baghdad.domain.exception.StorageFullException as DomainStorageFullException

suspend fun <T> executeSafely(block: suspend () -> T): T {
    return try {
        block()
    } catch (e: NoInternetNetworkException) {
        throw NoInternetException()
    } catch (e: SerializationNetworkException) {
        throw NetworkException()
    } catch (e: RequestTimeoutNetworkException) {
        throw NetworkException()
    } catch (e: TooManyRequestsNetworkException) {
        throw NetworkException()
    } catch (e: ServerNetworkException) {
        throw NetworkException()
    } catch (e: Exception) {
        throw UnKnownNetworkException()
    }
}


fun <T> getFlowSafely(block: () -> Flow<T>): Flow<T> {
    return try {
        block()
    } catch (e: StorageFullException) {
        throw DomainStorageFullException()
    } catch (e: Exception) {
        throw LocalDataBaseException()
    }
}

suspend fun <TEntity, TDto> executePagedCachedOperation(
    page: Int,
    pageSize: Int = 20,
    onStart: (suspend () -> Unit)? = null,
    shouldFetchFromRemote: suspend () -> Boolean,
    fetchFromRemote: suspend () -> PagedResultDto<TDto>,
    cacheRemoteData: suspend (List<TDto>) -> Unit,
    fetchFromLocal: suspend () -> List<TDto>,
    getTotalCount: suspend () -> Int,
    mapToEntity: (TDto) -> TEntity
): PagedResult<TEntity> {
    return executeSafely {
        onStart?.invoke()

        val shouldFetch = shouldFetchFromRemote()

        var hasNextPage = false
        if (shouldFetch) {
            val response = fetchFromRemote()
            hasNextPage = response.nextKey != null
            cacheRemoteData(response.data)
        }

        val localData = fetchFromLocal()
        val totalCount = getTotalCount()

        hasNextPage = hasNextPage || (page * pageSize) < totalCount

        PagedResult(
            data = localData.map(mapToEntity),
            nextKey = if (hasNextPage) page + 1 else null,
            prevKey = if (page > 1) page - 1 else null
        )
    }
}