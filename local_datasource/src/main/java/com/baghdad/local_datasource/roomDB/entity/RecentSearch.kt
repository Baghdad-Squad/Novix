package com.baghdad.local_datasource.roomDB.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.baghdad.local_datasource.roomDB.entity.RecentSearch.Companion.RECENTLY_VIEWED_TABLE_NAME
import com.baghdad.repository.model.RecentSearchDto

@Entity(tableName = RECENTLY_VIEWED_TABLE_NAME)
data class RecentSearch(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    val id: Long = 0L,
    @ColumnInfo(name = QUERY)
    val query: String,
    @ColumnInfo(name = SEARCHED_AT)
    val searchedAt: Long = System.currentTimeMillis()
) {
    companion object {
        const val RECENTLY_VIEWED_TABLE_NAME = "RecentlyViewed"
        const val ID = "id"
        const val QUERY = "query"
        const val SEARCHED_AT = "searchedAt"
    }
}

fun RecentSearch.toDto(): RecentSearchDto {
    return RecentSearchDto(
        id = this.id,
        query = this.query,
        searchedAt = this.searchedAt
    )
}
