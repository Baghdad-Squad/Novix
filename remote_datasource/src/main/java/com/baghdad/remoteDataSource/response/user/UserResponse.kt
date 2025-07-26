package com.baghdad.remoteDataSource.response.user

import com.google.gson.annotations.SerializedName

data class UserResponse (
    @SerializedName("avatar")
    val imageUrl : AvatarResponse?= null,
    @SerializedName("id")
    val id : Long? = null,
    @SerializedName("username")
    val userName : String? = null,
)