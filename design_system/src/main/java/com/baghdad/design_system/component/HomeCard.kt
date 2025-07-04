package com.baghdad.design_system.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.theme.Theme
import com.baghdad.islamic_image_loader.component.SafeImage

@Composable
fun HomeCard(
    url: String,
    contentDescription: String?,
    isSaved: Boolean,
    onSavedClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .size(158.dp, 210.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Theme.color.surface)
            .border(1.dp, Theme.color.stroke)
            ,
        contentAlignment = Alignment.Center
    ) {
        SafeImage(
            imageUrl = url,
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize()
        )

        SaveIcon(
            isSaved = isSaved,
            onClick = { onSavedClick() },
            modifier = Modifier.padding(14.dp).align(Alignment.TopStart)
        )

    }
}

@Preview
@Composable
private fun HomeCardPreview() {
    HomeCard(
        url = "https://images.pexels.com/photos/417074/pexels-photo-417074.jpeg?cs=srgb&dl=pexels-souvenirpixels-417074.jpg&fm=jpg",
        contentDescription = null,
        isSaved = false,
        onSavedClick = {}
    )
}