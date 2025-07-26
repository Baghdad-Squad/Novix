package com.baghdad.remoteDataSource.mapper.user

import com.baghdad.remoteDataSource.response.user.UserResponse
import com.baghdad.repository.model.UserDto

fun UserResponse.toDto(): UserDto {
    return UserDto(
        id = id ?: 0L,
        userName = userName.orEmpty(),
        imageUrl = "https://image.tmdb.org/t/p/w500" + imageUrl?.tmdb?.avatarPath.orEmpty()
    )
}