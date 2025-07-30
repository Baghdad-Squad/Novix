package com.baghdad.local_datasource.dataStore.user

import com.baghdad.repository.model.UserDto
import com.example.application.proto.User

fun User.toDto(): UserDto {
    return UserDto(
        imageUrl = this.imageUrl,
        id = this.id,
        userName = this.userName
    )
}