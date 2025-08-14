package com.baghdad.ui.feature.component.bottomSheet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.R
import com.baghdad.design_system.component.BaseBottomSheet
import com.baghdad.design_system.component.Icon
import com.baghdad.design_system.component.NovixTextField
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.component.button.PrimaryButton
import com.baghdad.design_system.modifier.noRippleClickable
import com.baghdad.design_system.theme.Theme

@Composable
fun AddListBottomSheet(
    isVisible: Boolean,
    isLoading: Boolean,
    listName: String,
    onDismiss: () -> Unit,
    onAddClick: () -> Unit,
    onListNameChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    BaseBottomSheet(
        isVisible = isVisible,
        onDismiss = onDismiss,
    ) {
        Column(
            modifier =
                modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 24.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            AddListBottomSheetHeader(
                onCloseClick = onDismiss,
            )
            AddListBottomSheetContent(
                listName = listName,
                onListNameChange = onListNameChange,
            )
            AddListBottomSheetFooter(
                isLoading = isLoading,
                isAddEnabled = listName.isNotBlank(),
                onAddClick = onAddClick,
            )
        }
    }
}

@Composable
private fun AddListBottomSheetHeader(
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.add_new_list),
            style = Theme.typography.title.large,
            color = Theme.color.title,
        )
        Box(
            modifier =
                Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(color = Theme.color.iconBackgroundLow)
                    .size(32.dp)
                    .border(
                        width = 1.dp,
                        color = Theme.color.stroke,
                        shape = RoundedCornerShape(8.dp),
                    )
                    .noRippleClickable { onCloseClick() },
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_close),
                contentDescription = stringResource(R.string.close_bottomsheet),
                tint = Theme.color.title,
                modifier = Modifier.size(16.dp),
            )
        }
    }
}

@Composable
private fun AddListBottomSheetContent(
    listName: String,
    onListNameChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(com.baghdad.ui.R.string.list_title),
            style = Theme.typography.title.small,
            color = Theme.color.title,
        )
        NovixTextField(
            value = listName,
            maxLength = 82,
            onValueChange = onListNameChange,
            singleLine = true,
            leadingIcon = painterResource(R.drawable.ic_all_bookmark),
        )
    }
}

@Composable
private fun AddListBottomSheetFooter(
    isLoading: Boolean,
    isAddEnabled: Boolean,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PrimaryButton(
        label = stringResource(R.string.add),
        onClick = onAddClick,
        isLoading = isLoading,
        isEnabled = isAddEnabled,
        modifier = modifier.fillMaxWidth(),
    )
}
