package com.baghdad.remoteDataSource.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class CastMembersResponse(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("cast")
    val cast: List<CastMemberResponse>? = null,
)

@Serializable
data class CastMemberResponse(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("profile_path")
    val profilePath: String? = null,
    @SerialName("known_for_department")
    val knownForDepartment: String? = null,
    @SerialName("character")
    val character: String? = null
)

