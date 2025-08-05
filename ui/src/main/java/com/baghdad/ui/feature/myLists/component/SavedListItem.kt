package com.baghdad.ui.feature.myLists.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.Icon
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.modifier.noRippleClickable
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.design_system.theme.Theme

@Composable
fun SavedListItem(
    name: String,
    itemCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .background(
                    color = Theme.color.surface,
                    shape = RoundedCornerShape(12.dp),
                )
                .border(
                    width = 1.dp,
                    color = Theme.color.stroke,
                    shape = RoundedCornerShape(12.dp),
                )
                .padding(
                    horizontal = 12.dp,
                    vertical = 16.dp,
                )
                .noRippleClickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = name,
            style = Theme.typography.title.medium,
            color = Theme.color.title,
            modifier = Modifier.weight(1f),
        )
        ListCountChip(itemCount = itemCount)
    }
}

@Composable
private fun ListCountChip(
    itemCount: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .background(
                    color = Theme.color.primaryVariant,
                    shape = RoundedCornerShape(8.dp),
                )
                .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = itemCount.toString(),
            style = Theme.typography.label.small,
            color = Theme.color.primary,
        )

        Icon(
            painter = painterResource(com.baghdad.design_system.R.drawable.ic_arrow_right),
            contentDescription = stringResource(com.baghdad.design_system.R.string.arrow_icon),
            tint = Theme.color.primary,
            modifier = Modifier.size(16.dp),
        )
    }
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
private fun SavedListItemPreview() {
    NovixTheme(isDarkTheme = true) {
        SavedListItem(name = "My Favorite ", itemCount = 12, onClick = {})
    }
}
