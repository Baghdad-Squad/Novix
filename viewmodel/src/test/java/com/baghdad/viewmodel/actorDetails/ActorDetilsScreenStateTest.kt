package com.baghdad.viewmodel.actorDetails

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ActorDetailsScreenStateTest {

    @Test
    fun `default ActorDetailsScreenState should have correct initial values`() {
        // Given
        // When
        val state = ActorDetailsScreenState()
        // Then
        assertEquals(emptyList<ActorDetailsScreenState.MovieUiState>(), state.topMoviesPicks)
        assertEquals(emptyList<ActorDetailsScreenState.TvShowUiState>(), state.topTvShowsPicks)
        assertEquals(emptyList<String>(), state.gallery)
        assertEquals(ActorDetailsScreenState.ActorInfoUiState(), state.actorInfo)
        assertFalse(state.isTextExpanded)
        assertFalse(state.isMoviesMoreThanTen)
        assertFalse(state.isTvShowsMoreThanTen)
        assertFalse(state.isGalleryMoreThanTen)
        assertFalse(state.isLoading)
    }

    @Test
    fun `ActorDetailsScreenState with custom values should return correct values`() {
        // Given
        val mockMovies = createMockMovieUiStateList()
        val mockTvShows = createMockTvShowUiStateList()
        val mockGallery = listOf("/image1.jpg", "/image2.jpg", "/image3.jpg")
        val mockActorInfo = createMockActorInfoUiState()
        // When
        val state = ActorDetailsScreenState(
            topMoviesPicks = mockMovies,
            topTvShowsPicks = mockTvShows,
            gallery = mockGallery,
            actorInfo = mockActorInfo,
            isTextExpanded = true,
            isMoviesMoreThanTen = true,
            isTvShowsMoreThanTen = true,
            isGalleryMoreThanTen = true,
            isLoading = true
        )
        // Then
        assertEquals(mockMovies, state.topMoviesPicks)
        assertEquals(mockTvShows, state.topTvShowsPicks)
        assertEquals(mockGallery, state.gallery)
        assertEquals(mockActorInfo, state.actorInfo)
        assertTrue(state.isTextExpanded)
        assertTrue(state.isMoviesMoreThanTen)
        assertTrue(state.isTvShowsMoreThanTen)
        assertTrue(state.isGalleryMoreThanTen)
        assertTrue(state.isLoading)
    }

    @Test
    fun `ActorDetailsScreenState should implement BaseUiState correctly`() {
        // Given + When
        val loadingState = ActorDetailsScreenState(isLoading = true)
        val notLoadingState = ActorDetailsScreenState(isLoading = false)
        // Then
        assertTrue(loadingState.isLoading)
        assertFalse(notLoadingState.isLoading)
    }

    @Test
    fun `copy function should work correctly with all parameters`() {
        // Given
        val originalState = ActorDetailsScreenState()
        val newMovies = createMockMovieUiStateList()
        val newActorInfo = createMockActorInfoUiState()
        // When
        val copiedState = originalState.copy(
            topMoviesPicks = newMovies,
            actorInfo = newActorInfo,
            isTextExpanded = true,
            isLoading = true
        )
        // Then
        assertEquals(newMovies, copiedState.topMoviesPicks)
        assertEquals(newActorInfo, copiedState.actorInfo)
        assertTrue(copiedState.isTextExpanded)
        assertTrue(copiedState.isLoading)
        assertEquals(emptyList<ActorDetailsScreenState.TvShowUiState>(), copiedState.topTvShowsPicks)
        assertEquals(emptyList<String>(), copiedState.gallery)
        assertFalse(copiedState.isMoviesMoreThanTen)
    }

    @Test
    fun `MovieUiState should have correct default values`() {
        // Given + When
        val movieUiState = ActorDetailsScreenState.MovieUiState()
        // Then
        assertEquals(0L, movieUiState.id)
        assertEquals("", movieUiState.posterPictureURL)
        assertFalse(movieUiState.isSaved)
    }

    @Test
    fun `MovieUiState should accept custom values correctly`() {
        // Given
        val movieId = 123L
        val posterUrl = "/movie_poster.jpg"
        val isSaved = true
        // When
        val movieUiState = ActorDetailsScreenState.MovieUiState(
            id = movieId,
            posterPictureURL = posterUrl,
            isSaved = isSaved
        )
        // Then
        assertEquals(movieId, movieUiState.id)
        assertEquals(posterUrl, movieUiState.posterPictureURL)
        assertTrue(movieUiState.isSaved)
    }

    @Test
    fun `TvShowUiState should have correct default values`() {
        // Given + When
        val tvShowUiState = ActorDetailsScreenState.TvShowUiState()
        // Then
        assertEquals(0L, tvShowUiState.id)
        assertEquals("", tvShowUiState.posterPictureURL)
        assertFalse(tvShowUiState.isSaved)
    }

    @Test
    fun `TvShowUiState should accept custom values correctly`() {
        // Given
        val tvShowId = 456L
        val posterUrl = "/tvshow_poster.jpg"
        val isSaved = true
        // When
        val tvShowUiState = ActorDetailsScreenState.TvShowUiState(
            id = tvShowId,
            posterPictureURL = posterUrl,
            isSaved = isSaved
        )
        // Then
        assertEquals(tvShowId, tvShowUiState.id)
        assertEquals(posterUrl, tvShowUiState.posterPictureURL)
        assertTrue(tvShowUiState.isSaved)
    }

    @Test
    fun `ActorInfoUiState should have correct default values`() {
        // Given + When
        val actorInfoUiState = ActorDetailsScreenState.ActorInfoUiState()
        // Then
        assertEquals("", actorInfoUiState.name)
        assertEquals(emptyList<String>(), actorInfoUiState.headerPictures)
        assertEquals("", actorInfoUiState.birthdayDate)
        assertEquals("", actorInfoUiState.placeOfBirth)
        assertNull(actorInfoUiState.deathDate)
        assertEquals("", actorInfoUiState.biography)
        assertEquals("", actorInfoUiState.department)
    }

    @Test
    fun `ActorInfoUiState should accept custom values correctly`() {
        // Given
        val name = "John Doe"
        val headerPictures = listOf("/header1.jpg", "/header2.jpg")
        val birthdayDate = "1980-01-01"
        val placeOfBirth = "New York, USA"
        val deathDate = "2023-12-31"
        val biography = "Famous actor biography"
        val department = "Acting"
        // When
        val actorInfoUiState = ActorDetailsScreenState.ActorInfoUiState(
            name = name,
            headerPictures = headerPictures,
            birthdayDate = birthdayDate,
            placeOfBirth = placeOfBirth,
            deathDate = deathDate,
            biography = biography,
            department = department
        )
        // Then
        assertEquals(name, actorInfoUiState.name)
        assertEquals(headerPictures, actorInfoUiState.headerPictures)
        assertEquals(birthdayDate, actorInfoUiState.birthdayDate)
        assertEquals(placeOfBirth, actorInfoUiState.placeOfBirth)
        assertEquals(deathDate, actorInfoUiState.deathDate)
        assertEquals(biography, actorInfoUiState.biography)
        assertEquals(department, actorInfoUiState.department)
    }

    @Test
    fun `ActorInfoUiState with null deathDate should work correctly`() {
        // Given
        val actorInfoUiState = ActorDetailsScreenState.ActorInfoUiState(
            name = "Living Actor",
            deathDate = null
        )
        // Then
        assertEquals("Living Actor", actorInfoUiState.name)
        assertNull(actorInfoUiState.deathDate)
    }

    @Test
    fun `equality should work correctly for ActorDetailsScreenState`() {
        // Given
        val state1 = ActorDetailsScreenState(
            topMoviesPicks = createMockMovieUiStateList(),
            isTextExpanded = true,
            isLoading = false
        )
        val state2 = ActorDetailsScreenState(
            topMoviesPicks = createMockMovieUiStateList(),
            isTextExpanded = true,
            isLoading = false
        )
        val state3 = ActorDetailsScreenState(
            topMoviesPicks = createMockMovieUiStateList(),
            isTextExpanded = false,
            isLoading = false
        )
        // Then
        assertEquals(state1, state2)
        assertTrue(state1 != state3)
    }

    @Test
    fun `equality should work correctly for nested data classes`() {
        // Given + Then
        val movie1 = ActorDetailsScreenState.MovieUiState(1L, "/poster1.jpg", true)
        val movie2 = ActorDetailsScreenState.MovieUiState(1L, "/poster1.jpg", true)
        val movie3 = ActorDetailsScreenState.MovieUiState(2L, "/poster2.jpg", false)

        val tvShow1 = ActorDetailsScreenState.TvShowUiState(1L, "/tvposter1.jpg", true)
        val tvShow2 = ActorDetailsScreenState.TvShowUiState(1L, "/tvposter1.jpg", true)
        val tvShow3 = ActorDetailsScreenState.TvShowUiState(2L, "/tvposter2.jpg", false)

        val actor1 = ActorDetailsScreenState.ActorInfoUiState(name = "Actor 1")
        val actor2 = ActorDetailsScreenState.ActorInfoUiState(name = "Actor 1")
        val actor3 = ActorDetailsScreenState.ActorInfoUiState(name = "Actor 2")

        assertEquals(movie1, movie2)
        assertTrue(movie1 != movie3)
        assertEquals(tvShow1, tvShow2)
        assertTrue(tvShow1 != tvShow3)
        assertEquals(actor1, actor2)
        assertTrue(actor1 != actor3)
    }

    @Test
    fun `hashCode should be consistent for equal objects`() {
        // Given
        val state1 = ActorDetailsScreenState(isTextExpanded = true, isLoading = false)
        val state2 = ActorDetailsScreenState(isTextExpanded = true, isLoading = false)
        // Then
        assertEquals(state1.hashCode(), state2.hashCode())
    }

    @Test
    fun `toString should work correctly`() {
        // Given
        val state = ActorDetailsScreenState(
            topMoviesPicks = listOf(ActorDetailsScreenState.MovieUiState(1L, "/poster.jpg", true)),
            isTextExpanded = true,
            isLoading = false
        )
        // When
        val result = state.toString()
        // Then
        assertNotNull(result)
        assertTrue(result.contains("ActorDetailsScreenState"))
        assertTrue(result.contains("isTextExpanded=true"))
        assertTrue(result.contains("isLoading=false"))
    }

    companion object {
        private fun createMockMovieUiStateList() = listOf(
            ActorDetailsScreenState.MovieUiState(
                id = 1L,
                posterPictureURL = "/movie1_poster.jpg",
                isSaved = true
            ),
            ActorDetailsScreenState.MovieUiState(
                id = 2L,
                posterPictureURL = "/movie2_poster.jpg",
                isSaved = false
            )
        )

        private fun createMockTvShowUiStateList() = listOf(
            ActorDetailsScreenState.TvShowUiState(
                id = 1L,
                posterPictureURL = "/tvshow1_poster.jpg",
                isSaved = true
            ),
            ActorDetailsScreenState.TvShowUiState(
                id = 2L,
                posterPictureURL = "/tvshow2_poster.jpg",
                isSaved = false
            )
        )

        private fun createMockActorInfoUiState() = ActorDetailsScreenState.ActorInfoUiState(
            name = "John Doe",
            headerPictures = listOf("/header1.jpg", "/header2.jpg", "/header3.jpg"),
            birthdayDate = "1980-01-01",
            placeOfBirth = "New York, USA",
            deathDate = null,
            biography = "Famous actor with a long and successful career in Hollywood.",
            department = "Acting"
        )
    }
}
