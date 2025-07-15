package com.baghdad.local_datasource.database.dto

import com.baghdad.repository.model.actor.ActorDto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LocalActorDtoTest {

    private val fakeActor1 = LocalActorDto(
        id = 1L,
        name = "Lyra Storme",
        profilePictureURL = "https://example.com/images/lyra.jpg",
        birthDate = "1988-04-25",
        placeOfBirth = "Oslo, Norway",
        deathDate = null,
        biography = "Lyra Storme rose to fame in Scandinavian cinema, known for her emotionally charged performances and multilingual roles.",
        headerPictures = listOf(
            "https://example.com/images/lyra-header1.jpg",
            "https://example.com/images/lyra-header2.jpg"
        ),
        department = "Acting"
    )

    @Test
    fun `toDto   Happy path`() {
        val dto = fakeActor1.toDto()

        assertEquals(fakeActor1.id, dto.id)
        assertEquals(fakeActor1.name, dto.name)
        assertEquals(fakeActor1.profilePictureURL, dto.imageUrl)
        assertEquals(fakeActor1.birthDate, dto.birthdayDate)
        assertEquals(fakeActor1.deathDate, dto.deathDate)
        assertEquals(fakeActor1.placeOfBirth, dto.placeOfBirth)
        assertEquals(fakeActor1.biography, dto.biography)
        assertEquals(fakeActor1.headerPictures, dto.headerPictures)
        assertEquals(fakeActor1.department, dto.department)
    }

    @Test
    fun `toDto   Empty name`() {
        val blankNameActor = fakeActor1.copy(name = "", profilePictureURL = "pic.jpg")
        val dto = blankNameActor.toDto()

        assertEquals("", dto.name)
        assertEquals("pic.jpg", dto.imageUrl)
    }

    @Test
    fun `toDto   Empty profilePictureURL`() {
        val emptyPicActor = fakeActor1.copy(name = "EmptyPic", profilePictureURL = "")
        val dto = emptyPicActor.toDto()

        assertEquals("EmptyPic", dto.name)
        assertEquals("", dto.imageUrl)
    }

    @Test
    fun `toEntity   Happy path with full fields`() {
        val dto = ActorDto(
            id = 10L,
            name = "Tom Holland",
            imageUrl = "tom.jpg",
            biography = "British actor known for playing Spider-Man.",
            birthdayDate = "1996-06-01",
            deathDate = null,
            placeOfBirth = "London, England",
            headerPictures = listOf("header1.jpg", "header2.jpg"),
            department = "Acting"
        )

        val entity = dto.toEntity()

        assertEquals(dto.id, entity.id)
        assertEquals(dto.name, entity.name)
        assertEquals(dto.imageUrl, entity.profilePictureURL)
        assertEquals(dto.biography, entity.biography)
        assertEquals(dto.birthdayDate, entity.birthDate)
        assertEquals(dto.deathDate, entity.deathDate)
        assertEquals(dto.placeOfBirth, entity.placeOfBirth)
        assertEquals(dto.headerPictures, entity.headerPictures)
        assertEquals(dto.department, entity.department)
    }

    @Test
    fun `toEntity   Empty name`() {
        val dto = ActorDto(
            id = 11L,
            name = "",
            imageUrl = "pic.png",
            biography = "",
            birthdayDate = "",
            deathDate = null,
            placeOfBirth = "",
            headerPictures = emptyList(),
            department = ""
        )

        val entity = dto.toEntity()

        assertEquals("", entity.name)
        assertEquals("pic.png", entity.profilePictureURL)
    }

    @Test
    fun `toEntity   Empty imageUrl`() {
        val dto = ActorDto(
            id = 12L,
            name = "EmptyImage",
            imageUrl = "",
            biography = "",
            birthdayDate = "",
            deathDate = null,
            placeOfBirth = "",
            headerPictures = emptyList(),
            department = ""
        )

        val entity = dto.toEntity()

        assertEquals("EmptyImage", entity.name)
        assertEquals("", entity.profilePictureURL)
    }
}
