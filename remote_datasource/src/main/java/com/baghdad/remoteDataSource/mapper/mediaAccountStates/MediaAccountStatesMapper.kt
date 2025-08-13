package com.baghdad.remoteDataSource.mapper.mediaAccountStates

import com.baghdad.remoteDataSource.response.mediaAccount.MediaAccountStatesResponse
import com.baghdad.repository.model.MediaAccountStateDto

fun MediaAccountStatesResponse.toDto(): MediaAccountStateDto{
    return MediaAccountStateDto(
        id = id ?: -1L,
        rated = ratedValue
    )
}