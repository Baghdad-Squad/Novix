package com.baghdad.remoteDataSource.response.mediaAccount

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class MediaAccountStatesResponse(
    @SerializedName("id") val id: Long? = null,
    @SerializedName("rated") val rated: JsonElement? = null
) {
    val ratedValue: Int?
        get() = (rated as? JsonObject)?.get("value")?.asInt
}