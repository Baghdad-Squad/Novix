package com.baghdad.remoteDataSource.util

import com.baghdad.repository.exception.NetworkException
import com.baghdad.repository.exception.NoInternetNetworkException
import com.baghdad.repository.exception.RequestTimeoutNetworkException
import com.baghdad.repository.exception.SerializationNetworkException
import com.baghdad.repository.exception.ServerNetworkException
import com.baghdad.repository.exception.TooManyRequestsNetworkException
import com.baghdad.repository.exception.UnauthorizedNetworkException
import com.baghdad.repository.exception.UnknownNetworkException
import com.baghdad.repository.logger.Logger
import io.ktor.utils.io.errors.IOException
import kotlinx.serialization.SerializationException
import retrofit2.Response
import java.net.HttpURLConnection

suspend inline fun <reified T> handleRequest(
    apiCall: suspend () -> Response<T>, logger: Logger
): T {
    return try {
        val response = apiCall()
        handleResponse(response, logger)
    } catch (e: Exception) {
        logger.logException(e)
        throw mapToNetworkException(e)
    }
}


fun mapToNetworkException(e: Throwable): NetworkException {
    return when (e) {
        is IOException -> NoInternetNetworkException()
        is SerializationException -> SerializationNetworkException()
        else -> UnknownNetworkException()
    }
}

fun <T> handleResponse(response: Response<T>, logger: Logger): T {
    return when {
        response.isSuccessful -> getResponseBodySafely(response, logger)
        response.code() == HttpURLConnection.HTTP_CLIENT_TIMEOUT -> throw RequestTimeoutNetworkException()
        response.code() == 429 -> throw TooManyRequestsNetworkException()
        response.code() == HttpURLConnection.HTTP_UNAUTHORIZED -> throw UnauthorizedNetworkException()
        response.code() in 500..599 -> throw ServerNetworkException()
        else -> throw UnknownNetworkException()
    }
}

fun <T> getResponseBodySafely(response: Response<T>, logger: Logger): T {
    return try {
        response.body() ?: throw NullPointerException("Response body is null")
    } catch (exception: Exception) {
        logger.logException(exception)
        throw SerializationNetworkException()
    }
}