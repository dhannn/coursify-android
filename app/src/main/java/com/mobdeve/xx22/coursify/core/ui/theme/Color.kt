package com.mobdeve.xx22.coursify.core.ui.theme

import androidx.compose.ui.graphics.Color

// Base colors
val Primary = Color(0xFF1C1C1C)
val Background = Color(0xFFF5F5F5)
val Secondary = Color(0xFFEFEFEF)
val AccentOne = Color(0xFF9BC8CD)
val AccentTwo = Color(0xFFEDC4C8)

val OnPrimary = Color.White        // For content (like text) on primary color
val OnBackground = Primary         // For content on background color
val OnSecondary = Primary         // For content on secondary color
val OnAccentOne = Primary         // For content on accent one
val OnAccentTwo = Primary         // For content on accent two

// Surface colors
val Surface = Background
val OnSurface = Primary

// Additional UI states using alpha variations
val PrimaryDisabled = Primary.copy(alpha = 0.38f)
val AccentOnePressed = AccentOne.copy(alpha = 0.9f)
val AccentTwoPressed = AccentTwo.copy(alpha = 0.9f)

// Overlay colors for various states
val Overlay = Primary.copy(alpha = 0.5f)  // For modals, dialogs
val Scrim = Primary.copy(alpha = 0.32f)   // For background dimming

// Status colors (using your palette as base)
val Success = AccentOne
val Error = AccentTwo.copy(red = 0.95f)   // Slightly adjusted for error states
val OnError = Color.White

