package com.baghdad.repository.exception

open class DatabaseException(message: String?) : Exception(message)

data class StorageFullException(override val message: String?) : DatabaseException(message)

data class DatabaseCorruptException(override val message: String?) : DatabaseException(message)
