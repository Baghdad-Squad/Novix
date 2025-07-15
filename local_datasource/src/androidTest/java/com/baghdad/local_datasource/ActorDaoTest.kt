package com.baghdad.local_datasource

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.baghdad.local_datasource.database.dao.ActorDao
import com.baghdad.local_datasource.database.dto.LocalActorDto
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.runner.RunWith
import org.junit.Before
import org.junit.Test


@RunWith(AndroidJUnit4::class)
@SmallTest
class ActorDaoTest {
    private lateinit var dataBase: NovixDatabase
    private lateinit var actorDao: ActorDao

    @Before
    fun setupDataBase() {
        dataBase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NovixDatabase::class.java
        ).allowMainThreadQueries().build()

        actorDao = dataBase.actorDao()
    }

    @After
    fun closeDataBase() {
        dataBase.close()
    }

    @Test
    fun deleteAllActors_returnsTrue() = runBlocking {


        actorDao.upsertActor(localActorDto = fakeActor1)
        actorDao.upsertActor(localActorDto = fakeActor2)


        actorDao.deleteAllActors()
        val result = actorDao.getAllActors().first()
        assertTrue(result.isEmpty())

    }

    @Test
    fun deleteActorById_returnsTrue() = runBlocking {

        val actorName = "actor1"

        actorDao.upsertActor(localActorDto = fakeActor1)

        actorDao.deleteActorById(id = 1L)
        val result = actorDao.searchActorsByName(actorName)
        assertTrue(result.isEmpty())

    }

    @Test
    fun searchActorByName_returnsEquals() = runBlocking {


        actorDao.upsertActor(localActorDto = fakeActor1)
        actorDao.upsertActor(localActorDto = fakeActor2)

        val result = actorDao.searchActorsByName(name = "ac")
        assertEquals(2, result.size)
        assertTrue(result.all { it.name.contains("ac") })
    }

    @Test
    fun getActorById_returnsCorrectActor() = runBlocking {

        actorDao.upsertActor(fakeActor1)
        actorDao.upsertActor(fakeActor2)

        val result = actorDao.getActorById(id = 1L)

        assertEquals(fakeActor1.name, result.name)
        assertEquals(fakeActor1.profilePictureURL, result.profilePictureURL)
        assertEquals(fakeActor1.id, result.id)
    }

    @Test
    fun upsertActor_insertsActorCorrectly() = runBlocking {

        actorDao.upsertActor(fakeActor1)
        val result = actorDao.getActorById(1L)

        assertEquals(fakeActor1.name, result.name)
        assertEquals(fakeActor1.profilePictureURL, result.profilePictureURL)
        assertEquals(fakeActor1.id, result.id)
    }

    @Test
    fun getAllActors_returnsAllInsertedActors() = runBlocking {

        actorDao.upsertActor(fakeActor1)
        actorDao.upsertActor(fakeActor2)

        val result = actorDao.getAllActors().first()

        assertEquals(2, result.size)
        assertTrue(result.any { it.id == 1L && it.name == "actor1" })
        assertTrue(result.any { it.id == 2L && it.name == "actor2" })
    }

    val fakeActor1 = LocalActorDto(
        id = 1L,
        name = "actor1",
        profilePictureURL = "https://example.com/profiles/elena.jpg",
        birthDate = "1985-06-17",
        placeOfBirth = "Madrid, Spain",
        deathDate = null,
        biography = "Elena Dusk is known for her compelling roles in noir thrillers and historical epics.",
        headerPictures = listOf(
            "https://example.com/headers/elena1.jpg",
            "https://example.com/headers/elena2.jpg"
        ),
        department = "Acting"
    )

    val fakeActor2 = LocalActorDto(
        id = 2L,
        name = "actor2",
        profilePictureURL = "https://example.com/profiles/jules.jpg",
        birthDate = "1940-12-05",
        placeOfBirth = "Montreal, Canada",
        deathDate = "2010-04-22",
        biography = "Jules Orion's deep voice and commanding screen presence defined post-war cinema.",
        headerPictures = listOf(
            "https://example.com/headers/jules1.jpg",
            "https://example.com/headers/jules2.jpg",
            "https://example.com/headers/jules3.jpg"
        ),
        department = "Directing"
    )


}