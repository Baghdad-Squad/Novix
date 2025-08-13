package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.apiService.EpisodeApiService
import com.baghdad.remoteDataSource.request.RatingRequest
import com.baghdad.remoteDataSource.response.castMembers.CastMembersResponse
import com.baghdad.remoteDataSource.response.episode.EpisodeDetailsResponse
import com.baghdad.remoteDataSource.response.episode.EpisodeVideosResponse
import com.baghdad.remoteDataSource.response.rate.RatingResponse
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.ActorDto
import com.baghdad.repository.model.CastMemberDto
import com.baghdad.repository.model.EpisodeDto
import com.baghdad.repository.model.MediaAccountStateDto
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import retrofit2.Response

class RemoteEpisodeDataSourceImplTest {
    private val episodeApiService = mockk<EpisodeApiService>()
    private val logger = mockk<Logger>(relaxed = true)
    private val dataSource = RemoteEpisodeDataSourceImpl(episodeApiService, logger)

    @Test
    fun `should return episode dto when getEpisodeDetails is called with valid parameters`() = runTest {
        val successResponse = Response.success(episodeDetailsResponse)
        coEvery {
            episodeApiService.getEpisodeDetails(TV_ID, SEASON_NUMBER, EPISODE_NUMBER)
        } returns successResponse

        val result = dataSource.getEpisodeDetails(TV_ID, SEASON_NUMBER, EPISODE_NUMBER)

        assertThat(result).isEqualTo(expectedEpisodeDto)
        coVerify(exactly = 1) {
            episodeApiService.getEpisodeDetails(TV_ID, SEASON_NUMBER, EPISODE_NUMBER)
        }
    }

    @Test
    fun `should return cast members list when getEpisodeCastMembers is called with valid parameters`() = runTest {
        val successResponse = Response.success(castMembersResponse)
        coEvery {
            episodeApiService.getEpisodeCastMembers(TV_ID, SEASON_NUMBER, EPISODE_NUMBER)
        } returns successResponse

        val result = dataSource.getEpisodeCastMembers(TV_ID, SEASON_NUMBER, EPISODE_NUMBER)

        assertThat(result).hasSize(1)
        assertThat(result[0]).isEqualTo(expectedCastMemberDto)
        coVerify(exactly = 1) {
            episodeApiService.getEpisodeCastMembers(TV_ID, SEASON_NUMBER, EPISODE_NUMBER)
        }
    }

