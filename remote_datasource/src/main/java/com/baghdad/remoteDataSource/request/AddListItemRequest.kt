package com.baghdad.remoteDataSource.request

import com.google.gson.annotations.SerializedName

data class AddListItemRequest(
    @SerializedName("media_id") val mediaId: Long
)