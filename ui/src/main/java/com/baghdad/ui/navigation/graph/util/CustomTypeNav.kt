package com.baghdad.ui.navigation.graph.util

import androidx.navigation.NavType
import androidx.savedstate.SavedState
import com.baghdad.viewmodel.review.ContentType

class CustomTypeNav : NavType<ContentType>(isNullableAllowed = false) {
    override fun put(
        bundle: SavedState,
        key: String,
        value: ContentType
    ) {
        return bundle.putString(key, value.name)
    }

    override fun get(
        bundle: SavedState,
        key: String
    ): ContentType? {
        return bundle.getString(key)?.let { ContentType.valueOf(it) }
    }

    override fun parseValue(value: String): ContentType {
        return ContentType.valueOf(value)
    }
}