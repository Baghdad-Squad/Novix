package com.baghdad.local_datasource.roomDB.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.baghdad.repository.model.actor.ActorDto

@Entity(tableName = "Actor")
data class Actor(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val name: String,
    val profilePictureURL: String,
)

fun Actor.toDto(): ActorDto = ActorDto(
    id = this.id,
    name = this.name,
    imageUrl = this.profilePictureURL
)

fun ActorDto.toDto(): Actor = Actor(
    id = this.id,
    name = this.name,
    profilePictureURL = this.imageUrl
)
