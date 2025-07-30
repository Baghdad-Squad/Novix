package com.baghdad.ui.feature.actorDetails.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.R
import com.baghdad.design_system.component.CircleDot
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.theme.Theme

@Composable
fun CardInfo(
    characterRole: String,
    birthPlace: String,
    birthDate: String?,
    deathDate: String?,
) {
    FlowRow(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        if (characterRole.isNotBlank()) {
            Text(
                text = characterRole,
                style = Theme.typography.label.small,
                color = Theme.color.body,
            )
        }

        if (birthPlace.isNotBlank()) {
            Row {
                CircleDot(modifier = Modifier.align(Alignment.CenterVertically))
                IconTextInfo(
                    text = birthPlace,
                    painter = painterResource(R.drawable.ic_birth_place),
                    contentDescription = "birth place",
                )
            }
        }

        if (!birthDate.isNullOrEmpty()) {
            Row {
                CircleDot(modifier = Modifier.align(Alignment.CenterVertically))
                IconTextInfo(
                    text = birthDate,
                    painter = painterResource(R.drawable.ic_birthday_cake),
                    contentDescription = stringResource(com.baghdad.ui.R.string.birthday),
                )
                if (!deathDate.isNullOrEmpty()) {
                    Text(
                        text = " - $deathDate",
                        style = Theme.typography.label.small,
                        color = Theme.color.body,
                    )
                }
            }
        }
    }
}
