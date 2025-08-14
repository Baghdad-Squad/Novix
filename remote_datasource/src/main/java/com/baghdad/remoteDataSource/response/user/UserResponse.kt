package com.baghdad.remoteDataSource.response.user

import com.google.gson.annotations.SerializedName
data class UserResponse (
    @SerializedName("id") val id : Long? = null,
    @SerializedName("avatar") val imageUrl : AvatarResponse?= null,
    @SerializedName("username") val userName : String? = null,
){
    data class AvatarResponse(
        @SerializedName("gravatar") val gravatar: GravatarResponse? = null,
        @SerializedName("tmdb") val tmdb: TmdbResponse? = null,
    )
    data class GravatarResponse(
        @SerializedName("hash") val hash: String? = null,
    )
    data class TmdbResponse(
        @SerializedName("avatar_path") val avatarPath: String? = null,
    )
}