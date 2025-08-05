package com.baghdad.ui.feature.component.bottomSheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.R
import com.baghdad.design_system.component.BaseBottomSheet
import com.baghdad.design_system.component.button.OutlinedButton
import com.baghdad.ui.feature.search.component.BottomSheetHeader

@Composable
fun LoginRequiredSheet(
    onBottomSheetCloseClick: () -> Unit,
    title: String,
    description: String,
    isVisible: Boolean,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    BaseBottomSheet(
        isVisible = isVisible,
        onDismiss = { onBottomSheetCloseClick() },
    ) {

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .padding(start = 16.dp, end = 16.dp)
        ) {

            BottomSheetHeader(
                onCloseClick = { onBottomSheetCloseClick() },
                title = title,
            )

            EmptyMediaState(
                imagePath = R.drawable.user_person_profile,
                contentDescription = stringResource(com.baghdad.ui.R.string.bottom_sheet_content_description),
                message = description,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            OutlinedButton(
                label = stringResource(com.baghdad.ui.R.string.login),
                onClick = onLoginClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
