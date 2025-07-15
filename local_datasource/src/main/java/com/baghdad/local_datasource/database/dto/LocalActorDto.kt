package com.baghdad.local_datasource.database.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.baghdad.local_datasource.database.dto.LocalActorDto.Companion.ACTOR_TABLE_NAME
import com.baghdad.repository.model.actor.ActorDto

@Entity(tableName = ACTOR_TABLE_NAME)
data class LocalActorDto(
    @PrimaryKey
    @ColumnInfo(name = ID)
    val id: Long = 0L,
    @ColumnInfo(name = NAME)
    val name: String,
    @ColumnInfo(name = PROFILE_PICTURE_URL)
    val profilePictureURL: String,
    @ColumnInfo(name = BIRTH_DATE)
    val birthDate: String,
    @ColumnInfo(name = PLACE_OF_BIRTH)
    val placeOfBirth: String,
    @ColumnInfo(name = DEATH_DATE)
    val deathDate: String?,
    @ColumnInfo(name = BIOGRAPHY)
    val biography: String,
    @ColumnInfo(name = HEADER_PICTURES)
    val headerPictures: List<String>,
    @ColumnInfo(name = DEPARTMENT)
    val department: String

) {
    companion object {
        const val ACTOR_TABLE_NAME = "Actor"
        const val ID = "id"
        const val NAME = "name"
        const val PROFILE_PICTURE_URL = "profilePictureURL"
        const val BIRTH_DATE = "birthDate"
        const val PLACE_OF_BIRTH = "placeOfBirth"
        const val DEATH_DATE = "deathDate"
        const val BIOGRAPHY = "biography"
        const val HEADER_PICTURES = "headerPictures"
        const val DEPARTMENT = "department"
    }
}

fun LocalActorDto.toDto(): ActorDto = ActorDto(
    id = this.id,
    name = this.name,
    imageUrl = this.profilePictureURL,
    biography = this.biography,
    birthdayDate = this.birthDate,
    deathDate = this.deathDate,
    placeOfBirth = this.placeOfBirth,
    headerPictures = this.headerPictures,
    department = this.department,
)

fun ActorDto.toEntity(): LocalActorDto = LocalActorDto(
    id = this.id,
    name = this.name,
    profilePictureURL = this.imageUrl,
    biography = this.biography,
    birthDate = this.birthdayDate,
    deathDate = this.deathDate,
    placeOfBirth = this.placeOfBirth,
    headerPictures = this.headerPictures,
    department = this.department,

)

