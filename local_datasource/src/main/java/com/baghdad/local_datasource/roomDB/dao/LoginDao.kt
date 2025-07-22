package com.baghdad.local_datasource.roomDB.dao

import androidx.room.Query
import androidx.room.Upsert
import com.baghdad.local_datasource.roomDB.entity.Login

interface LoginDao {
    @Upsert
    suspend fun upsertLogin(login: Login)

    @Upsert
    suspend fun upsertLoginList(logins: List<Login>)

    @Query("SELECT * FROM login WHERE userName = :userName AND password = :password")
    suspend fun getLoginByUsernameAndPassword(userName: String, password: String): Login?

    @Query("SELECT * FROM login WHERE sessionId = :sessionId")
    suspend fun getLoginBySessionId(sessionId: String): Login?

    @Query("SELECT * FROM login WHERE userId = :userId")
    suspend fun getLoginByUserId(userId: Long): Login?

    @Query("DELETE FROM login WHERE userId = :userId")
    suspend fun deleteLoginByUserId(userId: Long)


}