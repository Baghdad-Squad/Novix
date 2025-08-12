package com.baghdad.localDatasource

import androidx.datastore.core.CorruptionException
import com.baghdad.localDatasource.serializer.UserSerializer
import com.example.application.proto.User
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class UserSerializerTest {

    @Test
    fun `writeTo should write user data correctly when UserSerializer is used`() = runTest {
        val outputStream = ByteArrayOutputStream()

        UserSerializer.writeTo(user, outputStream)

        assertThat(outputStream.size() > 0).isTrue()
    }

    @Test
    fun `readFrom should read user data correctly when UserSerializer is used`() = runTest {
        val outputStream = ByteArrayOutputStream()
        user.writeTo(outputStream)

        val inputStream = ByteArrayInputStream(outputStream.toByteArray())
        val readUser = UserSerializer.readFrom(inputStream)

        assertThat(readUser).isEqualTo(user)
    }

    @Test
    fun `should return user default instance when defaultValue is called in UserSerializer`() {
        val defaultUser = UserSerializer.defaultValue

        assertThat(defaultUser).isEqualTo(User.getDefaultInstance())
    }

    @Test
    fun `readFrom should throw CorruptionException when input stream is corrupted`() = runTest {
        val corruptedData = byteArrayOf(0xFF.toByte(), 0xFE.toByte(), 0xFD.toByte())
        val inputStream = ByteArrayInputStream(corruptedData)

        val exception = assertThrows<CorruptionException> {
            UserSerializer.readFrom(inputStream)
        }

        assertThat(exception.cause).isNotNull()
    }


    private companion object {
        val user: User = User.newBuilder()
            .setId(1L)
            .setUserName("Snapshot")
            .setImageUrl("https://avatar.com/snapshot.png")
            .build()
    }
}