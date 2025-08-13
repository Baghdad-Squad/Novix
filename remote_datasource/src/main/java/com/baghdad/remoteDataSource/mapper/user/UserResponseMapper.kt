package com.baghdad.remoteDataSource.mapper.user

import com.baghdad.remoteDataSource.response.user.UserResponse
import com.baghdad.remoteDataSource.util.getImageUrlFromPath
import com.baghdad.repository.model.UserDto

fun UserResponse.toDto(): UserDto {
    return UserDto(
        id = id ?: -1,
        userName = userName.orEmpty(),
        imageUrl = getImageUrlFromPath(imageUrl?.tmdb?.avatarPath),
    )
}