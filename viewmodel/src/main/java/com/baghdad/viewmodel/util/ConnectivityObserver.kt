package com.baghdad.viewmodel.util

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    fun isConnected(): Flow<Boolean>
}