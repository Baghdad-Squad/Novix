package com.baghdad.repository.mapper

import com.baghdad.repository.dummyData.DummyDataFactory.PAGED_ITEMS_DTO
import com.baghdad.repository.dummyData.DummyDataFactory.SAVED_LIST_DETAILS_DTO
import com.baghdad.repository.dummyData.DummyDataFactory.SAVED_LIST_DTO
import com.baghdad.repository.model.savedList.SavableMovieDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class SavedListDetailsMapperTest {
    @Test
    fun `should map savedList correctly`() {
        val result = SAVED_LIST_DETAILS_DTO.toEntity()

        assertThat(result.savedList).isEqualTo(SAVED_LIST_DTO)
    }

    @Test
    fun `should map pagedItems correctly when converting SavedListDetailsDto to entity`() {
        val result = SAVED_LIST_DETAILS_DTO.toEntity()
        val expectedPagedItems =
            PAGED_ITEMS_DTO.toPagedResult(dataMapper = SavableMovieDto::toEntity)

        assertThat(result.pagedItems).isEqualTo(expectedPagedItems)
    }
}