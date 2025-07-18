package com.baghdad.local_datasource.roomDB.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.baghdad.repository.model.ActorDto

@Entity(tableName = "Actor")
data class Actor(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val name: String,
    val profilePictureURL: String,
    val birthDate: String,
    val placeOfBirth: String,
    val deathDate: String?,
    val biography: String,
    val headerPictures: List<String>,
    val department: String
)

fun Actor.toDto(): ActorDto = ActorDto(
    id = this.id,
    name = this.name,
    imageUrl = this.profilePictureURL,
    biography = this.biography,
    birthdayDate = this.birthDate,
    deathDate = this.deathDate,
    placeOfBirth = this.placeOfBirth,
    headerPictures = this.headerPictures,
    department = this.department
)

fun ActorDto.toDto(): Actor = Actor(
    id = this.id,
    name = this.name,
    profilePictureURL = this.imageUrl,
    biography = this.biography,
    birthDate = this.birthdayDate,
    deathDate = this.deathDate,
    placeOfBirth = this.placeOfBirth,
    headerPictures = this.headerPictures,
    department = this.department
)
