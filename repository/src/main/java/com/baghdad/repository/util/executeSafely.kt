package com.baghdad.repository.util

import com.baghdad.domain.exception.LocalDataBaseException
import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.exception.UnknownException
import com.baghdad.domain.model.PagedResult
import com.baghdad.repository.exception.DatabaseException
import com.baghdad.repository.exception.NetworkException
import com.baghdad.repository.exception.NoInternetNetworkException
import com.baghdad.repository.exception.RequestTimeoutNetworkException
import com.baghdad.repository.exception.SerializationNetworkException
import com.baghdad.repository.exception.ServerNetworkException
import com.baghdad.repository.exception.StorageFullException
import com.baghdad.repository.exception.TooManyRequestsNetworkException
import com.baghdad.repository.exception.UnknownNetworkException
import com.baghdad.repository.model.PagedResultDto
import kotlinx.coroutines.flow.Flow
import com.baghdad.domain.exception.StorageFullException as DomainStorageFullException

suspend fun <T> executeSafely(block: suspend () -> T): T {
    return try {
        block()
    } catch (_: NoInternetNetworkException) {
        throw NoInternetException()
    } catch (_: SerializationNetworkException) {
        throw NetworkException()
    } catch (_: RequestTimeoutNetworkException) {
        throw NetworkException()
    } catch (_: TooManyRequestsNetworkException) {
        throw NetworkException()
    } catch (_: ServerNetworkException) {
        throw NetworkException()
    } catch (_: StorageFullException) {
        throw DomainStorageFullException()
    } catch (_: UnknownNetworkException) {
        throw NetworkException()
    } catch (_: DatabaseException) {
        throw LocalDataBaseException()
    } catch (_: Exception) {
        throw UnknownException()
    }
}


fun <T> getFlowSafely(block: () -> Flow<T>): Flow<T> {
    return try {
        block()
    } catch (_: StorageFullException) {
        throw DomainStorageFullException()
    } catch (_: Exception) {
        throw LocalDataBaseException()
    }
}

suspend fun <TEntity, TDto> getPagedSafely(
    page: Int,
    pageSize: Int = 20,
    onStart: (suspend () -> Unit)? = null,
    getCachedPage: suspend (Int, Int) -> List<TDto>,
    getRemoteData: suspend (Int, Int) -> PagedResultDto<TDto>,
    cacheData: suspend (List<TDto>) -> Unit,
    mapToEntity: (TDto) -> TEntity
): PagedResult<TEntity> = executeSafely {
    onStart?.invoke()
    val localData = getCachedPage(page, pageSize)
    if (localData.isNotEmpty()) {
        PagedResult(
            data = localData.map(mapToEntity),
            nextKey = if (localData.size == pageSize) page + 1 else null,
            prevKey = if (page > 1) page - 1 else null
        )
    } else {
        val remoteData = getRemoteData(page, pageSize)
        if (remoteData.data.isNotEmpty()) {
            cacheData(remoteData.data)
            val localData = getCachedPage(page, pageSize)
            PagedResult(
                data = localData.map(mapToEntity),
                nextKey = remoteData.nextKey,
                prevKey = remoteData.prevKey
            )
        } else {
            PagedResult(
                data = emptyList(),
                nextKey = null,
                prevKey = if (page > 1) page - 1 else null
            )
        }
    }
}

suspend fun <TEntity, TDto> getRemotePagedSafely(
    page: Int,
    pageSize: Int = 20,
    onStart: (suspend () -> Unit)? = null,
    getRemoteData: suspend (Int, Int) -> PagedResultDto<TDto>,
    mapToEntity: (TDto) -> TEntity
): PagedResult<TEntity> = executeSafely {
    onStart?.invoke()

    val remoteData = getRemoteData(page, pageSize)

    PagedResult(
        data = remoteData.data.map(mapToEntity),
        nextKey = remoteData.nextKey,
        prevKey = remoteData.prevKey
    )
}


suspend fun <TEntity, TDto> getLocalPagedSafely(
    page: Int,
    pageSize: Int = 20,
    onStart: (suspend () -> Unit)? = null,
    getCachedPage: suspend (Int, Int) -> List<TDto>,
    mapToEntity: (TDto) -> TEntity
): PagedResult<TEntity> = executeSafely {
    onStart?.invoke()
    val localData = getCachedPage(page, pageSize)
    PagedResult(
        data = localData.map(mapToEntity),
        nextKey = if (localData.size == pageSize) page + 1 else null,
        prevKey = if (page > 1) page - 1 else null
    )
}
