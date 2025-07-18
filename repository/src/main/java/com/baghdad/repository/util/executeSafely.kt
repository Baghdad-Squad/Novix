package com.baghdad.repository.util

import android.util.Log
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
        Log.e("bla bla", e.message, e)
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
    getCachedTotalCount: suspend () -> Int,
    getRemoteTotalCount: suspend () -> Int,
    getCachedPage: suspend (Int, Int) -> List<TDto>,
    getRemoteData: suspend (Int, Int) -> PagedResultDto<TDto>,
    cacheData: suspend (List<TDto>) -> Unit,
    mapToEntity: (TDto) -> TEntity
): PagedResult<TEntity> {
    onStart?.invoke()
    val totalCachedCount = getCachedTotalCount()
    if (totalCachedCount >= (page * pageSize)) {
        val localData = getCachedPage(page, pageSize)
        if ((page * pageSize) < totalCachedCount) {
            return PagedResult(
                data = localData.map(mapToEntity),
                nextKey = if ((page * pageSize) < totalCachedCount) page + 1 else null,
                prevKey = if (page > 1) page - 1 else null
            )
        } else {
            val remoteTotalCount = getRemoteTotalCount()
            return PagedResult(
                data = localData.map(mapToEntity),
                nextKey = if (totalCachedCount < remoteTotalCount) page + 1 else null,
                prevKey = if (page > 1) page - 1 else null
            )
        }
    } else {
        val remoteTotalCount = getRemoteTotalCount()
        val remoteData = getRemoteData(page, pageSize)
        if (totalCachedCount < remoteTotalCount) {
            cacheData(remoteData.data)
        }
        val localData = getCachedPage(page, pageSize)
        return if ((page * pageSize) < totalCachedCount) {
            PagedResult(
                data = localData.map(mapToEntity),
                nextKey = if ((page * pageSize) < totalCachedCount) page + 1 else null,
                prevKey = if (page > 1) page - 1 else null
            )
        } else {
            Log.d(
                "testExecutePagedCachedOperation",
                "Remote total count: $remoteTotalCount, Cached total count: $totalCachedCount"
            )
            PagedResult(
                data = localData.map(mapToEntity),
                nextKey = if (totalCachedCount < remoteTotalCount) page + 1 else null,
                prevKey = if (page > 1) page - 1 else null
            )
        }
    }
}