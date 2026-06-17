package com.vistony.app.Screen.muestraProduccion

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vistony.app.Entidad.MuestraProduccionDetalle
import com.vistony.app.Entidad.MuestraProduccionTimelineItem
import com.vistony.app.ViewModel.MuestraViewModel
import com.vistony.app.ui.theme.theme.AppTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetalleMezclaScreen(
    docEntry: Int,
    modifier: Modifier = Modifier,
    muestraViewModel: MuestraViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onRegistrarMuestraClick: () -> Unit = {},
    onDeclararNoConformeClick: () -> Unit = {}
) {
    val detalle by muestraViewModel.muestraProduccionDetalle.collectAsState()
    val isLoading by muestraViewModel.isLoading.collectAsState()

    LaunchedEffect(docEntry) {
        muestraViewModel.obtenerMuestraProduccionDetalle(docEntry)
    }

    DetalleMezclaScreenContent(
        detalle = detalle,
        isLoading = isLoading,
        modifier = modifier,
        onBackClick = onBackClick,
        onNotificationClick = onNotificationClick,
        onRegistrarMuestraClick = onRegistrarMuestraClick,
        onDeclararNoConformeClick = onDeclararNoConformeClick
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleMezclaScreenContent(
    detalle: MuestraProduccionDetalle?,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onRegistrarMuestraClick: () -> Unit = {},
    onDeclararNoConformeClick: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Text(
                        text = "OF ${detalle?.ordenEnvase ?: ""}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.weight(1f)
                    )
                    BadgedBox(
                        badge = {
                            Badge(
                                containerColor = Color(0xFFD32F2F),
                                contentColor = Color.White,
                                modifier = Modifier.offset(x = (-4).dp, y = 4.dp)
                            ) {
                                Text("3")
                            }
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .border(1.dp, Color.LightGray, CircleShape)
                                .clickable { onNotificationClick() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = null,
                                tint = Color(0xFFFBC02D),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
                HorizontalDivider(color = Color.Black, thickness = 1.dp)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            if (isLoading && detalle == null) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (detalle == null) {
                Text(
                    text = "No se encontró información de la muestra",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            } else {
                MuestraSummaryCard(detalle = detalle)

                Spacer(modifier = Modifier.height(24.dp))

                SectionHeader(title = "Intentos de MEZCLA")

                Column(modifier = Modifier.padding(start = 4.dp, top = 16.dp)) {
                    detalle.timeline.forEachIndexed { index, item ->
                        MuestraTimelineItem(
                            item = item,
                            isLast = index == detalle.timeline.lastIndex
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                SectionHeader(title = "Órdenes de Envasado (SAP)")

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E0E0).copy(alpha = 0.5f)),
                    border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.3f))
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "— se generan en SAP al aprobar la mezcla —",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clickable { onRegistrarMuestraClick() },
                    color = Color(0xFF212121),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "+ Registrar muestra de mezcla",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clickable { onDeclararNoConformeClick() }
                        .drawBehind {
                            val stroke = Stroke(
                                width = 1.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                            )
                            drawRoundRect(
                                color = Color(0xFF9E4B4B),
                                style = stroke,
                                cornerRadius = CornerRadius(8.dp.toPx())
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Declarar mezcla NO CONFORME",
                        color = Color(0xFF9E4B4B),
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun MuestraSummaryCard(detalle: MuestraProduccionDetalle) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E0E0).copy(alpha = 0.4f)),
        border = BorderStroke(1.dp, Color.Black.copy(alpha = 0.8f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = detalle.descripcion,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.DarkGray
                )
                MuestraStatusBadge(status = detalle.status)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Lote ${detalle.lote} · 2 500 kg · Línea Mezcladora 3", // Mocking extended details from image
                color = Color.Gray,
                fontSize = 14.sp
            )
            Text(
                text = "Inicio: ${formatFechaHora(detalle.dateRegister)} · Resp. ${detalle.userRegister}",
                color = Color.Gray,
                fontSize = 14.sp
            )
            Text(
                text = "Datos desde SAP — solo lectura",
                color = Color.Gray,
                fontSize = 13.sp,
                fontStyle = FontStyle.Italic
            )
            if (detalle.status == "RECHAZADO" && (!detalle.reason.isNullOrBlank() || !detalle.typeReject.isNullOrBlank())) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = listOfNotNull(detalle.typeReject, detalle.reason).joinToString(" · "),
                    color = Color(0xFF9E4B4B),
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Column {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            color = Color.DarkGray
        )
        Spacer(modifier = Modifier.height(8.dp))
        MezclaDashedLine()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun MuestraTimelineItem(
    item: MuestraProduccionTimelineItem,
    isLast: Boolean
) {
    val statusColor = timelineStatusColor(item.status)
    val dotColor = if (item.esCurrent) Color.Black else statusColor
    
    val detail = when {
        !item.reason.isNullOrBlank() -> item.reason
        !item.typeReject.isNullOrBlank() -> item.typeReject
        item.status == "PENDIENTE" -> "En espera de evaluación"
        else -> ""
    }

    Row(modifier = Modifier.height(IntrinsicSize.Min)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(24.dp)
        ) {
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(if (item.esCurrent) dotColor else Color.Transparent, CircleShape)
                    .border(2.dp, dotColor, CircleShape)
            )
            if (!isLast) {
                Canvas(modifier = Modifier.weight(1f).width(2.dp)) {
                    drawLine(
                        color = Color.Gray,
                        start = Offset(size.width / 2, 8f),
                        end = Offset(size.width / 2, size.height),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f),
                        strokeWidth = 2.dp.toPx()
                    )
                }
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.padding(bottom = 24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = item.version,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = " · ${formatFechaHora(item.dateRegister)} · ",
                    fontSize = 15.sp,
                    color = Color.Gray
                )
                MuestraStatusBadge(status = item.status)
            }
            if (detail != null && detail.isNotBlank()) {
                Text(
                    text = detail,
                    color = Color.Gray,
                    fontSize = 15.sp
                )
            }
        }
    }
}

private fun timelineStatusColor(status: String): Color = when (status) {
    "RECHAZADO" -> Color(0xFF9E4B4B)
    "APROBADO" -> Color(0xFF568057)
    "EN_ANALISIS" -> Color(0xFF3C5A81)
    else -> Color(0xFF8B7A4D)
}

@Composable
private fun MuestraStatusBadge(status: String) {
    val (bgColor, textColor) = when (status) {
        "EN_ANALISIS" -> Color(0xFFD7E3F4) to Color(0xFF3C5A81)
        "APROBADO" -> Color(0xFFDFF0E0) to Color(0xFF568057)
        "PENDIENTE" -> Color(0xFFFAF3E0) to Color(0xFF8B7A4D)
        "RECHAZADO" -> Color(0xFFF9E0E0) to Color(0xFF9E4B4B)
        else -> Color.LightGray to Color.DarkGray
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(1.dp, textColor.copy(alpha = 0.3f))
    ) {
        Text(
            text = status,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun MezclaDashedLine() {
    Canvas(
        Modifier
            .fillMaxWidth()
            .height(1.dp)
    ) {
        drawLine(
            color = Color.Gray.copy(alpha = 0.5f),
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun formatFechaHora(iso: String?): String {
    if (iso.isNullOrBlank()) return "-"
    return try {
        LocalDateTime.parse(iso.removeSuffix("Z"))
            .format(DateTimeFormatter.ofPattern("dd/MM HH:mm"))
    } catch (e: Exception) {
        iso
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
private fun DetalleMezclaScreenPreview() {
    AppTheme {
        DetalleMezclaScreenContent(
            detalle = MuestraProduccionDetalle(
                code = "CODE",
                counter = 1,
                dateEndAnalysis = null,
                dateRegister = "2023-10-22T08:14:00",
                dateStartAnalysis = null,
                descripcion = "Shampoo Aloe 2x",
                docEntry = 123,
                isFinish = false,
                lote = "L-2401",
                observation = null,
                ordenEnvase = "26000456",
                ordenFabricacion = "OF-001",
                reason = null,
                status = "EN_ANALISIS",
                timeline = listOf(
                    MuestraProduccionTimelineItem(
                        counter = 1,
                        dateEndAnalysis = null,
                        dateRegister = "2023-10-22T09:02:00",
                        esCurrent = false,
                        reason = "viscosidad baja (3 800 cP)",
                        status = "RECHAZADO",
                        typeReject = null,
                        userRegister = "Ana Rojas",
                        version = "V1.0"
                    ),
                    MuestraProduccionTimelineItem(
                        counter = 2,
                        dateEndAnalysis = null,
                        dateRegister = "2023-10-22T11:20:00",
                        esCurrent = false,
                        reason = "particulas visibles",
                        status = "RECHAZADO",
                        typeReject = null,
                        userRegister = "Ana Rojas",
                        version = "V1.1"
                    ),
                    MuestraProduccionTimelineItem(
                        counter = 3,
                        dateEndAnalysis = null,
                        dateRegister = "2023-10-22T13:45:00",
                        esCurrent = true,
                        reason = "esperando Lab (00:48)",
                        status = "EN_ANALISIS",
                        typeReject = null,
                        userRegister = "Ana Rojas",
                        version = "V1.2"
                    )
                ),
                type = "ENV",
                typeReject = null,
                userEndAnalysis = null,
                userRegister = "Ana Rojas",
                userStartAnalysis = null,
                version = "V1.2"
            ),
            isLoading = false
        )
    }
}
