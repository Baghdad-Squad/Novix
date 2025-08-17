package com.baghdad.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.baghdad.design_system.component.SnackBarPosition
import com.baghdad.design_system.component.scaffold.ScaffoldSnackBarState
import com.baghdad.viewmodel.base.SnackBarState
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage

@Composable
fun SnackBarState.toScaffoldSnackBarState(messageMapper: (BaseSnackBarMessage) -> Int): ScaffoldSnackBarState =
    ScaffoldSnackBarState(
        isVisible = isVisible,
        message = stringResource(messageMapper(message)),
        isSuccess = isSuccess,
        actionLabel = actionLabelRes?.let { stringResource(it) },
        position = if (actionLabelRes == null) SnackBarPosition.TOP else SnackBarPosition.BOTTOM,
    )
