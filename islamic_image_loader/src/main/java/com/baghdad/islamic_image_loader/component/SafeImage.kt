package com.baghdad.islamic_image_loader.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.baghdad.islamic_image_loader.R

@Composable
fun SafeImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    placeHolder: @Composable (modifier: Modifier) -> Unit = { ImagePlaceholder(it) },
    blur: Dp = 16.dp,
    contentScale: ContentScale = ContentScale.Crop
) {

}

@Composable
private fun ImagePlaceholder(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.img_defualt_image),
        contentDescription = "Default Image",
        modifier = modifier.size(56.dp)
    )
}

