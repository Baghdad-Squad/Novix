package com.baghdad.domain.usecase.recentlyViewed

import com.baghdad.entity.media.Movie
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class AddRecentlyViewedUseCaseTest {
    private val addRecentlyViewedUseCase = AddRecentlyViewedUseCase()
    private val dummyMedia = Movie(
        id = 1L,
        title = "Dummy Movie",
        genres = emptyList(),
        imdbRating = 0.0,
        userRating = null,
        releaseDate = LocalDate(2023, 10, 1),
        overview = "",
        cast = emptyList(),
        posterPictureURL = "",
        backdropPicturesURLs = emptyList(),
        runtimeMinutes = 120
    )

    @Test
    fun dummyTest() = runTest {
        addRecentlyViewedUseCase(dummyMedia)
        assertTrue(true)
    }
}