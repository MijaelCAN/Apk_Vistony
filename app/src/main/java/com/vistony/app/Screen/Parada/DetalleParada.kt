package com.vistony.app.Screen.Parada

import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vistony.app.Entidad.Actividad
import com.vistony.app.Entidad.ListaRequest
import com.vistony.app.Entidad.Parada
import com.vistony.app.R
import com.vistony.app.Screen.Generic.CustomAlertDialog
import com.vistony.app.Screen.Generic.DialogType
import com.vistony.app.Screen.Generic.GenericDropdownMenu
import com.vistony.app.Screen.LoginScreen
import com.vistony.app.Screen.ParadaMantenimiento.TarjetaActividad
import com.vistony.app.ViewModel.ActividadViewModel
import com.vistony.app.ViewModel.EstadoParada
import com.vistony.app.ViewModel.ParadaViewModel
import com.vistony.app.ui.theme.theme.Dimensions
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetalleParada(
    id: String,
    role: String?,
    parada: Parada?,
    paradaViewModel: ParadaViewModel,
    actividadViewModel: ActividadViewModel,
    actividades : List<Actividad>,
    onClose: (String) -> Unit,
    onOpenActivity: () -> Unit
){
    val context = LocalContext.current
    val activity = context as Activity
    val windowSize = calculateWindowSizeClass(context)
    val padding_res = Dimensions.getPadding(windowSize.widthSizeClass)
    val buttonHeight = Dimensions.getButtonHeight(windowSize.widthSizeClass)
    val bodyFontSize = Dimensions.getBodyFontSize(windowSize.widthSizeClass)

    var showDialog by remember { mutableStateOf(false) }
    val estado by paradaViewModel.estadoParada.collectAsState()
    var statusButton by remember { mutableStateOf(true) }


    var selectedDateIni = paradaViewModel.fechaIni.value?.toLocalDate()?.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
    var selectedDateFin = paradaViewModel.fechaFin.value?.toLocalDate()?.format(DateTimeFormatter.ofPattern("yyyyMMdd"))

    var tecnico by remember { mutableStateOf(parada?.Usuario ?: "") }
    val expandedTecnico = remember { mutableStateOf(false) }
    val listTecnicos = listOf(
        "1" to "Junior A.",
        "2" to "Jhosep B.",
        "3" to "Victor L.",
        "4" to "Hercules H.",
        "5" to "Julio C.",
        "6" to "Ruben V.",
        "7" to "Jose M.",
        "8" to "Wilmer U.",
        "9" to "Jesus A.",
    )

    Log.d("TAG", "DetalleParada: $parada")

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = "Detalle de Parada",
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
        )
        Text(
            text = "Se detuvo la máquina por ${parada?.Motivo}",
            fontSize = 12.sp,
        )
        Spacer(Modifier.height(25.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(4.dp)
        ) {
            LiveIndicator(
                isActive = parada?.FechaHoraFin.isNullOrEmpty(),
                inactiveColor = if(parada?.FechaHoraFin.isNullOrEmpty()) Color(0xFF4CAF50).copy(alpha = 0.3f) else Color(0xFFB00020),
            )
            Spacer(modifier = Modifier.width(8.dp)) // espacio entre punto y texto
            Text(
                text = if (parada?.FechaHoraFin.isNullOrEmpty()) "En curso" else "Finalizado",
                color = if (parada?.FechaHoraFin.isNullOrEmpty()) Color(0xFF4CAF50) else Color(0xFF1B2733),
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
        }

        Spacer(Modifier.height(16.dp))

        if (parada == null) return
        // Formato del backend: "2025-10-15 1013" -> "yyyy-MM-dd Hmm"
        val formatterBackend = DateTimeFormatter.ofPattern("yyyy-MM-dd Hmm")
        // Formato para mostrar: "15-10-2025 08:21"
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")

        Box(modifier = Modifier.clip(RoundedCornerShape(20.dp))){
            Image(
                painter = painterResource(id = R.drawable.not_connection2),
                contentDescription = "background",
                modifier = Modifier
                    .fillMaxWidth()
                    .matchParentSize()
                    //.height(250.dp)
                    .blur(7.dp),
                contentScale = ContentScale.Crop,
            )
            Card(
                modifier = Modifier.padding(4.dp),
                shape = RoundedCornerShape(20.dp),
                backgroundColor = Color(0x7DFFFFFF),
                border = BorderStroke(1.dp, color = Color(0x25FFFFFF)),
                elevation = 0.dp,
            ) {
                Box(
                    modifier = Modifier
                        .alpha(1f)
                        .blur(radius = 28.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                        .background(
                            Brush.radialGradient(
                                listOf(
                                    Color(0x12FFFFFF),
                                    Color(0xDFFFFFF),
                                    Color(0x9FFFFFFF)

                                ),
                                radius = 2200f,
                                center = Offset.Infinite
                            )
                        )
                )
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    TiempoTranscurrido(
                        fechaInicio = LocalDateTime.parse(parada.FechaHoraInicio, formatterBackend),
                        fechaFin = if (parada.FechaHoraFin.isNullOrBlank()) null else LocalDateTime.parse(
                            parada.FechaHoraFin,
                            formatterBackend
                        )
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = parada.Maquina ?: "-")
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(25.dp))
        Divider(
            color = Color(0xFFE0E0E0),
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        InfoRow("Área:", parada.Area ?: "-")
        InfoRow("Coment.:", parada.Comentario ?: "-")
        InfoRow("Inicio:", try {
            LocalDateTime.parse(parada.FechaHoraInicio, formatterBackend).format(formatter)
        } catch (e: Exception) {
            parada.FechaHoraInicio ?: "-"
        })
        InfoRow("Fin:", if (parada.FechaHoraFin.isNullOrBlank()) "En curso" else try {
            LocalDateTime.parse(parada.FechaHoraFin, formatterBackend).format(formatter)
        } catch (e: Exception) {
            parada.FechaHoraFin ?: "En curso"
        })

        val listaFiltrada = actividades.filter { it.paradaDocEntry == parada.DocEntry }
        if (role == "mantenimiento") {

            val actividadesPares = listaFiltrada.chunked(2) // Divide la lista en grupos de 2
            statusButton = listaFiltrada.all { it.statusActividad } && tecnico.isNotEmpty()

            if(actividadesPares.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Box(
                        modifier = Modifier
                            .padding(top = 2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                //modifier = Modifier.padding(top = 16.dp),
                                text = "${listaFiltrada.size} Actividades",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                lineHeight = 14.sp,
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            IconButton(onClick = { onOpenActivity() }) {
                                Icon(
                                    imageVector = Icons.Rounded.AddCircle,
                                    contentDescription = null,
                                    tint = Color.LightGray
                                )
                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .clickable(
                                onClick = { },
                                indication = null, // Sin ripple
                                interactionSource = remember { MutableInteractionSource() } // Para quitar ripple
                            )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Ver Todo",
                                fontSize = 12.sp,
                                lineHeight = 14.sp,
                                color = Color.Unspecified // o el color que quieras
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = Color.LightGray
                            )
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
            }
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(actividadesPares) { par ->
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.width(350.dp) // Ajusta el ancho según tu diseño
                    ) {
                        par.forEach { actividad ->
                            TarjetaActividad(
                                actividad = actividad,
                                actividadViewModel = actividadViewModel,
                                id = id,
                                function = {
                                    // función para actividad
                                }
                            )
                        }
                        if (par.size == 1) {
                            Spacer(modifier = Modifier.height( /* altura aproximada de TarjetaActividad */ 100.dp))
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))

            if(actividadesPares.isEmpty()){
                GenericDropdownMenu(
                    modifier = Modifier.padding(horizontal = padding_res),
                    label = "Técnico mantenimiento",
                    options = listTecnicos,
                    selectedValue = tecnico,
                    onValueChange = { tecnico = it },
                    onCodeChange = { },
                    expanded = expandedTecnico.value,
                    onExpandedChange = { expandedTecnico.value = it }
                )
                Spacer(Modifier.height(8.dp))
            }
        }


        if(role == "operador") Spacer(modifier = Modifier.weight(1f))

        if(parada?.FechaHoraFin.isNullOrEmpty() && listaFiltrada.isEmpty()){
            Button(
                enabled = statusButton ,
                onClick = {
                    if(role == "mantenimiento") {
                        if(listaFiltrada.isEmpty()){
                            parada.Usuario = tecnico
                            onClose("Asignados")
                        }else{
                            // AQUI DEBE LLAMAR A LA API DE CERRAR ACTIVIDAD, ASIMISMO CREAR UN PDF EXPORTABLE
                        }
                    }
                    Log.d("Role", "Rol del usuario: $role")
                    if(role?.lowercase() == "producción") {
                        paradaViewModel.detenerParada(Integer.parseInt(parada.DocEntry))
                        showDialog = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(buttonHeight)
                    .padding(horizontal = padding_res)
                    .clip(RoundedCornerShape(0.dp)),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFC6A68),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = if(role == "mantenimiento") if(listaFiltrada.isEmpty())"Asignarme" else "Cerrar actividad y genear PDF" else "Detener",
                    fontSize = bodyFontSize.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
    }

    if (showDialog) {
        when (estado) {
            EstadoParada.Cargando ->
                CustomAlertDialog(
                    showDialog = true,
                    title = "Cargando",
                    message = "Espere por favor...",
                    confirmButtonText = "",
                    dismissButtonText = null,
                    onDismiss = { showDialog = false },
                    dialogType = DialogType.LOADING
                )

            is EstadoParada.Error -> TODO()
            EstadoParada.Exitoso -> {
                CustomAlertDialog(
                    showDialog = true,
                    title = "Exitoso",
                    message = "Parada finalizada",
                    confirmButtonText = "Aceptar",
                    dismissButtonText = null,
                    onDismiss = {
                        showDialog = false
                        paradaViewModel.obtenerParadas(
                            ListaRequest(
                                selectedDateIni,
                                selectedDateFin,
                                "T",
                                id
                            ))
                        onClose("Todos")
                    },
                    dialogType = DialogType.SUCCESS
                )
            }
            EstadoParada.Idle -> TODO()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TiempoTranscurrido(fechaInicio: LocalDateTime, fechaFin: LocalDateTime?) {
    var tiempo by remember { mutableStateOf(Duration.ZERO) }

    // Actualizar cada segundo si no hay fechaFin
    LaunchedEffect(fechaFin, fechaInicio) {
        while (true) {
            val fin = fechaFin ?: LocalDateTime.now()
            tiempo = Duration.between(fechaInicio, fin)
            if (fechaFin != null) break
            delay(1000L)
        }
    }

    val dias = tiempo.toDays()
    val horas = tiempo.toHours() % 24
    val minutos = tiempo.toMinutes() % 60

    val texto = when {
        dias > 0 -> "$dias días, $horas horas, $minutos minutos"
        horas > 0 -> "$horas horas, $minutos minutos"
        else -> "$minutos minutos"
    }

    Text(
        text = texto,
        fontSize = 32.sp,
        color = Color.Black,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
fun LiveIndicator(
    isActive: Boolean,
    activeColor: Color = Color(0xFF4CAF50),
    inactiveColor: Color = activeColor.copy(alpha = 0.3f),
    size: Dp = 12.dp,
    blinkDurationMs: Long = 500L
) {
    var isOn by remember { mutableStateOf(true) }

    if (isActive) {
        LaunchedEffect(Unit) {
            while (true) {
                isOn = !isOn
                delay(blinkDurationMs)
            }
        }
    } else {
        isOn = false
    }

    Box(
        modifier = Modifier
            .size(size)
            .background(
                color = if (isActive && isOn) activeColor else inactiveColor,
                shape = CircleShape
            )
    )
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Color.Black
        )
        Text(
            text = value,
            fontWeight = FontWeight.Light,
            fontSize = 14.sp,
            color = if(value == "En curso") Color(0xFF4CAF50).copy(alpha = 0.3f) else Color.Gray
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
    Divider(
        color = Color(0xFFE0E0E0),
        thickness = 1.dp,
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(8.dp))
}