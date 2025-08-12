package com.baghdad.repository.mapper

import com.baghdad.repository.dummyData.DummyDataFactory.REVIEW_DTO
import com.baghdad.repository.model.ReviewDto
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class ReviewMapperTest {

    @Test
    fun `should map correctly to entity when ReviewDto contains valid data`() {
        val result = REVIEW_DTO.toEntity()

        assertThat(result).isEqualTo(REVIEW_DTO)
    }

    @Test
    fun `should map ReviewDto id to entity id correctly`() {
        val result = REVIEW_DTO.toEntity()

        assertThat(result.id).isEqualTo(REVIEW_DTO.id)
    }

    @Test
    fun `should map ReviewDto contentTitle to entity contentTitle correctly`() {
        val result = REVIEW_DTO.toEntity()

        assertThat(result.contentTitle).isEqualTo(REVIEW_DTO.contentTitle)
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

        assertThat(result.postedDate).isEqualTo(REVIEW_DTO.postedDate)
    }

    @Test
    fun `should map empty ReviewDto id to entity id correctly`() {
        val dto = REVIEW_DTO.copy(id = "")
        val result = dto.toEntity()

        assertThat(result.id).isEqualTo("")
    }

    @Test
    fun `should map empty ReviewDto contentTitle to entity contentTitle correctly`() {
        val dto = REVIEW_DTO.copy(contentTitle = "")
        val result = dto.toEntity()

        assertThat(result.contentTitle).isEqualTo("")
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

    @Test
    fun `should map empty ReviewDto postedDate to entity postedDate correctly`() {
        val dto = REVIEW_DTO.copy(postedDate = "")
        val result = dto.toEntity()

        assertThat(result.postedDate).isEqualTo("")
    }

} 