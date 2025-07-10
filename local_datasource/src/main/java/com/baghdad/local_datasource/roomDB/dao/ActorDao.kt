package com.baghdad.local_datasource.roomDB.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.baghdad.local_datasource.roomDB.entity.Actor
import com.baghdad.local_datasource.roomDB.entity.Movie
import kotlinx.coroutines.flow.Flow

@Dao
interface ActorDao {
    @Upsert
    suspend fun upsertActor(actor: Actor)

    @Query("SELECT * FROM Actor Where name = :title")
    suspend fun getActorsByTitle(title: String): Flow<List<Actor>>

}