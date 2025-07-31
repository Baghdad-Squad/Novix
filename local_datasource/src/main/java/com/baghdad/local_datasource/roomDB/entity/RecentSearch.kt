package com.baghdad.local_datasource.roomDB.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.baghdad.repository.model.RecentSearchDto

@Entity(
    tableName = "RecentSearch",
    indices = [Index(value = ["query"], unique = true)],
)
data class RecentSearch(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val query: String,
    val searchedAt: Long = System.currentTimeMillis()
)

fun RecentSearch.toDto(): RecentSearchDto {
    return RecentSearchDto(
        id = this.id,
        query = this.query,
        searchedAt = this.searchedAt
    )
}
