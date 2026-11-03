package com.offlinepay.app.ui.theme

import androidx.compose.ui.graphics.Color

// True Black AMOLED Fintech Color System
val DarkBackground = Color(0xFF000000) // True Black
val DarkSurface = Color(0xFF121212) // Deep Dark Gray Surface
val DarkSecondarySurface = Color(0xFF1E1E22) // Elevated Surface
val DarkPrimary = Color(0xFF34A853) // Google Green
val DarkPrimaryText = Color(0xFFF8FAFC)
val DarkSecondaryText = Color(0xFF94A3B8)
val DarkBorder = Color(0xFF26262C)

val LightBackground = Color(0xFFF8F9FA)
val LightSurface = Color(0xFFFFFFFF)
val LightSecondarySurface = Color(0xFFF1F3F4)
val LightPrimary = Color(0xFF0B8043) // Deep Google Green
val LightPrimaryText = Color(0xFF202124)
val LightSecondaryText = Color(0xFF5F6368)
val LightBorder = Color(0xFFDADCE0)

// Universal Semantic Colors
val SuccessGreen = Color(0xFF34A853)
val WarningOrange = Color(0xFFFBBC04)
val ErrorRed = Color(0xFFEA4335)
val AccentTeal = Color(0xFF00C896)

// Backward Compatibility Aliases
val NavyDarkest = DarkBackground
val NavyDark = DarkSurface
val NavySurface = DarkSecondarySurface
val NavyCard = DarkSurface
val NavyElevated = DarkSecondarySurface

val EmeraldPrimary = DarkPrimary
val EmeraldLight = Color(0xFF81C995)
val EmeraldDark = Color(0xFF0B8043)
val EmeraldAccent = AccentTeal

val TextWhite = DarkPrimaryText
val TextGray = DarkSecondaryText
val TextMuted = Color(0xFF70757A)

val StatusOnline = SuccessGreen
val StatusOffline = WarningOrange
