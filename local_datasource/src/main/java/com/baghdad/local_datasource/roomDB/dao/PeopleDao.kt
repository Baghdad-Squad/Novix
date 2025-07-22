package com.baghdad.local_datasource.roomDB.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.baghdad.local_datasource.roomDB.entity.PopularPeopleEntity

@Dao
interface PeopleDao {
    @Upsert
    suspend fun upsertPopularPeople(people: List<PopularPeopleEntity>)

    @Query(
        """
    SELECT * FROM People
    LIMIT :pageSize OFFSET :offset
    """
    )
    suspend fun getAllPopularPeople(
        pageSize: Int,
        offset: Int
    ): List<PopularPeopleEntity>


    @Query("SELECT * FROM People WHERE id = :personId")
    suspend fun getPersonId(personId: Long): PopularPeopleEntity

    @Query("DELETE FROM People")
    suspend fun deleteAllPopularPeople()

}