package com.baghdad.local_datasource.database.errorHandler

import android.database.sqlite.SQLiteDatabaseCorruptException
import android.database.sqlite.SQLiteFullException
import com.baghdad.repository.exception.DatabaseCorruptException
import com.baghdad.repository.exception.DatabaseException
import com.baghdad.repository.exception.StorageFullException
import kotlinx.coroutines.flow.Flow


suspend fun <T> executeWithErrorHandling(block: suspend () -> T): T {
    return try {
        block()
    } catch (e: SQLiteFullException) {
        throw StorageFullException(e.message)
    } catch (e: SQLiteDatabaseCorruptException) {
        throw DatabaseCorruptException(e.message)
    } catch (e: Exception) {
        throw DatabaseException(e.message)
    }
}

fun <T> executeFlowWithErrorHandling(block: () -> Flow<T>): Flow<T> {
    return try {
        block()
    } catch (e: SQLiteFullException) {
        throw StorageFullException(e.message)
    } catch (e: SQLiteDatabaseCorruptException) {
        throw DatabaseCorruptException(e.message)
    } catch (e: Exception) {
        throw DatabaseException(e.message)
    }
}

