package com.baghdad.repository.util

import com.baghdad.domain.exception.InValidUserCredentialException
import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.exception.UnKnownNetworkException
import com.baghdad.repository.exception.NetworkException
import com.baghdad.repository.exception.NoInternetNetworkException
import com.baghdad.repository.exception.RequestTimeoutNetworkException
import com.baghdad.repository.exception.SerializationNetworkException
import com.baghdad.repository.exception.ServerNetworkException
import com.baghdad.repository.exception.TooManyRequestsNetworkException
import com.baghdad.repository.exception.UnauthorizedNetworkException


suspend fun <T> executeLoginSafely(block: suspend () -> T): T {
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
    } catch (_: UnauthorizedNetworkException) {
        throw InValidUserCredentialException()
    } catch (_: ServerNetworkException) {
        throw NetworkException()
    } catch (_: Exception) {
        throw UnKnownNetworkException()
    }
}
