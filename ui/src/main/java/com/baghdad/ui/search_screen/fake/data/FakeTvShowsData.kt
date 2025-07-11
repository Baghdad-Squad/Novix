package com.baghdad.ui.search_screen.fake.data

import androidx.compose.runtime.Composable

data class TvShow(
    val id: Int,
    val title: String,
    val posterUrl: String,
    val year: String,
    val rating: Float,
    val isSaved: Boolean
)

@Composable
fun getFakeTvShows(): List<TvShow> {
    return listOf(
        TvShow(
            id = 1,
            title = "Breaking Bad",
            posterUrl = "https://image.tmdb.org/t/p/w500/ggFHVNu6YYI5L9pCfOacjizRGt.jpg",
            year = "2008-2013",
            rating = 9.5f,
            isSaved = true
        ),
        TvShow(
            id = 2,
            title = "Game of Thrones",
            posterUrl = "https://image.tmdb.org/t/p/w500/u3bZgnGQ9T01sWNhyveQz0wH0Hl.jpg",
            year = "2011-2019",
            rating = 9.2f,
            isSaved = false
        ),
        TvShow(
            id = 3,
            title = "Stranger Things",
            posterUrl = "https://image.tmdb.org/t/p/w500/49WJfeN0moxb9IPfGn8AIqMGskD.jpg",
            year = "2016-",
            rating = 8.7f,
            isSaved = true
        )
    )
}