// package com.baghdad.viewmodel.actorDetails
//
// import com.baghdad.entity.media.Genre
// import com.baghdad.entity.media.Movie
// import com.baghdad.entity.media.TvShow
// import com.baghdad.entity.person.Actor
// import kotlinx.datetime.LocalDate
// import org.junit.jupiter.api.Assertions.assertEquals
// import org.junit.jupiter.api.Test
//
// class ActorDetailsScreenMapperTest {
//
//    @Test
//    fun `toActorInfoUI should map Actor to ActorInfoUiState correctly when deathDate is null`() {
//
//        val actor = createMockActorWithoutDeathDate()
//        val expectedActorInfoUiState = ActorDetailsScreenState.ActorInfoUiState(
//            name = "John Doe",
//            biography = "Famous actor biography",
//            birthdayDate = "1980-01-01",
//            placeOfBirth = "New York, USA",
//            deathDate = null,
//            headerPictures = listOf("/header1.jpg", "/header2.jpg", "/header3.jpg"),
//            department = "Acting"
//        )
//
//        val result = actor.toActorInfoUI()
//
//        assertEquals(expectedActorInfoUiState, result)
//    }
//
//    @Test
//    fun `toActorInfoUI should map Actor to ActorInfoUiState correctly when deathDate is not null`() {
//
//        val actor = createMockActorWithDeathDate()
//        val expectedActorInfoUiState = ActorDetailsScreenState.ActorInfoUiState(
//            name = "Jane Smith",
//            biography = "Legendary actress biography",
//            birthdayDate = "1970-05-15",
//            placeOfBirth = "Los Angeles, USA",
//            deathDate = "2020-12-31",
//            headerPictures = listOf("/header1.jpg", "/header2.jpg"),
//            department = "Acting"
//        )
//
//        val result = actor.toActorInfoUI()
//
//        assertEquals(expectedActorInfoUiState, result)
//    }
//
//    @Test
//    fun `toMovieUI should handle empty posterImageURL correctly`() {
//        // Given
//        val movie = createMockMovie().copy(posterImageURL = "")
//        val expectedMovieUiState = ActorDetailsScreenState.MovieUiState(
//            id = 456L,
//            posterPictureURL = "",
//            isSaved = false
//        )
//
//        val result = movie.to()
//
//        assertEquals(expectedMovieUiState, result)
//    }
//
//    @Test
//    fun `toTvShowUI should handle empty posterImageURL correctly`() {
//
//        val tvShow = createMockTvShow().copy(posterImageURL = "")
//        val expectedTvShowUiState = ActorDetailsScreenState.TvShowUiState(
//            id = 789L,
//            posterPictureURL = "",
//            isSaved = false
//        )
//
//        val result = tvShow.toTvShowUI()
//
//        assertEquals(expectedTvShowUiState, result)
//    }
//
//    @Test
//    fun `toActorInfoUI should handle empty biography correctly`() {
//
//        val actor = createMockActorWithoutDeathDate().copy(biography = "")
//        val expectedActorInfoUiState = ActorDetailsScreenState.ActorInfoUiState(
//            name = "John Doe",
//            biography = "",
//            birthdayDate = "1980-01-01",
//            placeOfBirth = "New York, USA",
//            deathDate = null,
//            headerPictures = listOf("/header1.jpg", "/header2.jpg", "/header3.jpg"),
//            department = "Acting"
//        )
//
//        val result = actor.toActorInfoUI()
//
//        assertEquals(expectedActorInfoUiState, result)
//    }
//
//    @Test
//    fun `toActorInfoUI should handle empty headerPictures list correctly`() {
//
//        val actor = createMockActorWithoutDeathDate().copy(headerPictures = emptyList())
//        val expectedActorInfoUiState = ActorDetailsScreenState.ActorInfoUiState(
//            name = "John Doe",
//            biography = "Famous actor biography",
//            birthdayDate = "1980-01-01",
//            placeOfBirth = "New York, USA",
//            deathDate = null,
//            headerPictures = emptyList(),
//            department = "Acting"
//        )
//
//        val result = actor.toActorInfoUI()
//
//        assertEquals(expectedActorInfoUiState, result)
//    }
//
//    companion object {
//        private fun createMockMovie() = Movie(
//            id = 456L,
//            title = "Test Movie",
//            genres = listOf(Genre(123L, "Action")),
//            averageRating = 8.0,
//            userRating = 7.5,
//            releaseDate = LocalDate.parse("2023-01-01"),
//            overview = "Test movie overview",
//            posterImageURL = "/movie_poster.jpg",
//            runtimeMinutes = 120,
//            trailerURL = "https://trailer.com"
//        )
//
//        private fun createMockTvShow() = TvShow(
//            id = 789L,
//            title = "Test TV Show",
//            genres = listOf(Genre(654L, "Comedy")),
//            averageRating = 7.9,
//            userRating = 8,
//            releaseDate = LocalDate.parse("2023-01-01"),
//            overview = "Test overview for TV Show",
//            posterImageURL = "/tv_poster.jpg",
//            numberOfSeasons = 3,
//            trailerURL = "https://trailer.com",
//            headerImagesURLs = listOf("/header1.jpg", "/header2.jpg", "/header3.jpg")
//        )
//
//        private fun createMockActorWithoutDeathDate() = Actor(
//            id = 123L,
//            name = "John Doe",
//            profilePictureURL = "/profile.jpg",
//            birthDate = LocalDate.parse("1980-01-01"),
//            placeOfBirth = "New York, USA",
//            deathDate = null,
//            biography = "Famous actor biography",
//            headerPictures = listOf("/header1.jpg", "/header2.jpg", "/header3.jpg"),
//            department = "Acting"
//        )
//
//        private fun createMockActorWithDeathDate() = Actor(
//            id = 124L,
//            name = "Jane Smith",
//            profilePictureURL = "/profile2.jpg",
//            birthDate = LocalDate.parse("1970-05-15"),
//            placeOfBirth = "Los Angeles, USA",
//            deathDate = LocalDate.parse("2020-12-31"),
//            biography = "Legendary actress biography",
//            headerPictures = listOf("/header1.jpg", "/header2.jpg"),
//            department = "Acting"
//        )
//    }
// }
