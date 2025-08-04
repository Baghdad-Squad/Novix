package com.baghdad.ui.feature.profile.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.BaseBottomSheet
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.component.button.NegativeButton
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R
import com.baghdad.ui.feature.search.component.BottomSheetHeader

@Composable
fun LogOutBottomSheet(
    isVisible: Boolean,
    onBottomSheetCloseClick: () -> Unit,
    onLogOutClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BaseBottomSheet(
        isVisible = isVisible,
        onDismiss = onBottomSheetCloseClick,
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .padding(start = 16.dp, end = 16.dp)
        ) {
            BottomSheetHeader(
                onCloseClick = { onBottomSheetCloseClick() },
                title = stringResource(R.string.log_out)
            )
            Text(
                text = stringResource(R.string.log_out_description),
                style = Theme.typography.title.large,
                color = Theme.color.title,
            )
            NegativeButton(
                label = stringResource(R.string.log_out),
                onClick = onLogOutClick,
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
            )
        }
    }

}
@Preview
@Composable
fun LogOutBottomSheetPrv(){
    NovixTheme {
        LogOutBottomSheet(
            isVisible =true,
            onBottomSheetCloseClick = {},
            onLogOutClick = {},
        )
    }
}
