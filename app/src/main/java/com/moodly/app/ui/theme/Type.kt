// app/src/main/java/com/moodly/app/ui/theme/Type.kt
package com.moodly.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val MoodlyTypography = Typography(
    headlineLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize   = 28.sp,
        lineHeight = 34.sp,
        color      = TextPrimary
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize   = 22.sp,
        lineHeight = 28.sp,
        color      = TextPrimary
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize   = 16.sp,
        color      = TextPrimary
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize   = 14.sp,
        color      = TextPrimary
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize   = 13.sp,
        color      = TextSecondary
    ),
    labelSmall = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize   = 10.sp,
        color      = TextHint
    )
)