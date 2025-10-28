package com.vistony.app.Screen.Generic

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Thermostat
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vistony.app.ui.theme.theme.Dimensions
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun TemperatureTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    enabled: Boolean = true,
    isError: Boolean = false,
    errorMessage: String? = null,
    minLines: Int = 1,
    maxLines: Int = 1,
    readOnly: Boolean = false
) {
    val context = LocalContext.current
    val activity = context as Activity
    val windowSize = calculateWindowSizeClass(context)
    val bodyFontSize = Dimensions.getBodyFontSize(windowSize.widthSizeClass)
    
    var isFocused by remember { mutableStateOf(false) }
    
    // Colores adaptativos (mismo estilo que MuestraTextField)
    val borderColor by animateColorAsState(
        targetValue = when {
            isError -> Color(0xFFDC2626)
            isFocused -> Color(0xFF667EEA) // Color primario de temperatura
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
            isFocused -> Color(0xFF667EEA) // Color primario de temperatura
            !enabled -> Color(0xFF9CA3AF)
            else -> Color(0xFF374151)
        },
        label = "label_color"
    )
    
    Column(modifier = modifier) {
        // Label
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = labelColor,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        
        // Campo de texto con animación (mismo estilo que MuestraTextField)
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
                // Leading Icon
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
                
                // TextField usando BasicTextField
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
                        keyboardType = keyboardType
                    ),
                    singleLine = (minLines == 1 && maxLines == 1),
                    maxLines = maxLines,
                    minLines = minLines,
                    cursorBrush = SolidColor(Color(0xFF667EEA)),
                    decorationBox = { innerTextField ->
                        Box(
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (value.isEmpty() && placeholder.isNotEmpty()) {
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
                
                // Trailing Icon
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
        
        // Error message
        if (isError && errorMessage != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.padding(start = 16.dp),
                verticalAlignment = Alignment.CenterVertically
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
                    color = Color(0xFFDC2626),
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
fun TemperatureButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    icon: ImageVector? = null,
    backgroundColor: Color = Color(0xFF667EEA),
    contentColor: Color = Color.White,
    buttonHeight: androidx.compose.ui.unit.Dp = 56.dp
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(100),
        label = "scale"
    )
    
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
            .clip(RoundedCornerShape(16.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (enabled) backgroundColor else Color(0xFFCBD5E0),
            contentColor = contentColor
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = contentColor,
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp
            )
            Spacer(modifier = Modifier.width(12.dp))
        }
        
        if (icon != null && !isLoading) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun TemperatureCard(
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
fun TemperatureInfoChip(
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
            color = Color(0xFF2D3748),
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun TemperatureIcon(
    temperature: Double,
    size: androidx.compose.ui.unit.Dp = 60.dp,
    modifier: Modifier = Modifier
) {
    val color = getTemperatureColor(temperature)
    val icon = getTemperatureIcon(temperature)
    
    Box(
        modifier = modifier
            .size(size)
            .background(
                color = color.copy(alpha = 0.1f),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Temperatura",
            modifier = Modifier.size(size * 0.5f),
            tint = color
        )
    }
}

@Composable
fun TemperatureHeader(
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
                        colors = listOf(
                            Color(0xFF01398D),// 0xFF667EEA
                            Color(0xFF06C581) // 0xFF764BA2
                        )
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
fun TemperatureEmptyState(
    icon: ImageVector = Icons.Default.Thermostat,
    title: String,
    subtitle: String,
    actionButton: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    TemperatureCard(
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
                        color = Color(0xFF667EEA).copy(alpha = 0.1f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = "Estado vacío",
                    modifier = Modifier.size(40.dp),
                    tint = Color(0xFF667EEA)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3748),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = subtitle,
                fontSize = 14.sp,
                color = Color(0xFF718096),
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
fun TemperatureLoadingCard(
    message: String = "Cargando...",
    modifier: Modifier = Modifier
) {
    TemperatureCard(
        modifier = modifier.fillMaxWidth(),
        elevation = 0.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = Color(0xFF667EEA),
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                fontSize = 16.sp,
                color = Color(0xFF718096)
            )
        }
    }
}

@Composable
fun TemperatureSectionTitle(
    title: String,
    subtitle: String? = null,
    icon: ImageVector? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = Color(0xFF667EEA).copy(alpha = 0.1f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = "Sección",
                    tint = Color(0xFF667EEA),
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
                color = Color(0xFF2D3748)
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Color(0xFF718096)
                )
            }
        }
    }
}


fun getTemperatureColor(temperatura: Double): Color {
    return when {
        temperatura < 0 -> Color(0xFF4299E1) // Azul frío
        temperatura < 20 -> Color(0xFF48BB78) // Verde fresco
        temperatura < 40 -> Color(0xFFED8936) // Naranja cálido
        temperatura < 60 -> Color(0xFFE53E3E) // Rojo caliente
        else -> Color(0xFF9F7AEA) // Púrpura muy caliente
    }
}

fun getTemperatureIcon(temperatura: Double): ImageVector {
    return when {
        temperatura < 20 -> Icons.Default.Thermostat
        temperatura < 40 -> Icons.Default.Schedule
        else -> Icons.Default.Thermostat
    }
}

fun formatTemperature(temperatureString: String): String {
    val temperature = temperatureString.toDoubleOrNull() ?: 0.0
    
    // Si no tiene decimales o son .0, mostrar sin decimales
    if (temperature == temperature.toInt().toDouble()) {
        return temperature.toInt().toString()
    }
    
    // Si tiene decimales, mostrar solo 1 decimal eliminando ceros innecesarios
    return String.format("%.1f", temperature).trimEnd('0').trimEnd('.')
}
