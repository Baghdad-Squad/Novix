package com.baghdad.local_datasource.roomDB.errorHandler

import android.database.sqlite.SQLiteDatabaseCorruptException
import android.database.sqlite.SQLiteFullException
import com.baghdad.repository.exception.DatabaseCorruptException
import com.baghdad.repository.exception.DatabaseException
import com.baghdad.repository.exception.StorageFullException
import com.baghdad.repository.logger.Logger
import kotlinx.coroutines.flow.Flow


suspend fun <T> executeWithErrorHandling(logger: Logger, block: suspend () -> T): T {
    return try {
        block()
    } catch (e: SQLiteFullException) {
        logger.logException(e)
        throw StorageFullException(e.message)
    } catch (e: SQLiteDatabaseCorruptException) {
        logger.logException(e)
        throw DatabaseCorruptException(e.message)
    } catch (e: Exception) {
        logger.logException(e)
        throw DatabaseException(e.message)
    }
}

fun <T> executeFlowWithErrorHandling(logger: Logger, block: () -> Flow<T>): Flow<T> {
    return try {
        block()
    } catch (e: SQLiteFullException) {
        logger.logException(e)
        throw StorageFullException(e.message)
    } catch (e: SQLiteDatabaseCorruptException) {
        logger.logException(e)
        throw DatabaseCorruptException(e.message)
    } catch (e: Exception) {
        logger.logException(e)
        throw DatabaseException(e.message)
    }
}

