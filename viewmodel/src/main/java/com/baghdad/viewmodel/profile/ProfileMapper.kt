package com.baghdad.viewmodel.profile

import com.baghdad.entity.user.User

fun User.toUIState(): ProfileScreenState.User {
    return ProfileScreenState.User(
        userName = userName,
        imageUrl = imageUrl
    )
}