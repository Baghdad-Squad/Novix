package com.baghdad.local_datasource.database.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.baghdad.local_datasource.database.dto.LocalActorDto.Companion.ACTOR_TABLE_NAME
import com.baghdad.repository.model.ActorDto

@Entity(tableName = ACTOR_TABLE_NAME)
data class LocalActorDto(
    @PrimaryKey
    @ColumnInfo(name = ID)
    val id: Long = 0L,
    @ColumnInfo(name = NAME)
    val name: String,
    @ColumnInfo(name = PROFILE_PICTURE_URL)
    val profilePictureURL: String,
) {
    companion object {
        const val ACTOR_TABLE_NAME = "Actor"
        const val ID = "id"
        const val NAME = "name"
        const val PROFILE_PICTURE_URL = "profilePictureURL"
    }
}

fun LocalActorDto.toDto(): ActorDto = ActorDto(
    id = this.id,
    name = this.name,
    imageUrl = this.profilePictureURL
)

fun ActorDto.toEntity(): LocalActorDto = LocalActorDto(
    id = this.id,
    name = this.name,
    profilePictureURL = this.imageUrl
)

