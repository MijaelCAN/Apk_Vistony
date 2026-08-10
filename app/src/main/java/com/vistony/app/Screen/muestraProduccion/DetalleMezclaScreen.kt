package com.vistony.app.Screen.muestraProduccion

import android.os.Build
import android.util.Log
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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vistony.app.Entidad.FinalizarAnalisisRequest
import com.vistony.app.Entidad.MuestraProduccionDetalle
import com.vistony.app.Entidad.MuestraProduccionTimelineItem
import com.vistony.app.Entidad.ParametroValorRequest
import com.vistony.app.Entidad.UserResponse
import com.vistony.app.ViewModel.CalculoDensidadUiState
import com.vistony.app.ViewModel.EnvacePesoInfo
import com.vistony.app.ViewModel.MuestraViewModel
import com.vistony.app.ui.theme.theme.AppTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetalleMezclaScreen(
    docEntry: String,
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
    val motivosRechazo by muestraViewModel.motivosRechazo.collectAsState()
    val calculoDensidad by muestraViewModel.calculoDensidad.collectAsState()
    val pendientesCount = muestrasProduccion.count { it.status == "PENDIENTE" }

    LaunchedEffect(docEntry) {
        muestraViewModel.obtenerMuestraProduccionDetalle(docEntry)
        Log.d("DetalleMezclaScreen", "LaunchedEffect: $docEntry")
    }

    LaunchedEffect(Unit) {
        muestraViewModel.cargarMotivosRechazo()
    }

    DetalleMezclaScreenContent(
        detalle = detalle,
        isLoading = isLoading,
        esCalidad = currentUser.role.equals("ASEG. CALIDAD", ignoreCase = true),
        isProcesando = isProcesando,
        errorMessage = errorMessage,
        pendientesCount = pendientesCount,
        motivosRechazo = motivosRechazo,
        calculoDensidad = calculoDensidad,
        modifier = modifier,
        onBackClick = onBackClick,
        onNotificationClick = onNotificationClick,
        onRefresh = { muestraViewModel.obtenerMuestraProduccionDetalle(docEntry) },
        onRegistrarMuestraClick = onRegistrarMuestraClick,
        onIniciarAnalisisClick = {
            muestraViewModel.iniciarAnalisis(docEntry, currentUser.dni)
        },
        onCalcularDensidadClick = { densidad ->
            // Se identifica la muestra por "lote", igual que hace hoy el formulario de
            // Manufacturing al recalcular densidad (ver INFORME_MODULO_LABORATORIO.md)
            val lote = detalle?.lote.orEmpty()
            muestraViewModel.calcularDensidad(lote, densidad)
        },
        onConfirmarResolucion = { decision, motivo, causa, observacion, densidad, seRealizoCorreccion, seRealizoReproceso ->
            val esRechazoONoConforme = decision == "RECHAZADO" || decision == "NO_CONFORME"
            // idParametro 1/2/3 son provisionales (pendiente catálogo real de parámetros).
            // Se replica lo que hacía el formulario de Manufacturing: además de la densidad
            // ingresada, también se envía el peso óptimo/máximo calculado (antes solo se mostraba).
            val parametros = buildList {
                if (densidad.isNotBlank()) add(ParametroValorRequest(idParametro = 1, valor = densidad))
                calculoDensidad.pesoOptimo?.let { add(ParametroValorRequest(idParametro = 2, valor = it)) }
                calculoDensidad.pesoMaximo?.let { add(ParametroValorRequest(idParametro = 3, valor = it)) }
            }
            val request = FinalizarAnalisisRequest(
                status = decision,
                userEndAnalysis = currentUser.dni,
                typeReject = if (esRechazoONoConforme) motivo else null,
                reason = if (esRechazoONoConforme) causa else null,
                observation = observacion.ifBlank { null },
                isCorrection = seRealizoCorreccion,
                isReprocess = seRealizoReproceso
                //parametros = parametros
            )
            muestraViewModel.finalizarAnalisis(docEntry, request)
        },
        onConfirmarRecepcionClick = {
            muestraViewModel.confirmarRecepcionMuestra(docEntry, currentUser.dni)
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
    motivosRechazo: List<String> = emptyList(),
    calculoDensidad: CalculoDensidadUiState = CalculoDensidadUiState(),
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onRefresh: () -> Unit = {},
    onRegistrarMuestraClick: () -> Unit = {},
    onIniciarAnalisisClick: () -> Unit = {},
    onCalcularDensidadClick: (String) -> Unit = {},
    onConfirmarResolucion: (decision: String, motivo: String?, causa: String, observacion: String, densidad: String, seRealizoCorreccion: Boolean, seRealizoReproceso: Boolean) -> Unit = { _, _, _, _, _, _, _ -> },
    onConfirmarRecepcionClick: () -> Unit = {}
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
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
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

                SectionHeader(title = "Intentos de ${when (detalle.type) {
                    "ENVASADO_INICIO" -> "ENVASADO-INICIO"
                    "ENVASADO_FIN"    -> "ENVASADO-FIN"
                    else              -> detalle.type
                }}")

                Column(modifier = Modifier.padding(start = 4.dp, top = 16.dp)) {
                    detalle.timeline.forEachIndexed { index, item ->
                        MuestraTimelineItem(
                            item = item,
                            isLast = index == detalle.timeline.lastIndex
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                SectionHeader(title = " ")

                Spacer(modifier = Modifier.height(16.dp))

                /*Card(
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

                Spacer(modifier = Modifier.height(32.dp))*/

                if (esCalidad) {
                    AccionesCalidad(
                        detalle = detalle,
                        isProcesando = isProcesando,
                        errorMessage = errorMessage,
                        motivosRechazo = motivosRechazo,
                        calculoDensidad = calculoDensidad,
                        onIniciarAnalisisClick = onIniciarAnalisisClick,
                        onCalcularDensidadClick = onCalcularDensidadClick,
                        onConfirmarResolucion = onConfirmarResolucion
                    )
                } else {
                    val siguienteTipo = detalle.siguienteTipoMuestra()
                    val tipoLabel = when (siguienteTipo) {
                        "ENVASADO_INICIO" -> "ENVASADO-INICIO"
                        "ENVASADO_FIN"    -> "ENVASADO-FIN"
                        else              -> siguienteTipo
                    }

                    // El backend aún no expone la fecha de confirmación de recepción en el detalle,
                    // así que por ahora se simula localmente: hasta que en esta pantalla se presione
                    // "Confirmar Recepción", no se habilita registrar la siguiente muestra ENVASADO.
                    var recepcionConfirmada by remember(detalle.docEntry) { mutableStateOf(false) }
                    val requiereConfirmarRecepcion = siguienteTipo == "ENVASADO_INICIO" || siguienteTipo == "ENVASADO_FIN"

                    val bloqueado = detalle.status == "PENDIENTE" || detalle.status == "EN_ANALISIS" ||
                        (requiereConfirmarRecepcion && !recepcionConfirmada)
                    val mensajeBloqueado = when {
                        detalle.status == "PENDIENTE" -> "Muestra pendiente de análisis en Laboratorio. Solo puedes registrar un nuevo intento cuando sea rechazada."
                        detalle.status == "EN_ANALISIS" -> "Muestra en análisis por Laboratorio. Solo puedes registrar un nuevo intento cuando sea rechazada."
                        requiereConfirmarRecepcion && !recepcionConfirmada -> "Debes confirmar la recepción antes de registrar la muestra $tipoLabel."
                        else -> null
                    }
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clickable(enabled = !bloqueado) { onRegistrarMuestraClick() },
                        color = if (bloqueado) Color.Gray else Color(0xFF212121),
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
                    if (mensajeBloqueado != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = mensajeBloqueado,
                            color = Color(0xFF8B7A4D),
                            fontSize = 13.sp
                        )
                    }

                    val habilitaConfirmarRecepcion = detalle.timeline.any { it.status == "APROBADO" || it.status == "NO_CONFORME" }
                        || detalle.status == "APROBADO" || detalle.status == "NO_CONFORME"
                    if (habilitaConfirmarRecepcion && !recepcionConfirmada) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .clickable(enabled = !isProcesando) {
                                    recepcionConfirmada = true
                                    onConfirmarRecepcionClick()
                                },
                            color = if (isProcesando) Color.Gray else Color(0xFF1B5E20),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = if (isProcesando) "Confirmando..." else "Confirmar Recepción",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
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

// Flujo de etapas: MEZCLA (aprobada) → ENVASADO_INICIO (aprobada) → ENVASADO_FIN
// El API puede devolver distintas representaciones del tipo (con guion, espacio, o
// nombres cortos como "Envase"). Se mapea a la forma canónica antes de evaluar el flujo.
fun MuestraProduccionDetalle.siguienteTipoMuestra(): String {
    val estaAprobada = isFinish.equals("Y", ignoreCase = true)
        || isFinish.equals("true", ignoreCase = true)
        || isFinish == "1"
        || status.equals("APROBADO", ignoreCase = true)
    val tipoCanonico = tipoMuestraCanonico(type)
    return when {
        estaAprobada && tipoCanonico == "MEZCLA"          -> "ENVASADO_INICIO"
        estaAprobada && tipoCanonico == "ENVASADO_INICIO" -> "ENVASADO_FIN"
        else                                               -> tipoCanonico
    }
}

// Normaliza el campo "type" (puede llegar con guion, espacio, o nombres cortos como "Envase")
// a su forma canónica: MEZCLA | ENVASADO_INICIO | ENVASADO_FIN
fun tipoMuestraCanonico(type: String): String {
    val tipoNorm = type.trim().replace("-", "_").replace(" ", "_").uppercase()
    return when (tipoNorm) {
        "MEZCLA"                           -> "MEZCLA"
        "ENVASADO_INICIO", "ENVASE_INICIO",
        "ENVASE"                           -> "ENVASADO_INICIO"
        "ENVASADO_FIN", "ENVASE_FIN"       -> "ENVASADO_FIN"
        else                               -> tipoNorm
    }
}

// Muestras de tipo ENVASADO (inicio o fin) ya no miden densidad manualmente en esta pantalla:
// el backend calcula y devuelve densidad/pesoOptimo/pesoMaximo directamente en el detalle
// (calculados en la etapa de MEZCLA), por lo que solo se muestran de forma informativa.
fun esTipoEnvasado(type: String): Boolean {
    val canonico = tipoMuestraCanonico(type)
    return canonico == "ENVASADO_INICIO" || canonico == "ENVASADO_FIN"
}

// La primera versión de cualquier tipo de muestra (Mezcla, Envasado-Inicio, Envasado-Fin)
// nunca puede ser una corrección: recién ahí no hay nada previo que corregir. Esto solo habilita
// la pregunta "¿Se ha realizado corrección?" — NO permite asumir que toda versión > .0 SÍ lo fue:
// un segundo (o tercer) intento puede deberse a un simple reproceso (más centrifugado, más
// agitación...) sin que se haya corregido nada. Esa respuesta solo la sabe Calidad al finalizar
// el análisis de ese intento (ver el check en AccionesCalidad), por eso el timeline debe mostrar
// el valor ya confirmado (`item.isCorrection`) y nunca inferirlo a partir de la versión.
fun esPrimeraVersion(version: String): Boolean = version.substringAfterLast('.', "0") == "0"

// Productos cuya muestra no requiere medir densidad (no aplica el cálculo de peso óptimo/máximo)
private val PRODUCTOS_SIN_DENSIDAD = listOf(
    "GRASA", "LEJIA", "LEJÍA", "LAVAVAJILLA", "ADITIVO HIGHT"
)

private fun requiereDensidad(descripcion: String): Boolean {
    val descripcionNormalizada = descripcion.uppercase()
    return PRODUCTOS_SIN_DENSIDAD.none { descripcionNormalizada.contains(it) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccionesCalidad(
    detalle: MuestraProduccionDetalle,
    isProcesando: Boolean,
    errorMessage: String?,
    motivosRechazo: List<String>,
    calculoDensidad: CalculoDensidadUiState,
    onIniciarAnalisisClick: () -> Unit,
    onCalcularDensidadClick: (String) -> Unit,
    onConfirmarResolucion: (decision: String, motivo: String?, causa: String, observacion: String, densidad: String, seRealizoCorreccion: Boolean, seRealizoReproceso: Boolean) -> Unit
) {
    val analisisIniciado = !detalle.dateStartAnalysis.isNullOrBlank()
    val analisisFinalizado = !detalle.dateEndAnalysis.isNullOrBlank()
    val densidadRequerida = requiereDensidad(detalle.descripcion)
    val esEnvasado = esTipoEnvasado(detalle.type)
    val primeraVersion = esPrimeraVersion(detalle.version)

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
            var densidad by remember(detalle.docEntry) { mutableStateOf("") }
            var seRealizoCorreccion by remember(detalle.docEntry) { mutableStateOf(false) }
            var seRealizoReproceso by remember(detalle.docEntry) { mutableStateOf(false) }

            val puedeConfirmar = decisionSeleccionada != null && !isProcesando

            if (densidadRequerida && esEnvasado) {
                // Para ENVASADO (inicio/fin) la densidad y los pesos óptimo/máximo ya vienen
                // calculados por el backend en el propio detalle (calculados en la etapa de
                // MEZCLA): no se pide densidad manual ni se muestra el botón CALCULAR.
                Label(text = "DENSIDAD MEDIDA")
                Text(
                    text = detalle.densidad?.takeIf { it.isNotBlank() } ?: "-",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    Column {
                        Label(text = "PESO ÓPTIMO")
                        Text(
                            text = detalle.pesoOptimo?.takeIf { it.isNotBlank() } ?: "-",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Column {
                        Label(text = "PESO MÁXIMO")
                        Text(
                            text = detalle.pesoMaximo?.takeIf { it.isNotBlank() } ?: "-",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            } else if (densidadRequerida) {
                Label(text = "DENSIDAD MEDIDA")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = densidad,
                        onValueChange = { densidad = it },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Gray.copy(alpha = 0.8f),
                            cursorColor = Color.Black
                        )
                    )
                    Surface(
                        modifier = Modifier
                            .height(56.dp)
                            .clickable(enabled = densidad.isNotBlank() && !calculoDensidad.isLoading) {
                                onCalcularDensidadClick(densidad)
                            },
                        color = Color(0xFF212121),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Box(modifier = Modifier.padding(horizontal = 16.dp), contentAlignment = Alignment.Center) {
                            Text(
                                text = if (calculoDensidad.isLoading) "Calculando..." else "CALCULAR",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                if (calculoDensidad.error != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = calculoDensidad.error, color = Color(0xFF9E4B4B), fontSize = 13.sp)
                }

                if (calculoDensidad.calculado) {
                    Spacer(modifier = Modifier.height(12.dp))
                    if (calculoDensidad.envaces.isNotEmpty()) {
                        calculoDensidad.envaces.forEachIndexed { index, envace ->
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "Lote ${envace.lote}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Gray
                                )
                                Text(
                                    text = envace.descripcion,
                                    fontSize = 12.sp,
                                    color = Color.Gray,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                                    Column {
                                        Label(text = "PESO ÓPTIMO")
                                        Text(
                                            text = envace.pesoOptimo.ifBlank { "-" },
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Column {
                                        Label(text = "PESO MÁXIMO")
                                        Text(
                                            text = envace.pesoMaximo.ifBlank { "-" },
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                            if (index < calculoDensidad.envaces.lastIndex) {
                                Spacer(modifier = Modifier.height(8.dp))
                                HorizontalDivider(color = Color.Gray.copy(alpha = 0.3f))
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    } else {
                        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                            Column {
                                Label(text = "PESO ÓPTIMO")
                                Text(text = calculoDensidad.pesoOptimo ?: "-", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                            Column {
                                Label(text = "PESO MÁXIMO")
                                Text(text = calculoDensidad.pesoMaximo ?: "-", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            } else {
                Label(text = "DENSIDAD MEDIDA")
                Text(
                    text = "Este producto no requiere medición de densidad",
                    color = Color.Gray,
                    fontSize = 13.sp,
                    fontStyle = FontStyle.Italic
                )
            }

            if (!primeraVersion) {
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { seRealizoCorreccion = !seRealizoCorreccion }
                ) {
                    Checkbox(
                        checked = seRealizoCorreccion,
                        onCheckedChange = { seRealizoCorreccion = it }
                    )
                    Text(
                        text = "¿Se ha realizado corrección?",
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { seRealizoReproceso = !seRealizoReproceso }
                ) {
                    Checkbox(
                        checked = seRealizoReproceso,
                        onCheckedChange = { seRealizoReproceso = it }
                    )
                    Text(
                        text = "¿Se ha realizado reproceso?",
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                }
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

            if (decisionSeleccionada == "RECHAZADO" || decisionSeleccionada == "NO_CONFORME") {
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
                        motivosRechazo.forEach { motivo ->
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

            Spacer(modifier = Modifier.height(24.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clickable(enabled = puedeConfirmar) {
                        onConfirmarResolucion(
                            decisionSeleccionada ?: "", motivoSeleccionado, causa, observacion,
                            densidad, seRealizoCorreccion, seRealizoReproceso
                        )
                    },
                color = if (puedeConfirmar) Color(0xFF212121) else Color.Gray,
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
                    color = Color.DarkGray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                MuestraStatusBadge(status = detalle.status)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Lote ${detalle.lote}", // Mocking extended details from image
                color = Color.Gray,
                fontSize = 14.sp
            )
            Text(
                text = "Inicio: ${formatFechaHora(detalle.dateRegister)} · Resp. ${detalle.userRegister}",
                color = Color.Gray,
                fontSize = 14.sp
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
    val dotColor = if (item.esCurrent == "Y") Color.Black else statusColor
    
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
                    .background(if (item.esCurrent == "Y") dotColor else Color.Transparent, CircleShape)
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
                if (item.isCorrection == true) {
                    Spacer(modifier = Modifier.width(6.dp))
                    TimelineTag(text = "CORRECCIÓN", color = Color(0xFF6B5B95))
                }
                if (item.isReprocess == true) {
                    Spacer(modifier = Modifier.width(6.dp))
                    TimelineTag(text = "REPROCESO", color = Color(0xFF8B7A4D))
                }
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

@Composable
private fun TimelineTag(text: String, color: Color) {
    Surface(
        color = color.copy(alpha = 0.12f),
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(1.dp, color.copy(alpha = 0.4f))
    ) {
        Text(
            text = text,
            color = color,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
        )
    }
}

private fun timelineStatusColor(status: String): Color = when (status) {
    "RECHAZADO" -> Color(0xFF9E4B4B)
    "NO_CONFORME" -> Color(0xFF9E4B4B)
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
        "NO_CONFORME" -> Color(0xFFF9E0E0) to Color(0xFF9E4B4B)
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

/*@RequiresApi(Build.VERSION_CODES.O)
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
}*/
