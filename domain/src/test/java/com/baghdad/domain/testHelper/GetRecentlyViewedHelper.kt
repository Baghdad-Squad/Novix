package com.baghdad.domain.testHelper

import com.baghdad.domain.model.search.RecentlyViewed
import kotlinx.datetime.LocalDateTime

fun getRecentlyViewedItem(
    contentId: Long = 1L,
    contentImageUrl: String = "https://example.com/image.jpg",
    contentType: RecentlyViewed.ContentType = RecentlyViewed.ContentType.MOVIE,
    viewedAt: LocalDateTime = LocalDateTime(2023, 10, 1, 12, 0, 0)

) = RecentlyViewed(
    contentId = contentId,
    contentImageUrl = contentImageUrl,
    contentType = contentType,
    viewedAt = viewedAt
)

fun getRecentlyViewedList(
    size: Int
): List<RecentlyViewed> = List(size) { index ->
    getRecentlyViewedItem(
        contentId = index.toLong() + 1,
        contentImageUrl = "https://example.com/image${index + 1}.jpg",
        contentType = if (index % 2 == 0) RecentlyViewed.ContentType.MOVIE else RecentlyViewed.ContentType.TV_SHOW,
        viewedAt = LocalDateTime(2023, 10, 1, 12, index, 0)
    )
}