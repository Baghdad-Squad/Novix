//package com.baghdad.viewmodel.tvShowDetails
//
//import org.junit.jupiter.api.Assertions.assertEquals
//import org.junit.jupiter.api.Assertions.assertFalse
//import org.junit.jupiter.api.Assertions.assertNotNull
//import org.junit.jupiter.api.Assertions.assertNull
//import org.junit.jupiter.api.Assertions.assertTrue
//import org.junit.jupiter.api.Test
//
//class TvShowDetailsScreenStateTest {
//
//    @Test
//    fun `default TvShowDetailsScreenState should have correct initial values`() {
//
//        val state = TvShowDetailsScreenState()
//
//        assertEquals(TvShowDetailsScreenState.TvShowInfoUiState(), state.tvShowInfo)
//        assertEquals(emptyList<TvShowDetailsScreenState.CastMemberUiState>(), state.castMembers)
//        assertEquals(emptyList<TvShowDetailsScreenState.EpisodeUiState>(), state.episodes)
//        assertEquals(0, state.selectedSeasonIndex)
//        assertFalse(state.isBottomSheetVisible)
//        assertFalse(state.isTextExpanded)
//        assertEquals(0.0, state.userRating)
//        assertTrue(state.hasTrailer)
//        assertFalse(state.isTvShowRated)
//        assertFalse(state.isTvShowSaved)
//        assertFalse(state.isLoading)
//    }
//
//    @Test
//    fun `TvShowDetailsScreenState with custom values should return correct values`() {
//
//        val mockTvShowInfo = createMockTvShowInfoUiState()
//        val mockCastMembers = createMockCastMemberUiStateList()
//        val mockEpisodes = createMockEpisodeUiStateList()
//
//        val state = TvShowDetailsScreenState(
//            tvShowInfo = mockTvShowInfo,
//            castMembers = mockCastMembers,
//            episodes = mockEpisodes,
//            selectedSeasonIndex = 2,
//            isBottomSheetVisible = true,
//            isTextExpanded = true,
//            userRating = 8.5,
//            hasTrailer = false,
//            isTvShowRated = true,
//            isTvShowSaved = true,
//            isLoading = true
//        )
//
//        assertEquals(mockTvShowInfo, state.tvShowInfo)
//        assertEquals(mockCastMembers, state.castMembers)
//        assertEquals(mockEpisodes, state.episodes)
//        assertEquals(2, state.selectedSeasonIndex)
//        assertTrue(state.isBottomSheetVisible)
//        assertTrue(state.isTextExpanded)
//        assertEquals(8.5, state.userRating)
//        assertFalse(state.hasTrailer)
//        assertTrue(state.isTvShowRated)
//        assertTrue(state.isTvShowSaved)
//        assertTrue(state.isLoading)
//    }
//
//    @Test
//    fun `TvShowDetailsScreenState should implement BaseUiState correctly`() {
//
//        val loadingState = TvShowDetailsScreenState(isLoading = true)
//        val notLoadingState = TvShowDetailsScreenState(isLoading = false)
//
//        assertTrue(loadingState.isLoading)
//        assertFalse(notLoadingState.isLoading)
//    }
//
//    @Test
//    fun `copy function should work correctly with all parameters`() {
//
//        val originalState = TvShowDetailsScreenState()
//        val newTvShowInfo = createMockTvShowInfoUiState()
//        val newCastMembers = createMockCastMemberUiStateList()
//
//        val copiedState = originalState.copy(
//            tvShowInfo = newTvShowInfo,
//            castMembers = newCastMembers,
//            selectedSeasonIndex = 3,
//            isTextExpanded = true,
//            userRating = 9.2,
//            isLoading = true
//        )
//
//        assertEquals(newTvShowInfo, copiedState.tvShowInfo)
//        assertEquals(newCastMembers, copiedState.castMembers)
//        assertEquals(3, copiedState.selectedSeasonIndex)
//        assertTrue(copiedState.isTextExpanded)
//        assertEquals(9.2, copiedState.userRating)
//        assertTrue(copiedState.isLoading)
//
//        assertEquals(emptyList<TvShowDetailsScreenState.EpisodeUiState>(), copiedState.episodes)
//        assertFalse(copiedState.isBottomSheetVisible)
//        assertTrue(copiedState.hasTrailer)
//    }
//
//    @Test
//    fun `TvShowInfoUiState should have correct default values`() {
//
//        val tvShowInfoUiState = TvShowDetailsScreenState.TvShowInfoUiState()
//
//        assertEquals("", tvShowInfoUiState.title)
//        assertEquals(emptyList<TvShowDetailsScreenState.GenreUiState>(), tvShowInfoUiState.genres)
//        assertEquals(0.0, tvShowInfoUiState.rating)
//        assertEquals("", tvShowInfoUiState.releaseDate)
//        assertEquals(0, tvShowInfoUiState.seasonCount)
//        assertEquals("", tvShowInfoUiState.overView)
//        assertEquals("", tvShowInfoUiState.trailerURL)
//        assertEquals("", tvShowInfoUiState.posterPictureURL)
//        assertEquals(emptyList<String>(), tvShowInfoUiState.headerImagesURLs)
//    }
//
//    @Test
//    fun `TvShowInfoUiState should accept custom values correctly`() {
//
//        val title = "Breaking Bad"
//        val genres = listOf(
//            TvShowDetailsScreenState.GenreUiState(1L, "Drama"),
//            TvShowDetailsScreenState.GenreUiState(2L, "Crime")
//        )
//        val rating = 9.5
//        val releaseDate = "20/01/2008"
//        val seasonCount = 5
//        val overView = "A high school chemistry teacher diagnosed with inoperable lung cancer"
//        val trailerURL = "/trailer.mp4"
//        val posterURL = "/poster.jpg"
//        val headerImages = listOf("/header1.jpg", "/header2.jpg")
//
//        val tvShowInfoUiState = TvShowDetailsScreenState.TvShowInfoUiState(
//            title = title,
//            genres = genres,
//            rating = rating,
//            releaseDate = releaseDate,
//            seasonCount = seasonCount,
//            overView = overView,
//            trailerURL = trailerURL,
//            posterPictureURL = posterURL,
//            headerImagesURLs = headerImages
//        )
//
//        assertEquals(title, tvShowInfoUiState.title)
//        assertEquals(genres, tvShowInfoUiState.genres)
//        assertEquals(rating, tvShowInfoUiState.rating)
//        assertEquals(releaseDate, tvShowInfoUiState.releaseDate)
//        assertEquals(seasonCount, tvShowInfoUiState.seasonCount)
//        assertEquals(overView, tvShowInfoUiState.overView)
//        assertEquals(trailerURL, tvShowInfoUiState.trailerURL)
//        assertEquals(posterURL, tvShowInfoUiState.posterPictureURL)
//        assertEquals(headerImages, tvShowInfoUiState.headerImagesURLs)
//    }
//
//    @Test
//    fun `GenreUiState should have correct default values`() {
//
//        val genreUiState = TvShowDetailsScreenState.GenreUiState(null)
//
//        assertNull(genreUiState.id)
//        assertEquals("", genreUiState.name)
//    }
//
//    @Test
//    fun `GenreUiState should accept custom values correctly`() {
//
//        val genreId = 123L
//        val genreName = "Action"
//
//        val genreUiState = TvShowDetailsScreenState.GenreUiState(
//            id = genreId,
//            name = genreName
//        )
//
//        assertEquals(genreId, genreUiState.id)
//        assertEquals(genreName, genreUiState.name)
//    }
//
//    @Test
//    fun `CastMemberUiState should have correct default values`() {
//
//        val castMemberUiState = TvShowDetailsScreenState.CastMemberUiState(null)
//
//        assertNull(castMemberUiState.id)
//        assertEquals("", castMemberUiState.name)
//        assertEquals("", castMemberUiState.imageUrl)
//        assertEquals("", castMemberUiState.characterName)
//    }
//
//    @Test
//    fun `CastMemberUiState should accept custom values correctly`() {
//
//        val castId = 456L
//        val name = "Bryan Cranston"
//        val imageUrl = "/bryan_cranston.jpg"
//        val characterName = "Walter White"
//
//        val castMemberUiState = TvShowDetailsScreenState.CastMemberUiState(
//            id = castId,
//            name = name,
//            imageUrl = imageUrl,
//            characterName = characterName
//        )
//
//        assertEquals(castId, castMemberUiState.id)
//        assertEquals(name, castMemberUiState.name)
//        assertEquals(imageUrl, castMemberUiState.imageUrl)
//        assertEquals(characterName, castMemberUiState.characterName)
//    }
//
//    @Test
//    fun `EpisodeUiState should have correct default values`() {
//
//        val episodeUiState = TvShowDetailsScreenState.EpisodeUiState(null)
//
//        assertNull(episodeUiState.id)
//        assertEquals("", episodeUiState.name)
//        assertEquals(0, episodeUiState.episodeNumber)
//        assertEquals(0.0, episodeUiState.rating)
//        assertEquals("", episodeUiState.duration)
//        assertEquals("", episodeUiState.releaseDate)
//        assertEquals(0, episodeUiState.currentSeason)
//    }
//
//    @Test
//    fun `EpisodeUiState should accept custom values correctly`() {
//
//        val episodeId = 789L
//        val name = "Pilot"
//        val episodeNumber = 1
//        val rating = 8.2
//        val duration = "58 min"
//        val releaseDate = "20 Jan 2008"
//        val currentSeason = 1
//
//        val episodeUiState = TvShowDetailsScreenState.EpisodeUiState(
//            id = episodeId,
//            name = name,
//            episodeNumber = episodeNumber,
//            rating = rating,
//            duration = duration,
//            releaseDate = releaseDate,
//            currentSeason = currentSeason
//        )
//
//        assertEquals(episodeId, episodeUiState.id)
//        assertEquals(name, episodeUiState.name)
//        assertEquals(episodeNumber, episodeUiState.episodeNumber)
//        assertEquals(rating, episodeUiState.rating)
//        assertEquals(duration, episodeUiState.duration)
//        assertEquals(releaseDate, episodeUiState.releaseDate)
//        assertEquals(currentSeason, episodeUiState.currentSeason)
//    }
//
//    @Test
//    fun `equality should work correctly for TvShowDetailsScreenState`() {
//
//        val state1 = TvShowDetailsScreenState(
//            tvShowInfo = createMockTvShowInfoUiState(),
//            selectedSeasonIndex = 1,
//            isTextExpanded = true,
//            isLoading = false
//        )
//        val state2 = TvShowDetailsScreenState(
//            tvShowInfo = createMockTvShowInfoUiState(),
//            selectedSeasonIndex = 1,
//            isTextExpanded = true,
//            isLoading = false
//        )
//        val state3 = TvShowDetailsScreenState(
//            tvShowInfo = createMockTvShowInfoUiState(),
//            selectedSeasonIndex = 2,
//            isTextExpanded = true,
//            isLoading = false
//        )
//
//        assertEquals(state1, state2)
//        assertTrue(state1 != state3)
//    }
//
//    @Test
//    fun `equality should work correctly for nested data classes`() {
//
//        val genre1 = TvShowDetailsScreenState.GenreUiState(1L, "Drama")
//        val genre2 = TvShowDetailsScreenState.GenreUiState(1L, "Drama")
//        val genre3 = TvShowDetailsScreenState.GenreUiState(2L, "Comedy")
//
//        val cast1 = TvShowDetailsScreenState.CastMemberUiState(1L, "Actor 1", "/image1.jpg", "Character 1")
//        val cast2 = TvShowDetailsScreenState.CastMemberUiState(1L, "Actor 1", "/image1.jpg", "Character 1")
//        val cast3 = TvShowDetailsScreenState.CastMemberUiState(2L, "Actor 2", "/image2.jpg", "Character 2")
//
//        val episode1 = TvShowDetailsScreenState.EpisodeUiState(1L, "Episode 1", 1, 8.5, "45 min", "01 Jan 2008", 1)
//        val episode2 = TvShowDetailsScreenState.EpisodeUiState(1L, "Episode 1", 1, 8.5, "45 min", "01 Jan 2008", 1)
//        val episode3 = TvShowDetailsScreenState.EpisodeUiState(2L, "Episode 2", 2, 8.7, "47 min", "08 Jan 2008", 1)
//
//        val tvShowInfo1 = TvShowDetailsScreenState.TvShowInfoUiState(title = "Show 1")
//        val tvShowInfo2 = TvShowDetailsScreenState.TvShowInfoUiState(title = "Show 1")
//        val tvShowInfo3 = TvShowDetailsScreenState.TvShowInfoUiState(title = "Show 2")
//
//        assertEquals(genre1, genre2)
//        assertTrue(genre1 != genre3)
//        assertEquals(cast1, cast2)
//        assertTrue(cast1 != cast3)
//        assertEquals(episode1, episode2)
//        assertTrue(episode1 != episode3)
//        assertEquals(tvShowInfo1, tvShowInfo2)
//        assertTrue(tvShowInfo1 != tvShowInfo3)
//    }
//
//    @Test
//    fun `hashCode should be consistent for equal objects`() {
//
//        val state1 = TvShowDetailsScreenState(selectedSeasonIndex = 1, isTextExpanded = true, isLoading = false)
//        val state2 = TvShowDetailsScreenState(selectedSeasonIndex = 1, isTextExpanded = true, isLoading = false)
//
//        assertEquals(state1.hashCode(), state2.hashCode())
//    }
//
//    @Test
//    fun `toString should work correctly`() {
//
//        val state = TvShowDetailsScreenState(
//            tvShowInfo = TvShowDetailsScreenState.TvShowInfoUiState(title = "Test Show"),
//            selectedSeasonIndex = 1,
//            isTextExpanded = true,
//            isLoading = false
//        )
//
//        val result = state.toString()
//
//        assertNotNull(result)
//        assertTrue(result.contains("TvShowDetailsScreenState"))
//        assertTrue(result.contains("selectedSeasonIndex=1"))
//        assertTrue(result.contains("isTextExpanded=true"))
//        assertTrue(result.contains("isLoading=false"))
//    }
//
//    @Test
//    fun `userRating should handle decimal values correctly`() {
//
//        val userRating = 7.85
//
//        val state = TvShowDetailsScreenState(userRating = userRating)
//
//        assertEquals(userRating, state.userRating)
//    }
//
//    @Test
//    fun `selectedSeasonIndex should handle negative values correctly`() {
//
//        val negativeIndex = -1
//
//        val state = TvShowDetailsScreenState(selectedSeasonIndex = negativeIndex)
//
//        assertEquals(negativeIndex, state.selectedSeasonIndex)
//    }
//
//    companion object {
//        private fun createMockTvShowInfoUiState() = TvShowDetailsScreenState.TvShowInfoUiState(
//            title = "Breaking Bad",
//            genres = listOf(
//                TvShowDetailsScreenState.GenreUiState(1L, "Drama"),
//                TvShowDetailsScreenState.GenreUiState(2L, "Crime")
//            ),
//            rating = 9.5,
//            releaseDate = "20/01/2008",
//            seasonCount = 5,
//            overView = "A high school chemistry teacher diagnosed with inoperable lung cancer turns to manufacturing and selling methamphetamine",
//            trailerURL = "/breaking_bad_trailer.mp4",
//            posterPictureURL = "/breaking_bad_poster.jpg",
//            headerImagesURLs = listOf("/header1.jpg", "/header2.jpg", "/header3.jpg")
//        )
//
//        private fun createMockCastMemberUiStateList() = listOf(
//            TvShowDetailsScreenState.CastMemberUiState(
//                id = 1L,
//                name = "Bryan Cranston",
//                imageUrl = "/bryan_cranston.jpg",
//                characterName = "Walter White"
//            ),
//            TvShowDetailsScreenState.CastMemberUiState(
//                id = 2L,
//                name = "Aaron Paul",
//                imageUrl = "/aaron_paul.jpg",
//                characterName = "Jesse Pinkman"
//            )
//        )
//
//        private fun createMockEpisodeUiStateList() = listOf(
//            TvShowDetailsScreenState.EpisodeUiState(
//                id = 1L,
//                name = "Pilot",
//                episodeNumber = 1,
//                rating = 8.2,
//                duration = "58 min",
//                releaseDate = "20 Jan 2008",
//                currentSeason = 1
//            ),
//            TvShowDetailsScreenState.EpisodeUiState(
//                id = 2L,
//                name = "Cat's in the Bag...",
//                episodeNumber = 2,
//                rating = 8.2,
//                duration = "48 min",
//                releaseDate = "27 Jan 2008",
//                currentSeason = 1
//            )
//        )
//    }
//}