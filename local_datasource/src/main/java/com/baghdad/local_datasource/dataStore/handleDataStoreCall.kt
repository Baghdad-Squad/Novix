package com.baghdad.local_datasource.dataStore

import androidx.datastore.core.CorruptionException
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
        throw DatabaseException(e.toString())
    } catch (e: CorruptionException) {
        logger.logException(e)
        throw DatabaseException(e.toString())
    } catch (e: Exception) {
        logger.logException(e)
        throw e
    }
}