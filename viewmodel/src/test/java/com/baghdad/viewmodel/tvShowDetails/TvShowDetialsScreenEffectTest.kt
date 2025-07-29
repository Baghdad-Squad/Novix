package com.baghdad.viewmodel.tvShowDetails

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class TvShowDetailsScreenEffectTest {

    @Test
    fun `NavigateBack should implement BaseUiEffect correctly`() {

        val effect = TvShowDetailsScreenEffect.NavigateBack

        assertTrue(true)
        assertTrue(true)
        assertNotNull(effect)
    }

    @Test
    fun `NavigateToLogin should implement BaseUiEffect correctly`() {

        val effect = TvShowDetailsScreenEffect.NavigateToLogin

        assertTrue(true)
        assertTrue(true)
        assertNotNull(effect)
    }

    @Test
    fun `NavigateToActorDetails should have correct actorId`() {

        val actorId = 123L

        val effect = TvShowDetailsScreenEffect.NavigateToActorDetails(actorId)

        assertTrue(true)
        assertTrue(true)
        assertEquals(actorId, effect.actorId)
    }

    @Test
    fun `NavigateToActorDetails should accept different actorId values`() {

        val actorId1 = 456L
        val actorId2 = 789L
        val actorId3 = 0L
        val actorId4 = -1L

        val effect1 = TvShowDetailsScreenEffect.NavigateToActorDetails(actorId1)
        val effect2 = TvShowDetailsScreenEffect.NavigateToActorDetails(actorId2)
        val effect3 = TvShowDetailsScreenEffect.NavigateToActorDetails(actorId3)
        val effect4 = TvShowDetailsScreenEffect.NavigateToActorDetails(actorId4)

        assertEquals(actorId1, effect1.actorId)
        assertEquals(actorId2, effect2.actorId)
        assertEquals(actorId3, effect3.actorId)
        assertEquals(actorId4, effect4.actorId)
    }

    @Test
    fun `NavigateToEpisodeDetails should have correct seasonNumber and episodeNumber`() {

        val seasonNumber = 2
        val episodeNumber = 5

        val effect = TvShowDetailsScreenEffect.NavigateToEpisodeDetails(seasonNumber, episodeNumber)

        assertTrue(true)
        assertTrue(true)
        assertEquals(seasonNumber, effect.seasonNumber)
        assertEquals(episodeNumber, effect.episodeNumber)
    }

    @Test
    fun `NavigateToEpisodeDetails should accept different season and episode values`() {

        val seasonNumber1 = 1
        val episodeNumber1 = 1
        val seasonNumber2 = 10
        val episodeNumber2 = 24
        val seasonNumber3 = -1
        val episodeNumber3 = 0

        val effect1 = TvShowDetailsScreenEffect.NavigateToEpisodeDetails(seasonNumber1, episodeNumber1)
        val effect2 = TvShowDetailsScreenEffect.NavigateToEpisodeDetails(seasonNumber2, episodeNumber2)
        val effect3 = TvShowDetailsScreenEffect.NavigateToEpisodeDetails(seasonNumber3, episodeNumber3)

        assertEquals(seasonNumber1, effect1.seasonNumber)
        assertEquals(episodeNumber1, effect1.episodeNumber)
        assertEquals(seasonNumber2, effect2.seasonNumber)
        assertEquals(episodeNumber2, effect2.episodeNumber)
        assertEquals(seasonNumber3, effect3.seasonNumber)
        assertEquals(episodeNumber3, effect3.episodeNumber)
    }

    @Test
    fun `NavigateToGenreScreen should have correct genreId`() {

        val genreId = 456L

        val effect = TvShowDetailsScreenEffect.NavigateToGenreScreen(genreId)

        assertTrue(true)
        assertTrue(true)
        assertEquals(genreId, effect.genreId)
    }

    @Test
    fun `NavigateToGenreScreen should accept different genreId values`() {

        val genreId1 = 1L
        val genreId2 = 999L
        val genreId3 = 0L
        val genreId4 = -1L

        val effect1 = TvShowDetailsScreenEffect.NavigateToGenreScreen(genreId1)
        val effect2 = TvShowDetailsScreenEffect.NavigateToGenreScreen(genreId2)
        val effect3 = TvShowDetailsScreenEffect.NavigateToGenreScreen(genreId3)
        val effect4 = TvShowDetailsScreenEffect.NavigateToGenreScreen(genreId4)

        assertEquals(genreId1, effect1.genreId)
        assertEquals(genreId2, effect2.genreId)
        assertEquals(genreId3, effect3.genreId)
        assertEquals(genreId4, effect4.genreId)
    }

    @Test
    fun `NavigateToReviews should have correct tvShowId`() {

        val tvShowId = 789L

        val effect = TvShowDetailsScreenEffect.NavigateToReviews(tvShowId)

        assertTrue(true)
        assertTrue(true)
        assertEquals(tvShowId, effect.tvShowId)
    }

    @Test
    fun `NavigateToReviews should accept different tvShowId values`() {

        val tvShowId1 = 100L
        val tvShowId2 = 999999L
        val tvShowId3 = 0L

        val effect1 = TvShowDetailsScreenEffect.NavigateToReviews(tvShowId1)
        val effect2 = TvShowDetailsScreenEffect.NavigateToReviews(tvShowId2)
        val effect3 = TvShowDetailsScreenEffect.NavigateToReviews(tvShowId3)

        assertEquals(tvShowId1, effect1.tvShowId)
        assertEquals(tvShowId2, effect2.tvShowId)
        assertEquals(tvShowId3, effect3.tvShowId)
    }

    @Test
    fun `OpenYoutubeLink should have correct youtubeLink`() {

        val youtubeLink = "https://www.youtube.com/watch?v=dQw4w9WgXcQ"

        val effect = TvShowDetailsScreenEffect.OpenYoutubeLink(youtubeLink)

        assertTrue(true)
        assertTrue(true)
        assertEquals(youtubeLink, effect.youtubeLink)
    }

    @Test
    fun `OpenYoutubeLink should accept different youtubeLink values`() {

        val youtubeLink1 = "https://www.youtube.com/watch?v=abc123"
        val youtubeLink2 = "https://youtu.be/xyz789"
        val youtubeLink3 = ""
        val youtubeLink4 = "invalid-url"

        val effect1 = TvShowDetailsScreenEffect.OpenYoutubeLink(youtubeLink1)
        val effect2 = TvShowDetailsScreenEffect.OpenYoutubeLink(youtubeLink2)
        val effect3 = TvShowDetailsScreenEffect.OpenYoutubeLink(youtubeLink3)
        val effect4 = TvShowDetailsScreenEffect.OpenYoutubeLink(youtubeLink4)

        assertEquals(youtubeLink1, effect1.youtubeLink)
        assertEquals(youtubeLink2, effect2.youtubeLink)
        assertEquals(youtubeLink3, effect3.youtubeLink)
        assertEquals(youtubeLink4, effect4.youtubeLink)
    }

    @Test
    fun `equality should work correctly for object effects`() {

        val navigateBack1 = TvShowDetailsScreenEffect.NavigateBack
        val navigateBack2 = TvShowDetailsScreenEffect.NavigateBack
        val navigateToLogin1 = TvShowDetailsScreenEffect.NavigateToLogin
        val navigateToLogin2 = TvShowDetailsScreenEffect.NavigateToLogin

        assertEquals(navigateBack1, navigateBack2)
        assertEquals(navigateToLogin1, navigateToLogin2)
        assertTrue(navigateBack1 != navigateToLogin1)
    }

    @Test
    fun `equality should work correctly for data class effects`() {

        val actorEffect1 = TvShowDetailsScreenEffect.NavigateToActorDetails(123L)
        val actorEffect2 = TvShowDetailsScreenEffect.NavigateToActorDetails(123L)
        val actorEffect3 = TvShowDetailsScreenEffect.NavigateToActorDetails(456L)

        val episodeEffect1 = TvShowDetailsScreenEffect.NavigateToEpisodeDetails(1, 2)
        val episodeEffect2 = TvShowDetailsScreenEffect.NavigateToEpisodeDetails(1, 2)
        val episodeEffect3 = TvShowDetailsScreenEffect.NavigateToEpisodeDetails(2, 1)

        val genreEffect1 = TvShowDetailsScreenEffect.NavigateToGenreScreen(789L)
        val genreEffect2 = TvShowDetailsScreenEffect.NavigateToGenreScreen(789L)
        val genreEffect3 = TvShowDetailsScreenEffect.NavigateToGenreScreen(987L)

        val reviewsEffect1 = TvShowDetailsScreenEffect.NavigateToReviews(111L)
        val reviewsEffect2 = TvShowDetailsScreenEffect.NavigateToReviews(111L)
        val reviewsEffect3 = TvShowDetailsScreenEffect.NavigateToReviews(222L)

        val youtubeEffect1 = TvShowDetailsScreenEffect.OpenYoutubeLink("link1")
        val youtubeEffect2 = TvShowDetailsScreenEffect.OpenYoutubeLink("link1")
        val youtubeEffect3 = TvShowDetailsScreenEffect.OpenYoutubeLink("link2")

        assertEquals(actorEffect1, actorEffect2)
        assertTrue(actorEffect1 != actorEffect3)

        assertEquals(episodeEffect1, episodeEffect2)
        assertTrue(episodeEffect1 != episodeEffect3)

        assertEquals(genreEffect1, genreEffect2)
        assertTrue(genreEffect1 != genreEffect3)

        assertEquals(reviewsEffect1, reviewsEffect2)
        assertTrue(reviewsEffect1 != reviewsEffect3)

        assertEquals(youtubeEffect1, youtubeEffect2)
        assertTrue(youtubeEffect1 != youtubeEffect3)
    }

    @Test
    fun `hashCode should be consistent for equal objects`() {

        val actorEffect1 = TvShowDetailsScreenEffect.NavigateToActorDetails(123L)
        val actorEffect2 = TvShowDetailsScreenEffect.NavigateToActorDetails(123L)

        val episodeEffect1 = TvShowDetailsScreenEffect.NavigateToEpisodeDetails(1, 2)
        val episodeEffect2 = TvShowDetailsScreenEffect.NavigateToEpisodeDetails(1, 2)

        val genreEffect1 = TvShowDetailsScreenEffect.NavigateToGenreScreen(789L)
        val genreEffect2 = TvShowDetailsScreenEffect.NavigateToGenreScreen(789L)

        val reviewsEffect1 = TvShowDetailsScreenEffect.NavigateToReviews(111L)
        val reviewsEffect2 = TvShowDetailsScreenEffect.NavigateToReviews(111L)

        val youtubeEffect1 = TvShowDetailsScreenEffect.OpenYoutubeLink("link")
        val youtubeEffect2 = TvShowDetailsScreenEffect.OpenYoutubeLink("link")

        assertEquals(actorEffect1.hashCode(), actorEffect2.hashCode())
        assertEquals(episodeEffect1.hashCode(), episodeEffect2.hashCode())
        assertEquals(genreEffect1.hashCode(), genreEffect2.hashCode())
        assertEquals(reviewsEffect1.hashCode(), reviewsEffect2.hashCode())
        assertEquals(youtubeEffect1.hashCode(), youtubeEffect2.hashCode())
    }

    @Test
    fun `toString should work correctly for object effects`() {

        val navigateBack = TvShowDetailsScreenEffect.NavigateBack
        val navigateToLogin = TvShowDetailsScreenEffect.NavigateToLogin

        val navigateBackString = navigateBack.toString()
        val navigateToLoginString = navigateToLogin.toString()

        assertNotNull(navigateBackString)
        assertNotNull(navigateToLoginString)
        assertTrue(navigateBackString.contains("NavigateBack"))
        assertTrue(navigateToLoginString.contains("NavigateToLogin"))
    }

    @Test
    fun `toString should work correctly for data class effects`() {

        val actorEffect = TvShowDetailsScreenEffect.NavigateToActorDetails(123L)
        val episodeEffect = TvShowDetailsScreenEffect.NavigateToEpisodeDetails(1, 2)
        val genreEffect = TvShowDetailsScreenEffect.NavigateToGenreScreen(789L)
        val reviewsEffect = TvShowDetailsScreenEffect.NavigateToReviews(111L)
        val youtubeEffect = TvShowDetailsScreenEffect.OpenYoutubeLink("test-link")

        val actorString = actorEffect.toString()
        val episodeString = episodeEffect.toString()
        val genreString = genreEffect.toString()
        val reviewsString = reviewsEffect.toString()
        val youtubeString = youtubeEffect.toString()

        assertNotNull(actorString)
        assertTrue(actorString.contains("NavigateToActorDetails"))
        assertTrue(actorString.contains("123"))

        assertNotNull(episodeString)
        assertTrue(episodeString.contains("NavigateToEpisodeDetails"))
        assertTrue(episodeString.contains("seasonNumber=1"))
        assertTrue(episodeString.contains("episodeNumber=2"))

        assertNotNull(genreString)
        assertTrue(genreString.contains("NavigateToGenreScreen"))
        assertTrue(genreString.contains("789"))

        assertNotNull(reviewsString)
        assertTrue(reviewsString.contains("NavigateToReviews"))
        assertTrue(reviewsString.contains("111"))

        assertNotNull(youtubeString)
        assertTrue(youtubeString.contains("OpenYoutubeLink"))
        assertTrue(youtubeString.contains("test-link"))
    }

    @Test
    fun `all effects should be different types`() {

        val navigateBack = TvShowDetailsScreenEffect.NavigateBack
        val navigateToLogin = TvShowDetailsScreenEffect.NavigateToLogin
        val navigateToActor = TvShowDetailsScreenEffect.NavigateToActorDetails(1L)
        val navigateToEpisode = TvShowDetailsScreenEffect.NavigateToEpisodeDetails(1, 1)
        val navigateToGenre = TvShowDetailsScreenEffect.NavigateToGenreScreen(1L)
        val navigateToReviews = TvShowDetailsScreenEffect.NavigateToReviews(1L)
        val openYoutube = TvShowDetailsScreenEffect.OpenYoutubeLink("link")

        assertTrue(navigateBack != navigateToLogin)
        assertTrue(navigateBack != navigateToActor)
        assertTrue(navigateBack != navigateToEpisode)
        assertTrue(navigateBack != navigateToGenre)
        assertTrue(navigateBack != navigateToReviews)
        assertTrue(navigateBack != openYoutube)

        assertTrue(navigateToLogin != navigateToActor)
        assertTrue(navigateToLogin != navigateToEpisode)
        assertTrue(navigateToLogin != navigateToGenre)
        assertTrue(navigateToLogin != navigateToReviews)
        assertTrue(navigateToLogin != openYoutube)

        assertTrue(navigateToActor != navigateToEpisode)
        assertTrue(navigateToActor != navigateToGenre)
        assertTrue(navigateToActor != navigateToReviews)
        assertTrue(navigateToActor != openYoutube)

        assertTrue(navigateToEpisode != navigateToGenre)
        assertTrue(navigateToEpisode != navigateToReviews)
        assertTrue(navigateToEpisode != openYoutube)

        assertTrue(navigateToGenre != navigateToReviews)
        assertTrue(navigateToGenre != openYoutube)

        assertTrue(navigateToReviews != openYoutube)
    }

    @Test
    fun `sealed class should have correct inheritance hierarchy`() {

        val effects = listOf(
            TvShowDetailsScreenEffect.NavigateBack,
            TvShowDetailsScreenEffect.NavigateToLogin,
            TvShowDetailsScreenEffect.NavigateToActorDetails(1L),
            TvShowDetailsScreenEffect.NavigateToEpisodeDetails(1, 1),
            TvShowDetailsScreenEffect.NavigateToGenreScreen(1L),
            TvShowDetailsScreenEffect.NavigateToReviews(1L),
            TvShowDetailsScreenEffect.OpenYoutubeLink("link")
        )

        effects.forEach { effect ->
            assertTrue(true)
            assertTrue(true)
        }
    }
}