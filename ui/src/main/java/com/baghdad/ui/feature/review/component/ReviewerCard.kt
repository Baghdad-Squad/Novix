package com.baghdad.ui.feature.review.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.R
import com.baghdad.design_system.component.ExpandableText
import com.baghdad.design_system.component.LabeledIconRow
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.design_system.theme.Theme

@Composable
fun ReviewerCard(
    title: String,
    rate: Double,
    authorName: String,
    reviewDate: String,
    authorAvatar: String,
    contentName: String,
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
    onExpandedChange: (Boolean) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .border(1.dp, Theme.color.stroke, RoundedCornerShape(12.dp))
            .background(Theme.color.surface)
    ) {
        ReviewerHeader(
            imageUrl = authorAvatar,
            name = authorName,
            contentName = contentName,
            rate = rate
        )

        ExpandableText(
            text = title,
            isExpanded = isExpanded,
            onExpandedChange = onExpandedChange,
            readMoreMaxLines = 5,
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        LabeledIconRow(
            title = reviewDate,
            icon = painterResource(R.drawable.ic_calender),
            modifier = Modifier.padding(12.dp)
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ReviewerCardPreview() {
    NovixTheme(isDarkTheme = false) {
        val (expanded, onExpandedChange) = remember { mutableStateOf(false) }
        ReviewerCard(
            authorAvatar = "https://via.placeholder.com/150",
            title = "The show explores life's complexities through a whimsical metaphor involving a mouse in a man's pocket, which might perplex younger audiences. One episode poignantly addresses  experiences her first period im sical metaphor involving a mouse in a man's pocket, which might perplex younger audiences. One episode poignantly addresses  experiences her first period ",
            rate = 9.8,
            authorName = "CinephileHub",
            contentName = "MovieBuff1967",
            reviewDate = "03-12-2001",
            isExpanded = expanded,
            onExpandedChange = onExpandedChange
        )
    }
}
