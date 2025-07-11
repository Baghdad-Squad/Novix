package com.baghdad.local_datasource.roomDB.errorHandler

import android.database.sqlite.SQLiteDatabaseCorruptException
import android.database.sqlite.SQLiteFullException
import com.baghdad.repository.exception.DatabaseCorruptException
import com.baghdad.repository.exception.DatabaseException
import com.baghdad.repository.exception.StorageFullException


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
