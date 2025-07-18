package com.baghdad.design_system.component.appBar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.R
import com.baghdad.design_system.component.button.IconButton
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.design_system.theme.Theme

@Composable
fun TopAppBar(
    modifier: Modifier = Modifier,
    onGoBackClick: (() -> Unit)? = null,
    screenTitle: String? = null,
    content: @Composable RowScope.() -> Unit,
) {

    BasicTopAppBar(
        modifier = modifier
    ) {
        onGoBackClick?.let {
            IconButton(
                icon = painterResource(R.drawable.ic_arrow_left),
                tintIcon = Theme.color.title,
                onClick = onGoBackClick,
                modifier = Modifier
                    .padding(end = 12.dp)
            )
        }

        screenTitle?.let {
            Text(
                text = screenTitle,
                style = Theme.typography.title.large,
                color = Theme.color.title,
                modifier = Modifier
                    .weight(1f)
            )
        }
        content()
    }
}


@Preview(
    name = "AppBar Preview", showBackground = true,
    backgroundColor = 0xFFFFFFFF,
)
@Composable
private fun NovixAppBarPreview() {
    NovixTheme {
        TopAppBar(
            modifier = Modifier,
            onGoBackClick = { /*TODO*/ },
            screenTitle = "My account",
            content = {
                IconButton(
                    icon = painterResource(R.drawable.ic_pencil_edit),
                    tintIcon = Theme.color.title,
                    onClick = { }
                )
                IconButton(
                    icon = painterResource(R.drawable.ic_delete),
                    onClick = { },
                    tintIcon = Theme.color.redAccent,
                )
            }
        )
    }
}
