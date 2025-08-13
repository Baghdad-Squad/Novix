package com.baghdad.repository.mapper

import com.baghdad.repository.model.MediaAccountStateDto

fun MediaAccountStateDto.toIsMediaRated(): Boolean {
    return rated != null
}