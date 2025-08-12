package com.baghdad.localDatasource.mapper

import com.baghdad.localDatasource.roomDB.entity.RecentlyViewed
import com.baghdad.repository.model.RecentlyViewedDto

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