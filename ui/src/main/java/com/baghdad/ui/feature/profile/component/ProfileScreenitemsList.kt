package com.baghdad.ui.feature.profile.component

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.ui.R

@Composable
fun ProfileScreenItemsList(
    appearance: String,
    language: String,
    onclickWatchingHistory: () -> Unit,
    onclickMyRating: () -> Unit,
    onclickContentRestriction: () -> Unit,
    onclickChangePassword: () -> Unit,
    onclickAppearance: () -> Unit,
    onclickLanguage: () -> Unit,
) {
    ProfileScreenItem(
        title = stringResource(R.string.watching_history),
        icon = painterResource(com.baghdad.design_system.R.drawable.ic_time_schedule),
        onClick = { onclickWatchingHistory() }
    )
    ProfileScreenDivider()
    ProfileScreenItem(
        title = stringResource(R.string.my_rating),
        icon = painterResource(com.baghdad.design_system.R.drawable.ic_star_square),
        onClick = { onclickMyRating() }
    )
    ProfileScreenDivider()
    ProfileScreenItem(
        title = stringResource(R.string.content_restriction),
        icon = painterResource(R.drawable.shield_energy),
        onClick = { onclickContentRestriction() }
    )
    ProfileScreenDivider()
    ProfileScreenItem(
        title = stringResource(R.string.change_password),
        icon = painterResource(com.baghdad.design_system.R.drawable.ic_lock_key),
        onClick = { onclickChangePassword() }
    )
    ProfileScreenDivider()
    ProfileItemWithDefaults(
        title = stringResource(R.string.appearance),
        icon = painterResource(com.baghdad.design_system.R.drawable.ic_moon),
        defaultValue = appearance,
        onClick = { onclickAppearance() }
    )
    ProfileScreenDivider()
    ProfileItemWithDefaults(
        title = stringResource(R.string.language),
        icon = painterResource(com.baghdad.design_system.R.drawable.ic_language_circle),
        defaultValue = language,
        onClick = { onclickLanguage() },
        modifier = Modifier.padding(bottom = 16.dp)
    )
}