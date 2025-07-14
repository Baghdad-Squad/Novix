package com.baghdad.local_datasource.database.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.baghdad.local_datasource.database.dto.RecentlyViewed.Companion.RECENTLY_VIEWED_TABLE_NAME
import com.baghdad.repository.model.RecentlyViewedDto


@Entity(tableName = RECENTLY_VIEWED_TABLE_NAME)
data class RecentlyViewed(
    @PrimaryKey
    @ColumnInfo(name = CONTENT_ID)
    val contentId: Long,
    @ColumnInfo(name = CONTENT_TYPE)
    val contentType: String,
    @ColumnInfo(name = CONTENT_IMAGE_URL)
    val contentImageURL: String,
    @ColumnInfo(name = VIEWED_AT)
    val viewedAt: Long = System.currentTimeMillis()
) {
    companion object {
        const val RECENTLY_VIEWED_TABLE_NAME = "RecentlyViewed"
        const val CONTENT_ID = "contentId"
        const val CONTENT_TYPE = "contentType"
        const val CONTENT_IMAGE_URL = "contentImageUrl"
        const val VIEWED_AT = "viewedAt"
    }
}

fun RecentlyViewed.toDto(): RecentlyViewedDto {
    return RecentlyViewedDto(
        contentId = this.contentId,
        contentType = RecentlyViewedDto.ContentType.valueOf(contentType),
        contentImageUrl = contentImageURL,
        viewedAtEpochMillis = viewedAt
    )
}

fun RecentlyViewedDto.toEntity(): RecentlyViewed {
    return RecentlyViewed(
        contentId = this.contentId,
        contentType = contentType.name,
        contentImageURL = contentImageUrl,
        viewedAt = viewedAtEpochMillis
    )
}
