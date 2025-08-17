package com.baghdad.repository.mapper

import com.baghdad.domain.model.profile.ContentRestrictionTypes
import com.baghdad.repository.model.ContentRestrictionTypesDto

fun ContentRestrictionTypesDto.toContentRestrictionType(): ContentRestrictionTypes {
    return when (this) {
        ContentRestrictionTypesDto.STRICT -> ContentRestrictionTypes.STRICT
        ContentRestrictionTypesDto.MODERATE -> ContentRestrictionTypes.MODERATE
        ContentRestrictionTypesDto.NONE -> ContentRestrictionTypes.NONE
    }
}

fun ContentRestrictionTypes.toContentRestrictionTypeDto(): ContentRestrictionTypesDto {
    return when (this) {
        ContentRestrictionTypes.STRICT -> ContentRestrictionTypesDto.STRICT
        ContentRestrictionTypes.MODERATE -> ContentRestrictionTypesDto.MODERATE
        ContentRestrictionTypes.NONE -> ContentRestrictionTypesDto.NONE
    }
}