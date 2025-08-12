import com.baghdad.remoteDataSource.mapper.movie.toMovieDtos
import com.baghdad.remoteDataSource.response.movie.PopularMoviesResponse
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class PopularMoviesResponseMapperTest {

    companion object {
        private val COMPLETE_MOVIE_RESPONSE = PopularMoviesResponse.Result(
            id = 123L,
            title = "Inception",
            genreIds = listOf(28L, 878L),
            voteAverage = 8.4,
            overview = "A thief who steals corporate secrets...",
            posterPath = "/inception.jpg",
            releaseDate = "2010-07-16"
        )

        private val EXPECTED_COMPLETE_DTO = MovieDto(
            id = 123L,
            title = "Inception",
            genres = listOf(
                GenreDto(28L, "", GenreDto.GenreType.MOVIE),
                GenreDto(878L, "", GenreDto.GenreType.MOVIE)
            ),
            imdbRating = 8.4,
            userRating = 0.0,
            releaseDate = "2010-07-16",
            overview = "A thief who steals corporate secrets...",
            posterPictureURL = "https://image.tmdb.org/t/p/w500/inception.jpg",
            trailerURL = "",
            runtimeMinutes = 0
        )

        private val MIXED_NULL_MOVIE_RESPONSE = PopularMoviesResponse.Result(
            id = 456L,
            title = "Interstellar",
            genreIds = listOf(12L, null, 18L),
            voteAverage = 8.6,
            overview = null,
            posterPath = "/interstellar.jpg",
            releaseDate = null
        )

        private val EXPECTED_MIXED_NULL_DTO = MovieDto(
            id = 456L,
            title = "Interstellar",
            genres = listOf(
                GenreDto(12L, "", GenreDto.GenreType.MOVIE),
                GenreDto(18L, "", GenreDto.GenreType.MOVIE)
            ),
            imdbRating = 8.6,
            userRating = 0.0,
            releaseDate = "0001-01-01",
            overview = "",
            posterPictureURL = "https://image.tmdb.org/t/p/w500/interstellar.jpg",
            trailerURL = "",
            runtimeMinutes = 0
        )

        private val COMPLETE_POPULAR_MOVIES_RESPONSE = PopularMoviesResponse(
            results = listOf(COMPLETE_MOVIE_RESPONSE, MIXED_NULL_MOVIE_RESPONSE)
        )

        private val NULL_RESULTS_POPULAR_MOVIES_RESPONSE = PopularMoviesResponse(
            results = null
        )

        private val EMPTY_RESULTS_POPULAR_MOVIES_RESPONSE = PopularMoviesResponse(
            results = emptyList()
        )

        private val RESULTS_WITH_NULL_ITEMS = PopularMoviesResponse(
            results = listOf(null, COMPLETE_MOVIE_RESPONSE, null, MIXED_NULL_MOVIE_RESPONSE)
        )
    }

    @Test
    fun `should convert PopularMoviesResponse with valid results to list of MovieDtos`() {
        val result = COMPLETE_POPULAR_MOVIES_RESPONSE.toMovieDtos()

        assertThat(result).containsExactly(EXPECTED_COMPLETE_DTO, EXPECTED_MIXED_NULL_DTO)
    }

    @Test
    fun `should handle null results in PopularMoviesResponse by returning empty list`() {
        val result = NULL_RESULTS_POPULAR_MOVIES_RESPONSE.toMovieDtos()

        assertThat(result).isEmpty()
    }

    @Test
    fun `should handle empty results in PopularMoviesResponse by returning empty list`() {
        val result = EMPTY_RESULTS_POPULAR_MOVIES_RESPONSE.toMovieDtos()

        assertThat(result).isEmpty()
    }

    @Test
    fun `should filter out null items in results list`() {
        val result = RESULTS_WITH_NULL_ITEMS.toMovieDtos()

        assertThat(result).containsExactly(EXPECTED_COMPLETE_DTO, EXPECTED_MIXED_NULL_DTO)
    }

    @Test
    fun `should filter out items with null id in results list`() {
        val response = PopularMoviesResponse(
            results = listOf(
                COMPLETE_MOVIE_RESPONSE.copy(id = null),
                MIXED_NULL_MOVIE_RESPONSE
            )
        )
        val result = response.toMovieDtos()

        assertThat(result).containsExactly(EXPECTED_MIXED_NULL_DTO)
    }
}