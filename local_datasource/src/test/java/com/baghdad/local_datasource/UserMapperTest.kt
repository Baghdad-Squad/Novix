package com.baghdad.local_datasource

import com.baghdad.local_datasource.dataStore.user.toDto
import com.example.application.proto.User
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class UserMapperTest {

    @Test
    fun `toDto should correctly map User to UserDto`() {
        // Given
        val user = User.newBuilder()
            .setId(101L)
            .setUserName("RogueCoder")
            .setImageUrl("https://avatar.com/rogue.png")
            .build()
        val dto = user.toDto()

        // When & Then
        Assertions.assertEquals(user.id, dto.id)
        Assertions.assertEquals(user.userName, dto.userName)
        Assertions.assertEquals(user.imageUrl, dto.imageUrl)
    }

}