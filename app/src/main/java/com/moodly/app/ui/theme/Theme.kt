// app/src/main/java/com/moodly/app/ui/theme/Theme.kt
package com.moodly.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val MoodlyColorScheme = lightColorScheme(
    primary          = Accent,
    onPrimary        = Surface,
    primaryContainer = AccentLight,
    background       = Background,
    surface          = Surface,
    surfaceVariant   = Surface2,
    onBackground     = TextPrimary,
    onSurface        = TextPrimary,
    onSurfaceVariant = TextSecondary,
    outline          = TagBg
)

@Composable
fun MoodlyTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MoodlyColorScheme,
        typography  = MoodlyTypography,
        content     = content
    )
}