package com.vistony.app.Screen.Generic

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vistony.app.Entidad.CriterioEvaluacion
import com.vistony.app.Entidad.MuestraCompleta
import com.vistony.app.ui.theme.theme.Dimensions

// Colores del módulo de Muestras
val PrimaryMuestraColor = Color(0xFF01398D) // Índigo moderno
val SecondaryMuestraColor = Color(0xFF06C581) // Púrpura elegante
val SuccessColor = Color(0xFF10B981) // Verde éxito
val WarningColor = Color(0xFFF59E0B) // Amarillo advertencia
val ErrorColor = Color(0xFFEF4444) // Rojo error
val InfoColor = Color(0xFF3B82F6) // Azul información
val BackgroundGradientStart = Color(0xFFF8FAFC)
val BackgroundGradientEnd = Color(0xFFE2E8F0)

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun MuestraTextField2(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    enabled: Boolean = true,
    isError: Boolean = false,
    errorMessage: String? = null,
    minLines: Int = 1,
    maxLines: Int = 1,
    placeholder: String? = null
) {
    val context = LocalContext.current
    val activity = context as Activity
    val windowSize = calculateWindowSizeClass(context)
    val bodyFontSize = Dimensions.getBodyFontSize(windowSize.widthSizeClass)
    
    var isFocused by remember { mutableStateOf(false) }
    
    val borderColor = when {
        isError -> ErrorColor
        isFocused -> PrimaryMuestraColor
        else -> Color(0xFFD1D5DB)
    }
    
    val backgroundColor = if (enabled) Color.White else Color(0xFFF9FAFB)
    
    Column(modifier = modifier) {
        // Label
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = if (isFocused) PrimaryMuestraColor else Color(0xFF6B7280),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        // TextField Container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp) // Altura más compacta
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    width = if (isFocused) 2.dp else 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Leading Icon
                leadingIcon?.let {
                    Box(modifier = Modifier.padding(end = 8.dp)) {
                        it()
                    }
                }
                
                // TextField
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    enabled = enabled,
                    singleLine = maxLines == 1,
                    maxLines = maxLines,
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontSize = 16.sp,
                        color = if (enabled) Color(0xFF111827) else Color(0xFF9CA3AF)
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                    decorationBox = { innerTextField ->
                        if (value.isEmpty() && placeholder != null) {
                            Text(
                                text = placeholder,
                                color = Color(0xFF9CA3AF),
                                fontSize = 16.sp
                            )
                        }
                        innerTextField()
                    }
                )
                
                // Trailing Icon
                trailingIcon?.let {
                    Box(modifier = Modifier.padding(start = 8.dp)) {
                        it()
                    }
                }
            }
        }
        
        if (isError && errorMessage != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = errorMessage,
                color = ErrorColor,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}

@Composable
fun MuestraButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    icon: ImageVector? = null,
    backgroundColor: Color = PrimaryMuestraColor,
    contentColor: Color = Color.White,
    buttonHeight: androidx.compose.ui.unit.Dp = 56.dp,
    variant: ButtonVariant = ButtonVariant.Filled
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(100),
        label = "scale"
    )
    
    val buttonColors = when (variant) {
        ButtonVariant.Filled -> ButtonDefaults.buttonColors(
            containerColor = if (enabled) backgroundColor else Color(0xFFD1D5DB),
            contentColor = contentColor
        )
        ButtonVariant.Outlined -> ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = if (enabled) backgroundColor else Color(0xFF9CA3AF)
        )
        ButtonVariant.Text -> ButtonDefaults.textButtonColors(
            containerColor = Color.Transparent,
            contentColor = if (enabled) backgroundColor else Color(0xFF9CA3AF)
        )
    }
    
    val buttonShape = RoundedCornerShape(12.dp)
    
    when (variant) {
        ButtonVariant.Filled -> {
            Button(
                onClick = {
                    isPressed = true
                    onClick()
                },
                enabled = enabled && !isLoading,
                modifier = modifier
                    .fillMaxWidth()
                    .height(buttonHeight)
                    .scale(scale)
                    .clip(buttonShape),
                colors = buttonColors,
                shape = buttonShape
            ) {
                ButtonContent(text, icon, isLoading, contentColor)
            }
        }
        ButtonVariant.Outlined -> {
            OutlinedButton(
                onClick = {
                    isPressed = true
                    onClick()
                },
                enabled = enabled && !isLoading,
                modifier = modifier
                    .fillMaxWidth()
                    .height(buttonHeight)
                    .scale(scale)
                    .clip(buttonShape),
                colors = buttonColors,
                shape = buttonShape,
                border = androidx.compose.foundation.BorderStroke(
                    width = 1.dp,
                    color = if (enabled) backgroundColor else Color(0xFFD1D5DB)
                )
            ) {
                ButtonContent(text, icon, isLoading, if (enabled) backgroundColor else Color(0xFF9CA3AF))
            }
        }
        ButtonVariant.Text -> {
            TextButton(
                onClick = {
                    isPressed = true
                    onClick()
                },
                enabled = enabled && !isLoading,
                modifier = modifier
                    .fillMaxWidth()
                    .height(buttonHeight)
                    .scale(scale)
                    .clip(buttonShape),
                colors = buttonColors,
                shape = buttonShape
            ) {
                ButtonContent(text, icon, isLoading, if (enabled) backgroundColor else Color(0xFF9CA3AF))
            }
        }
    }
}

