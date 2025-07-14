package com.baghdad.local_datasource.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.baghdad.local_datasource.database.dto.LocalActorDto
import kotlinx.coroutines.flow.Flow

@Dao
interface ActorDao {
    @Upsert
    suspend fun upsertActor(localActorDto: LocalActorDto)

    @Query("SELECT * FROM Actor")
    fun getAllActors(): Flow<List<LocalActorDto>>

    @Query("SELECT * FROM Actor Where id = :id")
    suspend fun getActorById(id: Long): LocalActorDto

    @Query("DELETE FROM Actor WHERE id = :id")
    suspend fun deleteActorById(id: Long)

    @Query("DELETE FROM Actor")
    suspend fun deleteAllActors()

    @Query("SELECT * FROM Actor WHERE name LIKE '%' || :name || '%'")
    suspend fun searchActorsByName(name: String): List<LocalActorDto>

}
