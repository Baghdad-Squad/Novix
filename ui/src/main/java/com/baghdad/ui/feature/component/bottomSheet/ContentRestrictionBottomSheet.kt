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
import com.baghdad.design_system.component.BaseBottomSheet
import com.baghdad.design_system.component.Selection
import com.baghdad.design_system.component.button.PrimaryButton
import com.baghdad.design_system.shared.Selectable
import com.baghdad.ui.R
import com.baghdad.ui.feature.search.component.BottomSheetHeader
import kotlin.collections.forEach

@Composable
fun ContentRestrictionBottomSheet(
    onBottomSheetCloseClick: () -> Unit,
    isVisible: Boolean,
    contentRestrictionOptions: List<Selectable<String>>,
    onContentRestrictionSelected: (String) -> Unit,
    onSaveClick: () -> Unit,
    description: String,
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
                .padding(horizontal = 16.dp)
        ) {
            BottomSheetHeader(
                onCloseClick = { onBottomSheetCloseClick() },
                title = stringResource(R.string.content_restriction),
            )

            contentRestrictionOptions.forEach { contentRestriction ->
                Selection(
                    option = contentRestriction,
                    onClick = { onContentRestrictionSelected(contentRestriction.value) },
                    description = description,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            PrimaryButton(
                label = stringResource(R.string.save),
                onClick = onSaveClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp, top = 12.dp)
            )
        }
    }
}