package com.baghdad.remoteDataSource.util

import com.baghdad.repository.exception.NetworkException
import com.baghdad.repository.exception.NoInternetNetworkException
import com.baghdad.repository.exception.RequestTimeoutNetworkException
import com.baghdad.repository.exception.SerializationNetworkException
import com.baghdad.repository.exception.ServerNetworkException
import com.baghdad.repository.exception.UnauthorizedNetworkException
import com.baghdad.repository.exception.UnknownNetworkException
import com.baghdad.repository.logger.Logger
import com.google.gson.stream.MalformedJsonException
import okio.IOException
import retrofit2.HttpException
import java.net.SocketTimeoutException

suspend fun <T> safeRetrofitCall(
    apiCall: suspend () -> T,
    logger: Logger
): T {
    return try {
        apiCall()
    } catch (e: SocketTimeoutException) {
        logger.logException(e)
        throw RequestTimeoutNetworkException()
    } catch (e: IOException) {
        logger.logException(e)
        throw NoInternetNetworkException()
    } catch (e: HttpException) {
        logger.logException(e)
        when (e.code()) {
            401 -> throw UnauthorizedNetworkException()
            in 500..599 -> throw ServerNetworkException()
            else -> throw NetworkException()
        }
    } catch (e: MalformedJsonException) {
        logger.logException(e)
        throw SerializationNetworkException()
    } catch (e: Exception) {
        logger.logException(e)

        throw UnknownNetworkException()
    }
}
