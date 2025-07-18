package com.baghdad.domain.usecase.genre

import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import kotlinx.datetime.LocalDate

class GetGenreMoviesUseCase {
    suspend operator fun invoke(genreId: Long): List<Movie> {
        return dummyMovies
    }

    val dummyMovies = listOf(
        Movie(
            id = 1L,
            title = "The Shawshank Redemption",
            genres = listOf(Genre(18, "Drama"), Genre(80, "Crime")),
            averageRating = 9.3,
            userRating = null,
            releaseDate = LocalDate(1994, 9, 22),
            overview = "Framed in the 1940s for the double murder of his wife and her lover, upstanding banker Andy Dufresne begins a new life at Shawshank prison.",
            posterImageURL = "https://image.tmdb.org/t/p/w500/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg",
            runtimeMinutes = 142
        ),
        Movie(
            id = 2L,
            title = "The Godfather",
            genres = listOf(Genre(18, "Drama"), Genre(80, "Crime")),
            averageRating = 9.2,
            userRating = null,
            releaseDate = LocalDate(1972, 3, 24),
            overview = "The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.",
            posterImageURL = "https://image.tmdb.org/t/p/w500/3bhkrj58Vtu7enYsRolD1fZdja1.jpg",
            runtimeMinutes = 175
        ),
        Movie(
            id = 3L,
            title = "The Dark Knight",
            genres = listOf(Genre(28, "Action"), Genre(80, "Crime"), Genre(18, "Drama")),
            averageRating = 9.0,
            userRating = null,
            releaseDate = LocalDate(2008, 7, 18),
            overview = "Batman must accept one of the greatest psychological and physical tests of his ability to fight injustice.",
            posterImageURL = "https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg",
            runtimeMinutes = 152
        ),
        Movie(
            id = 4L,
            title = "Inception",
            genres = listOf(
                Genre(28, "Action"),
                Genre(878, "Science Fiction"),
                Genre(12, "Adventure")
            ),
            averageRating = 8.8,
            userRating = null,
            releaseDate = LocalDate(2010, 7, 16),
            overview = "A thief who steals corporate secrets through dream-sharing is given the inverse task of planting an idea into a target's subconscious.",
            posterImageURL = "https://image.tmdb.org/t/p/w500/edv5CZvWj09upOsy2Y6IwDhK8bt.jpg",
            runtimeMinutes = 148
        ),
        Movie(
            id = 5L,
            title = "Pulp Fiction",
            genres = listOf(Genre(80, "Crime"), Genre(53, "Thriller")),
            averageRating = 8.9,
            userRating = null,
            releaseDate = LocalDate(1994, 10, 14),
            overview = "The lives of two mob hitmen, a boxer, a gangster's wife, and a pair of diner bandits intertwine in four tales of violence and redemption.",
            posterImageURL = "https://image.tmdb.org/t/p/w500/d5iIlFn5s0ImszYzBPb8JPIfbXD.jpg",
            runtimeMinutes = 154
        ),
        Movie(
            id = 6L,
            title = "Fight Club",
            genres = listOf(Genre(18, "Drama")),
            averageRating = 8.8,
            userRating = null,
            releaseDate = LocalDate(1999, 10, 15),
            overview = "An insomniac office worker and a devil-may-care soapmaker form an underground fight club that evolves into something much more.",
            posterImageURL = "https://image.tmdb.org/t/p/w500/pB8BM7pdSp6B6Ih7QZ4DrQ3PmJK.jpg",
            runtimeMinutes = 139
        ),
        Movie(
            id = 7L,
            title = "Forrest Gump",
            genres = listOf(Genre(35, "Comedy"), Genre(18, "Drama"), Genre(10749, "Romance")),
            averageRating = 8.8,
            userRating = null,
            releaseDate = LocalDate(1994, 7, 6),
            overview = "The life journey of Forrest Gump, a slow-witted but kind-hearted man from Alabama who witnesses and influences several defining historical events.",
            posterImageURL = "https://image.tmdb.org/t/p/w500/arw2vcBveWOVZr6pxd9XTd1TdQa.jpg",
            runtimeMinutes = 142
        ),
        Movie(
            id = 8L,
            title = "Interstellar",
            genres = listOf(
                Genre(12, "Adventure"),
                Genre(18, "Drama"),
                Genre(878, "Science Fiction")
            ),
            averageRating = 8.6,
            userRating = null,
            releaseDate = LocalDate(2014, 11, 7),
            overview = "A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival.",
            posterImageURL = "https://image.tmdb.org/t/p/w500/rAiYTfKGqDCRIIqo664sY9XZIvQ.jpg",
            runtimeMinutes = 169
        ),
        Movie(
            id = 9L,
            title = "The Matrix",
            genres = listOf(Genre(28, "Action"), Genre(878, "Science Fiction")),
            averageRating = 8.7,
            userRating = null,
            releaseDate = LocalDate(1999, 3, 31),
            overview = "A computer hacker learns about the true nature of his reality and his role in the war against its controllers.",
            posterImageURL = "https://image.tmdb.org/t/p/w500/f89U3ADr1oiB1s9GkdPOEpXUk5H.jpg",
            runtimeMinutes = 136
        ),
        Movie(
            id = 10L,
            title = "The Lord of the Rings: The Fellowship of the Ring",
            genres = listOf(Genre(12, "Adventure"), Genre(14, "Fantasy"), Genre(28, "Action")),
            averageRating = 8.8,
            userRating = null,
            releaseDate = LocalDate(2001, 12, 19),
            overview = "A meek Hobbit from the Shire and eight companions set out on a journey to destroy the One Ring and save Middle-earth.",
            posterImageURL = "https://image.tmdb.org/t/p/w500/6oom5QYQ2yQTMJIbnvbkBL9cHo6.jpg",
            runtimeMinutes = 178
        )
    )

}