package com.baghdad.viewmodel.profile

import com.baghdad.domain.model.profile.ContentRestrictionTypes
import com.baghdad.entity.user.User

fun User.toUIState(): ProfileScreenState.User {
    return ProfileScreenState.User(
        userName = userName,
        imageUrl = imageUrl
    )
}

fun ContentRestrictionTypes.toUiState(): ContentRestriction {
    return when (this) {
        ContentRestrictionTypes.STRICT -> ContentRestriction.STRICT
        ContentRestrictionTypes.MODERATE -> ContentRestriction.MODERATE
        ContentRestrictionTypes.NONE -> ContentRestriction.NONE
    }
}

fun ContentRestriction.toDomainModel(): ContentRestrictionTypes {
    return when (this) {
        ContentRestriction.STRICT -> ContentRestrictionTypes.STRICT
        ContentRestriction.MODERATE -> ContentRestrictionTypes.MODERATE
        ContentRestriction.NONE -> ContentRestrictionTypes.NONE
    }
}