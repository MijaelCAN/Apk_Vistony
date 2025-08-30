package com.vistony.app.ui.theme.theme

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp

object Dimensions {

    // Padding
    fun getPadding(windowSize: WindowWidthSizeClass): Dp {
        return when (windowSize) {
            WindowWidthSizeClass.Compact -> 16.dp
            WindowWidthSizeClass.Medium -> 32.dp
            WindowWidthSizeClass.Expanded -> 64.dp
            else -> 16.dp
        }
    }

    // Button Dimensions
    fun getButtonHeight(windowSize: WindowWidthSizeClass): Dp {
        return when (windowSize) {
            WindowWidthSizeClass.Compact -> 56.dp
            WindowWidthSizeClass.Medium -> 70.dp
            WindowWidthSizeClass.Expanded -> 70.dp
            else -> 56.dp
        }
    }

    fun getButtonWidth(windowSize: WindowWidthSizeClass): Dp {
        return when (windowSize) {
            WindowWidthSizeClass.Compact -> 100.dp // Ancho mínimo para móviles
            WindowWidthSizeClass.Medium -> 80.dp // Ancho para tabletas pequeñas
            WindowWidthSizeClass.Expanded -> 200.dp // Ancho para tabletas grandes
            else -> 100.dp // Ancho por defecto
        }
    }

    // TextField Dimensions
    fun getTextFieldHeight(windowSize: WindowWidthSizeClass): Dp {
        return when (windowSize) {
            WindowWidthSizeClass.Compact -> 56.dp
            WindowWidthSizeClass.Medium -> 70.dp
            WindowWidthSizeClass.Expanded -> 90.dp
            else -> 56.dp

        }
    }

    // Font Sizes
    fun getTitleFontSize(windowType: WindowWidthSizeClass): Float {
        return when (windowType) {
            WindowWidthSizeClass.Compact -> 20f// Móvil
            WindowWidthSizeClass.Medium -> 28f // Tableta pequeña
            WindowWidthSizeClass.Expanded -> 36f // Tableta grande
            else -> 20f // Ancho por defecto
        }
    }

    fun getBodyFontSize(windowType: WindowWidthSizeClass): Float {
        return when (windowType) {
            WindowWidthSizeClass.Compact -> 16f // Móvil
            WindowWidthSizeClass.Medium -> 18f // Tableta pequeña
            WindowWidthSizeClass.Expanded -> 20f // Tableta grande
            else -> 16f // Ancho por defecto

        }
    }

    // Image Sizes
    fun getImageSize(windowSize: WindowWidthSizeClass): Dp {
        return when (windowSize) {
            WindowWidthSizeClass.Compact -> 100.dp // Móvil
            WindowWidthSizeClass.Medium -> 150.dp // Tableta pequeña
            WindowWidthSizeClass.Expanded -> 200.dp // Tableta grande
            else -> 100.dp // Ancho por defecto
        }
    }

    // Card Dimensions
    fun getCardHeight(windowSize: WindowWidthSizeClass): Dp {
        return when (windowSize) {
            WindowWidthSizeClass.Compact -> 150.dp // Móvil
            WindowWidthSizeClass.Medium -> 200.dp // Tableta pequeña
            WindowWidthSizeClass.Expanded -> 250.dp // Tableta grande
            else -> 150.dp // Altura por defecto
        }
    }

    fun getCardPadding(windowSize: WindowWidthSizeClass): Dp {
        return when (windowSize) {
            WindowWidthSizeClass.Compact -> 8.dp // Móvil
            WindowWidthSizeClass.Medium -> 16.dp // Tableta pequeña
            WindowWidthSizeClass.Expanded -> 24.dp // Tableta grande
            else -> 8.dp // Altura por defecto

        }
    }
}