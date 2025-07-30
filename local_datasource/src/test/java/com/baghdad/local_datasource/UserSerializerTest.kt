package com.baghdad.local_datasource

import com.baghdad.local_datasource.dataStore.user.UserSerializer
import com.example.application.proto.User
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class UserSerializerTest {

    @Test
    fun `should write user data correctly when UserSerializer is used`() = runTest {
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
        assertThat(outputStream.size() > 0).isTrue()
    }

    @Test
    fun `should read user data correctly when UserSerializer is used`() = runTest {
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
        assertThat(readUser).isEqualTo(user)
    }

    @Test
    fun `should return user default instance when defaultValue is called in UserSerializer`() {
        // When
        val defaultUser = UserSerializer.defaultValue

        // Then
        assertThat(defaultUser).isEqualTo(User.getDefaultInstance())
    }
}