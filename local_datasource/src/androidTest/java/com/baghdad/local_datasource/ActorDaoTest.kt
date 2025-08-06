//package com.baghdad.local_datasource
//
//import androidx.room.Room
//import androidx.test.core.app.ApplicationProvider
//import androidx.test.filters.SmallTest
//import com.baghdad.local_datasource.roomDB.database.NovixDatabase
//import com.baghdad.local_datasource.roomDB.entity.Actor
//import com.baghdad.local_datasource.roomDB.entity.SearchQuery
//import com.google.common.truth.Truth.assertThat
//import kotlinx.coroutines.flow.first
//import kotlinx.coroutines.runBlocking
//import org.junit.After
//import org.junit.Before
//import org.junit.Test
//
//@SmallTest
//class ActorDaoTest {
//    private lateinit var dataBase: NovixDatabase
//    private lateinit var actorDao: ActorDao
//
//    @Before
//    fun setupDataBase() {
//        dataBase = Room.inMemoryDatabaseBuilder(
//            ApplicationProvider.getApplicationContext(),
//            NovixDatabase::class.java
//        ).allowMainThreadQueries().build()
//
//        actorDao = dataBase.actorDao()
//    }
//
//    @After
//    fun closeDataBase() {
//        dataBase.close()
//    }
//
//    @Test
//    fun shouldDeleteAllActorsWhenDeleteAllActorsIsCalled() = runBlocking {
//        // Given
//        actorDao.upsertActor(fakeActor1)
//        actorDao.upsertActor(fakeActor2)
//
//        // When
//        actorDao.deleteAllActors()
//        val result = actorDao.getAllActors().first()
//
//        // Then
//        assertThat(result).isEmpty()
//    }
//
//    @Test
//    fun shouldDeleteActorWhenDeleteActorByIdIsCalled() = runBlocking {
//        // Given
//        val actorName = "actor1"
//
//        // When
//        actorDao.upsertActor(fakeActor1)
//        actorDao.deleteActorById(id = 1L)
//        val result = actorDao.searchActorsByName(actorName)
//
//        // Then
//        assertThat(result).isEmpty()
//    }
//
//    @Test
//    fun shouldReturnMatchingActorsWhenSearchActorsByNameIsCalled() = runBlocking {
//        // Given
//        actorDao.upsertActor(fakeActor1)
//        actorDao.upsertActor(fakeActor2)
//
//        // When
//        val result = actorDao.searchActorsByName(name = "ac")
//
//        // Then
//        assertThat(result.size).isEqualTo(2)
//        assertThat(result.any { it.name.contains("ac") }).isTrue()
//    }
//
//    @Test
//    fun shouldReturnCorrectActorWhenGetActorByIdIsCalled() = runBlocking {
//        // Given
//        actorDao.upsertActor(fakeActor1)
//        actorDao.upsertActor(fakeActor2)
//
//        // When
//        val result = actorDao.getActorById(id = 1L)
//
//        // Then
//        assertThat(result.id).isEqualTo(fakeActor1.id)
//        assertThat(result.name).isEqualTo(fakeActor1.name)
//        assertThat(result.profilePictureURL).isEqualTo(fakeActor1.profilePictureURL)
//    }
//
//    @Test
//    fun shouldInsertActorCorrectlyWhenUpsertActorIsCalled() = runBlocking {
//        // Given
//        actorDao.upsertActor(fakeActor1)
//
//        // When
//        val result = actorDao.getActorById(1L)
//
//        // Then
//        assertThat(result.id).isEqualTo(fakeActor1.id)
//        assertThat(result.name).isEqualTo(fakeActor1.name)
//        assertThat(result.profilePictureURL).isEqualTo(fakeActor1.profilePictureURL)
//    }
//
//    @Test
//    fun shouldReturnAllActorsWhenGetAllActorsIsCalled() = runBlocking {
//        // Given
//        actorDao.upsertActor(fakeActor1)
//        actorDao.upsertActor(fakeActor2)
//
//        // When
//        val result = actorDao.getAllActors().first()
//
//        // Then
//        assertThat(result.size).isEqualTo(2)
//        assertThat(result.any { it.id == 1L && it.name == "actor1" }).isTrue()
//        assertThat(result.any { it.id == 2L && it.name == "actor2" }).isTrue()
//    }
//
//    @Test
//    fun shouldInsertMultipleActorsWhenUpsertActorsIsCalled() = runBlocking {
//        // Given
//        val actors = listOf(fakeActor1, fakeActor2)
//
//        // When
//        actorDao.upsertActors(actors)
//        val result = actorDao.getAllActors().first()
//
//        // Then
//        assertThat(result.size).isEqualTo(2)
//        assertThat(result.any { it.id == 2L && it.name == "actor2" }).isTrue()
//        assertThat(result.any { it.id == 1L && it.name == "actor1" }).isTrue()
//    }
//
//    @Test
//    fun shouldReturnActorsFromSearchQueryWhenGetActorsFromSearchQueryIsCalled() = runBlocking {
//        // Given
//        actorDao.upsertActors(listOf(fakeActor1, fakeActor2))
//        dataBase.searchQueryDao().addSearchQueries(listOf(fakeQuery1, fakeQuery2))
//
//        // When
//        val result = actorDao.getActorsFromSearchQuery(
//            queryName = "famous1",
//            pageSize = 10,
//            offset = 0
//        )
//
//        // Then
//        assertThat(result.size).isEqualTo(1)
//        assertThat(result.first().id).isEqualTo(fakeActor1.id)
//    }
//
//    companion object {
//        val fakeActor1 = Actor(
//            id = 1L,
//            name = "actor1",
//            profilePictureURL = "https://example.com/profiles/elena.jpg",
//            birthDate = "1985-06-17",
//            placeOfBirth = "Madrid, Spain",
//            deathDate = null,
//            biography = "Elena Dusk is known for her compelling roles in noir thrillers and historical epics.",
//            headerPictures = listOf(
//                "https://example.com/headers/elena1.jpg",
//                "https://example.com/headers/elena2.jpg"
//            ),
//            department = "Acting"
//        )
//
//        val fakeActor2 = Actor(
//            id = 2L,
//            name = "actor2",
//            profilePictureURL = "https://example.com/profiles/jules.jpg",
//            birthDate = "1940-12-05",
//            placeOfBirth = "Montreal, Canada",
//            deathDate = "2010-04-22",
//            biography = "Jules Orion's deep voice and commanding screen presence defined post-war cinema.",
//            headerPictures = listOf(
//                "https://example.com/headers/jules1.jpg",
//                "https://example.com/headers/jules2.jpg",
//                "https://example.com/headers/jules3.jpg"
//            ),
//            department = "Directing"
//        )
//
//        val fakeQuery1 = SearchQuery(
//            queryName = "famous1",
//            mediaId = 1L,
//            mediaType = "ACTOR",
//            timeStamp = 1625158800000
//        )
//
//        val fakeQuery2 = SearchQuery(
//            queryName = "famous2",
//            mediaId = 2L,
//            mediaType = "ACTOR",
//            timeStamp = 1625258800000
//        )
//    }
//}
