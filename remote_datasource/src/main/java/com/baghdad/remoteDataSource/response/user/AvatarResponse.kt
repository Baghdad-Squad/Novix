package com.baghdad.remoteDataSource.response.user

import com.google.gson.annotations.SerializedName


data class AvatarResponse(
    @SerializedName("gravatar")
    val gravatar: GravatarResponse? = null,
    @SerializedName("tmdb")
    val tmdb: TmdbResponse? = null,
)


data class GravatarResponse(
    @SerializedName("hash")
    val hash: String? = null
)


data class TmdbResponse(
    @SerializedName("avatar_path")
    val avatarPath: String? = null
)