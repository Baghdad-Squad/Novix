package com.baghdad.local_datasource.roomDB.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.baghdad.repository.model.SearchQueryDto

@Entity(tableName = "search_query")
data class SearchQuery(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val queryName: String,
    val mediaId: Long,
    val mediaType: String,
    val timeStamp: Long = System.currentTimeMillis()
)

fun SearchQueryDto.toLocalDto(): SearchQuery {
    return SearchQuery(
        queryName = queryName,
        mediaId = mediaId,
        mediaType = mediaType.name,
    )
}

fun SearchQuery.toDto(): SearchQueryDto {
    return SearchQueryDto(
        queryName = queryName,
        mediaId = mediaId,
        mediaType = SearchQueryDto.MediaType.valueOf(mediaType)
    )
}