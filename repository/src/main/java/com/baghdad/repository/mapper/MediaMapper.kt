package com.baghdad.repository.mapper

import com.baghdad.entity.media.Media
import com.baghdad.repository.model.MediaDto
import com.baghdad.repository.model.MediaType
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.TvShowDto

fun MediaDto.toEntity() : Media {
    if(this.type == MediaType.MOVIE){
        return (this as MovieDto).toEntity()
    }
    else{
        return (this as TvShowDto).toEntity()
    }
}