package com.baghdad.ui.feature.myLists.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.component.button.OutlinedButton
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R

@Composable
fun EmptyStateContent(
    isUserLoggedIn: Boolean,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (isUserLoggedIn) {
        EmptyListContent(modifier)
    } else {
        NoLoginPrompt(
            onLoginClick = onLoginClick,
            modifier = modifier,
        )
    }
}

@Composable
private fun NoLoginPrompt(
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Image(
                painter = painterResource(R.drawable.img_no_login_lists_placeholder),
                contentDescription = stringResource(R.string.my_lists_no_login_caption),
                modifier = Modifier.size(128.dp),
            )
            Text(
                text = stringResource(R.string.my_lists_no_login_caption),
                style = Theme.typography.body.small,
                color = Theme.color.body,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 48.dp),
            )
            OutlinedButton(
                label = stringResource(R.string.login),
                onClick = onLoginClick,
                modifier = Modifier.padding(top = 12.dp),
            )
        }
    }
}

@Composable
private fun EmptyListContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Image(
                painter = painterResource(R.drawable.img_empty_lists_placeholder),
                contentDescription = stringResource(R.string.my_lists_empty_list_caption),
                modifier = Modifier.size(128.dp),
            )
            Text(
                text = stringResource(R.string.my_lists_empty_list_caption),
                style = Theme.typography.body.small,
                color = Theme.color.body,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 48.dp),
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun NoLoginPromptPreview() {
    NovixTheme {
        NoLoginPrompt({})
    }
}

@Preview
@Composable
private fun EmptyListContent() {
    NovixTheme {
        EmptyListContent()
    }
}
