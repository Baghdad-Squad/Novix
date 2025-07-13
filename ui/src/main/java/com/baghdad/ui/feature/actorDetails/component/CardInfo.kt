package com.baghdad.ui.feature.actorDetails.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.CircleDot
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.theme.Theme

@Composable
fun CardInfo(
    fullName: String,
    characterRole: String,
    birthPlace: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = fullName,
        style = Theme.typography.title.medium,
        color = Theme.color.title
    )

    Row(
        modifier = modifier
            .padding(top = 30.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        Text(
            text = characterRole,
            style = Theme.typography.label.small,
            color = Theme.color.body
        )

        CircleDot()

        IconTextInfo(
            text = birthPlace,
            painter = painterResource(com.baghdad.design_system.R.drawable.location_05),
            contentDescription = "birth Place"
        )

        CircleDot()

    }
}
