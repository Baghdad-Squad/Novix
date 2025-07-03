package com.baghdad.design_system.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.theme.Theme


@Composable
fun NovixSelection(
    selectedOption: String,
    onOptionSelected: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .background(Theme.color.primaryVariant
            ,RoundedCornerShape(8.dp))
            .border(
                width = 2.dp,
                color = Theme.color.primary,
                shape = RoundedCornerShape(8.dp)
            ).wrapContentHeight()
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null,
            ){
                onOptionSelected()
            }
        ,
        contentAlignment = Alignment.CenterStart
    ){
        Text(
            text = selectedOption,
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 16.dp),
            color = Theme.typography.label.large.color,
        )

    }
}