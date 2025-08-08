package com.baghdad.ui.feature.component.bottomSheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.BaseBottomSheet
import com.baghdad.design_system.component.Star
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.component.button.PrimaryButton
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R
import com.baghdad.ui.feature.search.component.BottomSheetHeader

@Composable
fun RatingBottomSheet(
    onBottomSheetCloseClick: () -> Unit,
    isVisible: Boolean,
    rate: Int,
    onRateChanged: (Int) -> Unit,
    onSubmitClick: () -> Unit,
    isButtonEnabled: Boolean,
    modifier: Modifier = Modifier
) {

    BaseBottomSheet(
        isVisible = isVisible,
        onDismiss = { onBottomSheetCloseClick() },
    ) {

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .padding(start = 16.dp, end = 16.dp)
        ) {

            BottomSheetHeader(
                onCloseClick = { onBottomSheetCloseClick() },
                title = stringResource(R.string.rate_it),
            )

            Text(
                text = stringResource(R.string.select_how_much_you_like_it),
                style = Theme.typography.body.medium,
                color = Theme.color.body,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(bottom = 12.dp)
            )

            StarsRate(
                rate = rate,
                onRatingChanged = onRateChanged,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 24.dp),
            )

            PrimaryButton(
                label = stringResource(R.string.submit),
                onClick = onSubmitClick,
                isEnabled = isButtonEnabled,
                modifier = Modifier.fillMaxWidth()
                    .padding(bottom = 24.dp)
            )
        }
    }
}

@Composable
private fun StarsRate(
    rate: Int,
    onRatingChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        repeat(10) {
            Star(
                isFilled = it < rate,
                onClick = { onRatingChanged(it + 1) },
                starSize = 24.dp
            )
        }
    }
}


@Preview
@Composable
private fun RatingBottomSheetPrev() {
    NovixTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Theme.color.surface),
            verticalArrangement = Arrangement.Center
        ) {
            RatingBottomSheet(
                onBottomSheetCloseClick = {},
                isVisible = true,
                rate = 5,
                onRateChanged = {},
                onSubmitClick = {},
                isButtonEnabled = true
            )
        }
    }
}