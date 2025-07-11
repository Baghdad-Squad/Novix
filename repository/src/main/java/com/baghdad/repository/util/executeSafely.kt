package com.baghdad.repository.util

import com.baghdad.domain.exception.LocalDataBaseException
import com.baghdad.domain.exception.RequestTimeoutException
import com.baghdad.repository.exception.DatabaseException
import com.baghdad.repository.exception.NetworkException
import kotlinx.coroutines.flow.Flow

suspend fun <T> executeSafely(block: suspend () -> T): T {
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
