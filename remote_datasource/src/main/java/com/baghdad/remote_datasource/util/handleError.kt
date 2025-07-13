package com.baghdad.remote_datasource.util
import com.baghdad.repository.exception.NetworkException
import com.baghdad.repository.exception.NoInternetNetworkException
import com.baghdad.repository.exception.RequestTimeoutNetworkException
import com.baghdad.repository.exception.SerializationNetworkException
import com.baghdad.repository.exception.ServerNetworkException
import com.baghdad.repository.exception.TooManyRequestsNetworkException
import com.baghdad.repository.exception.UnknownNetworkException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import io.ktor.utils.io.errors.IOException
import kotlinx.serialization.SerializationException


suspend inline fun <reified T> handleRequest(
    client: HttpClient,
    url: String,
    params: Map<String, String> = emptyMap(),
): T {
    val response = try {
        client.get(url) {
            configureRequest(params)
        }
    }catch (e: Throwable) {
        throw mapToNetworkException(e)
    }

    return when {
        response.status.isSuccess() -> {
            try {
                response.body<T>()
            } catch (_: IOException) {
                throw SerializationNetworkException()
            }
        }
        response.status == HttpStatusCode.RequestTimeout -> {
            throw RequestTimeoutNetworkException()
        }
        response.status == HttpStatusCode.TooManyRequests -> {
            throw TooManyRequestsNetworkException()
        }
        response.status.value in 500..599 -> {
            throw ServerNetworkException()
        }
        else -> {
            throw UnknownNetworkException()
        }
    }
}


fun HttpRequestBuilder.configureRequest(
    params: Map<String, String>,
) {
    params.forEach { (key, value) ->
        parameter(key, value)
    }
}


fun mapToNetworkException(e: Throwable): NetworkException {
    return when (e) {
        is IOException -> NoInternetNetworkException()
        is SerializationException -> SerializationNetworkException()
        else -> UnknownNetworkException()
    }
}