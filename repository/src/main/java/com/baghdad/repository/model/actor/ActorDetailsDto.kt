package com.baghdad.repository.model.actor

data class ActorDetailsDto(
    val actorDto: ActorDto,
    val biography: String,
    val birthday: String,
    val deathday: String,
    val homepage: String,
    val knownForDepartment: String,
    val placeOfBirth: String
)