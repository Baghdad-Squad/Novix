package com.baghdad.local_datasource.roomDB.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.baghdad.repository.model.ActorDto

@Entity(tableName = "trendingActor")
data class TrendingActorEntity(
    @PrimaryKey val id: Long = 0L,
    val name: String,
    val profilePictureURL: String
)

fun TrendingActorEntity.toDto(): ActorDto {
    return ActorDto(
        id = this.id,
        name = this.name,
        imageUrl = this.profilePictureURL,
        biography = "",
        birthdayDate = "",
        deathDate = null,
        placeOfBirth = "",
        headerPictures = emptyList(),
        department = ""
    )
}

fun ActorDto.toTrendingActorEntity(): TrendingActorEntity {
    return TrendingActorEntity(
        id = id,
        name = name,
        profilePictureURL = imageUrl
    )
}
