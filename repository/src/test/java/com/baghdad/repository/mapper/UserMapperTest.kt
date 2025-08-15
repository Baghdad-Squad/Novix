package com.baghdad.repository.mapper

import com.baghdad.entity.user.User
import com.baghdad.repository.dummyData.DummyDataFactory.DummyDataFactory.USER_DTO
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class UserMapperTest {
    @Test
    fun `should map UserDto to entity when enter correctly`() {
        val excepted = User(
            id = USER_DTO.id,
            userName = USER_DTO.userName,
            imageUrl = USER_DTO.imageUrl.orEmpty()
        )

        val result = USER_DTO.toEntity()

        assertThat(result).isEqualTo(excepted)
    }

    @Test
    fun `should map UserDto id to entity id when enter correctly`() {
        val result = USER_DTO.toEntity()

        assertThat(result.id).isEqualTo(USER_DTO.id)
    }

    @Test
    fun `should map UserDto userName to entity userName when enter correctly`() {
        val result = USER_DTO.toEntity()

        assertThat(result.userName).isEqualTo(USER_DTO.userName)
    }

    @Test
    fun `should map UserDto imageUrl to entity imageUrl when enter correctly`() {
        val result = USER_DTO.toEntity()

        assertThat(result.imageUrl).isEqualTo(USER_DTO.imageUrl)
    }
}