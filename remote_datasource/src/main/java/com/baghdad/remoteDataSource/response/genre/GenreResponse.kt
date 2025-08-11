package com.baghdad.remoteDataSource.response.genre

import com.google.gson.annotations.SerializedName
data class GenreListResponse(
    @SerializedName("genres") val genres: List<GenreItemDto>,
) {
    data class GenreItemDto(
        @SerializedName("id") val id: Long? = null,
        @SerializedName("name") val name: String,
    )
}
