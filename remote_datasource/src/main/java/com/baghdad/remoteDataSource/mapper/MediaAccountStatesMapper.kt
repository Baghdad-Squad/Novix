package com.baghdad.remoteDataSource.mapper

import com.baghdad.remoteDataSource.response.mediaAccount.MediaAccountStatesResponse
import com.baghdad.repository.model.MediaAccountStateDto

fun MediaAccountStatesResponse.toDto(): MediaAccountStateDto{
    return MediaAccountStateDto(
        id = id ?: 0L,
        rated = ratedValue
    )
}