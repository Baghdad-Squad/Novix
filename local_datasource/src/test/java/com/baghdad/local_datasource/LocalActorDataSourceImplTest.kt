//package com.baghdad.local_datasource
//
//import com.baghdad.local_datasource.roomDB.entity.toEntity
//import com.baghdad.repository.logger.Logger
//import com.baghdad.repository.model.ActorDto
//import com.google.common.truth.Truth.assertThat
//import io.mockk.coEvery
//import io.mockk.mockk
//import kotlinx.coroutines.flow.count
//import kotlinx.coroutines.flow.flowOf
//import kotlinx.coroutines.test.runTest
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.assertThrows
//
//class LocalActorDataSourceImplTest {
//
//    lateinit var actorDao: ActorDao
//    lateinit var logger: Logger
//
//    lateinit var localActorDataSourceImpl: LocalActorDataSourceImpl
//
//    @BeforeEach
//    fun setUp() {
//        actorDao = mockk()
//        logger = mockk()
//
//        localActorDataSourceImpl = LocalActorDataSourceImpl(actorDao, logger)
//    }
//
//    @Test
//    fun `should log an exception when dao throws an exception`() = runTest {
//        // Given
//        val exception = Exception("DB error")
//        coEvery { actorDao.getActorById(10) } throws exception
//        coEvery { logger.logException(exception) } returns Unit
//
//        // When & Then
//        assertThrows<Exception> { localActorDataSourceImpl.getActorById(10) }
//    }
//
//    @Test
//    fun `should add a new actor when addActor is called`() = runTest {
//        // Given
//        coEvery { actorDao.upsertActor(ACTOR.toEntity()) } returns Unit
//        coEvery { logger.logException(any()) } returns Unit
//
//        // When
//        val result = localActorDataSourceImpl.addActor(ACTOR)
//
//        // Then
//        assertThat(result).isEqualTo(Unit)
//    }
//
//    @Test
//    fun `should add actors when addActors is called`() = runTest {
//        // Given
//        coEvery { actorDao.upsertActors(ACTORS.map(ActorDto::toEntity)) } returns Unit
//        coEvery { logger.logException(any()) } returns Unit
//
//        // When
//        val result = localActorDataSourceImpl.addActors(ACTORS)
//
//        // Then
//        assert(result == Unit)
//    }
//
//    @Test
//    fun `should delete actor by id when deleteActorById is called`() = runTest {
//        // Given
//        coEvery { actorDao.deleteActorById(1) } returns Unit
//        coEvery { logger.logException(any()) } returns Unit
//
//        // When
//        val result = localActorDataSourceImpl.deleteActorById(1)
//
//        // Then
//        assertThat(result).isEqualTo(Unit)
//    }
//
//    @Test
//    fun `should get actor by id when getActorById is called`() = runTest {
//        // Given
//        coEvery { actorDao.getActorById(1) } returns ACTOR.toEntity()
//        coEvery { logger.logException(any()) } returns Unit
//
//        // When
//        val result = localActorDataSourceImpl.getActorById(1)
//
//        // Then
//        assertThat(result).isEqualTo(ACTOR)
//    }
//
//    @Test
//    fun `should get all actors from database when getAllActors is called`() = runTest {
//        // Given
//        coEvery { actorDao.getAllActors() } returns flowOf(ACTORS.map { it.toEntity() })
//        coEvery { logger.logException(any()) } returns Unit
//
//        // When
//        val result = localActorDataSourceImpl.getAllActors()
//
//        // Then
//        assertThat(result.count()).isEqualTo(flowOf(ACTORS).count())
//    }
//
//    @Test
//    fun `should clear all actors from database when deleteAllActors is called`() = runTest {
//        // Given
//        coEvery { actorDao.deleteAllActors() } returns Unit
//        coEvery { logger.logException(any()) } returns Unit
//
//        // When
//        val result = localActorDataSourceImpl.deleteAllActors()
//
//        // Then
//        assertThat(result).isEqualTo(Unit)
//    }
//
//    @Test
//    fun `should update actor when updateActor is called`() = runTest {
//        // Given
//        coEvery { actorDao.upsertActor(ACTOR.toEntity()) } returns Unit
//        coEvery { logger.logException(any()) } returns Unit
//
//        // When
//        val result = localActorDataSourceImpl.updateActor(ACTOR)
//
//        // Then
//        assertThat(result).isEqualTo(Unit)
//    }
//
//    @Test
//    fun `should get actor by name when searchActorsByName is called`() = runTest {
//        // Given
//        coEvery {
//            actorDao.getActorsFromSearchQuery(any(), any(), any())
//        } returns ACTORS.map { it.toEntity() }
//        coEvery { logger.logException(any()) } returns Unit
//
//        // When
//        val result = localActorDataSourceImpl.searchActorsByName("Mahmoud", 1, 10)
//
//        // Then
//        assertThat(result).isEqualTo(ACTORS)
//    }
//
//    companion object {
//        val ACTOR = ActorDto(
//            id = 1,
//            name = "Mahmoud Tarek",
//            imageUrl = "Test",
//            biography = "Test",
//            birthdayDate = "22-2-2002",
//            deathDate = "1-4-2025",
//            placeOfBirth = "Egypt",
//            headerPictures = emptyList(),
//            department = "Test"
//        )
//        val ACTORS = listOf(
//            ACTOR.copy(id = 2, name = "Ahmed"),
//            ACTOR.copy(id = 3, name = "Ali"),
//            ACTOR.copy(id = 4, name = "Omar"),
//            ACTOR.copy(id = 5, name = "Mahmoud"),
//            ACTOR.copy(id = 6, name = "Ahmed"),
//            ACTOR.copy(id = 7, name = "Ali")
//        )
//    }
//
//}