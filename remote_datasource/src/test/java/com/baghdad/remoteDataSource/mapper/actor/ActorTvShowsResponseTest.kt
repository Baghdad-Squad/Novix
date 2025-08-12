package com.baghdad.remoteDataSource.mapper.actor

import com.baghdad.remoteDataSource.response.actor.ActorTvShowsResponse
import com.baghdad.repository.model.TvShowDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class ActorTvShowsResponseTest {

    companion object {
        private val COMPLETE_TV_SHOWS_RESPONSE = ActorTvShowsResponse(
            cast = listOf(
                ActorTvShowsResponse.ActorTvShowDto(
                    id = 123L,
                    genreIds = listOf(18L, 9648L),
                    name = "Breaking Bad",
                    overview = "A high school chemistry teacher turned meth manufacturer.",
                    posterPath = "/breaking_bad_poster.jpg",
                    voteAverage = 9.3,
                    firstAirDate = "2008-01-20",
                    originalName = "Breaking Bad Original"
                ),
                ActorTvShowsResponse.ActorTvShowDto(
                    id = 456L,
                    genreIds = listOf(10759L, 878L),
                    name = "Stranger Things",
                    overview = "Kids encounter supernatural forces in their town.",
                    posterPath = "/stranger_things_poster.jpg",
                    voteAverage = 8.7,
                    firstAirDate = "2016-07-15",
                    originalName = "Stranger Things Original"
                )
            )
        )

        private val NULL_CAST_RESPONSE = ActorTvShowsResponse(
            cast = null
        )

        private val EMPTY_CAST_RESPONSE = ActorTvShowsResponse(
            cast = emptyList()
        )

        private val NULL_VALUES_TV_SHOWS_RESPONSE = ActorTvShowsResponse(
            cast = listOf(
                ActorTvShowsResponse.ActorTvShowDto(
                    id = 789L,
                    genreIds = null,
                    name = null,
                    overview = null,
                    posterPath = null,
                    voteAverage = null,
                    firstAirDate = null,
                    originalName = null
                ),
                ActorTvShowsResponse.ActorTvShowDto(
                    id = 101L,
                    genreIds = listOf(35L),
                    name = "The Office",
                    overview = "A mockumentary about office workers.",
                    posterPath = "/office_poster.jpg",
                    voteAverage = 8.9,
                    firstAirDate = "2005-03-24",
                    originalName = "The Office US"
                )
            )
        )

        private val EMPTY_STRING_VALUES_RESPONSE = ActorTvShowsResponse(
            cast = listOf(
                ActorTvShowsResponse.ActorTvShowDto(
                    id = 202L,
                    genreIds = emptyList(),
                    name = "",
                    overview = "",
                    posterPath = "",
                    voteAverage = 5.0,
                    firstAirDate = "",
                    originalName = ""
                )
            )
        )

        private val NULL_ID_TV_SHOWS_RESPONSE = ActorTvShowsResponse(
            cast = listOf(
                ActorTvShowsResponse.ActorTvShowDto(
                    id = null,
                    genreIds = listOf(16L),
                    name = "Invalid Show",
                    overview = "This should be filtered out.",
                    posterPath = "/invalid_poster.jpg",
                    voteAverage = 7.5,
                    firstAirDate = "2023-08-15",
                    originalName = "Invalid Show Original"
                ),
                ActorTvShowsResponse.ActorTvShowDto(
                    id = 303L,
                    genreIds = listOf(27L),
                    name = "Valid Show",
                    overview = "A horror series.",
                    posterPath = "/horror_poster.jpg",
                    voteAverage = 6.3,
                    firstAirDate = "2023-10-31",
                    originalName = "Valid Show Original"
                )
            )
        )

        private val TITLE_FALLBACK_RESPONSE = ActorTvShowsResponse(
            cast = listOf(
                ActorTvShowsResponse.ActorTvShowDto(
                    id = 404L,
                    genreIds = listOf(99L),
                    name = null,
                    overview = "Show with original name fallback.",
                    posterPath = "/fallback_poster.jpg",
                    voteAverage = 7.8,
                    firstAirDate = "2024-01-01",
                    originalName = "Original Title"
                )
            )
        )

        private val EXPECTED_COMPLETE_TV_SHOWS = listOf(
            TvShowDto(
                id = 123,
                title = "Breaking Bad",
                genres = emptyList(),
                imdbRating = 9.3,
                userRating = null,
                releaseDate = "2008-01-20",
                overview = "A high school chemistry teacher turned meth manufacturer.",
                posterPictureURL = "https://image.tmdb.org/t/p/w500/breaking_bad_poster.jpg",
                numberOfSeasons = 0,
                trailerURL = "",
                headerImagesURLs = emptyList()
            ),
            TvShowDto(
                id = 456,
                title = "Stranger Things",
                genres = emptyList(),
                imdbRating = 8.7,
                userRating = null,
                releaseDate = "2016-07-15",
                overview = "Kids encounter supernatural forces in their town.",
                posterPictureURL = "https://image.tmdb.org/t/p/w500/stranger_things_poster.jpg",
                numberOfSeasons = 0,
                trailerURL = "",
                headerImagesURLs = emptyList()
            )
        )

        private val EXPECTED_EMPTY_LIST = emptyList<TvShowDto>()

        private val EXPECTED_NULL_VALUES_TV_SHOWS = listOf(
            TvShowDto(
                id = 789,
                title = "",
                genres = emptyList(),
                imdbRating = 0.0,
                userRating = null,
                releaseDate = "0001-01-01",
                overview = "",
                posterPictureURL = "",
                numberOfSeasons = 0,
                trailerURL = "",
                headerImagesURLs = emptyList()
            ),
            TvShowDto(
                id = 101,
                title = "The Office",
                genres = emptyList(),
                imdbRating = 8.9,
                userRating = null,
                releaseDate = "2005-03-24",
                overview = "A mockumentary about office workers.",
                posterPictureURL = "https://image.tmdb.org/t/p/w500/office_poster.jpg",
                numberOfSeasons = 0,
                trailerURL = "",
                headerImagesURLs = emptyList()
            )
        )

        private val EXPECTED_EMPTY_STRING_VALUES = listOf(
            TvShowDto(
                id = 202,
                title = "",
                genres = emptyList(),
                imdbRating = 5.0,
                userRating = null,
                releaseDate = "0001-01-01",
                overview = "",
                posterPictureURL = "",
                numberOfSeasons = 0,
                trailerURL = "",
                headerImagesURLs = emptyList()
            )
        )

        private val EXPECTED_FILTERED_NULL_ID = listOf(
            TvShowDto(
                id = 303,
                title = "Valid Show",
                genres = emptyList(),
                imdbRating = 6.3,
                userRating = null,
                releaseDate = "2023-10-31",
                overview = "A horror series.",
                posterPictureURL = "https://image.tmdb.org/t/p/w500/horror_poster.jpg",
                numberOfSeasons = 0,
                trailerURL = "",
                headerImagesURLs = emptyList()
            )
        )

        private val EXPECTED_TITLE_FALLBACK = listOf(
            TvShowDto(
                id = 404,
                title = "Original Title",
                genres = emptyList(),
                imdbRating = 7.8,
                userRating = null,
                releaseDate = "2024-01-01",
                overview = "Show with original name fallback.",
                posterPictureURL = "https://image.tmdb.org/t/p/w500/fallback_poster.jpg",
                numberOfSeasons = 0,
                trailerURL = "",
                headerImagesURLs = emptyList()
            )
        )
    }

    @Test
    fun `should convert all valid TV shows to TvShowDto list`() {
        val tvShowsResponse = COMPLETE_TV_SHOWS_RESPONSE

        val result = tvShowsResponse.toActorTvShowList()

        assertThat(result).isEqualTo(EXPECTED_COMPLETE_TV_SHOWS)
    }

    @Test
    fun `should return empty list when cast is null`() {
        val tvShowsResponse = NULL_CAST_RESPONSE

        val result = tvShowsResponse.toActorTvShowList()

        assertThat(result).isEqualTo(EXPECTED_EMPTY_LIST)
    }

    @Test
    fun `should return empty list when cast is empty`() {
        val tvShowsResponse = EMPTY_CAST_RESPONSE

        val result = tvShowsResponse.toActorTvShowList()

        assertThat(result).isEqualTo(EXPECTED_EMPTY_LIST)
    }

    @Test
    fun `should handle null values by using default values`() {
        val tvShowsResponse = NULL_VALUES_TV_SHOWS_RESPONSE

        val result = tvShowsResponse.toActorTvShowList()

        assertThat(result).isEqualTo(EXPECTED_NULL_VALUES_TV_SHOWS)
    }

    @Test
    fun `should handle empty string values correctly`() {
        val tvShowsResponse = EMPTY_STRING_VALUES_RESPONSE

        val result = tvShowsResponse.toActorTvShowList()

        assertThat(result).isEqualTo(EXPECTED_EMPTY_STRING_VALUES)
    }

    @Test
    fun `should filter out TV shows with null IDs`() {
        val tvShowsResponse = NULL_ID_TV_SHOWS_RESPONSE

        val result = tvShowsResponse.toActorTvShowList()

        assertThat(result).isEqualTo(EXPECTED_FILTERED_NULL_ID)
    }

    @Test
    fun `should fallback to originalName when name is null`() {
        val tvShowsResponse = TITLE_FALLBACK_RESPONSE

        val result = tvShowsResponse.toActorTvShowList()

        assertThat(result).isEqualTo(EXPECTED_TITLE_FALLBACK)
    }
}