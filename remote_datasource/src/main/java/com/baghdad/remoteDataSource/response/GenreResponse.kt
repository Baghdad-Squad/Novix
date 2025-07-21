package com.baghdad.remoteDataSource.response

import com.google.gson.annotations.SerializedName

data class GenreListResponse(
    @SerializedName("genres")
    val genres: List<GenreItemDto>
)

data class GenreItemDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)
