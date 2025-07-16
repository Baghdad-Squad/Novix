package com.baghdad.repository.util

import com.baghdad.domain.exception.LocalDataBaseException
import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.exception.UnKnownNetworkException
import com.baghdad.repository.exception.NetworkException
import com.baghdad.repository.exception.NoInternetNetworkException
import com.baghdad.repository.exception.RequestTimeoutNetworkException
import com.baghdad.repository.exception.SerializationNetworkException
import com.baghdad.repository.exception.ServerNetworkException
import com.baghdad.repository.exception.StorageFullException
import com.baghdad.repository.exception.TooManyRequestsNetworkException
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