@Composable
private fun ButtonContent(
    text: String,
    icon: ImageVector?,
    isLoading: Boolean,
    contentColor: Color
) {
    if (isLoading) {
        CircularProgressIndicator(
            color = contentColor,
            modifier = Modifier.size(20.dp),
            strokeWidth = 2.dp
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "Cargando...",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = contentColor
        )
    } else {
        icon?.let {
            Icon(
                imageVector = it,
                contentDescription = text,
                modifier = Modifier.size(20.dp),
                tint = contentColor
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = contentColor
        )
    }
}

enum class ButtonVariant {
    Filled, Outlined, Text
}

@Composable
fun MuestraCard(
    modifier: Modifier = Modifier,
    elevation: androidx.compose.ui.unit.Dp = 4.dp,
    shape: Shape = RoundedCornerShape(16.dp),
    backgroundColor: Color = Color.White,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            content = content
        )
    }
}

@Composable
fun MuestraHeader(
    title: String,
    subtitle: String? = null,
    menuButton: @Composable (() -> Unit)? = null,
    actionButton: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(PrimaryMuestraColor, SecondaryMuestraColor)
                    )
                )
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                menuButton?.invoke()
                Column {
                    Text(
                        text = title,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    if (subtitle != null) {
                        Text(
                            text = subtitle,
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
                
                actionButton?.invoke()
            }
        }
    }
}

@Composable
fun MuestraSectionTitle(
    title: String,
    subtitle: String? = null,
    icon: ImageVector? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = PrimaryMuestraColor.copy(alpha = 0.1f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = PrimaryMuestraColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
        }
        
        Column {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Color(0xFF6B7280)
                )
            }
        }
    }
}

@Composable
fun MuestraChip(
    text: String,
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    color: Color = PrimaryMuestraColor
) {
    val backgroundColor = if (isSelected) color else Color.Transparent
    val contentColor = if (isSelected) Color.White else color
    val borderColor = color
    
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = contentColor
        )
    }
}

@Composable
fun MuestraCriterioSelector(
    label: String,
    selectedValue: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF374151),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CriterioEvaluacion.values().forEach { criterio ->
                MuestraChip(
                    text = criterio.displayName,
                    isSelected = selectedValue == criterio.name,
                    onClick = { onValueChange(criterio.name) },
                    color = when (criterio) {
                        CriterioEvaluacion.APROBADO -> SuccessColor
                        CriterioEvaluacion.OBSERVADO -> WarningColor
                        CriterioEvaluacion.RECHAZADO -> ErrorColor
                    }
                )
            }
        }
    }
}

@Composable
fun MuestraEmptyState(
    icon: ImageVector = Icons.Default.Science,
    title: String,
    subtitle: String,
    actionButton: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    MuestraCard(
        modifier = modifier.fillMaxWidth(),
        elevation = 0.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        color = PrimaryMuestraColor.copy(alpha = 0.1f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = "Estado vacío",
                    modifier = Modifier.size(40.dp),
                    tint = PrimaryMuestraColor
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = subtitle,
                fontSize = 14.sp,
                color = Color(0xFF6B7280),
                textAlign = TextAlign.Center
            )

            if (actionButton != null) {
                Spacer(modifier = Modifier.height(24.dp))
                actionButton()
            }
        }
    }
}

@Composable
fun MuestraLoadingCard(
    message: String = "Cargando...",
    modifier: Modifier = Modifier
) {
    MuestraCard(
        modifier = modifier.fillMaxWidth(),
        elevation = 0.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = PrimaryMuestraColor,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                fontSize = 16.sp,
                color = Color(0xFF6B7280)
            )
        }
    }
}

