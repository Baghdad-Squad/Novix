package com.baghdad.ui.feature.tvShowDetails.component

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baghdad.design_system.R
import com.baghdad.design_system.component.CircleDot
import com.baghdad.design_system.component.LabeledIconRow
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.modifier.noRippleClickable
import com.baghdad.design_system.theme.Theme
import com.baghdad.viewmodel.tvShowDetails.TvShowDetailsScreenState

@Composable
fun TvShowDetailsCard(
    tvShowId: Long,
    title: String,
    genres: List<TvShowDetailsScreenState.GenreUiState>,
    rating: Double,
    date: String,
    seasonsCount: Int,
    onReviewClick: (tvShowId: Long) -> Unit,
    onGenreClick: (genreId: Long?) -> Unit,
    modifier: Modifier = Modifier
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
            style = Theme.typography.title.medium,
            color = Theme.color.title,
            modifier = Modifier.padding(bottom = 44.dp)
        )
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            genres.forEachIndexed { index, genre ->
                Text(
                    text = genre.name,
                    style = Theme.typography.label.small,
                    color = Theme.color.body,
                    modifier = Modifier
                        .noRippleClickable { onGenreClick(genre.id) }
                )
                if (index < genres.size - 1) {
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
                title = date,
                icon = painterResource(R.drawable.ic_calender),
            )
            CircleDot()
            LabeledIconRow(
                title = seasonsCount.toString(),
                icon = painterResource(R.drawable.ic_tv),
            )
        }
        Text(
            text = stringResource(com.baghdad.ui.R.string.view_reviews),
            fontSize = 12.sp,
            style = Theme.typography.label.medium,
            color = Theme.color.primary,
            modifier = Modifier.noRippleClickable {
                onReviewClick(tvShowId)
            }
        )
    }
}