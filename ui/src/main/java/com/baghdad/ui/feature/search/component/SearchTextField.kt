package com.baghdad.ui.feature.search.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.Icon
import com.baghdad.design_system.component.NovixTextField
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.modifier.noRippleClickable
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R
import com.baghdad.viewmodel.search.SearchTab

@Composable
fun SearchTextField(
    query: String,
    onQueryChange: (String) -> Unit,
    onFilterIconClick: () -> Unit,
    searchTab: SearchTab,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.search),
            style = Theme.typography.title.large,
            color = Theme.color.title,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            NovixTextField(
                value = query,
                onValueChange = { onQueryChange(it) },
                leadingIcon = painterResource(com.baghdad.design_system.R.drawable.search_icon),
                hint = stringResource(R.string.search),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )

            AnimatedVisibility(searchTab == SearchTab.MOVIES || searchTab == SearchTab.TV_SHOWS || query.isBlank()) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .aspectRatio(1f)
                        .background(Theme.color.primary, shape = RoundedCornerShape(12.dp))
                        .noRippleClickable { onFilterIconClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(com.baghdad.design_system.R.drawable.filter_horizontal),
                        contentDescription = stringResource(R.string.filter_icon),
                        modifier = Modifier
                            .fillMaxSize(0.5f)
                            .size(20.dp)

                    )
                }
            }

        }
    }
}
