package com.baghdad.design_system.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.R
import com.baghdad.design_system.preview.NovixPreviews
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.design_system.theme.Theme
import com.webtoonscorp.android.readmore.foundation.ToggleArea
import com.webtoonscorp.android.readmore.material3.ReadMoreText

@Composable
fun ExpandableText(
    text: String,
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
    readMoreMaxLines: Int,
    onExpandedChange: (Boolean) -> Unit,
) {
    AnimatedContent(
        targetState = isExpanded,
        transitionSpec = {
            fadeIn(animationSpec = tween(300)) togetherWith
                    fadeOut(animationSpec = tween(300))
        },
        label = "ReadMoreAnimation"
    ) { expanded ->
        ReadMoreText(
            text = text,
            expanded = expanded,
            onExpandedChange = onExpandedChange,
            style = Theme.typography.body.small,
            color = Theme.color.body,

            readMoreText = stringResource(R.string.read_more),
            readMoreColor = Theme.color.primary,
            readMoreStyle = Theme.typography.label.medium.toSpanStyle(),
            readMoreMaxLines = readMoreMaxLines,

            readLessText = stringResource(R.string.read_less),
            readLessColor = Theme.color.primary,
            readLessStyle = Theme.typography.label.medium.toSpanStyle(),

            toggleArea = ToggleArea.All,
            modifier = modifier
        )
    }
}

@NovixPreviews
@Composable
private fun ReadMoreTextComposePreview() {
    var expanded by rememberSaveable { mutableStateOf(false) }

    NovixTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Theme.color.surface)
                .padding(16.dp)
        ) {
            ExpandableText(
                text = "It is a 1994 American drama film, considered one of the greatest films in cinematic history. It revolves around Andy Dufresne, a banker wrongfully convicted of the murder of his wife and her lover, and imprisoned in Shawshank Red State Prison. There, he forms a friendship with another inmate named Red and embarks on a long journey of pain, hope, and planning for freedom.",
                isExpanded = expanded,
                onExpandedChange = { expanded = it },
                readMoreMaxLines = 4,
            )
        }
    }
}