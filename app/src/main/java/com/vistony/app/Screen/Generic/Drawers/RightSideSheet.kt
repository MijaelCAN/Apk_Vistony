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
fun RightCurtainDrawer(
    visible: Boolean,
    onClose: () -> Unit,
    animationDuration: Int = 500, // ms, control de velocidad
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val screenWidthPx = with(density) { LocalConfiguration.current.screenWidthDp.dp.toPx() }

    // Animatable para el offset X (en px)
    val offsetX = remember { Animatable(screenWidthPx) }

    LaunchedEffect(visible) {
        if (visible) {
            // Abrir: animar de fuera (screenWidthPx) a 0
            offsetX.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = animationDuration, easing = FastOutSlowInEasing)
            )
        } else {
            // Cerrar: animar de 0 a fuera (screenWidthPx)
            offsetX.animateTo(
                targetValue = screenWidthPx,
                animationSpec = tween(durationMillis = animationDuration, easing = FastOutSlowInEasing)
            )
            onClose()
        }
    }

    if (visible || offsetX.value < screenWidthPx) {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color(0x80000000)) // Fondo semitransparente
                .clickable {
                    // Cerrar al tocar fuera
                    /*scope.launch {
                        offsetX.animateTo(
                            targetValue = screenWidthPx,
                            animationSpec = tween(durationMillis = animationDuration, easing = FastOutSlowInEasing)
                        )
                        onClose()
                    }*/
                }
        ) {
            Box(
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                    .background(Color(0xFFF7F7F7))
            ) {
                content()
            }
        }
    }
}


