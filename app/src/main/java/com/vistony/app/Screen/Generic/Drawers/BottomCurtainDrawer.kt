package com.vistony.app.Screen.Generic.Drawers

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun BottomCurtainDrawer(
    visible: Boolean,
    onClose: () -> Unit,
    animationDuration: Int = 500, // ms, control de velocidad
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val screenHeightPx = with(density) { LocalConfiguration.current.screenHeightDp.dp.toPx() }

    // Animatable para el offset Y (en px)
    val offsetY = remember { Animatable(screenHeightPx) }

    LaunchedEffect(visible) {
        if (visible) {
            // Abrir: animar de fuera (screenHeightPx) a 0
            offsetY.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = animationDuration, easing = FastOutSlowInEasing)
            )
        } else {
            // Cerrar: animar de 0 a fuera (screenHeightPx)
            offsetY.animateTo(
                targetValue = screenHeightPx,
                animationSpec = tween(durationMillis = animationDuration, easing = FastOutSlowInEasing)
            )
            onClose()
        }
    }

    if (visible || offsetY.value < screenHeightPx) {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color(0x80000000)) // Fondo semitransparente
                .clickable {
                    // Aquí puedes activar el cierre al tocar fuera si quieres
                }
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .offset { IntOffset(0, offsetY.value.roundToInt()) }
                    .background(Color(0xFFF7F7F7))
            ) {
                content()
            }
        }
    }
}
