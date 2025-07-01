package com.baghdad.design_system.textStyle.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.baghdad.design_system.R
import com.baghdad.design_system.textStyle.model.NovixTextStyle
import com.baghdad.design_system.textStyle.model.NovixTextStyle.SizedTextStyle

val ibmPlexSans = FontFamily(
    Font(R.font.ibm_plex_sans_medium, FontWeight.Medium),
    Font(R.font.ibm_plex_sans_regular, FontWeight.Normal),
    Font(R.font.ibm_plex_sans_semibold, FontWeight.SemiBold)
)

val novixTextStyle = NovixTextStyle(
    headline = SizedTextStyle(
        large = TextStyle(
            fontFamily = ibmPlexSans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 28.sp,
            lineHeight = 42.sp,
        ),
        medium = TextStyle(
            fontFamily = ibmPlexSans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            lineHeight = 36.sp,
        ),
        small = TextStyle(
            fontFamily = ibmPlexSans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            lineHeight = 30.sp,
        )
    ),
    title = SizedTextStyle(
        large = TextStyle(
            fontFamily = ibmPlexSans,
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp,
            lineHeight = 30.sp,
        ),
        medium = TextStyle(
            fontFamily = ibmPlexSans,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
            lineHeight = 28.sp,
        ),
        small = TextStyle(
            fontFamily = ibmPlexSans,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            lineHeight = 24.sp,
        )
    ),
    body = SizedTextStyle(
        large = TextStyle(
            fontFamily = ibmPlexSans,
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp,
            lineHeight = 28.sp,
        ),
        medium = TextStyle(
            fontFamily = ibmPlexSans,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
        ),
        small = TextStyle(
            fontFamily = ibmPlexSans,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 22.sp,
        )
    ),
    label = SizedTextStyle(
        large = TextStyle(
            fontFamily = ibmPlexSans,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            lineHeight = 24.sp,
        ),
        medium = TextStyle(
            fontFamily = ibmPlexSans,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 22.sp,
        ),
        small = TextStyle(
            fontFamily = ibmPlexSans,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            lineHeight = 18.sp,
        )
    )
)
