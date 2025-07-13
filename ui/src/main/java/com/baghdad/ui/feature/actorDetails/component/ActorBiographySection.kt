package com.baghdad.ui.feature.actorDetails.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R

@Composable
fun ActorBiographySection(
    biography: String,
    modifier: Modifier
) {
    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = stringResource(R.string.biography),
            style = Theme.typography.title.medium,
            color = Theme.color.title
        )
        ExpandableText(biography)
    }
}


@Preview(showBackground = true)
@Composable
private fun ActorBiographySectionPreview() {
    ActorBiographySection(
        biography = "Matthew Paige Damon is an American actor, film producer, and screenwriter. " +
                "He was ranked among Forbes' most bankable stars in 2007 and, in 2010, was one of the highest-grossing " +
                "actors of all time, known for roles in Good Will Hunting, The Bourne series, and The Martian. " +
                "His career spans decades with numerous awards and nominations for his versatile performances.",
        modifier = Modifier
    )
}
