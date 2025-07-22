package com.baghdad.local_datasource.roomDB.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.baghdad.repository.model.PeopleDto

@Entity(tableName = "People")
data class PopularPeopleEntity(
    @PrimaryKey val id: Long = 0L,
    val name: String,
    val profilePictureURL: String
)

fun PopularPeopleEntity.toDto(): PeopleDto {
    return PeopleDto(
        id = this.id,
        name = this.name,
        profilePictureUrl = this.profilePictureURL
    )
}

fun PeopleDto.toEntity(): PopularPeopleEntity {
    return PopularPeopleEntity(
        id = this.id,
        name = this.name,
        profilePictureURL = this.profilePictureUrl
    )
}
