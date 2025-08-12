package com.baghdad.repository.dummyData

import com.baghdad.domain.model.ContinueWatching
import com.baghdad.domain.model.RatedMedia
import com.baghdad.domain.model.search.RecentlyViewed
import com.baghdad.entity.media.Episode
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.baghdad.entity.person.Actor
import com.baghdad.entity.person.CastMember
import com.baghdad.repository.model.ActorDto
import com.baghdad.repository.model.CastMemberDto
import com.baghdad.repository.model.ContinueWatchingDto
import com.baghdad.repository.model.EpisodeDto
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.RecentSearchDto
import com.baghdad.repository.model.RecentlyViewedDto
import com.baghdad.repository.model.ReviewDto
import com.baghdad.repository.model.SavedListDto
import com.baghdad.repository.model.TvShowDto
import com.baghdad.repository.model.UserDto
import com.baghdad.repository.model.savedList.SavableMovieDto
import kotlinx.datetime.LocalDate

object DummyDataFactory {
    val RATED_MEDIA = RatedMedia(
        id = 101L,
        userRating = 10,
        posterImageURL = "https://example.com/breakingbad.jpg",
        contentType = RatedMedia.ContentType.TV_SHOW
    )
    val MOVIE_DTO = MovieDto(
        id = 1L,
        title = "Test Movie",
        genres = listOf(
            createMockGenreDto(id = 1, name = "Action"),
            createMockGenreDto(id = 2, name = "Adventure")
        ),
        userRating = 7.5,
        releaseDate = "2023-01-01",
        overview = "This is a test movie overview.",
        trailerURL = "https://youtube.com/watch?v=test_movie_trailer",
        posterPictureURL = "/movie_poster.jpg",
        imdbRating = 8.0,
        runtimeMinutes = 120
    )

    val SAVABLE_MOVIE_DTO = SavableMovieDto(
        movie = MOVIE_DTO,
        isSaved = true,
        listId = 42L
    )

    val SAVED_LIST_DTO = SavedListDto(
        id = 10L,
        name = "Favorites",
        itemCount = 5
    )

    val USER_DTO = UserDto(
        id = 1L,
        userName = "johndoe",
        imageUrl = "https://example.com/avatar.jpg"
    )
    val RECENT_SEARCH_DTO = RecentSearchDto(
        id = 1L,
        query = "test query",
        searchedAt = 1672574400000L
    )

    val REVIEW_DTO = ReviewDto(
        id = "review123",
        contentTitle = "Great movie! Highly recommended.",
        authorName = "John Reviewer",
        rating = 9.0,
        authorAvatarUrl = "https://example.com/avatar.jpg",
        reviewText = "This movie was fantastic! The plot was engaging and the acting was top-notch.",
        postedDate = "2023-01-01"
    )

    val TV_SHOW_DTO = TvShowDto(
        id = 123L,
        title = "Test TV Show",
        genres = listOf(createMockGenreDto(35L, "Comedy")),
        imdbRating = 8.5,
        userRating = 8,
        releaseDate = "2023-01-01",
        overview = "Test TV show overview",
        posterPictureURL = "/tv_show_poster.jpg",
        headerImagesURLs = listOf("/header1.jpg", "/header2.jpg"),
        trailerURL = " ",
        numberOfSeasons = 3
    )

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
        userRating = 7,
        trailerUrl = " "
    )

    fun createMockCastMemberDto() = CastMemberDto(
        actor = createMockActorDto(),
        characterName = "Test Character"
    )

    fun createMockMovieDto() =
        listOf(
            MovieDto(
                id = 1L,
                title = "Test Movie",
                genres = listOf(
                    createMockGenreDto(id = 1, name = "Action"),
                    createMockGenreDto(id = 2, name = "Adventure")
                ),
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
                genres = listOf(
                    createMockGenreDto(id = 3, name = "Comedy"),
                    createMockGenreDto(id = 4, name = "Drama")
                ),
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