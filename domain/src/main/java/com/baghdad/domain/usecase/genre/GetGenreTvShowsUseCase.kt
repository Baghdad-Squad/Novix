package com.baghdad.domain.usecase.genre

import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.TvShow
import kotlinx.datetime.LocalDate

class GetGenreTvShowsUseCase {
    suspend operator fun invoke(genreId: Long): List<TvShow> {
        return dummyTvShows
        //return dummyTvShows.filter { it.genres.any { genre -> genre.id == categoryId } }
    }

    val dummyTvShows = listOf(
        TvShow(
            id = 1,
            title = "Breaking Bad",
            genres = listOf(Genre(18, "Drama"), Genre(80, "Crime")),
            averageRating = 9.5,
            userRating = null,
            releaseDate = LocalDate(2008, 1, 20),
            overview = "A high school chemistry teacher turned methamphetamine producer navigates the dangers of the criminal underworld.",
            posterImageURL = "https://image.tmdb.org/t/p/w500/ggFHVNu6YYI5L9pCfOacjizRGt.jpg",
            numberOfSeasons = 5
        ),
        TvShow(
            id = 2,
            title = "Stranger Things",
            genres = listOf(
                Genre(18, "Drama"), Genre(9648, "Mystery"), Genre(10765, "Sci-Fi & Fantasy")
            ),
            averageRating = 8.7,
            userRating = null,
            releaseDate = LocalDate(2016, 7, 15),
            overview = "When a young boy vanishes, a small town uncovers a mystery involving secret experiments and supernatural forces.",
            posterImageURL = "https://image.tmdb.org/t/p/w500/x2LSRK2Cm7MZhjluni1msVJ3wDF.jpg",
            numberOfSeasons = 5
        ),
        TvShow(
            id = 3,
            title = "Game of Thrones",
            genres = listOf(
                Genre(10765, "Sci-Fi & Fantasy"),
                Genre(18, "Drama"),
                Genre(10759, "Action & Adventure")
            ),
            averageRating = 9.3,
            userRating = null,
            releaseDate = LocalDate(2011, 4, 17),
            overview = "Nine noble families wage war against each other to gain control over the mythical land of Westeros.",
            posterImageURL = "https://image.tmdb.org/t/p/w500/u3bZgnGQ9T01sWNhyveQz0wH0Hl.jpg",
            numberOfSeasons = 8
        ),
        TvShow(
            id = 4,
            title = "The Witcher",
            genres = listOf(
                Genre(10765, "Sci-Fi & Fantasy"),
                Genre(18, "Drama"),
                Genre(10759, "Action & Adventure")
            ),
            averageRating = 8.1,
            userRating = null,
            releaseDate = LocalDate(2019, 12, 20),
            overview = "Geralt of Rivia, a mutated monster hunter for hire, journeys toward his destiny in a turbulent world.",
            posterImageURL = "https://image.tmdb.org/t/p/w500/1XsiylUROQhEvayH2x4ezbT90gP.jpg",
            numberOfSeasons = 3
        ),
        TvShow(
            id = 5,
            title = "The Office",
            genres = listOf(Genre(35, "Comedy")),
            averageRating = 8.9,
            userRating = null,
            releaseDate = LocalDate(2005, 3, 24),
            overview = "A mockumentary on a group of typical office workers, where the workday consists of ego clashes and inappropriate behavior.",
            posterImageURL = "https://image.tmdb.org/t/p/w500/qWnJzyZhyy74gjpSjIXWmuk0ifX.jpg",
            numberOfSeasons = 9
        ),
        TvShow(
            id = 6,
            title = "Breaking Bad",
            genres = listOf(Genre(18, "Drama"), Genre(80, "Crime")),
            averageRating = 9.5,
            userRating = null,
            releaseDate = LocalDate(2008, 1, 20),
            overview = "A high school chemistry teacher turned methamphetamine producer navigates the dangers of the criminal underworld.",
            posterImageURL = "https://image.tmdb.org/t/p/w500/ggFHVNu6YYI5L9pCfOacjizRGt.jpg",
            numberOfSeasons = 5
        ),
        TvShow(
            id = 7,
            title = "Stranger Things",
            genres = listOf(
                Genre(18, "Drama"), Genre(9648, "Mystery"), Genre(10765, "Sci-Fi & Fantasy")
            ),
            averageRating = 8.7,
            userRating = null,
            releaseDate = LocalDate(2016, 7, 15),
            overview = "When a young boy vanishes, a small town uncovers a mystery involving secret experiments and supernatural forces.",
            posterImageURL = "https://image.tmdb.org/t/p/w500/x2LSRK2Cm7MZhjluni1msVJ3wDF.jpg",
            numberOfSeasons = 5
        ),
        TvShow(
            id = 8,
            title = "Game of Thrones",
            genres = listOf(
                Genre(10765, "Sci-Fi & Fantasy"),
                Genre(18, "Drama"),
                Genre(10759, "Action & Adventure")
            ),
            averageRating = 9.3,
            userRating = null,
            releaseDate = LocalDate(2011, 4, 17),
            overview = "Nine noble families wage war against each other to gain control over the mythical land of Westeros.",
            posterImageURL = "https://image.tmdb.org/t/p/w500/u3bZgnGQ9T01sWNhyveQz0wH0Hl.jpg",
            numberOfSeasons = 8
        ),
        TvShow(
            id = 9,
            title = "The Witcher",
            genres = listOf(
                Genre(10765, "Sci-Fi & Fantasy"),
                Genre(18, "Drama"),
                Genre(10759, "Action & Adventure")
            ),
            averageRating = 8.1,
            userRating = null,
            releaseDate = LocalDate(2019, 12, 20),
            overview = "Geralt of Rivia, a mutated monster hunter for hire, journeys toward his destiny in a turbulent world.",
            posterImageURL = "https://image.tmdb.org/t/p/w500/1XsiylUROQhEvayH2x4ezbT90gP.jpg",
            numberOfSeasons = 3
        ),
        TvShow(
            id = 10,
            title = "The Office",
            genres = listOf(Genre(35, "Comedy")),
            averageRating = 8.9,
            userRating = null,
            releaseDate = LocalDate(2005, 3, 24),
            overview = "A mockumentary on a group of typical office workers, where the workday consists of ego clashes and inappropriate behavior.",
            posterImageURL = "https://image.tmdb.org/t/p/w500/qWnJzyZhyy74gjpSjIXWmuk0ifX.jpg",
            numberOfSeasons = 9
        )
    )

}