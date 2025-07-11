package com.baghdad.local_datasource.roomDB.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.baghdad.local_datasource.roomDB.entity.Actor
import kotlinx.coroutines.flow.Flow

@Dao
interface ActorDao {
    @Upsert
    suspend fun upsertActor(actor: Actor)

    @Query("SELECT * FROM Actor")
    suspend fun getAllActors(): Flow<List<Actor>>

    @Query("SELECT * FROM Actor Where name = :id")
    suspend fun getActorById(id: Long): Actor

    @Query("DELETE FROM Actor WHERE id = :id")
    suspend fun deleteActorById(id: Long)

    @Query("DELETE FROM Actor")
    suspend fun deleteAllActors()

}
