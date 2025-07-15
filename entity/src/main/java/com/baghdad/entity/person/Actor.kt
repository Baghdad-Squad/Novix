package com.baghdad.entity.person

import kotlinx.datetime.LocalDate

data class Actor(
    val id: Long,
    val name: String,
    val profilePictureURL: String,
    val birthDate: LocalDate,
    val placeOfBirth: String,
    val deathDate: LocalDate?,
    val biography: String,
    val headerPictures: List<String>,
    val department: String
)