    @Test
    fun `should return empty list when getEpisodeCastMembers receives null cast`() = runTest {
        val successResponse = Response.success(castMembersResponseWithNullCast)
        coEvery {
            episodeApiService.getEpisodeCastMembers(TV_ID, SEASON_NUMBER, EPISODE_NUMBER)
        } returns successResponse

        val result = dataSource.getEpisodeCastMembers(TV_ID, SEASON_NUMBER, EPISODE_NUMBER)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should filter out cast members with null id when getEpisodeCastMembers is called`() = runTest {
        val successResponse = Response.success(castMembersResponseWithNullId)
        coEvery {
            episodeApiService.getEpisodeCastMembers(TV_ID, SEASON_NUMBER, EPISODE_NUMBER)
        } returns successResponse

        val result = dataSource.getEpisodeCastMembers(TV_ID, SEASON_NUMBER, EPISODE_NUMBER)

        assertThat(result).hasSize(1)
        assertThat(result[0].actor.id).isEqualTo(CAST_MEMBER_ID)
    }

    @Test
    fun `should return empty string when getEpisodeTrailer finds no youtube videos`() = runTest {
        val successResponse = Response.success(episodeVideosResponseWithNoYoutube)
        coEvery {
            episodeApiService.getEpisodeTrailer(TV_ID, SEASON_NUMBER, EPISODE_NUMBER)
        } returns successResponse

        val result = dataSource.getEpisodeTrailer(TV_ID, SEASON_NUMBER, EPISODE_NUMBER)

        assertThat(result).isEqualTo("")
    }

    @Test
    fun `should return empty string when getEpisodeTrailer receives null results`() = runTest {
        val successResponse = Response.success(episodeVideosResponseWithNullResults)
        coEvery {
            episodeApiService.getEpisodeTrailer(TV_ID, SEASON_NUMBER, EPISODE_NUMBER)
        } returns successResponse

        val result = dataSource.getEpisodeTrailer(TV_ID, SEASON_NUMBER, EPISODE_NUMBER)

        assertThat(result).isEqualTo("")
    }

    @Test
    fun `should return empty string when getEpisodeTrailer receives empty results`() = runTest {
        val successResponse = Response.success(episodeVideosResponseEmpty)
        coEvery {
            episodeApiService.getEpisodeTrailer(TV_ID, SEASON_NUMBER, EPISODE_NUMBER)
        } returns successResponse

        val result = dataSource.getEpisodeTrailer(TV_ID, SEASON_NUMBER, EPISODE_NUMBER)

        assertThat(result).isEqualTo("")
    }

    @Test
    fun `should call api service when addEpisodeRate is called`() = runTest {
        val successResponse = Response.success(ratingResponse)
        coEvery {
            episodeApiService.addEpisodeRate(TV_ID, SEASON_NUMBER, EPISODE_NUMBER, ratingRequest)
        } returns successResponse

        dataSource.addEpisodeRate(TV_ID, SEASON_NUMBER, EPISODE_NUMBER, SESSION_ID, RATING)

        coVerify(exactly = 1) {
            episodeApiService.addEpisodeRate(TV_ID, SEASON_NUMBER, EPISODE_NUMBER, ratingRequest)
        }
    }

    companion object {
        const val TV_ID = 123L
        const val SEASON_NUMBER = 1
        const val EPISODE_NUMBER = 5
        const val SESSION_ID = "session123"
        const val RATING = 8
        const val EPISODE_ID = 456L
        const val EPISODE_NAME = "Episode Title"
        const val VOTE_AVERAGE = 8.5
        const val RUNTIME = 45
        const val AIR_DATE = "2023-01-01"
        const val OVERVIEW = "Episode overview"
        const val CAST_MEMBER_ID = 789L
        const val CAST_MEMBER_NAME = "Actor Name"
        const val PROFILE_PATH = "/profile.jpg"
        const val CHARACTER_NAME = "Character Name"
        const val KNOWN_FOR_DEPARTMENT = "Acting"
        const val VIDEO_KEY = "abc123"
        const val YOUTUBE_URL = "https://www.youtube.com/watch?v=abc123"

        val episodeDetailsResponse = EpisodeDetailsResponse(
            id = EPISODE_ID,
            name = EPISODE_NAME,
            episodeNumber = EPISODE_NUMBER,
            voteAverage = VOTE_AVERAGE,
            runtime = RUNTIME,
            airDate = AIR_DATE,
            seasonNumber = SEASON_NUMBER,
            overview = OVERVIEW
        )

        val episodeDetailsResponseWithNulls = EpisodeDetailsResponse(
            id = null,
            name = null,
            episodeNumber = EPISODE_NUMBER,
            voteAverage = VOTE_AVERAGE,
            runtime = RUNTIME,
            airDate = AIR_DATE,
            seasonNumber = SEASON_NUMBER,
            overview = null
        )

        val castMemberResponse = CastMembersResponse.CastMemberResponse(
            id = CAST_MEMBER_ID,
            name = CAST_MEMBER_NAME,
            profilePath = PROFILE_PATH,
            character = CHARACTER_NAME,
            knownForDepartment = KNOWN_FOR_DEPARTMENT
        )

        val castMemberResponseWithNulls = CastMembersResponse.CastMemberResponse(
            id = null,
            name = null,
            profilePath = null,
            character = null,
            knownForDepartment = null
        )

        val castMemberResponseValidActor = CastMembersResponse.CastMemberResponse(
            id = CAST_MEMBER_ID,
            name = CAST_MEMBER_NAME,
            profilePath = PROFILE_PATH,
            character = CHARACTER_NAME,
            knownForDepartment = KNOWN_FOR_DEPARTMENT
        )

        val castMembersResponse = CastMembersResponse(
            cast = listOf(castMemberResponse)
        )

        val castMembersResponseWithNullId = CastMembersResponse(
            cast = listOf(castMemberResponseValidActor, castMemberResponseWithNulls)
        )

        val castMembersResponseWithNullCast = CastMembersResponse(
            cast = null
        )

        val youtubeTrailerVideo = EpisodeVideosResponse.Result(
            key = VIDEO_KEY,
            site = "YouTube",
            type = "Trailer"
        )

        val youtubeNonTrailerVideo = EpisodeVideosResponse.Result(
            key = VIDEO_KEY,
            site = "YouTube",
            type = "Clip"
        )

        val nonYoutubeVideo = EpisodeVideosResponse.Result(
            key = VIDEO_KEY,
            site = "Vimeo",
            type = "Trailer"
        )

        val episodeVideosResponseWithTrailer = EpisodeVideosResponse(
            results = listOf(youtubeTrailerVideo)
        )

        val episodeVideosResponseWithNonTrailer = EpisodeVideosResponse(
            results = listOf(youtubeNonTrailerVideo)
        )

        val episodeVideosResponseWithNoYoutube = EpisodeVideosResponse(
            results = listOf(nonYoutubeVideo)
        )

        val episodeVideosResponseWithNullResults = EpisodeVideosResponse(
            results = null
        )

        val episodeVideosResponseEmpty = EpisodeVideosResponse(
            results = emptyList()
        )

        val ratingRequest = RatingRequest(RATING)

        val ratingResponse = RatingResponse(
            isSuccess = true,
            statusCode = 200,
            statusMessage = "Success"
        )

        val expectedEpisodeDto = EpisodeDto(
            id = EPISODE_ID,
            title = EPISODE_NAME,
            episodeNumber = EPISODE_NUMBER,
            rating = VOTE_AVERAGE,
            duration = RUNTIME.toString(),
            releasedDate = AIR_DATE,
            currentSeason = SEASON_NUMBER,
            overview = OVERVIEW,
            headerPictures = emptyList(),
            trailerUrl = "",
            userRating = 0,
            genres = emptyList()
        )

        val expectedEpisodeDtoWithDefaults = EpisodeDto(
            id = 0L,
            title = "",
            episodeNumber = EPISODE_NUMBER,
            rating = VOTE_AVERAGE,
            duration = RUNTIME.toString(),
            releasedDate = AIR_DATE,
            currentSeason = SEASON_NUMBER,
            overview = "",
            headerPictures = emptyList(),
            trailerUrl = "",
            userRating = 0,
            genres = emptyList()
        )

        val expectedCastMemberDto = CastMemberDto(
            actor = ActorDto(
                id = CAST_MEMBER_ID,
                name = CAST_MEMBER_NAME,
                imageUrl = "https://image.tmdb.org/t/p/w500$PROFILE_PATH",
                biography = "",
                birthdayDate = null,
                deathDate = null,
                placeOfBirth = "",
                headerPictures = emptyList(),
                department = KNOWN_FOR_DEPARTMENT
            ),
            characterName = CHARACTER_NAME
        )
    }
}