@Composable
fun MuestraWizardStep(
    stepNumber: Int,
    title: String,
    isActive: Boolean = false,
    isCompleted: Boolean = false,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    color = when {
                        isCompleted -> SuccessColor
                        isActive -> PrimaryMuestraColor
                        else -> Color(0xFFE5E7EB)
                    },
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isCompleted) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Completado",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            } else {
                Text(
                    text = stepNumber.toString(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isActive) Color.White else Color(0xFF9CA3AF)
                )
            }
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
            color = when {
                isCompleted -> SuccessColor
                isActive -> PrimaryMuestraColor
                else -> Color(0xFF6B7280)
            }
        )
    }
}

@Composable
fun MuestraInfoChip(
    icon: ImageVector,
    text: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = text,
            fontSize = 14.sp,
            color = Color(0xFF374151),
            fontWeight = FontWeight.Medium
        )
    }
}


//==================================================================
@Composable
fun MuestraTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null,
    supportingText: String? = null,
    maxLines: Int = 1,
    minLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onImeAction: (() -> Unit)? = null
) {
    var isFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    // Colores adaptativos
    val borderColor by animateColorAsState(
        targetValue = when {
            isError -> Color(0xFFDC2626)
            isFocused -> Color(0xFF2563EB)
            !enabled -> Color(0xFFE5E7EB)
            else -> Color(0xFFD1D5DB)
        },
        label = "border_color"
    )

    val backgroundColor by animateColorAsState(
        targetValue = when {
            !enabled -> Color(0xFFF9FAFB)
            readOnly -> Color(0xFFF3F4F6)
            else -> Color.White
        },
        label = "bg_color"
    )

    val labelColor by animateColorAsState(
        targetValue = when {
            isError -> Color(0xFFDC2626)
            isFocused -> Color(0xFF2563EB)
            !enabled -> Color(0xFF9CA3AF)
            else -> Color(0xFF374151)
        },
        label = "label_color"
    )

    Column(modifier = modifier) {
        // Label
        AnimatedVisibility(
            visible = label.isNotEmpty(),
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = labelColor,
                modifier = Modifier.padding(bottom = 6.dp)
            )
        }

        // Campo de texto con animación
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = backgroundColor,
            border = BorderStroke(
                width = if (isFocused) 2.dp else 1.dp,
                color = borderColor
            ),
            shadowElevation = if (isFocused) 2.dp else 0.dp
        ) {
            Row(
                modifier = Modifier
                    .defaultMinSize(minHeight = 56.dp)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Leading Icon con animación
                leadingIcon?.let { icon ->
                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        icon()
                    }
                }

                // TextField
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier
                        .weight(1f)
                        .onFocusChanged { isFocused = it.isFocused },
                    enabled = enabled,
                    readOnly = readOnly,
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = if (enabled) Color(0xFF111827) else Color(0xFF9CA3AF),
                        lineHeight = 24.sp
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = keyboardType,
                        imeAction = imeAction
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            onImeAction?.invoke()
                            focusManager.clearFocus()
                        },
                        onNext = { onImeAction?.invoke() },
                        onGo = { onImeAction?.invoke() },
                        onSearch = { onImeAction?.invoke() }
                    ),
                    singleLine = maxLines == 1,
                    maxLines = if (minLines > maxLines) minLines else maxLines,
                    minLines = minLines,
                    visualTransformation = visualTransformation,
                    cursorBrush = SolidColor(Color(0xFF2563EB)),
                    decorationBox = { innerTextField ->
                        Box(
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (value.isEmpty()) {
                                Text(
                                    text = placeholder,
                                    fontSize = 16.sp,
                                    color = Color(0xFF9CA3AF),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            innerTextField()
                        }
                    }
                )

                // Trailing Icon con animación
                trailingIcon?.let { icon ->
                    Box(
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .size(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        icon()
                    }
                }
            }
        }

        // Supporting text o error message
        AnimatedVisibility (
            visible = (isError && errorMessage != null) || supportingText != null,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 6.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (isError && errorMessage != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = Color(0xFFDC2626),
                            modifier = Modifier
                                .size(14.dp)
                                .padding(end = 4.dp)
                        )
                        Text(
                            text = errorMessage,
                            fontSize = 12.sp,
                            color = Color(0xFFDC2626),
                            lineHeight = 16.sp
                        )
                    }
                } else if (supportingText != null) {
                    Text(
                        text = supportingText,
                        fontSize = 12.sp,
                        color = Color(0xFF6B7280),
                        lineHeight = 16.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

// Variante con contador de caracteres
@Composable
fun ModernTextFieldWithCounter(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    maxLength: Int,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    leadingIcon: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    isError: Boolean = false,
    errorMessage: String? = null,
    maxLines: Int = 1
) {
    val isOverLimit = value.length > maxLength

    MuestraTextField(
        value = value,
        onValueChange = { if (it.length <= maxLength) onValueChange(it) },
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        enabled = enabled,
        isError = isError || isOverLimit,
        errorMessage = if (isOverLimit) "Máximo $maxLength caracteres" else errorMessage,
        maxLines = maxLines,
        modifier = modifier,
        trailingIcon = {
            Text(
                text = "${value.length}/$maxLength",
                fontSize = 12.sp,
                color = if (isOverLimit) Color(0xFFDC2626) else Color(0xFF9CA3AF),
                fontWeight = FontWeight.Medium
            )
        }
    )
}

// Componente para mostrar el progreso de completitud
@Composable
fun MuestraProgressIndicator(
    muestra: MuestraCompleta,
    modifier: Modifier = Modifier
) {
    val porcentaje = muestra.getCompletitudPorcentaje()
    val estado = muestra.getEstadoGeneral()
    val seccionesPendientes = muestra.getSeccionesPendientes()
    
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                porcentaje == 100 -> Color(0xFFF0FDF4) // Verde claro
                porcentaje >= 75 -> Color(0xFFFEF3C7) // Amarillo claro
                porcentaje >= 50 -> Color(0xFFFEF2F2) // Rojo claro
                else -> Color(0xFFF3F4F6) // Gris claro
            }
        ),
        border = BorderStroke(
            width = 1.dp,
            color = when {
                porcentaje == 100 -> Color(0xFF10B981) // Verde
                porcentaje >= 75 -> Color(0xFFF59E0B) // Amarillo
                porcentaje >= 50 -> Color(0xFFEF4444) // Rojo
                else -> Color(0xFFD1D5DB) // Gris
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header con porcentaje y estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Progreso: $porcentaje%",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF374151)
                )
                
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = when {
                        porcentaje == 100 -> Color(0xFF10B981)
                        porcentaje >= 75 -> Color(0xFFF59E0B)
                        porcentaje >= 50 -> Color(0xFFEF4444)
                        else -> Color(0xFF6B7280)
                    }
                ) {
                    Text(
                        text = estado,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Barra de progreso
            LinearProgressIndicator(
                progress = porcentaje / 100f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = when {
                    porcentaje == 100 -> Color(0xFF10B981)
                    porcentaje >= 75 -> Color(0xFFF59E0B)
                    porcentaje >= 50 -> Color(0xFFEF4444)
                    else -> Color(0xFF6B7280)
                },
                trackColor = Color(0xFFE5E7EB)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Secciones completadas y pendientes
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Secciones completadas
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Completadas:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF6B7280)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    val seccionesCompletadas = listOf(
                        "Materiales" to muestra.materialesCompletado,
                        "Inspecciones" to muestra.inspeccionesCompletado,
                        "CheckLists" to muestra.checkListsCompletado,
                        "Evaluación" to muestra.evaluacionCompletado
                    )
                    
                    seccionesCompletadas.forEach { (nombre, completado) ->
                        if (completado) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color(0xFF10B981),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = nombre,
                                    fontSize = 11.sp,
                                    color = Color(0xFF10B981),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
                
                // Secciones pendientes
                if (seccionesPendientes.isNotEmpty()) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Pendientes:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF6B7280)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        seccionesPendientes.forEach { nombre ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Schedule,
                                    contentDescription = null,
                                    tint = Color(0xFF6B7280),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = nombre,
                                    fontSize = 11.sp,
                                    color = Color(0xFF6B7280),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
            
            // Fecha de última actualización
            if (muestra.fechaUltimaActualizacion.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Última actualización: ${muestra.fechaUltimaActualizacion}",
                    fontSize = 10.sp,
                    color = Color(0xFF9CA3AF),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.End
                )
            }
        }
    }
}

// Componente para mostrar el estado de la muestra
@Composable
fun MuestraStatusChip(
    estado: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = color.copy(alpha = 0.1f),
        border = BorderStroke(1.dp, color)
    ) {
        Text(
            text = estado,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color = color,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}