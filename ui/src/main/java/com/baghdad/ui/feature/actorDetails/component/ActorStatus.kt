package com.baghdad.ui.feature.actorDetails.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R

@Composable
fun ActorStatus(
    birthPlace: String,
    isDeceased: Boolean,
    deathDate: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(top = 8.dp)
    ) {
        IconTextInfo(
            text = birthPlace,
            painter = painterResource(com.baghdad.design_system.R.drawable.ic_birthday_cake),
            contentDescription = stringResource(R.string.birthday),
        )
        if (isDeceased) {
            Text(
                text = deathDate,
                style = Theme.typography.body.medium,
                color = Theme.color.body
            )
        }
    }
}