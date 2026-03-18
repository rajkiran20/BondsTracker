package app.le.bondstracker.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = GoldPrimary,
    onPrimary = NavyDeep,
    primaryContainer = GoldDark,
    onPrimaryContainer = TextPrimary,
    secondary = GoldLight,
    onSecondary = NavyDeep,
    background = NavyDeep,
    onBackground = TextPrimary,
    surface = NavySurface,
    onSurface = TextPrimary,
    surfaceVariant = NavyCard,
    onSurfaceVariant = TextSecondary,
    error = RedError,
    onError = TextPrimary,
    outline = DividerColor,
    outlineVariant = NavyElevated,
)

private val LightColorScheme = lightColorScheme(
    primary = GoldDark,
    onPrimary = SnowBackground,
    primaryContainer = GoldPrimary,
    onPrimaryContainer = TextPrimaryLight,
    secondary = GoldDark,
    onSecondary = SnowBackground,
    background = SnowBackground,
    onBackground = TextPrimaryLight,
    surface = SnowSurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = SnowCard,
    onSurfaceVariant = TextSecondaryLight,
    error = RedError,
    onError = SnowBackground,
    outline = DividerColorLight,
    outlineVariant = SnowElevated,
)

@Composable
fun BondsTrackerTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
