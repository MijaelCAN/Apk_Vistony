package com.vistony.app.Screen.Generic.Recursos

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.triStateToggleable
import androidx.compose.material.minimumInteractiveComponentSize
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import kotlin.math.floor

private val CheckboxSize = 20.dp
private val StrokeWidth = 2.dp
private val RadiusSize = 3.dp
private const val CheckAnimationDuration = 100
private const val BoxOutDuration = 50

@Composable
fun CustomTriStateCheckbox(
    state: ToggleableState,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: CheckboxColors = CheckboxDefaults.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val toggleableModifier =
        if (onClick != null) {
            Modifier.triStateToggleable(
                state = state,
                onClick = onClick,
                enabled = enabled,
                role = Role.Checkbox,
                interactionSource = interactionSource,
                indication = rememberRipple(
                    bounded = false,
                    radius = 40.0.dp / 2
                )
            )
        } else {
            Modifier
        }

    CustomCheckboxImpl(
        enabled = enabled,
        value = state,
        modifier = modifier
            .then(
                if (onClick != null) Modifier.minimumInteractiveComponentSize() else Modifier
            )
            .then(toggleableModifier)
            .padding(4.dp),
        colors = colors
    )
}

@Composable
private fun CustomCheckboxImpl(
    enabled: Boolean,
    value: ToggleableState,
    modifier: Modifier,
    colors: CheckboxColors
) {
    val transition = updateTransition(targetState = value, label = "checkboxTransition")

    val checkDrawFraction by transition.animateFloat(
        transitionSpec = {
            when {
                initialState == ToggleableState.Off -> tween(CheckAnimationDuration)
                targetState == ToggleableState.Off -> snap(BoxOutDuration)
                else -> spring()
            }
        },
        label = "checkDrawFraction"
    ) { state ->
        when (state) {
            ToggleableState.On -> 1f
            ToggleableState.Off -> 0f
            ToggleableState.Indeterminate -> 1f
        }
    }

    val checkCenterGravitationShiftFraction by transition.animateFloat(
        transitionSpec = {
            when {
                initialState == ToggleableState.Off -> snap()
                targetState == ToggleableState.Off -> snap(BoxOutDuration)
                else -> tween(durationMillis = CheckAnimationDuration)
            }
        },
        label = "checkCenterGravitationShiftFraction"
    ) { state ->
        when (state) {
            ToggleableState.On -> 0f
            ToggleableState.Off -> 0f
            ToggleableState.Indeterminate -> 1f
        }
    }

    //val checkColor = colors.checkmarkColor(value).value
    //val checkColor = colors.checkmarkColor(value).value
    val checkColor = animatedCheckmarkColor(
        state = value,
        checkedColor = Color.White,
        uncheckedColor = Color.Transparent
    ).value

    //val boxColor = colors.boxColor(enabled, value).value
    //val borderColor = colors.borderColor(enabled, value).value
    val boxColor = animatedBoxColor(
        enabled = enabled,
        state = value,
        checkedBoxColor = colors.checkedBoxColor, // Amarillo para Indeterminate y On 0xFFFFC107
        uncheckedBoxColor = Color.Gray,
        disabledCheckedBoxColor = Color.LightGray,
        disabledIndeterminateBoxColor = Color.LightGray,
        disabledUncheckedBoxColor = Color(0xFFE0E0E0)
    ).value

    val borderColor = animatedBorderColor(
        enabled = enabled,
        state = value,
        checkedBorderColor = Color(0xFF4CAF50), // 0xFFFFC107
        uncheckedBorderColor = Color.DarkGray,
        disabledBorderColor = Color.LightGray,
        disabledIndeterminateBorderColor = Color.LightGray,
        disabledUncheckedBorderColor = Color(0xFFBDBDBD)
    ).value


    Canvas(
        modifier = modifier
            .wrapContentSize(Alignment.Center)
            .requiredSize(CheckboxSize)
    ) {
        val strokeWidthPx = floor(StrokeWidth.toPx())

        // Dibuja el cuadro
        drawRoundRect(
            color = boxColor,
            size = size,
            cornerRadius = CornerRadius(RadiusSize.toPx())
        )
        drawRoundRect(
            color = borderColor,
            size = size,
            style = Stroke(width = strokeWidthPx),
            cornerRadius = CornerRadius(RadiusSize.toPx())
        )

        when (value) {
            ToggleableState.On -> {
                // Dibuja el check clásico
                drawCheckMark(
                    color = checkColor,
                    fraction = checkDrawFraction,
                    strokeWidth = strokeWidthPx
                )
            }
            ToggleableState.Indeterminate -> {
                // Dibuja la "X"
                drawXMark(
                    color = checkColor,
                    fraction = checkDrawFraction,
                    strokeWidth = strokeWidthPx
                )
            }
            else -> {
                // No dibuja nada en Off
            }
        }
    }
}

