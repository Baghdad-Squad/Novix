package com.baghdad.local_datasource.dataStore.user

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.example.application.proto.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream


object UserSerializer : Serializer<User> {

    override val defaultValue: User
        get() = User.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): User = withContext(Dispatchers.IO) {
        runCatching {
            input.use { it.readUser() }
        }.getOrElse { exception ->
            throw CorruptionException(ERROR_READ_FAILED, exception)
        }
    }

    override suspend fun writeTo(user: User, output: OutputStream) = withContext(Dispatchers.IO) {
        runCatching {
            output.use { it.writeUser(user) }
        }.getOrElse { exception ->
            throw CorruptionException(ERROR_WRITE_FAILED, exception)
        }
    }

    private fun InputStream.readUser(): User = User.parseFrom(this)

    private fun OutputStream.writeUser(user: User) = user.writeTo(this)

    private const val ERROR_READ_FAILED = "Failed to read user data"
    private const val ERROR_WRITE_FAILED = "Failed to write user data"
}
