package com.baghdad.ui.feature.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.DropDownMenu
import com.baghdad.design_system.component.Icon
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.modifier.noRippleClickable
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R
import com.baghdad.ui.feature.component.islamicImage.IslamicImage
import com.baghdad.ui.feature.review.component.PlaceHolderReviewerImage

@Composable
fun ProfileHeaderWithOption(
    userName: String,
    imageUrl: String,
    onLogoutClick: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.padding(bottom = 40.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = Theme.color.iconBackgroundLow,
                    shape = RoundedCornerShape(12.dp),
                )
                .border(
                    width = 1.dp,
                    color = Theme.color.stroke,
                    shape = RoundedCornerShape(12.dp),
                ),
            contentAlignment = Alignment.Center
        ) {
            IslamicImage(
                imageUrl = imageUrl,
                errorContent = { PlaceHolderReviewerImage() },
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(48.dp)
            )
        }
        Text(
            text = userName,
            style = Theme.typography.title.medium,
            color = Theme.color.title,
            modifier =
                Modifier
                    .padding(start = 8.dp),
        )
        Spacer(Modifier.weight(1f))

        Box(
            modifier = Modifier.padding(1.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.more_vertical),
                contentDescription = "more_icon_list",
                tint = Theme.color.body,
                modifier = Modifier.noRippleClickable { expanded = !expanded }
            )

            DropDownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                tonalElevation = 0.dp,
                shadowElevation = 0.dp,
                containerColor = Theme.color.surface,
                modifier = Modifier
                    .width(172.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color = Theme.color.surface, shape = RoundedCornerShape(8.dp))
                    .border(
                        width = 1.dp,
                        color = Theme.color.stroke,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 12.dp, top = 12.dp, bottom = 12.dp)
                        .background(color = Theme.color.surface, shape = RoundedCornerShape(8.dp))
                        .noRippleClickable {
                            onLogoutClick()
                            expanded = false
                        },
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        painterResource(com.baghdad.design_system.R.drawable.ic_logout),
                        tint = Theme.color.redAccent,
                        contentDescription = stringResource(R.string.logout_icon),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        stringResource(R.string.logout),
                        style = Theme.typography.label.medium,
                        color = Theme.color.redAccent
                    )
                }
            }
        }
    }
}