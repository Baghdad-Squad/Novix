package com.baghdad.ui.feature.actorDetails.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.preview.NovixPreviews
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.design_system.theme.Theme

@Composable
fun ActorCardDetails(
    modifier: Modifier = Modifier,
    fullName: String,
    characterRole: String,
    birthPlace: String,
    birthDate: String,
    deathDate: String,
    isDeceased: Boolean = false
) {
    Box(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .aspectRatio(2.80f)
            .border(
                width = 1.dp,
                color = Theme.color.stroke,
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                color = Theme.color.surface,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            CardInfo(
                fullName = fullName,
                characterRole = characterRole,
                birthPlace = birthPlace
            )
            ActorStatus(
                birthPlace = birthDate,
                isDeceased = isDeceased,
                deathDate = deathDate
            )
        }
    }
}
@Preview(showBackground = true)
@NovixPreviews
@Composable
private fun CardPreview() {
    NovixTheme {
        ActorCardDetails(
            fullName = "Tom Hanks",
            characterRole = "Acting",
            birthPlace = "Santa Cruz del Norte, Cuba",
            birthDate = "1988-04-30",
            deathDate = " - 2012-30-03",
            isDeceased = false
        )
    }

}
