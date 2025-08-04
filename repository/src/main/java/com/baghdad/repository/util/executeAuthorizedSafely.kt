package com.baghdad.repository.util

import com.baghdad.domain.exception.LocalDataBaseException
import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.exception.UnAuthorizedException
import com.baghdad.domain.exception.UnknownException
import com.baghdad.repository.exception.DatabaseException
import com.baghdad.repository.exception.NetworkException
import com.baghdad.repository.exception.NoInternetNetworkException
import com.baghdad.repository.exception.RequestTimeoutNetworkException
import com.baghdad.repository.exception.SerializationNetworkException
import com.baghdad.repository.exception.ServerNetworkException
import com.baghdad.repository.exception.StorageFullException
import com.baghdad.repository.exception.TooManyRequestsNetworkException
import com.baghdad.repository.exception.UnknownNetworkException
import com.baghdad.domain.exception.StorageFullException as DomainStorageFullException

suspend fun <T> executeAuthorizedSafely(
    sessionId: String?,
    block: suspend () -> T,
): T {
    if (sessionId == null) throw UnAuthorizedException()
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
