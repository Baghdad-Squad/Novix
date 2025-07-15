package com.baghdad.local_datasource

import com.baghdad.local_datasource.database.dao.ActorDao
import com.baghdad.local_datasource.database.dto.LocalActorDto
import com.baghdad.local_datasource.database.dto.toDto
import com.baghdad.local_datasource.database.dto.toEntity
import com.baghdad.repository.model.actor.ActorDto
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LocalActorDataSourceImplTest {

    private lateinit var actorDao: ActorDao
    private lateinit var dataSource: LocalActorDataSourceImpl

    @BeforeEach
    fun setup() {
        actorDao = mockk(relaxed = true)
        dataSource = LocalActorDataSourceImpl(actorDao)
    }

    @Test
    fun `addActor inserts actor into dao`() = runTest {
        val name = "Zendaya"
        val imageUrl = "url.jpg"
        val expectedEntity = LocalActorDto(
            id = 0L,
            name = name,
            profilePictureURL = imageUrl,
            birthDate = "",
            placeOfBirth = "",
            deathDate = null,
            biography = "",
            headerPictures = emptyList(),
            department = ""
        )

        coEvery { actorDao.upsertActor(expectedEntity) } just Runs

        dataSource.addActor(name, imageUrl)

        coVerify { actorDao.upsertActor(expectedEntity) }
    }

    @Test
    fun `deleteActorById calls dao with correct id`() = runTest {
        val id = 123L
        coEvery { actorDao.deleteActorById(id) } just Runs

        dataSource.deleteActorById(id)

        coVerify { actorDao.deleteActorById(id) }
    }

    @Test
    fun `getActorById returns mapped ActorDto`() = runTest {
        val local = LocalActorDto(
            id = 1L,
            name = "Tom",
            profilePictureURL = "tom.jpg",
            birthDate = "1996-06-01",
            placeOfBirth = "London",
            deathDate = null,
            biography = "Actor from England.",
            headerPictures = listOf("header1.jpg"),
            department = "Acting"
        )

        coEvery { actorDao.getActorById(local.id) } returns local

        val result = dataSource.getActorById(local.id)

        assertEquals(local.toDto(), result)
        coVerify { actorDao.getActorById(local.id) }
    }

    @Test
    fun `getAllActors maps each LocalActorDto to ActorDto`() = runTest {
        val actors = listOf(
            LocalActorDto(
                id = 1L, name = "A", profilePictureURL = "a.jpg",
                birthDate = "1990-01-01", placeOfBirth = "NYC", deathDate = null,
                biography = "Bio A", headerPictures = listOf("a1.jpg"), department = "Acting"
            ),
            LocalActorDto(
                id = 2L,
                name = "B",
                profilePictureURL = "b.jpg",
                birthDate = "1985-03-15",
                placeOfBirth = "LA",
                deathDate = null,
                biography = "Bio B",
                headerPictures = listOf("b1.jpg", "b2.jpg"),
                department = "Directing"
            )
        )
        val flow = flowOf(actors)
        coEvery { actorDao.getAllActors() } returns flow

        val result = dataSource.getAllActors().first()

        assertEquals(actors.map { it.toDto() }, result)
    }

    @Test
    fun `deleteAllActors clears dao`() = runTest {
        coEvery { actorDao.deleteAllActors() } just Runs

        dataSource.deleteAllActors()

        coVerify { actorDao.deleteAllActors() }
    }

    @Test
    fun `updateActor upserts mapped LocalActorDto`() = runTest {
        val dto = ActorDto(
            id = 5L,
            name = "Chris",
            imageUrl = "chris.jpg",
            biography = "Chris biography",
            birthdayDate = "1980-01-01",
            deathDate = null,
            placeOfBirth = "Boston",
            headerPictures = listOf("header.jpg"),
            department = "Acting"
        )
        val expectedEntity = dto.toEntity()

        coEvery { actorDao.upsertActor(expectedEntity) } just Runs

        dataSource.updateActor(dto)

        coVerify { actorDao.upsertActor(expectedEntity) }
    }

    @Test
    fun `searchActorsByName maps results correctly`() = runTest {
        val query = "Tom"
        val matches = listOf(
            LocalActorDto(
                id = 1L,
                name = "Tom Holland",
                profilePictureURL = "tom.jpg",
                birthDate = "1996-06-01",
                placeOfBirth = "London",
                deathDate = null,
                biography = "Spider-Man actor",
                headerPictures = listOf("h1.jpg"),
                department = "Acting"
            ),
            LocalActorDto(
                id = 2L, name = "Tom Hardy", profilePictureURL = "hardy.jpg",
                birthDate = "1977-09-15", placeOfBirth = "London", deathDate = null,
                biography = "Venom actor", headerPictures = listOf("h2.jpg"), department = "Acting"
            )
        )
        coEvery { actorDao.searchActorsByName(query) } returns matches

        val result = dataSource.searchActorsByName(query)

        assertEquals(matches.map { it.toDto() }, result)
        coVerify { actorDao.searchActorsByName(query) }
    }
}