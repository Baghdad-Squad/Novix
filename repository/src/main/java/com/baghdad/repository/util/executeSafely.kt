package com.baghdad.repository.util

import com.baghdad.domain.exception.LocalDataBaseException
import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.exception.RequestTimeoutException
import com.baghdad.domain.exception.SerializationException
import com.baghdad.domain.exception.ServerException
import com.baghdad.domain.exception.TooManyRequestsException
import com.baghdad.repository.exception.DatabaseCorruptException
import com.baghdad.repository.exception.DatabaseException
import com.baghdad.repository.exception.NetworkException
import com.baghdad.repository.exception.RequestTimeoutNetworkException
import com.baghdad.repository.exception.SerializationNetworkException
import com.baghdad.repository.exception.ServerNetworkException
import com.baghdad.repository.exception.StorageFullException
import com.baghdad.repository.exception.TooManyRequestsNetworkException
import com.baghdad.repository.exception.UnknownNetworkException
import kotlinx.coroutines.flow.Flow

suspend fun <T> executeSafely(block: suspend () -> T): T {
    return try {
        block()
    } catch (e: Exception) {
        when (e) {
            is SerializationNetworkException -> throw SerializationException()
            is RequestTimeoutNetworkException -> throw RequestTimeoutException()
            is TooManyRequestsNetworkException -> throw TooManyRequestsException()
            is ServerNetworkException -> throw ServerException()
            is UnknownNetworkException -> throw com.baghdad.domain.exception.NetworkException()
            is NoInternetException -> throw NoInternetException()
            is StorageFullException -> throw com.baghdad.domain.exception.StorageFullException()
            is DatabaseCorruptException -> throw com.baghdad.domain.exception.DataBaseCorruptException()
            else -> throw Exception()

        }

    }
}

fun <T> getFlowSafely(block: () -> Flow<T>): Flow<T> {
    return try {
        block()
    } catch (e: Exception) {
        when (e) {
            is NetworkException -> throw RequestTimeoutException()
            is DatabaseException -> throw LocalDataBaseException()
            else -> throw Exception()

        }

    }
}
