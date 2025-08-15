// package com.baghdad.viewmodel.categoryMovies
//
// import com.baghdad.entity.media.Genre
// import com.baghdad.entity.media.Movie
// import com.google.common.truth.Truth.assertThat
// import kotlinx.coroutines.flow.flowOf
// import kotlinx.datetime.LocalDate
// import org.junit.jupiter.api.Test
//
// class CategoryMoviesStateTest {
//
//    @Test
//    fun `should have expected defaults when CategoryMoviesState created with default constructor`() {
//
//        val state = CategoryMoviesState()
//
//        assertThat(state.isLoading).isFalse()
//        assertThat(state.categoryName).isEqualTo("")
//        assertThat(state.moviesFlow).isNotNull()
//    }
//
//    @Test
//    fun `should return correct values when CategoryMoviesState created with custom values`() {
//
//        val state = CategoryMoviesState(
//            isLoading = true,
//            moviesFlow = flowOf(),
//            categoryName = "Action"
//        )
//
//        assertThat(state.isLoading).isTrue()
//        assertThat(state.categoryName).isEqualTo("Action")
//        assertThat(state.moviesFlow).isNotNull()
//    }
//
//    @Test
//    fun `should have expected default values when MovieUiState created with default constructor`() {
//
//        val movie = CategoryMoviesState.MovieUiState()
//
//        assertThat(movie.id).isEqualTo(0L)
//        assertThat(movie.posterPictureURL).isEmpty()
//        assertThat(movie.isSaved).isFalse()
//    }
//
//    @Test
//    fun `should return those values when MovieUiState created with custom values`() {
//
//        val movie = CategoryMoviesState.MovieUiState(
//            id = 10L,
//            posterPictureURL = "poster-url",
//            isSaved = true
//        )
//
//        assertThat(movie.id).isEqualTo(10L)
//        assertThat(movie.posterPictureURL).isEqualTo("poster-url")
//        assertThat(movie.isSaved).isTrue()
//    }
//
//    @Test
//    fun `should be equal when two MovieUiStates have same values`() {
//
//        val m1 = CategoryMoviesState.MovieUiState(1L, "url", true)
//        val m2 = CategoryMoviesState.MovieUiState(1L, "url", true)
//
//        assertThat(m1).isEqualTo(m2)
//        assertThat(m1.hashCode()).isEqualTo(m2.hashCode())
//    }
//
//    @Test
//    fun `should include all fields when MovieUiState toString is called`() {
//        val movie = CategoryMoviesState.MovieUiState(5L, "url", false)
//        val str = movie.toString()
//
//        assertThat(str).contains("id=5")
//        assertThat(str).contains("posterPictureURL=url")
//        assertThat(str).contains("isSaved=false")
//    }
//
//    @Test
//    fun `should map id and posterPictureURL and set isSaved false when toUiState called`() {
//        val result = testMovie.toUiState()
//
//        assertThat(result.id).isEqualTo(testMovie.id)
//        assertThat(result.posterPictureURL).isEqualTo(testMovie.posterImageURL)
//        assertThat(result.isSaved).isFalse()
//    }
//
//    @Test
//    fun `should map correctly when toUiState called on movie with empty poster`() {
//
//        val movie = testMovie.copy(posterImageURL = "")
//        val result = movie.toUiState()
//
//        assertThat(result.posterPictureURL).isEmpty()
//    }
//
//    private val testMovie = Movie(
//        id = 77L,
//        title = "Test Movie",
//        genres = listOf(Genre(1L, "Action")),
//        averageRating = 8.5,
//        userRating = 9.0,
//        releaseDate = LocalDate.parse("2023-10-01"),
//        overview = "A test overview",
//        posterImageURL = "https://test.com/poster.jpg",
//        trailerURL = "https://test.com/trailer.mp4",
//        runtimeMinutes = 120
//    )
// }
