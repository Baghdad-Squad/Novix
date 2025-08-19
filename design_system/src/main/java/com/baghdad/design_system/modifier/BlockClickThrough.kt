package com.baghdad.design_system.modifier

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun Modifier.blockClickThrough(): Modifier = this.then(
    Modifier.clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
    ) {}
)