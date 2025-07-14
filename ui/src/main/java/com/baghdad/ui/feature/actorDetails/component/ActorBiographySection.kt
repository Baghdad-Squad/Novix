package com.baghdad.ui.feature.actorDetails.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.ExpandableText
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R

@Composable
fun ActorBiographySection(
    biography: String,
    isExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier
) {
    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        Text(
            text = stringResource(R.string.biography),
            style = Theme.typography.title.medium,
            color = Theme.color.title,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        ExpandableText(
            modifier = modifier,
            text = biography,
            isExpanded = isExpanded,
            onExpandedChange = onExpandedChange
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun ActorBiographySectionPreview() {
    val (expanded, onExpandedChange) = rememberSaveable { mutableStateOf(false) }
    ActorBiographySection(
        biography = "It is a 1994 American drama film, considered one of the greatest films in cinematic history. It revolves around Andy Dufresne, a banker wrongfully convicted of the murder of his wife and her lover, and imprisoned in Shawshank Red State Prison. There, he forms a friendship with another inmate named Red and embarks on a long journey of pain, hope, and planning for freedom.",
        isExpanded = expanded,
        onExpandedChange = onExpandedChange,
        modifier = Modifier
    )
}
