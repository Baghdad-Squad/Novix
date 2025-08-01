package com.baghdad.local_datasource

import com.baghdad.local_datasource.dataStore.user.toDto
import com.example.application.proto.User
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class UserMapperTest {

    @Test
    fun `should correctly map user to UserDto when toDto is called`() {
        // Given
        val user = User.newBuilder()
            .setId(101L)
            .setUserName("RogueCoder")
            .setImageUrl("https://avatar.com/rogue.png")
            .build()
        val dto = user.toDto()

        // When & Then
        assertThat(user.id).isEqualTo(dto.id)
        assertThat(user.imageUrl).isEqualTo(dto.imageUrl)
        assertThat(user.userName).isEqualTo(dto.userName)
    }

}