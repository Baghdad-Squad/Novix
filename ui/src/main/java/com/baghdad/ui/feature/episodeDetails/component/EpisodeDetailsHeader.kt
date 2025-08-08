package com.baghdad.ui.feature.episodeDetails.component

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baghdad.design_system.component.CircleDot
import com.baghdad.design_system.component.LabeledIconRow
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.modifier.noRippleClickable
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R
import com.baghdad.viewmodel.episodeDetails.EpisodeDetailsScreenState

@Composable
fun EpisodeDetailsHeader(
    title: String,
    categories: List<EpisodeDetailsScreenState.CategoryUiState>,
    onCategoryClicked: (id: Long) -> Unit,
    rating: Int,
    releaseDate: String,
    seasonNumber: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .background(Theme.color.surface, shape = RoundedCornerShape(16.dp))
                .border(1.dp, Theme.color.stroke, shape = RoundedCornerShape(16.dp))
                .padding(12.dp),
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            style = Theme.typography.title.medium,
            color = Theme.color.title,
            modifier = Modifier.padding(bottom = 44.dp),
        )
        FlowRow(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalArrangement = Arrangement.Center,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            categories.forEachIndexed { index, category ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    if (index > 0) {
                        CircleDot(modifier = Modifier.align(Alignment.CenterVertically))
                    }
                    Text(
                        text = category.name,
                        fontSize = 14.sp,
                        style = Theme.typography.label.medium,
                        color = Theme.color.body,
                        modifier = Modifier.noRippleClickable { onCategoryClicked(category.id) },
                    )
                }
            }
        }
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            LabeledIconRow(
                title = rating.toString(),
                icon = painterResource(com.baghdad.design_system.R.drawable.ic_star_filled),
                tint = Theme.color.yellowAccent,
            )
            if (releaseDate.isNotBlank()) {
                CircleDot()
                LabeledIconRow(
                    title = releaseDate,
                    icon = painterResource(com.baghdad.design_system.R.drawable.ic_calendar),
                )
            }
            CircleDot()
            LabeledIconRow(
                title = stringResource(R.string.season_template, seasonNumber),
                icon = painterResource(R.drawable.ic_tv),
                tint = Theme.color.body,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EpisodeDetailsHeaderPreview() {
    NovixTheme {
        EpisodeDetailsHeader(
            title = "Test Episode",
            categories =
                listOf(
                    EpisodeDetailsScreenState.CategoryUiState(name = "Action"),
                    EpisodeDetailsScreenState.CategoryUiState(name = "Drama"),
                ),
            onCategoryClicked = {},
            rating = 7,
            releaseDate = "1950-05-13",
            seasonNumber = 1,
        )
    }
}
