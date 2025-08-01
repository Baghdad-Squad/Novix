import com.baghdad.remoteDataSource.mapper.movie.toDto
import com.baghdad.remoteDataSource.mapper.movie.toMovieDtos
import com.baghdad.remoteDataSource.response.movie.PopularMoviesResponse
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class PopularMoviesResponseMapperTest {

    @Test
    fun `should map movies correctly when results contain valid data`() {
        // Given
        val response = PopularMoviesResponse(
            results = listOf(
                PopularMoviesResponse.Result(
                    id = 1,
                    title = "Inception",
                    genreIds = listOf(12L, 18L),
                    voteAverage = 8.8,
                    releaseDate = "2010-07-16",
                    overview = "A mind-bending thriller",
                    posterPath = "/inception.jpg"
                )
            )
        )

        // When
        val result = response.toMovieDtos()

        // Then
        assertThat(result).hasSize(1)
        val movie = result.first()
        assertThat(movie.id).isEqualTo(response.results?.first()?.id)
        assertThat(movie.title).isEqualTo(response.results?.first()?.title)
        assertThat(movie.genres.size).isEqualTo(2)
        assertThat(movie.posterPictureURL)
            .isEqualTo("https://image.tmdb.org/t/p/w500" + response.results?.first()?.posterPath)
    }

    @Test
    fun `should return empty list when results is null`() {
        // Given
        val response = PopularMoviesResponse(results = null)

        // When
        val result = response.toMovieDtos()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should skip movies when id is null`() {
        // Given
        val response = PopularMoviesResponse(
            results = listOf(PopularMoviesResponse.Result(id = null))
        )

        // When
        val result = response.toMovieDtos()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should return empty list when fields are null`() {
        // Given
        val response = PopularMoviesResponse(
            results = listOf(
                PopularMoviesResponse.Result(
                    id = 0,
                    title = null,
                    genreIds = null,
                    voteAverage = null,
                    releaseDate = null,
                    overview = null,
                    posterPath = null
                )
            )
        )

        // When
        val result = response.toMovieDtos()

        // Then
        assertThat(result).hasSize(1)
        val movie = result.first()
        assertThat(movie.id).isEqualTo(0)
        assertThat(movie.title).isEmpty()
        assertThat(movie.genres).isEmpty()
        assertThat(movie.imdbRating).isEqualTo(0.0)
        assertThat(movie.releaseDate).isEqualTo("0001-01-01")
        assertThat(movie.posterPictureURL).isEmpty()
    }

    @Test
    fun `should map genreIds when elements contain nulls`() {
        // Given
        val result = PopularMoviesResponse.Result(
            id = 20,
            title = "Null Genre Ids",
            genreIds = listOf(null, 15L),
            voteAverage = 7.0,
            releaseDate = "2022-05-05",
            overview = "Some overview",
            posterPath = "/poster2.jpg"
        )

        // When
        val movieDto = result.toDto()

        // Then
        assertThat(movieDto.genres.size).isEqualTo(2)
        assertThat(movieDto.genres.first().id).isEqualTo(0L)
        assertThat(movieDto.genres.last().id).isEqualTo(15L)
    }
}