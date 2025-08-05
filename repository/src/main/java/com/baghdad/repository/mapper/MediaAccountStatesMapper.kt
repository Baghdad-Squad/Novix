package com.baghdad.repository.mapper

import com.baghdad.domain.model.MediaAccountStates
import com.baghdad.repository.model.MediaAccountStateDto

fun MediaAccountStateDto.toEntity(): MediaAccountStates{
    return MediaAccountStates(
        isMediaRated = rated != null,
    )
}