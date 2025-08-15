package com.baghdad.domain.usecase.userWatchedMedia

import com.baghdad.domain.model.continueWatching.UserWatchedMedia
import com.baghdad.domain.repository.UserWatchedMediaRepository
import javax.inject.Inject

class AddUserWatchedMediaUseCase @Inject constructor(
    private val userWatchedMediaRepository: UserWatchedMediaRepository
) {
    suspend operator fun invoke(
        contentId: Long,
        genreIds: List<Long>,
        contentImageUrl: String,
        contentType: UserWatchedMedia.ContentType
    ) {
        userWatchedMediaRepository.addUserWatchedMedia(
            contentId = contentId,
            genreIds = genreIds,
            contentImageUrl = contentImageUrl,
            contentType = contentType
        )
    }
}