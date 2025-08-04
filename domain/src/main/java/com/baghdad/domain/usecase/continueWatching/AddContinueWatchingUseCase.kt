package com.baghdad.domain.usecase.continueWatching

import com.baghdad.domain.model.ContinueWatching
import com.baghdad.domain.repository.ContinueWatchingRepository
import javax.inject.Inject

class AddContinueWatchingUseCase @Inject constructor(
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