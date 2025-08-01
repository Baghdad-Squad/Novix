package com.baghdad.repository.dummyData

import com.baghdad.entity.media.Episode
import com.baghdad.entity.media.Genre
import com.baghdad.entity.person.Actor
import com.baghdad.entity.person.CastMember
import com.baghdad.repository.model.ActorDto
import com.baghdad.repository.model.CastMemberDto
import com.baghdad.repository.model.EpisodeDto
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.TvShowDto
import kotlinx.datetime.LocalDate

object DummyDataFactory {

    fun createMockActorDto() = ActorDto(
        id = 789L,
        name = "Test Actor",
        imageUrl = "/actor_profile.jpg",
        biography = "Test actor biography",
        birthdayDate = "1985-03-10",
        deathDate = null,
        placeOfBirth = "Los Angeles, USA",
        headerPictures = listOf("/actor_header1.jpg", "/actor_header2.jpg"),
        department = "Acting"
    )

    fun createMockActor() = Actor(
        id = 789L,
        name = "Test Actor",
        profilePictureURL = "/actor_profile.jpg",
        birthDate = LocalDate.Companion.parse("1985-03-10"),
        placeOfBirth = "Los Angeles, USA",
        deathDate = null,
        biography = "Test actor biography",
        headerPictures = listOf("/actor_header1.jpg", "/actor_header2.jpg"),
        department = "Acting"
    )

    fun createMockGenreDto(id: Long, name: String) = GenreDto(
        id = id,
        name = name,
        type = GenreDto.GenreType.TV_SHOW
    )

    fun createMockGenre(id: Long, name: String) = Genre(
        id = id,
        name = name
    )

    fun createMockEpisodeDto() = EpisodeDto(
        id = 456L,
        title = "Test Episode",
        episodeNumber = 5,
        rating = 8.5,
        duration = "45 min",
        releasedDate = "2023-05-15",
        currentSeason = 2,
        overview = "Test episode overview",
        headerPictures = listOf("/header1.jpg", "/header2.jpg"),
        genres = emptyList(),
        trailerUrl = " "
    )

    fun createMockEpisode() = Episode(
        id = 456L,
        title = "Test Episode",
        episodeNumber = 5,
        rating = 8.5,
        duration = "45 min",
        releasedDate = LocalDate.Companion.parse("2023-05-15"),
        currentSeason = 2,
        overview = "Test episode overview",
        headerPictures = listOf("/image1.jpg", "/image2.jpg", "/image3.jpg"),
        genres = emptyList(),
        trailerUrl = " "
    )

    fun createMockCastMemberDto() = CastMemberDto(
        actor = createMockActorDto(),
        characterName = "Test Character"
    )

    fun createMockCastMember() = CastMember(
        actor = createMockActor(),
        characterName = "Test Character"
    )

    fun createMockTvShowDto() =
        listOf(
            TvShowDto(
                id = 123L,
                title = "Test TV Show",
                genres = listOf(createMockGenreDto(1, "Drama"), createMockGenreDto(2, "Comedy")),
                userRating = 7.5,
                releaseDate = "2023-01-01",
                overview = "This is a test TV show overview.",
                trailerURL = "https://youtube.com/watch?v=test_trailer",
                headerImagesURLs = listOf("/header1.jpg", "/header2.jpg"),
                posterPictureURL = "/tv_show_poster.jpg",
                imdbRating = 8.0,
                numberOfSeasons = 3
            ),
            TvShowDto(
                id = 456L,
                title = "Another Test TV Show",
                genres = listOf(createMockGenreDto(3, "Action"), createMockGenreDto(4, "Sci-Fi")),
                userRating = 8.0,
                releaseDate = "2022-05-15",
                overview = "This is another test TV show overview.",
                trailerURL = "https://youtube.com/watch?v=another_test_trailer",
                headerImagesURLs = listOf("/header3.jpg", "/header4.jpg"),
                posterPictureURL = "/another_tv_show_poster.jpg",
                imdbRating = 9.0,
                numberOfSeasons = 2
            )
        )
    fun createMockMovieDto() =
        listOf(
            MovieDto(
                id = 1L,
                title = "Test Movie",
                genres = listOf(createMockGenreDto(1, "Action"), createMockGenreDto(2, "Adventure")),
                userRating = 7.5,
                releaseDate = "2023-01-01",
                overview = "This is a test movie overview.",
                trailerURL = "https://youtube.com/watch?v=test_movie_trailer",
                posterPictureURL = "/movie_poster.jpg",
                imdbRating = 8.0,
                runtimeMinutes = 120
            ),
            MovieDto(
                id = 2L,
                title = "Another Test Movie",
                genres = listOf(createMockGenreDto(3, "Comedy"), createMockGenreDto(4, "Drama")),
                userRating = 8.0,
                releaseDate = "2022-05-15",
                overview = "This is another test movie overview.",
                trailerURL = "https://youtube.com/watch?v=another_test_movie_trailer",
                posterPictureURL = "/another_movie_poster.jpg",
                imdbRating = 9.0,
                runtimeMinutes = 150
            )
        )
}