package com.baghdad.domain.repository

interface OnBoardingRepository {
    suspend fun isFirstTimeUser(): Boolean
    suspend fun setFirstTimeUser(firstTime: Boolean)
    suspend fun clearFirstTimeUser()
}