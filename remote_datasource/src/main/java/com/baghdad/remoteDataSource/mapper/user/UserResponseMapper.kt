package com.baghdad.remoteDataSource.mapper.user

import com.baghdad.remoteDataSource.response.user.UserResponse
import com.baghdad.repository.model.UserDto

fun UserResponse.toDto(): UserDto {
    return UserDto(
        id = id?: 0L,
        userName = userName?: "",
        imageUrl = imageUrl.toString()
    )
}