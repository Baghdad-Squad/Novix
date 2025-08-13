package com.baghdad.repository.model

import com.baghdad.entity.user.User

data class UserDto(
    val id: Long,
    val userName: String,
    val imageUrl: String? = null,
)

fun UserDto.toEntity(): User {
    return User(
        id = id,
        userName = userName,
        imageUrl = imageUrl.orEmpty()
    )
}