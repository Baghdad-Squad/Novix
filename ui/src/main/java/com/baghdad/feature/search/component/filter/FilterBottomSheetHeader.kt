package com.baghdad.feature.search.component.filter

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.R
import com.baghdad.design_system.component.Icon
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.theme.Theme

@Composable
fun FilterBottomSheetHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.filter),
            style = Theme.typography.title.large,
            color = Theme.color.title
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(color = Theme.color.iconBackgroundLow)
                .size(32.dp)
                .border(
                    width = 1.dp,
                    color = Theme.color.stroke,
                    shape = RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_close),
                contentDescription = stringResource(R.string.close_filter_bottomsheet),
                tint = Theme.color.title,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}