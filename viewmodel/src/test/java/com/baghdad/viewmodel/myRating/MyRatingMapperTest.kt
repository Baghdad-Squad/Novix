package com.baghdad.viewmodel.myRating

import com.baghdad.domain.model.userRating.RatedMedia
import com.baghdad.domain.model.userRating.RatedMedia.ContentType
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class MyRatingMapperTest {

    @Test
    fun `should map RatedMedia to MediaItemUiState correctly for movie`() {

        val actual = movieRatedMedia.toUiState()
        val expected = expectedMovieRatedMedia

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should map RatedMedia to MediaItemUiState correctly for tv show`() {

        val actual = tvShowRatedMedia.toUiState()
        val expected = expectedTvShowRatedMedia

        assertThat(actual).isEqualTo(expected)
    }



    private companion object {
        val movieRatedMedia = RatedMedia(
            id = 123L,
            userRating = 8,
            posterImageURL = "/test_poster.jpg",
            contentType =  ContentType.MOVIE,
        )
        val expectedMovieRatedMedia = MyRatingState.MediaItemUiState(
            id = 123L,
            posterPictureURL = "/test_poster.jpg",
            contentType = MyRatingState.ContentType.MOVIE,
            rating = 8
        )

        val tvShowRatedMedia = RatedMedia(
            id = 123L,
            userRating = 8,
            posterImageURL = "/test_poster.jpg",
            contentType =  ContentType.TV_SHOW,
        )

        val expectedTvShowRatedMedia = MyRatingState.MediaItemUiState(
            id = 123L,
            posterPictureURL = "/test_poster.jpg",
            contentType = MyRatingState.ContentType.TV_SHOW,
            rating = 8
        )
    }
}