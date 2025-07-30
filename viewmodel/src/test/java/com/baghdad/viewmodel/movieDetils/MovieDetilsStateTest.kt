package com.baghdad.viewmodel.movieDetails

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MovieDetailsStateTest {

    @Test
    fun `constructor with all parameters should set values correctly`() {
        val movieImages = listOf(imageUrl1, imageUrl2)
        val categories = listOf(createCategoryUiState())
        val castMembers = listOf(createActorCardInfo())
        val moreLikeThisMovies = listOf(createMoreLikeThisMovie())

        val state = MovieDetailsState(
            movieId = movieId,
            movieImages = movieImages,
            movieName = movieName,
            movieTrailerURL = trailerUrl,
            categories = categories,
            rating = movieRating,
            duration = duration,
            date = releaseDate,
            overView = overview,
            castMembers = castMembers,
            posterImageURL = posterUrl,
            moreLikeThisMovie = moreLikeThisMovies,
            isExtendText = true,
            isStared = true,
            isSaved = true,
            isHasTrailer = false,
            isLoading = true
        )

        Assertions.assertEquals(movieId, state.movieId)
        Assertions.assertEquals(movieImages, state.movieImages)
        Assertions.assertEquals(movieName, state.movieName)
        Assertions.assertEquals(trailerUrl, state.movieTrailerURL)
        Assertions.assertEquals(categories, state.categories)
        Assertions.assertEquals(movieRating, state.rating)
        Assertions.assertEquals(duration, state.duration)
        Assertions.assertEquals(releaseDate, state.date)
        Assertions.assertEquals(overview, state.overView)
        Assertions.assertEquals(castMembers, state.castMembers)
        Assertions.assertEquals(posterUrl, state.posterImageURL)
        Assertions.assertEquals(moreLikeThisMovies, state.moreLikeThisMovie)
        Assertions.assertTrue(state.isExtendText)
        Assertions.assertTrue(state.isStared)
        Assertions.assertTrue(state.isSaved)
        Assertions.assertFalse(state.isHasTrailer)
        Assertions.assertTrue(state.isLoading)
    }

    @Test
    fun `isLoading property should be accessible from BaseUiState interface`() {
        val state = MovieDetailsState(isLoading = true)

        Assertions.assertTrue(state.isLoading)
    }

    @Test
    fun `copy function should work correctly with movieId change`() {
        val originalState = MovieDetailsState(movieId = 123L)
        val newMovieId = 456L

        val newState = originalState.copy(movieId = newMovieId)

        Assertions.assertEquals(newMovieId, newState.movieId)
        Assertions.assertEquals(123L, originalState.movieId) // Original unchanged
    }

    @Test
    fun `copy function should work correctly with boolean flags`() {
        val originalState = MovieDetailsState(
            isExtendText = false,
            isStared = false,
            isSaved = false,
            isHasTrailer = true
        )

        val newState = originalState.copy(
            isExtendText = true,
            isStared = true,
            isSaved = true,
            isHasTrailer = false
        )

        Assertions.assertTrue(newState.isExtendText)
        Assertions.assertTrue(newState.isStared)
        Assertions.assertTrue(newState.isSaved)
        Assertions.assertFalse(newState.isHasTrailer)

        Assertions.assertFalse(originalState.isExtendText)
        Assertions.assertFalse(originalState.isStared)
        Assertions.assertFalse(originalState.isSaved)
        Assertions.assertTrue(originalState.isHasTrailer)
    }

    @Test
    fun `copy function should work correctly with lists`() {
        val originalImages = listOf(imageUrl1)
        val newImages = listOf(imageUrl2, imageUrl3)
        val originalState = MovieDetailsState(movieImages = originalImages)

        val newState = originalState.copy(movieImages = newImages)

        Assertions.assertEquals(newImages, newState.movieImages)
        Assertions.assertEquals(originalImages, originalState.movieImages)
    }

    @Test
    fun `equals should work correctly for identical states`() {
        val state1 = MovieDetailsState(
            movieId = movieId,
            movieName = movieName,
            rating = movieRating
        )
        val state2 = MovieDetailsState(
            movieId = movieId,
            movieName = movieName,
            rating = movieRating
        )

        Assertions.assertEquals(state1, state2)
    }

    @Test
    fun `equals should work correctly for different states`() {
        val state1 = MovieDetailsState(movieId = 123L)
        val state2 = MovieDetailsState(movieId = 456L)

        Assertions.assertNotEquals(state1, state2)
    }

    @Test
    fun `hashCode should be consistent for identical states`() {
        val state1 = MovieDetailsState(movieId = movieId, movieName = movieName)
        val state2 = MovieDetailsState(movieId = movieId, movieName = movieName)

        Assertions.assertEquals(state1.hashCode(), state2.hashCode())
    }

    @Test
    fun `ActorCardInfo default constructor should create with default values`() {
        val actorInfo = MovieDetailsState.ActorCardInfo()

        Assertions.assertEquals("", actorInfo.name)
        Assertions.assertNull(actorInfo.imageUrl)
        Assertions.assertEquals("", actorInfo.characterName)
        Assertions.assertEquals(0, actorInfo.id)
    }

    @Test
    fun `ActorCardInfo constructor with parameters should set values correctly`() {
        val actorInfo = MovieDetailsState.ActorCardInfo(
            name = actorName,
            imageUrl = actorImageUrl,
            characterName = characterName,
            id = actorId
        )

        Assertions.assertEquals(actorName, actorInfo.name)
        Assertions.assertEquals(actorImageUrl, actorInfo.imageUrl)
        Assertions.assertEquals(characterName, actorInfo.characterName)
        Assertions.assertEquals(actorId, actorInfo.id)
    }

    @Test
    fun `ActorCardInfo with null imageUrl should work correctly`() {
        val actorInfo = MovieDetailsState.ActorCardInfo(
            name = actorName,
            imageUrl = null,
            characterName = characterName,
            id = actorId
        )

        Assertions.assertEquals(actorName, actorInfo.name)
        Assertions.assertNull(actorInfo.imageUrl)
        Assertions.assertEquals(characterName, actorInfo.characterName)
        Assertions.assertEquals(actorId, actorInfo.id)
    }

    @Test
    fun `ActorCardInfo copy function should work correctly`() {
        val originalActor = MovieDetailsState.ActorCardInfo(name = "Original")
        val copiedActor = originalActor.copy(name = "Copied")

        Assertions.assertEquals("Copied", copiedActor.name)
        Assertions.assertEquals("Original", originalActor.name) // Original unchanged
    }

    // MoreLikeThisMovie Tests
    @Test
    fun `MoreLikeThisMovie default constructor should create with default values`() {
        val movie = MovieDetailsState.MoreLikeThisMovie()

        Assertions.assertEquals("", movie.imageUrl)
        Assertions.assertEquals(0L, movie.id)
        Assertions.assertFalse(movie.isSaved)
    }

    @Test
    fun `MoreLikeThisMovie constructor with parameters should set values correctly`() {
        val movie = MovieDetailsState.MoreLikeThisMovie(
            imageUrl = imageUrl1,
            id = movieId,
            isSaved = true
        )

        Assertions.assertEquals(imageUrl1, movie.imageUrl)
        Assertions.assertEquals(movieId, movie.id)
        Assertions.assertTrue(movie.isSaved)
    }

    @Test
    fun `MoreLikeThisMovie copy function should work correctly`() {
        val originalMovie = MovieDetailsState.MoreLikeThisMovie(isSaved = false)
        val copiedMovie = originalMovie.copy(isSaved = true)

        Assertions.assertTrue(copiedMovie.isSaved)
        Assertions.assertFalse(originalMovie.isSaved)
    }


    @Test
    fun `CategoryUiState copy function should work correctly`() {
        val originalCategory = MovieDetailsState.CategoryUiState(name = "Original")
        val copiedCategory = originalCategory.copy(name = "Copied")

        Assertions.assertEquals("Copied", copiedCategory.name)
        Assertions.assertEquals("Original", originalCategory.name)
    }

    @Test
    fun `state with multiple cast members should preserve order`() {
        val castMembers = listOf(
            createActorCardInfo(name = "Actor 1"),
            createActorCardInfo(name = "Actor 2"),
            createActorCardInfo(name = "Actor 3")
        )
        val state = MovieDetailsState(castMembers = castMembers)

        Assertions.assertEquals(3, state.castMembers.size)
        Assertions.assertEquals("Actor 1", state.castMembers[0].name)
        Assertions.assertEquals("Actor 2", state.castMembers[1].name)
        Assertions.assertEquals("Actor 3", state.castMembers[2].name)
    }

    @Test
    fun `state with multiple categories should preserve order`() {
        val categories = listOf(
            MovieDetailsState.CategoryUiState(id = 1L, name = "Action"),
            MovieDetailsState.CategoryUiState(id = 2L, name = "Drama"),
            MovieDetailsState.CategoryUiState(id = 3L, name = "Comedy")
        )
        val state = MovieDetailsState(categories = categories)

        Assertions.assertEquals(3, state.categories.size)
        Assertions.assertEquals("Action", state.categories[0].name)
        Assertions.assertEquals("Drama", state.categories[1].name)
        Assertions.assertEquals("Comedy", state.categories[2].name)
    }

    @Test
    fun `state with multiple similar movies should preserve order`() {
        val moreLikeThis = listOf(
            MovieDetailsState.MoreLikeThisMovie(id = 1L, imageUrl = "movie1.jpg"),
            MovieDetailsState.MoreLikeThisMovie(id = 2L, imageUrl = "movie2.jpg"),
            MovieDetailsState.MoreLikeThisMovie(id = 3L, imageUrl = "movie3.jpg")
        )
        val state = MovieDetailsState(moreLikeThisMovie = moreLikeThis)

        Assertions.assertEquals(3, state.moreLikeThisMovie.size)
        Assertions.assertEquals("movie1.jpg", state.moreLikeThisMovie[0].imageUrl)
        Assertions.assertEquals("movie2.jpg", state.moreLikeThisMovie[1].imageUrl)
        Assertions.assertEquals("movie3.jpg", state.moreLikeThisMovie[2].imageUrl)
    }

    private fun createActorCardInfo(name: String = actorName) = MovieDetailsState.ActorCardInfo(
        name = name,
        imageUrl = actorImageUrl,
        characterName = characterName,
        id = actorId
    )

    private fun createMoreLikeThisMovie() = MovieDetailsState.MoreLikeThisMovie(
        imageUrl = imageUrl1,
        id = movieId,
        isSaved = false
    )

    private fun createCategoryUiState() = MovieDetailsState.CategoryUiState(
        id = categoryId,
        name = categoryName
    )

    private companion object {
        const val movieId = 123L
        const val movieName = "The Dark Knight"
        const val trailerUrl = "https://trailer.com/darkknight"
        const val movieRating = 9.0
        const val duration = 152
        const val releaseDate = "2008-07-18"
        const val overview = "Batman faces the Joker"
        const val posterUrl = "https://poster.com/darkknight.jpg"
        const val imageUrl1 = "https://image1.com/img.jpg"
        const val imageUrl2 = "https://image2.com/img.jpg"
        const val imageUrl3 = "https://image3.com/img.jpg"
        const val actorName = "Christian Bale"
        const val actorImageUrl = "https://actor.com/bale.jpg"
        const val characterName = "Bruce Wayne"
        const val actorId = 456
        const val categoryId = 789L
        const val categoryName = "Action"
    }
}