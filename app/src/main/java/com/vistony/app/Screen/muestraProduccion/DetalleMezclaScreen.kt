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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vistony.app.Entidad.FinalizarAnalisisRequest
import com.vistony.app.Entidad.MuestraProduccionDetalle
import com.vistony.app.Entidad.MuestraProduccionTimelineItem
import com.vistony.app.Entidad.ParametroValorRequest
import com.vistony.app.Entidad.UserResponse
import com.vistony.app.ViewModel.MuestraViewModel
import com.vistony.app.ui.theme.theme.AppTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetalleMezclaScreen(
    docEntry: Int,
    modifier: Modifier = Modifier,
    currentUser: UserResponse = UserResponse(),
    muestraViewModel: MuestraViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onRegistrarMuestraClick: () -> Unit = {}
) {
    val detalle by muestraViewModel.muestraProduccionDetalle.collectAsState()
    val isLoading by muestraViewModel.isLoading.collectAsState()
    val isProcesando by muestraViewModel.isCreating.collectAsState()
    val errorMessage by muestraViewModel.errorMessage.collectAsState()
    val muestrasProduccion by muestraViewModel.muestrasProduccion.collectAsState()
    val pendientesCount = muestrasProduccion.count { it.status == "PENDIENTE" }

    LaunchedEffect(docEntry) {
        muestraViewModel.obtenerMuestraProduccionDetalle(docEntry)
    }

    DetalleMezclaScreenContent(
        detalle = detalle,
        isLoading = isLoading,
        esCalidad = currentUser.role.equals("ASEG. CALIDAD", ignoreCase = true),
        isProcesando = isProcesando,
        errorMessage = errorMessage,
        pendientesCount = pendientesCount,
        modifier = modifier,
        onBackClick = onBackClick,
        onNotificationClick = onNotificationClick,
        onRefresh = { muestraViewModel.obtenerMuestraProduccionDetalle(docEntry) },
        onRegistrarMuestraClick = onRegistrarMuestraClick,
        onIniciarAnalisisClick = {
            muestraViewModel.iniciarAnalisis(docEntry, currentUser.dni)
        },
        onConfirmarResolucion = { decision, motivo, causa, observacion ->
            val request = FinalizarAnalisisRequest(
                status = decision,
                userEndAnalysis = currentUser.dni,
                typeReject = if (decision == "RECHAZADO") motivo else null,
                reason = if (decision == "RECHAZADO") causa else null,
                observation = observacion.ifBlank { null },
                // TODO: reemplazar por los parámetros reales medidos cuando se defina el catálogo
                parametros = listOf(ParametroValorRequest(idParametro = 1, valor = "1000"))
            )
            muestraViewModel.finalizarAnalisis(docEntry, request)
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun DetalleMezclaScreenContent(
    detalle: MuestraProduccionDetalle?,
    isLoading: Boolean,
    esCalidad: Boolean = false,
    isProcesando: Boolean = false,
    errorMessage: String? = null,
    pendientesCount: Int = 0,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onRefresh: () -> Unit = {},
    onRegistrarMuestraClick: () -> Unit = {},
    onIniciarAnalisisClick: () -> Unit = {},
    onConfirmarResolucion: (decision: String, motivo: String?, causa: String, observacion: String) -> Unit = { _, _, _, _ -> }
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isLoading,
        onRefresh = onRefresh
    )

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
                                containerColor = if (pendientesCount > 0)Color(0xFFD32F2F) else Color.Transparent,
                                contentColor = Color.White,
                                modifier = Modifier.offset(x = (-4).dp, y = 4.dp)
                            ) {
                                Text("$pendientesCount")
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
                                tint = if (pendientesCount > 0) Color(0xFFFBC02D) else Color.LightGray,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
                HorizontalDivider(color = Color.Black, thickness = 1.dp)
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            if (detalle == null && !isLoading) {
                Text(
                    text = "No se encontró información de la muestra",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            } else if (detalle != null) {
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

                if (esCalidad) {
                    AccionesCalidad(
                        detalle = detalle,
                        isProcesando = isProcesando,
                        errorMessage = errorMessage,
                        onIniciarAnalisisClick = onIniciarAnalisisClick,
                        onConfirmarResolucion = onConfirmarResolucion
                    )
                } else {
                    val siguienteTipo = detalle.siguienteTipoMuestra()
                    val tipoLabel = when (siguienteTipo) {
                        "ENVASADO_INICIO" -> "ENVASADO-INICIO"
                        else -> siguienteTipo
                    }
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
                                text = "Registrar muestra $tipoLabel",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }

            PullRefreshIndicator(
                refreshing = isLoading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

// Si la muestra actual ya está finalizada (aprobada), el botón de Producción debe
// pasar a registrar la siguiente etapa del flujo en lugar de otro intento de la misma.
fun MuestraProduccionDetalle.siguienteTipoMuestra(): String {
    val finalizado = isFinish || status == "APROBADO"
    return if (finalizado && type == "MEZCLA") "ENVASADO_INICIO" else type
}

private val MOTIVOS_RECHAZO = listOf(
    "Viscosidad fuera de rango",
    "Partículas visibles",
    "Tonalidad incorrecta",
    "Olor no conforme",
    "Contaminación",
    "Otro"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccionesCalidad(
    detalle: MuestraProduccionDetalle,
    isProcesando: Boolean,
    errorMessage: String?,
    onIniciarAnalisisClick: () -> Unit,
    onConfirmarResolucion: (decision: String, motivo: String?, causa: String, observacion: String) -> Unit
) {
    val analisisIniciado = !detalle.dateStartAnalysis.isNullOrBlank()
    val analisisFinalizado = !detalle.dateEndAnalysis.isNullOrBlank()

    when {
        detalle.status == "PENDIENTE" && !analisisIniciado -> {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clickable(enabled = !isProcesando) { onIniciarAnalisisClick() },
                color = if (isProcesando) Color.Gray else Color(0xFF212121),
                shape = RoundedCornerShape(8.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = if (isProcesando) "Iniciando..." else "Iniciar Análisis",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = errorMessage, color = Color(0xFF9E4B4B), fontSize = 14.sp)
            }
        }
        analisisIniciado && !analisisFinalizado -> {
            var decisionSeleccionada by remember(detalle.docEntry) { mutableStateOf<String?>(null) }
            var motivoSeleccionado by remember(detalle.docEntry) { mutableStateOf<String?>(null) }
            var motivoExpanded by remember(detalle.docEntry) { mutableStateOf(false) }
            var causa by remember(detalle.docEntry) { mutableStateOf("") }
            var observacion by remember(detalle.docEntry) { mutableStateOf("") }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clickable(enabled = decisionSeleccionada != null && !isProcesando) {
                        onConfirmarResolucion(decisionSeleccionada ?: "", motivoSeleccionado, causa, observacion)
                    },
                color = if (decisionSeleccionada != null && !isProcesando) Color(0xFF212121) else Color.Gray,
                shape = RoundedCornerShape(8.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = if (isProcesando) "Enviando..." else "Confirmar Resolución",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = errorMessage, color = Color(0xFF9E4B4B), fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Decisión",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                DecisionChip(
                    label = "APROBAR",
                    isSelected = decisionSeleccionada == "APROBADO",
                    color = Color(0xFF568057),
                    modifier = Modifier.weight(1f),
                    onClick = { decisionSeleccionada = "APROBADO" }
                )
                DecisionChip(
                    label = "RECHAZAR",
                    isSelected = decisionSeleccionada == "RECHAZADO",
                    color = Color(0xFF9E4B4B),
                    modifier = Modifier.weight(1f),
                    onClick = { decisionSeleccionada = "RECHAZADO" }
                )
                DecisionChip(
                    label = "NO CONFORME",
                    isSelected = decisionSeleccionada == "NO_CONFORME",
                    color = Color(0xFF6B5B95),
                    modifier = Modifier.weight(1f),
                    onClick = { decisionSeleccionada = "NO_CONFORME" }
                )
            }

            if (decisionSeleccionada == "RECHAZADO") {
                Spacer(modifier = Modifier.height(16.dp))
                Label(text = "MOTIVO")
                ExposedDropdownMenuBox(
                    expanded = motivoExpanded,
                    onExpandedChange = { motivoExpanded = it }
                ) {
                    OutlinedTextField(
                        value = motivoSeleccionado ?: "",
                        onValueChange = {},
                        readOnly = true,
                        placeholder = { Text("Selecciona un motivo", color = Color.LightGray) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = motivoExpanded) },
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Gray.copy(alpha = 0.8f),
                            cursorColor = Color.Black
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = motivoExpanded,
                        onDismissRequest = { motivoExpanded = false }
                    ) {
                        MOTIVOS_RECHAZO.forEach { motivo ->
                            DropdownMenuItem(
                                text = { Text(motivo) },
                                onClick = {
                                    motivoSeleccionado = motivo
                                    motivoExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Label(text = "CAUSA")
                OutlinedTextField(
                    value = causa,
                    onValueChange = { causa = it },
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.8f),
                        cursorColor = Color.Black
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Label(text = "OBSERVACIÓN")
            OutlinedTextField(
                value = observacion,
                onValueChange = { observacion = it },
                singleLine = false,
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Gray.copy(alpha = 0.8f),
                    cursorColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )
        }
        else -> {
            // Ya resuelto (aprobado/rechazado): no hay acciones disponibles
        }
    }
}

@Composable
private fun DecisionChip(
    label: String,
    isSelected: Boolean,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) color else Color.White,
        border = BorderStroke(1.dp, color)
    ) {
        Box(
            modifier = Modifier.padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                color = if (isSelected) Color.White else color,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
        }
    }
}

@Composable
private fun Label(text: String) {
    Text(
        text = text,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Gray,
        modifier = Modifier.padding(bottom = 6.dp)
    )
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
