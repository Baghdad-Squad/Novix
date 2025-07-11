package com.baghdad.ui.search_screen.fake.data

import androidx.compose.runtime.Composable

data class Movie(
    val id: Int,
    val title: String,
    val posterUrl: String,
    val year: String,
    val rating: Float,
    val isSaved: Boolean
)

@Composable
fun getFakeMovies(): List<Movie> {
    return listOf(
        Movie(
            id = 1,
            title = "The Shawshank Redemption",
            posterUrl = "https://image.tmdb.org/t/p/w500/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg",
            year = "1994",
            rating = 9.3f,
            isSaved = true
        ),
        Movie(
            id = 2,
            title = "The Godfather",
            posterUrl = "https://image.tmdb.org/t/p/w500/3bhkrj58Vtu7enYsRolD1fZdja1.jpg",
            year = "1972",
            rating = 9.2f,
            isSaved = false
        ),
        Movie(
            id = 3,
            title = "The Dark Knight",
            posterUrl = "https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg",
            year = "2008",
            rating = 9.0f,
            isSaved = true
        ),
        Movie(
            id = 4,
            title = "Pulp Fiction",
            posterUrl = "https://image.tmdb.org/t/p/w500/d5iIlFn5s0ImszYzBPb8JPIfbXD.jpg",
            year = "1994",
            rating = 8.9f,
            isSaved = false
        ),
        Movie(
            id = 5,
            title = "Fight Club",
            posterUrl = "https://image.tmdb.org/t/p/w500/pB8BM7pdSp6B6Ih7QZ4DrQ3PmJK.jpg",
            year = "1999",
            rating = 8.8f,
            isSaved = true
        )
    )
}
