package com.baghdad.remoteDataSource.mapper.mediaAccountStates

import com.baghdad.remoteDataSource.response.mediaAccount.MediaAccountStatesResponse
import com.baghdad.repository.model.MediaAccountStateDto
import com.google.common.truth.Truth.assertThat
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import org.junit.jupiter.api.Test

class MediaAccountStatesMapperTest {

    companion object {
        private const val VALID_ID = 123L
        private const val RATING_VALUE = 8

        private val RATED_JSON = JsonObject().apply {
            addProperty("value", RATING_VALUE)
        }

        private val EXPECTED_FULL_DTO = MediaAccountStateDto(
            id = VALID_ID,
            rated = RATING_VALUE
        )

        private val EXPECTED_NULL_ID_DTO = MediaAccountStateDto(
            id = -1L,
            rated = RATING_VALUE
        )

        private val EXPECTED_NULL_RATED_DTO = MediaAccountStateDto(
            id = VALID_ID,
            rated = null
        )
    }

    @Test
    fun `should map id and rated value when both are present`() {
        val response = MediaAccountStatesResponse(
            id = VALID_ID,
            rated = RATED_JSON
        )

        val result = response.toDto()

        assertThat(result).isEqualTo(EXPECTED_FULL_DTO)
    }

    @Test
    fun `should map null id to default and keep rated value`() {
        val response = MediaAccountStatesResponse(
            id = null,
            rated = RATED_JSON
        )

        val result = response.toDto()

        assertThat(result).isEqualTo(EXPECTED_NULL_ID_DTO)
    }

    @Test
    fun `should map null rated to null in dto`() {
        val response = MediaAccountStatesResponse(
            id = VALID_ID,
            rated = null
        )

        val result = response.toDto()

        assertThat(result).isEqualTo(EXPECTED_NULL_RATED_DTO)
    }

    @Test
    fun `should map non JsonObject rated to null`() {
        val response = MediaAccountStatesResponse(
            id = VALID_ID,
            rated = JsonPrimitive("not_an_object")
        )

        val result = response.toDto()

        assertThat(result).isEqualTo(EXPECTED_NULL_RATED_DTO)
    }
}