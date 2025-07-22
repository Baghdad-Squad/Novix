package com.baghdad.local_datasource.dataStore

import android.util.Log
import androidx.datastore.core.IOException
import com.baghdad.repository.exception.DatabaseException
import com.baghdad.repository.logger.Logger


suspend inline fun <T> safeDataStoreCall(
    block: suspend () -> T,
    logger: Logger
): T? {
    return try {
        block()
    } catch (e: IOException) {
        logger.logException(e)
        Log.i("datastore", e.toString())
        throw DatabaseException(e.toString()) // You could also log or report here
    } catch (e: Exception) {
        logger.logException(e)
        Log.i("datastore", e.toString())
        throw e
    }
}