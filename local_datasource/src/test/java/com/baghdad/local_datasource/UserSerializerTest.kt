package com.baghdad.local_datasource

import com.baghdad.local_datasource.dataStore.user.UserSerializer
import com.example.application.proto.User
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class UserSerializerTest {

    @Test
    fun `UserSerializer should write user data correctly`() = runTest {
        // Given
        val user = User.newBuilder()
            .setId(1L)
            .setUserName("Snapshot")
            .setImageUrl("https://avatar.com/snapshot.png")
            .build()

        // When
        val outputStream = ByteArrayOutputStream()
        UserSerializer.writeTo(user, outputStream)

        // Then
        Assertions.assertTrue(outputStream.size() > 0)
    }

    @Test
    fun `UserSerializer should read user data correctly`() = runTest {
        // Given
        val user = User.newBuilder()
            .setId(1L)
            .setUserName("Snapshot")
            .setImageUrl("https://avatar.com/snapshot.png")
            .build()
        val outputStream = ByteArrayOutputStream()
        user.writeTo(outputStream)

        // When
        val inputStream = ByteArrayInputStream(outputStream.toByteArray())
        val readUser = UserSerializer.readFrom(inputStream)

        // Then
        Assertions.assertEquals(user, readUser)
    }

    @Test
    fun `UserSerializer defaultValue should return User default instance`() {
        // When
        val defaultUser = UserSerializer.defaultValue

        // Then
        Assertions.assertEquals(User.getDefaultInstance(), defaultUser)
    }
}