package com.baghdad.domain.usecase.recentlyViewed

import com.baghdad.entity.media.Media
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetRecentlyViewedUseCase {
    suspend operator fun invoke(): Flow<List<Media>> {
        //TODO("Not yet implemented")
        return flowOf(emptyList())
    }
}