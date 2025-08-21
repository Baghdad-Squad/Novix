package com.baghdad.domain.usecase.movie

import com.baghdad.domain.repository.MovieRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetMovieGalleryUseCaseTest {

    private val movieRepository = mockk<MovieRepository>()
    private val getMovieGalleryUseCase = GetMovieGalleryUseCase(movieRepository)

    @Test
    fun `getMovieGalleryUseCase should return list containing poster URL`() = runTest {
        coEvery { movieRepository.getMovieImages(movieId) } returns listOf(movieImageUrl)

        val result = getMovieGalleryUseCase(movieId)

        assertThat(result).containsExactly(movieImageUrl)
    }

    @Test
    fun `getMovieGalleryUseCase should return empty list when no images available`() = runTest {
        coEvery { movieRepository.getMovieImages(movieId) } returns emptyList()

        val result = getMovieGalleryUseCase(movieId)

        assertThat(result).isEmpty()
    }

    private companion object {
        val movieId = MovieMock.MOVIE_ID
        val movieImageUrl = "https://example.com/shawshank.jpg"
    }
}