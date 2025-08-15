package com.baghdad.repository.mapper

import com.baghdad.entity.savedList.SavedList
import com.baghdad.repository.dummyData.DummyDataFactory.DummyDataFactory.SAVED_LIST_DTO
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class SavedListMapperTest {
    @Test
    fun `should map SavedListDto to SavedList entity when enter correctly`() {
        val excepted = SavedList(
            id = SAVED_LIST_DTO.id,
            name = SAVED_LIST_DTO.name,
            itemCount = SAVED_LIST_DTO.itemCount
        )

        val result = SAVED_LIST_DTO.toEntity()

        assertThat(result).isEqualTo(excepted)
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