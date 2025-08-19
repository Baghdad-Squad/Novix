package com.baghdad.remoteDataSource.mapper.review

import com.baghdad.remoteDataSource.response.reviews.ReviewsResponse
import com.baghdad.repository.model.ReviewDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class ReviewResponseMapperTest {
    companion object {
        private val COMPLETE_REVIEW_RESPONSE = ReviewsResponse.ReviewResponse(
            id = "5f8d3a7b3f3c4a3d6c8b4567",
            author = "John Doe",
            authorDetails = ReviewsResponse.MovieAuthorDetails(
                name = "John Doe",
                username = "johndoe123",
                avatarPath = "/avatar.jpg",
                rating = 8.5
            ),
            content = "This movie was fantastic!",
            createdAt = "2023-01-15"
        )

        private val EXPECTED_COMPLETE_DTO = ReviewDto(
            id = "5f8d3a7b3f3c4a3d6c8b4567",
            authorName = "John Doe",
            authorAvatarUrl = "https://image.tmdb.org/t/p/w500/avatar.jpg",
            authorUsername = "johndoe123",
            rating = 8.5,
            reviewText = "This movie was fantastic!",
            postedDate = "2023-01-15"
        )

        private val NULL_VALUES_REVIEW_RESPONSE = ReviewsResponse.ReviewResponse(
            id = null,
            author = null,
            authorDetails = null,
            content = null,
            createdAt = null
        )

        private val EXPECTED_NULL_VALUES_DTO = ReviewDto(
            id = "",
            authorName = "",
            authorAvatarUrl = "",
            authorUsername = "",
            rating = 0.0,
            reviewText = "",
            postedDate = "0001-01-01"
        )

        private val MIXED_NULL_REVIEW_RESPONSE = ReviewsResponse.ReviewResponse(
            id = "6g9e4b8c4g4b4e7d9c9c5678",
            author = null,
            authorDetails = ReviewsResponse.MovieAuthorDetails(
                name = null,
                username = "movielover",
                avatarPath = null,
                rating = null
            ),
            content = "Good but not great",
            createdAt = null
        )

        private val EXPECTED_MIXED_NULL_DTO = ReviewDto(
            id = "6g9e4b8c4g4b4e7d9c9c5678",
            authorName = "",
            authorAvatarUrl = "",
            authorUsername = "movielover",
            rating = 0.0,
            reviewText = "Good but not great",
            postedDate = "0001-01-01"
        )

        private val COMPLETE_REVIEWS_RESPONSE = ReviewsResponse(
            results = listOf(COMPLETE_REVIEW_RESPONSE, MIXED_NULL_REVIEW_RESPONSE)
        )

        private val NULL_RESULTS_REVIEWS_RESPONSE = ReviewsResponse(
            results = null
        )

        private val EMPTY_RESULTS_REVIEWS_RESPONSE = ReviewsResponse(
            results = emptyList()
        )
    }

    @Test
    fun `should convert complete ReviewsResponse to list of ReviewDtos`() {
        val result = COMPLETE_REVIEWS_RESPONSE.toReviewDto()

        assertThat(result).containsExactly(EXPECTED_COMPLETE_DTO, EXPECTED_MIXED_NULL_DTO)
    }

    @Test
    fun `should handle null results by returning empty list`() {
        val result = NULL_RESULTS_REVIEWS_RESPONSE.toReviewDto()

        assertThat(result).isEmpty()
    }

    @Test
    fun `should handle empty results by returning empty list`() {
        val result = EMPTY_RESULTS_REVIEWS_RESPONSE.toReviewDto()

        assertThat(result).isEmpty()
    }

    @Test
    fun `should filter out items with null id`() {
        val response = ReviewsResponse(
            results = listOf(
                COMPLETE_REVIEW_RESPONSE.copy(id = null),
                MIXED_NULL_REVIEW_RESPONSE
            )
        )
        val result = response.toReviewDto()

        assertThat(result).containsExactly(EXPECTED_MIXED_NULL_DTO)
    }

    @Test
    fun `should handle complete ReviewResponse with all fields`() {
        val response = ReviewsResponse(results = listOf(COMPLETE_REVIEW_RESPONSE))
        val result = response.toReviewDto()

        assertThat(result.first()).isEqualTo(EXPECTED_COMPLETE_DTO)
    }

    @Test
    fun `should handle ReviewResponse with mixed null and non-null values`() {
        val response = ReviewsResponse(results = listOf(MIXED_NULL_REVIEW_RESPONSE))
        val result = response.toReviewDto()

        assertThat(result.first()).isEqualTo(EXPECTED_MIXED_NULL_DTO)
    }
}