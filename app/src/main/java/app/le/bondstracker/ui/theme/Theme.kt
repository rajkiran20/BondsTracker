package app.le.bondstracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = GeminiBlueDark,            // Fixed: Replaced GeminiBlue
    onPrimary = NavyDeep,
    primaryContainer = GeminiPurpleDark,
    onPrimaryContainer = TextPrimary,
    secondary = GeminiAmberDark,         // Fixed: Replaced GoldLight
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
    primary = GeminiBlue,                // Fixed: Replaced GeminiBlueDark
    onPrimary = SnowBackground,
    primaryContainer = GeminiPurple,
    onPrimaryContainer = TextPrimaryLight,
    secondary = GeminiBlue,              // Fixed: Replaced GeminiBlueDark
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
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    isDarkTheme = darkTheme
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}