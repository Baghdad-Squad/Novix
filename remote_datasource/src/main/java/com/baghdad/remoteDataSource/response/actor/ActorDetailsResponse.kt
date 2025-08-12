package com.baghdad.remoteDataSource.response.actor

import com.google.gson.annotations.SerializedName
data class ActorDetailsResponse(
    @SerializedName("id") val id: Long? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("biography") val biography: String? = null,
    @SerializedName("birthday") val birthday: String? = null,
    @SerializedName("deathday") val deathday: String? = null,
    @SerializedName("homepage") val homepage: String? = null,
    @SerializedName("known_for_department") val knownForDepartment: String? = null,
    @SerializedName("place_of_birth") val placeOfBirth: String? = null,
    @SerializedName("profile_path") val profilePath: String? = null
)



