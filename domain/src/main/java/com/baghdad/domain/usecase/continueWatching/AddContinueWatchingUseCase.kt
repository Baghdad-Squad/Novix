package com.baghdad.domain.usecase.continueWatching

import com.baghdad.domain.model.ContinueWatching
import com.baghdad.domain.repository.ContinueWatchingRepository

class AddContinueWatchingUseCase(
    private val continueWatchingRepository: ContinueWatchingRepository
) {
    suspend operator fun invoke(
        contentId: Long,
        genreIds: List<Long>,
        contentImageUrl: String,
        contentType: ContinueWatching.ContentType
    ) {
        continueWatchingRepository.addContinueWatching(
            contentId = contentId,
            genreIds = genreIds,
            contentImageUrl = contentImageUrl,
            contentType = contentType
        )
    }
}