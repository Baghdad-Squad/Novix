package com.baghdad.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.baghdad.ui.R

@Composable
fun EpisodeTitle(rawTitle: String): String {
    val context = LocalContext.current
    val episodeWord = context.getString(R.string.episode_title)

    return rawTitle
        .takeIf { it.isNotBlank() }
        ?.replace(episodeWord, "", ignoreCase = true)
        ?.replace(Regex("^\\d+\\s*[-:]?\\s*|\\s*[-:]?\\s*\\d+$"), "")
        ?.trim()
        ?.takeIf { it.isNotEmpty() }
        ?.let { "- $it" }
        ?: ""
}
