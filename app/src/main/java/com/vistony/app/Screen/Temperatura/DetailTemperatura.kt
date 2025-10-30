package com.vistony.app.Screen.Temperatura

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vistony.app.Entidad.Temperatura
import com.vistony.app.Screen.Generic.getTemperatureColor
import com.vistony.app.Screen.Generic.formatTemperature
import com.vistony.app.ViewModel.TemperaturaViewModel
import com.vistony.app.ui.theme.theme.Dimensions
import androidx.hilt.navigation.compose.hiltViewModel
import com.vistony.app.Screen.Generic.PrimaryMuestraColor
import com.vistony.app.Screen.Generic.SecondaryMuestraColor

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetailTemperatura(
    temperatura: Temperatura,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as Activity
    val windowSize = calculateWindowSizeClass(context)
    val padding_res = Dimensions.getPadding(windowSize.widthSizeClass)
    val bodyFontSize = Dimensions.getBodyFontSize(windowSize.widthSizeClass)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF8FAFF),
                        Color(0xFFE8F2FF)
                    )
                )
            )
    ) {
        // Header con gradiente
        TemperatureDetailHeader(
            temperatura = temperatura,
            onBackClick = onNavigateBack,
            padding = padding_res
        )

        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = padding_res, vertical = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Card de temperatura principal
            TemperatureMainCard(
                temperatura = temperatura,
                bodyFontSize = bodyFontSize
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Cards de información
            TemperatureInfoCards(
                temperatura = temperatura,
                bodyFontSize = bodyFontSize
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Card de observaciones si existe
            if (temperatura.observaciones.isNotEmpty()) {
                TemperatureObservationsCard(
                    observaciones = temperatura.observaciones,
                    bodyFontSize = bodyFontSize
                )
            }
        }
    }
}

@Composable
fun TemperatureDetailHeader(
    temperatura: Temperatura,
    onBackClick: () -> Unit,
    padding: androidx.compose.ui.unit.Dp
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = padding, vertical = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
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
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = Color.White.copy(alpha = 0.2f),
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Atrás",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "Detalle de Temperatura",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "OT: ${temperatura.ot}",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }

                TemperatureHeaderIcon(
                    temperatura = temperatura.temperatura.toDoubleOrNull() ?: 0.0
                )
            }
        }
    }
}

@Composable
fun TemperatureHeaderIcon(temperatura: Double) {
    val color = getTemperatureColor(temperatura)
    
    Box(
        modifier = Modifier
            .size(60.dp)
            .background(
                color = Color.White.copy(alpha = 0.2f),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Thermostat,
                contentDescription = "Temperatura",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "${formatTemperature(temperatura.toString())}°C",
                fontSize = 10.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun TemperatureMainCard(
    temperatura: Temperatura,
    bodyFontSize: Float
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icono de temperatura grande
            TemperatureLargeIcon(
                temperatura = temperatura.temperatura.toDoubleOrNull() ?: 0.0
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Valor de temperatura
            Text(
                text = "${formatTemperature(temperatura.temperatura)}°C",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = getTemperatureColor(temperatura.temperatura.toDoubleOrNull() ?: 0.0)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Descripción
            Text(
                text = temperatura.descripcion,
                fontSize = 16.sp,
                color = Color(0xFF718096),
                textAlign = TextAlign.Center,
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Fecha y hora
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TemperatureInfoChip(
                    icon = Icons.Default.CalendarToday,
                    text = temperatura.fecha,
                    color = Color(0xFF4299E1)
                )
                
                /*TemperatureInfoChip(
                    icon = Icons.Default.Schedule,
                    text = temperatura.hora,
                    color = Color(0xFF48BB78)
                )*/
            }
        }
    }
}

@Composable
fun TemperatureLargeIcon(temperatura: Double) {
    val color = getTemperatureColor(temperatura)
    
    Box(
        modifier = Modifier
            .size(100.dp)
            .background(
                color = color.copy(alpha = 0.1f),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Thermostat,
            contentDescription = "Temperatura",
            modifier = Modifier.size(50.dp),
            tint = color
        )
    }
}

@Composable
fun TemperatureInfoChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    color: Color
) {
    Row(
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
fun TemperatureInfoCards(
    temperatura: Temperatura,
    bodyFontSize: Float
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Card de OT
        TemperatureInfoCard(
            title = "Orden de Trabajo",
            content = temperatura.ot,
            icon = Icons.Default.Work,
            color = Color(0xFF667EEA),
            modifier = Modifier.weight(1f)
        )

        // Card de Auxiliar
        TemperatureInfoCard(
            title = "Auxiliar",
            content = temperatura.auxiliar,
            icon = Icons.Default.Person,
            color = Color(0xFF48BB78),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun TemperatureInfoCard(
    title: String,
    content: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = color.copy(alpha = 0.1f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                fontSize = 12.sp,
                color = Color(0xFF718096),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = content,
                fontSize = 14.sp,
                color = Color(0xFF2D3748),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
}

@Composable
fun TemperatureObservationsCard(
    observaciones: String,
    bodyFontSize: Float
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            color = Color(0xFFED8936).copy(alpha = 0.1f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Work,
                        contentDescription = "Observaciones",
                        tint = Color(0xFFED8936),
                        modifier = Modifier.size(16.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Observaciones",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3748)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = observaciones,
                fontSize = 14.sp,
                color = Color(0xFF718096),
                lineHeight = 20.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetailTemperaturaWithViewModel(
    temperaturaId: String,
    onNavigateBack: () -> Unit,
    temperaturaViewModel: TemperaturaViewModel
) {
    val temperatura = temperaturaViewModel.obtenerTemperaturaPorId(temperaturaId)
    
    if (temperatura != null) {
        DetailTemperatura(
            temperatura = temperatura,
            onNavigateBack = onNavigateBack
        )
    } else {
        // Mostrar pantalla de error o loading
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Temperatura no encontrada",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3748)
            )
        }
    }
}
