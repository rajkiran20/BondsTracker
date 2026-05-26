package app.le.bondstracker.ui.theme

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color

// Global theme state to toggle light/dark colors dynamically
private val isDarkThemeState = mutableStateOf(true)

var isDarkTheme: Boolean
    get() = isDarkThemeState.value
    set(value) {
        isDarkThemeState.value = value
    }

// Gemini Light Theme Colors (Clean, soft glassmorphism)
val SnowBackground = Color(0xFFF8F9FA)
val SnowSurface = Color(0xFFFFFFFF)
val SnowCard = Color(0xFFE8F0FE) // Light neural tint
val SnowElevated = Color(0xFFD2E3FC)

// Gemini Dark Theme Colors (Deep space charcoal, not harsh pure black)
val NavyDeep: Color
    get() = if (isDarkTheme) Color(0xFF131314) else Color(0xFFF8F9FA)

val NavySurface: Color
    get() = if (isDarkTheme) Color(0xFF1E1F20) else Color(0xFFFFFFFF)

val NavyCard: Color
    get() = if (isDarkTheme) Color(0xFF282A2D) else Color(0xFFE8F0FE)

val NavyElevated: Color
    get() = if (isDarkTheme) Color(0xFF333538) else Color(0xFFD2E3FC)

// Gemini Brand Accent Colors (Instead of traditional static Gold)
val GeminiBlue = Color(0xFF1A73E8)
val GeminiPurple = Color(0xFF7436BB)
val GeminiAmber = Color(0xFFFFC107)

val GeminiBlueDark = Color(0xFF8AB4F8)
val GeminiPurpleDark = Color(0xFFA062E8)
val GeminiAmberDark = Color(0xFFFFD54F)

// Status colors
val GreenSuccess = Color(0xFF4CAF82)
val GreenLight: Color
    get() = if (isDarkTheme) Color(0xFF1B4332) else Color(0xFFE8F5E9)

val AmberWarning = Color(0xFFFFB74D)
val AmberLight: Color
    get() = if (isDarkTheme) Color(0xFF3E2800) else Color(0xFFFFF8E1)

val RedError = Color(0xFFEF5350)
val RedLight: Color
    get() = if (isDarkTheme) Color(0xFF3E1111) else Color(0xFFFFEBEE)

// Text Colors
val TextPrimary: Color
    get() = if (isDarkTheme) Color(0xFFE3E3E3) else Color(0xFF1F1F1F)

val TextSecondary: Color
    get() = if (isDarkTheme) Color(0xFFC4C7C5) else Color(0xFF474747)

val TextMuted: Color
    get() = if (isDarkTheme) Color(0xFF8E918F) else Color(0xFF757575)

val TextPrimaryLight = Color(0xFF1F1F1F)
val TextSecondaryLight = Color(0xFF474747)
val TextMutedLight = Color(0xFF757575)

// Divider
val DividerColor: Color
    get() = if (isDarkTheme) Color(0xFF444746) else Color(0xFFE3E3E3)

val DividerColorLight = Color(0xFFE3E3E3)
