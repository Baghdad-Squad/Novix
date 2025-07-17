package com.baghdad.local_datasource.roomDB.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.baghdad.repository.model.RecentlyViewedDto


@Entity(tableName = "RecentlyViewed")
data class RecentlyViewed(
    @PrimaryKey val contentId: Long,
    val contentType: String,
    val contentImageURL: String,
    val viewedAt: Long = System.currentTimeMillis()
)

fun RecentlyViewed.toDto(): RecentlyViewedDto {
    return RecentlyViewedDto(
        contentId = this.contentId,
        contentType = RecentlyViewedDto.ContentType.valueOf(contentType),
        contentImageUrl = contentImageURL,
        viewedAtEpochMillis = viewedAt
    )
}

fun RecentlyViewedDto.toLocalDto(): RecentlyViewed {
    return RecentlyViewed(
        contentId = this.contentId,
        contentType = contentType.name,
        contentImageURL = contentImageUrl,
        viewedAt = viewedAtEpochMillis
    )
}
