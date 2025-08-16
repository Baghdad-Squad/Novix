package com.baghdad.localDatasource.mapper

import com.baghdad.repository.model.UserDto
import com.example.application.proto.User

fun User.toDto(): UserDto {
    return UserDto(
        imageUrl = this.imageUrl.orEmpty(),
        id = this.id,
        userName = this.userName
    )
}