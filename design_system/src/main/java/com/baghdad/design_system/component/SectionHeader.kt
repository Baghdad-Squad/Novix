package com.baghdad.design_system.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.R
import com.baghdad.design_system.theme.Theme

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    showAllLabel: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = Theme.typography.headline.small,
            color = Theme.color.title
        )

        if(showAllLabel != null) {
            Row {
                Text(
                    text = showAllLabel,
                    style = Theme.typography.label.medium,
                    color = Theme.color.primary
                )
            }
                Icon(
                    painter = painterResource(id = R.drawable.arrow_right),
                    contentDescription = stringResource(R.string.arrow_icon),
                    tint = Theme.color.primary,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(16.dp)
                )
            }
        }
    }

@Preview(showBackground = true)
@Composable
fun SectionHeaderPreview() {
    SectionHeader(
        title = "New arrival",
        showAllLabel = "All",
        onClick = { }
    )
}

