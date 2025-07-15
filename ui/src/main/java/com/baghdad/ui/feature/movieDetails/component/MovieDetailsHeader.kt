package com.baghdad.ui.feature.movieDetails.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baghdad.design_system.R
import com.baghdad.design_system.component.CircleDot
import com.baghdad.design_system.component.LabeledIconRow
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.modifier.noRippleClickable
import com.baghdad.design_system.theme.Theme

@Composable
fun MovieDetailsHeader(
    title: String,
    duration: String,
    releaseDate: String,
    rating: Double,
    categories: List<String>,
    modifier: Modifier,
    onViewReviewClicked: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Theme.color.surface, shape = RoundedCornerShape(16.dp))
            .border(1.dp, Theme.color.stroke, shape = RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            style = Theme.typography.title.medium,
            color = Theme.color.title,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEachIndexed { index, category ->
                Text(
                    text = category,
                    fontSize = 14.sp,
                    style = Theme.typography.label.medium,
                    color = Theme.color.body,
                    modifier = Modifier
                )
                if (index < categories.size - 1) {
                    CircleDot(modifier = Modifier.align(Alignment.CenterVertically))
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LabeledIconRow(
                title = rating.toString(),
                icon = painterResource(R.drawable.ic_star_filled),
                tint = Theme.color.yellowAccent
            )
            CircleDot()
            LabeledIconRow(
                title = duration,
                icon = painterResource(R.drawable.ic_time_oclock),
            )
            CircleDot()
            LabeledIconRow(
                title = releaseDate,
                icon = painterResource(R.drawable.ic_calendar),
            )
        }
        Text(
            text = "View reviews",
            fontSize = 12.sp,
            style = Theme.typography.label.medium,
            color = Theme.color.primary,
            modifier = Modifier.noRippleClickable {
                onViewReviewClicked()
            }
        )
    }
}

