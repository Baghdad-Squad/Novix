package com.baghdad.repository.mapper

import com.baghdad.repository.dummyData.DummyDataFactory.SAVED_LIST_DTO
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class SavedListMapperTest {
    @Test
    fun `should map SavedListDto to SavedList entity when enter correctly`() {
        val result = SAVED_LIST_DTO.toEntity()

        assertThat(result).isEqualTo(SAVED_LIST_DTO)
    }

    @Test
    fun `should map SavedListDto id to entity id when enter correctly`() {
        val result = SAVED_LIST_DTO.toEntity()
        assertThat(result.id).isEqualTo(SAVED_LIST_DTO.id)
    }

    @Test
    fun `should map SavedListDto name to entity name when enter correctly`() {
        val result = SAVED_LIST_DTO.toEntity()
        assertThat(result.name).isEqualTo(SAVED_LIST_DTO.name)
    }

    @Test
    fun `should map SavedListDto itemCount to entity itemCount when enter correctly`() {
        val result = SAVED_LIST_DTO.toEntity()
        assertThat(result.itemCount).isEqualTo(SAVED_LIST_DTO.itemCount)
    }
}