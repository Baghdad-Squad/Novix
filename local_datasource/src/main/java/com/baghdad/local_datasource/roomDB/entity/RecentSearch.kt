package com.baghdad.local_datasource.roomDB.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.baghdad.repository.model.RecentSearchDto

@Entity(tableName = "RecentSearch")
data class RecentSearch(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val query: String,
    val searchedAt: String
)

fun RecentSearch.toDto(): RecentSearchDto {
    return RecentSearchDto(
        id = this.id,
        query = this.query,
        searchedAt = this.searchedAt
    )
}
