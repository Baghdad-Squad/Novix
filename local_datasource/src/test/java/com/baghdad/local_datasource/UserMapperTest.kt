package com.baghdad.local_datasource

import com.baghdad.local_datasource.dataStore.user.toDto
import com.example.application.proto.User
import org.junit.Assert.*
import org.junit.jupiter.api.Test

class UserMapperTest {

    @Test
    fun `toDto should correctly map User to UserDto`() {
        val user = User.newBuilder()
            .setId(101L)
            .setUserName("RogueCoder")
            .setImageUrl("https://avatar.com/rogue.png")
            .build()

        val dto = user.toDto()

        assertEquals(user.id, dto.id)
        assertEquals(user.userName, dto.userName)
        assertEquals(user.imageUrl, dto.imageUrl)
    }

}