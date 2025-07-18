package com.baghdad.repository.model

data class ActorDto(
    val id : Long,
    val name :String,
    val imageUrl: String,
    val biography: String,
    val birthdayDate: String,
    val deathDate: String?,
    val placeOfBirth: String,
    val headerPictures: List<String>,
    val department: String,
)