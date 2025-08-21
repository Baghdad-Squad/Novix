package com.baghdad.domain.usecase.actor

import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.entity.person.Actor
import com.baghdad.entity.person.CastMember
import kotlinx.datetime.LocalDate

object ActorMock {

    const val PAGE = 1

    const val ACTOR_ID = 22L
    val ACTOR = Actor(
        id = 22L,
        name = "John Doe",
        biography = "An actor",
        profilePictureURL = "https://example.com/profile.jpg",
        birthDate = LocalDate(1980, 1, 1),
        placeOfBirth = "New York",
        deathDate = null,
        headerPictures = listOf("https://example.com/header.jpg"),
        department = "Actor Test"
    )

    val ACTORS = listOf(
        ACTOR,
        ACTOR.copy(id = 23, name = "Jane Smith", department = "Actress Test"),
        ACTOR.copy(id = 24, name = "Bob Johnson", department = "Director Test"),
        ACTOR.copy(id = 25, name = "Alice Brown", department = "Producer Test")
    )

    val ACTOR_RESULT = PagedResult(
        prevPage = null,
        nextPage = null,
        data = ACTORS
    )


    val CAST_MEMBER = CastMember(
        actor = ACTOR,
        characterName = "Character Name"
    )

    val CAST_MEMBERS = listOf(
        CAST_MEMBER,
        CAST_MEMBER.copy(actor = ACTOR.copy(id = 23, name = "Jane Smith")),
        CAST_MEMBER.copy(actor = ACTOR.copy(id = 24, name = "Bob Johnson")),
        CAST_MEMBER.copy(actor = ACTOR.copy(id = 25, name = "Alice Brown"))
    )
}