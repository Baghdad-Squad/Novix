package com.baghdad.viewmodel.utls

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import kotlinx.coroutines.flow.Flow

suspend fun <T : Any> collectAndSnapshot(
    flow: Flow<PagingData<T>>
): List<T> {
    return flow.asSnapshot()
}