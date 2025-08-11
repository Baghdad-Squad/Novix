package com.baghdad.repository.mapper

import com.baghdad.entity.User
import com.baghdad.repository.model.UserDto

fun UserDto.toEntity(): User {
    return User(
        id = id,
        userName = userName,
        imageUrl = imageUrl.orEmpty()
    )
}