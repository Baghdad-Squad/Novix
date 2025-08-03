package com.baghdad.remoteDataSource.request

import com.google.gson.annotations.SerializedName

data class AddItemsRequest(
    @SerializedName("items") val items: List<MediaItem>
)

data class MediaItem(
    @SerializedName("media_type") val mediaType: String,
    @SerializedName("media_id") val mediaId: Long
)