package com.baghdad.design_system.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.R
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.design_system.theme.Theme

@Composable
fun NovixAppBar(
    modifier: Modifier = Modifier,
    showGoBack: Boolean = false,
    onGoBackClick: (() -> Unit)? = null,
    showScreenTitle: Boolean = false,
    screenTitle: String? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val layoutDirection = LocalLayoutDirection.current
    val rotation = if (layoutDirection == LayoutDirection.Rtl) 180f else 0f

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        if (showGoBack) {
            IconButton(
                icon = R.drawable.ic_arrow_left,
                tintIcon = Theme.color.title,
                onClick = onGoBackClick,
                modifier = Modifier.padding(end = 12.dp).rotate(rotation)
            )
        }

        if (showScreenTitle) {
            screenTitle?.let {
                Text(
                    text = screenTitle,
                    style = Theme.typography.title.large,
                    color = Theme.color.title,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        content()
    }
}


@Preview(
    name = "AppBar Preview", showBackground = true,
    backgroundColor = 0xFFFFFFFF, widthDp = 360, heightDp = 56
)
@Composable
private fun NovixAppBarPreview() {
    NovixTheme {
        NovixAppBar(
            modifier = Modifier,
            showGoBack = true,
            onGoBackClick = { /*TODO*/ },
            showScreenTitle = true,
            screenTitle = "My account",
            content = {
                IconButton(
                    icon = R.drawable.ic_pencil_edit,
                    tintIcon = Theme.color.title,
                    onClick = { }
                )
                Spacer(Modifier.width(8.dp))
                IconButton(
                    icon = R.drawable.ic_delete,
                    onClick = { },
                    tintIcon = Theme.color.redAccent,
                )
            }
        )
    }
}
