package com.baghdad.remoteDataSource.mapper

import com.baghdad.remoteDataSource.response.MediaAccountStatesResponse
import com.baghdad.repository.model.MediaAccountStateDto

fun MediaAccountStatesResponse.toDto(): MediaAccountStateDto{
    return MediaAccountStateDto(
        id = id,
        rated = ratedValue
    )
}