private fun DrawScope.drawCheckMark(
    color: Color,
    fraction: Float,
    strokeWidth: Float
) {
    // Coordenadas para el check
    val start = Offset(size.width * 0.2f, size.height * 0.5f)
    val mid = Offset(size.width * 0.4f, size.height * 0.7f)
    val end = Offset(size.width * 0.8f, size.height * 0.3f)

    val path = Path().apply {
        moveTo(start.x, start.y)
        lineTo(mid.x, mid.y)
        lineTo(end.x, end.y)
    }

    drawPath(
        path = path,
        color = color,
        style = Stroke(strokeWidth, cap = StrokeCap.Round),
        alpha = fraction
    )
}

private fun DrawScope.drawXMark(
    color: Color,
    fraction: Float,
    strokeWidth: Float
) {
    val padding = size.width * 0.2f
    val start1 = Offset(padding, padding)
    val end1 = Offset(size.width - padding, size.height - padding)
    val start2 = Offset(size.width - padding, padding)
    val end2 = Offset(padding, size.height - padding)

    drawLine(
        color = color,
        start = start1,
        end = end1,
        strokeWidth = strokeWidth,
        cap = StrokeCap.Round,
        alpha = fraction
    )
    drawLine(
        color = color,
        start = start2,
        end = end2,
        strokeWidth = strokeWidth,
        cap = StrokeCap.Round,
        alpha = fraction
    )
}

private const val BoxInDuration1 = 100
private const val BoxOutDuration2 = 50

@Composable
fun animatedCheckmarkColor(
    state: ToggleableState,
    checkedColor: Color = Color.White,
    uncheckedColor: Color = Color.Transparent
): State<Color> {
    val targetColor = if (state == ToggleableState.Off) uncheckedColor else checkedColor
    val duration = if (state == ToggleableState.Off) BoxOutDuration2 else BoxInDuration1

    return animateColorAsState(targetColor, animationSpec = tween(durationMillis = duration))
}

@Composable
fun animatedBoxColor(
    enabled: Boolean,
    state: ToggleableState,
    checkedBoxColor: Color = Color(0xFF6200EE), // Ejemplo: morado
    uncheckedBoxColor: Color = Color(0xFFCCCCCC), // Ejemplo: gris claro
    disabledCheckedBoxColor: Color = Color(0xFF9E9E9E), // Ejemplo: gris medio
    disabledIndeterminateBoxColor: Color = Color(0xFF9E9E9E),
    disabledUncheckedBoxColor: Color = Color(0xFFEEEEEE) // Ejemplo: gris muy claro
): State<Color> {
    val target = if (enabled) {
        when (state) {
            ToggleableState.On, ToggleableState.Indeterminate -> checkedBoxColor
            ToggleableState.Off -> uncheckedBoxColor
        }
    } else {
        when (state) {
            ToggleableState.On -> disabledCheckedBoxColor
            ToggleableState.Indeterminate -> disabledIndeterminateBoxColor
            ToggleableState.Off -> disabledUncheckedBoxColor
        }
    }

    return if (enabled) {
        val duration = if (state == ToggleableState.Off) BoxOutDuration2 else BoxInDuration1
        animateColorAsState(target, animationSpec = tween(durationMillis = duration))
    } else {
        rememberUpdatedState(target)
    }
}

@Composable
fun animatedBorderColor(
    enabled: Boolean,
    state: ToggleableState,
    checkedBorderColor: Color = Color(0xFF6200EE), // Ejemplo: morado oscuro
    uncheckedBorderColor: Color = Color(0xFF757575), // Ejemplo: gris oscuro
    disabledBorderColor: Color = Color(0xFFBDBDBD), // Ejemplo: gris medio
    disabledIndeterminateBorderColor: Color = Color(0xFFBDBDBD),
    disabledUncheckedBorderColor: Color = Color(0xFFE0E0E0) // Ejemplo: gris claro
): State<Color> {
    val target = if (enabled) {
        when (state) {
            ToggleableState.On, ToggleableState.Indeterminate -> checkedBorderColor
            ToggleableState.Off -> uncheckedBorderColor
        }
    } else {
        when (state) {
            ToggleableState.Indeterminate -> disabledIndeterminateBorderColor
            ToggleableState.On -> disabledBorderColor
            ToggleableState.Off -> disabledUncheckedBorderColor
        }
    }

    return if (enabled) {
        val duration = if (state == ToggleableState.Off) BoxOutDuration2 else BoxInDuration1
        animateColorAsState(target, animationSpec = tween(durationMillis = duration))
    } else {
        rememberUpdatedState(target)
    }
}
