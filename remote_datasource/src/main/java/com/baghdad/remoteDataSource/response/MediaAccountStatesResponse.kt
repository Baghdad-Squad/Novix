package com.baghdad.remoteDataSource.response

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName

data class MediaAccountStatesResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("rated") val rated: JsonElement?,
) {
    val ratedValue: Int?
        get() = when {
            rated?.isJsonObject == true -> rated.asJsonObject.get("value")?.asInt
            else -> null
        }
}