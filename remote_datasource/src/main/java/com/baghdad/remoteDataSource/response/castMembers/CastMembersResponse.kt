package com.baghdad.remoteDataSource.response.castMembers

import com.google.gson.annotations.SerializedName

data class CastMembersResponse(
    @SerializedName("id") val id: Long? = null,
    @SerializedName("cast") val cast: List<CastMemberResponse>? = null,
) {
    data class CastMemberResponse(
        @SerializedName("id") val id: Long? = null,
        @SerializedName("name") val name: String? = null,
        @SerializedName("profile_path") val profilePath: String? = null,
        @SerializedName("known_for_department") val knownForDepartment: String? = null,
        @SerializedName("character") val character: String? = null
    )
}