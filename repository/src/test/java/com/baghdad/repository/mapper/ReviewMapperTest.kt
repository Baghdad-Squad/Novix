package com.baghdad.repository.mapper

import com.baghdad.entity.media.Review
import com.baghdad.repository.dummyData.DummyDataFactory.DummyDataFactory.REVIEW_DTO
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class ReviewMapperTest {

    @Test
    fun `should map correctly to entity when ReviewDto contains valid data`() {
        val expected = Review(
            id = REVIEW_DTO.id,
            authorUsername = REVIEW_DTO.authorUsername,
            authorName = REVIEW_DTO.authorName,
            rating = REVIEW_DTO.rating,
            authorAvatarUrl = REVIEW_DTO.authorAvatarUrl,
            reviewText = REVIEW_DTO.reviewText,
            postedDate = LocalDate.parse(REVIEW_DTO.postedDate)
        )

        val result = REVIEW_DTO.toEntity()

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `should map ReviewDto id to entity id correctly`() {
        val result = REVIEW_DTO.toEntity()

        assertThat(result.id).isEqualTo(REVIEW_DTO.id)
    }

    @Test
    fun `should map ReviewDto contentTitle to entity contentTitle correctly`() {
        val result = REVIEW_DTO.toEntity()

        assertThat(result.authorUsername).isEqualTo(REVIEW_DTO.authorUsername)
    }

    @Test
    fun `should map ReviewDto authorName to entity authorName correctly`() {
        val result = REVIEW_DTO.toEntity()

        assertThat(result.authorName).isEqualTo(REVIEW_DTO.authorName)
    }

    @Test
    fun `should map ReviewDto rating to entity rating correctly`() {
        val result = REVIEW_DTO.toEntity()

        assertThat(result.rating).isEqualTo(REVIEW_DTO.rating)
    }

    @Test
    fun `should map ReviewDto authorAvatarUrl to entity authorAvatarUrl correctly`() {
        val result = REVIEW_DTO.toEntity()

        assertThat(result.authorAvatarUrl).isEqualTo(REVIEW_DTO.authorAvatarUrl)
    }

    @Test
    fun `should map ReviewDto reviewText to entity reviewText correctly`() {
        val result = REVIEW_DTO.toEntity()

        assertThat(result.reviewText).isEqualTo(REVIEW_DTO.reviewText)
    }

    @Test
    fun `should map ReviewDto postedDate to entity postedDate correctly`() {
        val result = REVIEW_DTO.toEntity()

        assertThat(result.postedDate).isEqualTo(LocalDate.parse(REVIEW_DTO.postedDate))
    }

    @Test
    fun `should map empty ReviewDto id to entity id correctly`() {
        val dto = REVIEW_DTO.copy(id = "")
        val result = dto.toEntity()

        assertThat(result.id).isEqualTo("")
    }

    @Test
    fun `should map empty ReviewDto contentTitle to entity contentTitle correctly`() {
        val dto = REVIEW_DTO.copy(authorUsername = "")
        val result = dto.toEntity()

        assertThat(result.authorUsername).isEqualTo("")
    }

    @Test
    fun `should map empty ReviewDto authorName to entity authorName correctly`() {
        val dto = REVIEW_DTO.copy(authorName = "")
        val result = dto.toEntity()

        assertThat(result.authorName).isEqualTo("")
    }

    @Test
    fun `should map empty ReviewDto rating to entity rating correctly`() {
        val dto = REVIEW_DTO.copy(rating = 0.0)
        val result = dto.toEntity()

        assertThat(result.rating).isEqualTo(0.0)
    }

    @Test
    fun `should map empty ReviewDto authorAvatarUrl to entity authorAvatarUrl correctly`() {
        val dto = REVIEW_DTO.copy(authorAvatarUrl = "")
        val result = dto.toEntity()

        assertThat(result.authorAvatarUrl).isEqualTo("")
    }

    @Test
    fun `should map empty ReviewDto reviewText to entity reviewText correctly`() {
        val dto = REVIEW_DTO.copy(reviewText = "")
        val result = dto.toEntity()

        assertThat(result.reviewText).isEqualTo("")
    }
}