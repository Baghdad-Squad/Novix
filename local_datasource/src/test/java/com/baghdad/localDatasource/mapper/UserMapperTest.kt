package com.baghdad.localDatasource.mapper

import com.baghdad.repository.model.UserDto
import com.example.application.proto.User
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class UserMapperTest {

    @Test
    fun `should map User proto to UserDto correctly`() {
        val result = userProto.toDto()

        assertThat(result).isEqualTo(userDto)
    }

    companion object {
        private val userProto = User.newBuilder()
            .setId(123L)
            .setUserName("Alice")
            .setImageUrl("http://example.com/avatar.png")
            .build()

        private val userDto = UserDto(
            id = 123L,
            userName = "Alice",
            imageUrl = "http://example.com/avatar.png"
        )
    }
}