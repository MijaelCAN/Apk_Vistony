package com.vistony.salesforce.kotlin.view.Atoms.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColorScheme(
    primary = RedVistony,
    secondary = WhiteVistony,
    onPrimary = RedVistony,
)

private val LightColorPalette = lightColorScheme(
    primary = Color.White,
    secondary = BlueVistony,
    tertiary = BlueVistony,
    background = Color.White,
    surface = Color.White,
    surfaceVariant = Color(0xFFF2F2F2), // Gris claro
    onSurface = Color.Black,
    onPrimary = RedVistony,
    primaryContainer = RedVistony,
    onPrimaryContainer = RedVistony,
    errorContainer = RedVistony,
    onErrorContainer = RedVistony
)

@Composable
fun VistonyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}