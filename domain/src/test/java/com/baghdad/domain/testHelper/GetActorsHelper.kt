package com.baghdad.domain.testHelper

import com.baghdad.entity.person.Actor
import kotlinx.datetime.LocalDate

fun getSampleActor(
    id: Long = 1L,
    name: String = "John Doe",
    biography: String = "An actor",
    profilePictureURL: String = "https://example.com/profile.jpg",
    birthDate: LocalDate = LocalDate(1980, 1, 1),
    placeOfBirth: String = "New York",
    deathDate: LocalDate? = null,
    headerPictures: List<String> = listOf(
        "https://example.com/header.jpg",
        "https://example.com/header2.jpg"
    ),
    department: String = "Acting",
)= Actor(
    id = id,
    name = name,
    biography = biography,
    profilePictureURL = profilePictureURL,
    birthDate = birthDate,
    placeOfBirth = placeOfBirth,
    deathDate = deathDate,
    headerPictures = headerPictures,
    department = department
)

fun getMinimalActor(
    id: Long = 2L,
    name: String = "Jane Smith",
    biography: String = "",
    profilePictureURL: String = "",
    birthDate: LocalDate = LocalDate(1990, 5, 20),
    placeOfBirth: String = "",
    deathDate: LocalDate? = null,
    headerPictures: List<String> = emptyList(),
    department: String = ""
)= Actor(
    id = id,
    name = name,
    biography = biography,
    profilePictureURL = profilePictureURL,
    birthDate = birthDate,
    placeOfBirth = placeOfBirth,
    deathDate = deathDate,
    headerPictures = headerPictures,
    department = department
)