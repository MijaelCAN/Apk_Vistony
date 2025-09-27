package com.vistony.app.Screen.ParadaMantenimiento

import android.app.Activity
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.vistony.app.Entidad.UserState
import com.vistony.app.Extras.convertirFecha
import com.vistony.app.Screen.Generic.CustomOutlinedTextField
import com.vistony.app.Screen.Generic.FilterBoxsRow
import com.vistony.app.Screen.Generic.Images.ImagePickerExample
import com.vistony.app.Screen.Generic.Recursos.Data
import com.vistony.app.Screen.Generic.TimeOutlinedTextField
import com.vistony.app.ViewModel.ActividadViewModel
import com.vistony.app.ui.theme.theme.Dimensions
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/*@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun DetalleActividad(
    actividad: Activity2?,
    onClose: () -> Unit, // Para manejar el botón cerrar
    userState: UserState
) {
    // ======================= VARIABLES DE CONFIGURATION =======================
    val context = LocalContext.current
    val activity = context as Activity
    val windowSize = calculateWindowSizeClass(context)
    val padding_res = Dimensions.getPadding(windowSize.widthSizeClass)
    val buttonHeight = Dimensions.getButtonHeight(windowSize.widthSizeClass)
    val bodyFontSize = Dimensions.getBodyFontSize(windowSize.widthSizeClass)


    if (actividad == null) return

    /*Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = "Detalle de Actividad",
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
        )
        Text(
            text = "Actividad registrada el ${actividad.startTime}",
            fontSize = 12.sp,
        )
        Spacer(Modifier.height(25.dp))


        // Encabezado con icono y usuario
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Usuario",
                modifier = Modifier.size(40.dp)
            )
            Spacer(Modifier.width(10.dp))
            Column {
                Text(
                    text = actividad.userName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Tecnico: ${actividad.userPosition}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))


    }*/
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {

        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = "Detalle de Actividad",
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
        )
        Text(
            text = "Actividad registrada el ${actividad.startTime}",
            fontSize = 12.sp,
        )
        Spacer(Modifier.height(25.dp))

        // Sección de imágenes (carousel)
        if (actividad.evidences.isNotEmpty()) {
            ImageCarousel(images = actividad.evidences)
            Spacer(Modifier.height(16.dp))
        } else {
            // Placeholder cuando no hay imágenes
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.LightGray.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.PhotoCamera,
                    contentDescription = "Sin imágenes",
                    modifier = Modifier.size(64.dp),
                    tint = Color.Gray
                )
            }
            Spacer(Modifier.height(16.dp))
        }

        // Información principal
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            // Encabezado con información del técnico
            TechnicianInfo(actividad)
            Spacer(Modifier.height(24.dp))

            // Información de la parada
            ParadaInfo(actividad)
            Spacer(Modifier.height(24.dp))

            // Detalles técnicos
            TechnicalDetails(actividad)
            Spacer(Modifier.height(24.dp))

            // Acciones realizadas
            ActionsTaken(actividad)
            Spacer(Modifier.height(24.dp))

            // Tiempos de la actividad
            TimeInfo(actividad)
            Spacer(Modifier.height(24.dp))

            // Observaciones
            Observations(actividad)
        }

        Spacer(modifier = Modifier.height(32.dp)) // En caso d eque no haya espacio suficiente
        Spacer(modifier = Modifier.weight(1f))
        Button(
            enabled = true,
            onClick = {
                //viewModel.crearActividad()
                onClose()
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

            Text(text = "Finalizar actividad", fontSize = bodyFontSize.sp)
        }
        Spacer(modifier = Modifier.height(32.dp))

    }
}

@Composable
fun ImageCarousel(images: List<Uri>) {
    var currentIndex by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            // Aquí implementarías un carousel real con pager
            // Por ahora mostramos la primera imagen
            AsyncImage(
                model = images.getOrNull(currentIndex),
                contentDescription = "Evidencia de mantenimiento",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Indicadores si hay múltiples imágenes
            if (images.size > 1) {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp)
                ) {
                    images.forEachIndexed { index, _ ->
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .padding(2.dp)
                                .background(
                                    color = if (index == currentIndex)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        Color.White.copy(alpha = 0.7f),
                                    shape = CircleShape
                                )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TechnicianInfo(actividad: Activity2) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Información del Técnico",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Usuario",
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        text = actividad.userName,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = actividad.userPosition,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Área: ${actividad.area}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun ParadaInfo(actividad: Activity2) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Información de la Parada",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(12.dp))

            InfoRow(
                icon = Icons.Filled.Build,
                title = "Máquina",
                value = actividad.machine.name
            )

            InfoRow(
                icon = Icons.Filled.Settings,
                title = "Equipo",
                value = actividad.equipment.name
            )

            InfoRow(
                icon = Icons.Filled.Warning,
                title = "Tipo de Falla",
                value = actividad.reason.name + (actividad.otherReason?.let { " - $it" } ?: "")
            )

            InfoRow(
                icon = Icons.Filled.Description,
                title = "Descripción",
                value = actividad.description.ifEmpty { "Sin descripción" }
            )
        }
    }
}

@Composable
fun TechnicalDetails(actividad: Activity2) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Detalles Técnicos",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(12.dp))

            // Aquí puedes agregar más detalles técnicos específicos
            InfoRow(
                icon = Icons.Filled.Assignment,
                title = "Documento SAP",
                value = actividad.paradaDocEntry.ifEmpty { "No registrado" }
            )

            // Puedes agregar más campos técnicos según necesites
        }
    }
}

@Composable
fun ActionsTaken(actividad: Activity2) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Acciones Realizadas",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = actividad.actionTaken.ifEmpty { "No se registraron acciones" },
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimeInfo(actividad: Activity2) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Tiempos de la Actividad",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(12.dp))

            /*val duration = Duration.between(actividad.startTime, actividad.endTime)
            val hours = duration.toHours()
            val minutes = duration.toMinutes() % 60*/

            actividad.startTime?.let {
                InfoRow(
                    icon = Icons.Filled.PlayArrow,
                    title = "Inicio",
                    value = it.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                )
            }

            actividad.endTime?.let {
                InfoRow(
                    icon = Icons.Filled.Stop,
                    title = "Fin",
                    value = it.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                )
            }

            /*InfoRow(
                icon = Icons.Filled.Schedule,
                title = "Duración",
                value = "${hours}h ${minutes}m"
            )*/
        }
    }
}

@Composable
fun Observations(actividad: Activity2) {
    if (actividad.observations.isNotEmpty()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Observaciones",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = actividad.observations,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun InfoRow(icon: ImageVector, title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            modifier = Modifier
                .size(20.dp)
                .padding(top = 2.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.width(12.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}*/
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DetalleActividad(
    viewModel: ActividadViewModel,
    //actividad: Activity2?,
    onClose: () -> Unit,
    userState: UserState
) {
    // ============================ ESTADO GENERAL DE LA UI ===========================
    val uiState by viewModel.uiState.collectAsState()
    //val images = remember { mutableStateListOf<Uri>() }
    val images = remember(uiState.selectedActividad.evidences) { uiState.selectedActividad.evidences }
    val actividad = uiState.selectedActividad

    // ======================= VARIABLES DE CONFIGURATION =======================
    val context = LocalContext.current
    val activity = context as Activity
    val windowSize = calculateWindowSizeClass(context)
    val padding_res = Dimensions.getPadding(windowSize.widthSizeClass)
    val buttonHeight = Dimensions.getButtonHeight(windowSize.widthSizeClass)
    val bodyFontSize = Dimensions.getBodyFontSize(windowSize.widthSizeClass)

    // ============================ LISTAS Y VARIABLES DE CONTROL  ============================
    val data = Data()
    val otherReason = rememberSaveable { mutableStateOf("") }

    if (actividad == null) return

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize()
            .verticalScroll(rememberScrollState())
    ) {
        // ======================= HEADER SECTION =======================
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = "Detalle de Actividad",
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
        )
        Text(
            text = "Actividad registrada el ${formatDateTime(actividad.startTime)}. Revise los detalles del mantenimiento realizado",
            fontSize = 12.sp,
            lineHeight = 14.sp,
        )
        Spacer(Modifier.height(25.dp))

        // ======================= INFORMACIÓN TÉCNICO =======================
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = "Información del Técnico",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
        )
        Spacer(Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Técnico",
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        text = actividad.userName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Posición: ${actividad.userPosition}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        Spacer(Modifier.height(8.dp))

        // ======================= INFORMACIÓN DE EQUIPOS =======================
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = "Información de Equipos",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
        )
        Spacer(Modifier.height(8.dp))

        // Área (simulando dropdown disabled)
        CustomOutlinedTextField(
            value = actividad.area,
            onValueChange = {},
            label = "Área",
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Área"
                )
            }
        )
        Spacer(Modifier.height(8.dp))
        CustomOutlinedTextField(
            value = actividad.machine?.name ?: "No especificada",
            onValueChange = {},
            label = "Máquina",
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.PrecisionManufacturing,
                    contentDescription = "Máquina"
                )
            }
        )
        Spacer(Modifier.height(8.dp))
        CustomOutlinedTextField(
            value = actividad.equipment?.name ?: "No especificado",
            onValueChange = {},
            label = "Equipo",
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Build,
                    contentDescription = "Equipo"
                )
            }
        )
        Spacer(Modifier.height(8.dp))

        // ======================= TIPO DE FALLA =======================
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = "Tipo de Falla",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
        )
        Spacer(Modifier.height(8.dp))
        FilterBoxsRow(
            options = data.optionsFalla,
            selectedOption = uiState.selectedActividad.reason,
            onOptionSelected = viewModel::onReasonChange,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        )
        if(uiState.selectedActividad.reason.name == "Otro"){
            Spacer(Modifier.height(8.dp))
            CustomOutlinedTextField(
                value = uiState.selectedActividad.reason.description.ifEmpty { otherReason.value },
                onValueChange = {otherReason.value = it},
                label = "Otro tipo de falla",
                readOnly = if(uiState.selectedActividad.endTime != null) true else false,
            )
        }

        Spacer(Modifier.height(8.dp))
        CustomOutlinedTextField(
            value = uiState.selectedActividad.description,
            onValueChange = viewModel::onDescriptionChange,
            label = "Descripción del Problema",
            readOnly = if(uiState.selectedActividad.endTime != null) true else false,
            minLines = 3,
            maxLines = 3
        )
        Spacer(Modifier.height(8.dp))
        CustomOutlinedTextField(
            value = uiState.selectedActividad.actionTaken,
            onValueChange = viewModel::onActionChange,
            label = "Acción realizada",
            readOnly = if(uiState.selectedActividad.endTime != null) true else false,
            minLines = 3,
            maxLines = 3
        )
        Spacer(Modifier.height(8.dp))
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = "Evidencias",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
        )
        Spacer(Modifier.height(8.dp))
        ImagePickerExample(
            images = images,
            onAddImage = {uri ->
                if(uiState.selectedActividad.endTime != null) return@ImagePickerExample
                viewModel.addImage(uri)
            },
            onRemoveImage = { uri ->
                if(uiState.selectedActividad.endTime != null) return@ImagePickerExample
                viewModel.removeImage(uri)
            },
            enabled = uiState.selectedActividad.endTime == null
        )

        Spacer(Modifier.height(8.dp))
        CustomOutlinedTextField(
            value = uiState.selectedActividad.observations,
            onValueChange = viewModel::onObservationChange,
            label = "Observaciones",
            readOnly = if(uiState.selectedActividad.endTime != null) true else false,
            minLines = 3,
            maxLines = 3
        )
        Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {

            TimeOutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                texto = "Hora Final",
                readonly = if(uiState.selectedActividad.endTime != null) true else false,
                selectedTime = uiState.selectedActividad.finalHour,
                onTimeChange = { newTime ->
                    Log.d("TIME", newTime.toString())
                    // Combinar fecha existente con la nueva hora
                    val currentDate = uiState.selectedActividad.finalHour

                    Log.e("Current Date", currentDate.toString())
                    //if (currentDate != null && newTime != null) {
                    if ( newTime != null) {
                        val combinedDateTime = LocalDateTime.now()
                            .withHour(newTime.hour)
                            .withMinute(newTime.minute)
                            .truncatedTo(ChronoUnit.SECONDS)
                        viewModel.onFinalHourChange(combinedDateTime)
                    }
                },
                showDialog = uiState.showDialogTimeFin,
                onShowDialogChange = {
                    if(uiState.selectedActividad.endTime != null) return@TimeOutlinedTextField
                    viewModel.onShowDialogTimeFinChange(it)
                }
            )
        }
        Spacer(Modifier.height(8.dp))
        CustomOutlinedTextField(
            value = uiState.selectedActividad.lineTec,
            onValueChange = viewModel::onLineTecChange,
            label = "Tecnico de linea",
            readOnly = if(uiState.selectedActividad.endTime != null) true else false,
        )

        // ======================= TIEMPO DE ACTIVIDAD =======================
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = "Información Temporal",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
        )
        Spacer(Modifier.height(8.dp))
        val initialHourString = actividad.initialHour?.toString()
        val horaFormateadaInicio = if (!initialHourString.isNullOrEmpty()) {
            val fechaHoraInicio = LocalDateTime.parse(initialHourString)
            val formato = DateTimeFormatter.ofPattern("yy/MM/dd - HH:mm")
            fechaHoraInicio.format(formato)
        } else {
            "Sin fecha"
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = "Inicio",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Inicio",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = horaFormateadaInicio,//formatTime(actividad.startTime),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.width(8.dp))
            val finalHourString = actividad.finalHour?.toString()
            val horaFormateadaFin = if (!finalHourString.isNullOrEmpty()) {
                val fechaHoraFin = LocalDateTime.parse(finalHourString)
                val formato = DateTimeFormatter.ofPattern("yy/MM/dd - HH:mm")
                fechaHoraFin.format(formato)
            } else {
                "Sin fecha"
            }

            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Fin",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Fin",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = horaFormateadaFin,//formatTime(actividad.finalHour),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        Spacer(Modifier.height(8.dp))

        // ======================= EVIDENCIAS =======================
        /*Text(
            modifier = Modifier.padding(top = 16.dp),
            text = "Evidencias",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
        )
        Spacer(Modifier.height(8.dp))

        if (actividad.evidences.isNotEmpty()) {
            ImageCarousel(images = actividad.evidences)
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.PhotoCamera,
                        contentDescription = "Sin evidencias",
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "No se agregaron evidencias",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        Spacer(Modifier.height(8.dp))

        // ======================= OBSERVACIONES =======================
        CustomOutlinedTextField(
            value = actividad.observations.ifBlank { "Sin observaciones adicionales" },
            onValueChange = {},
            label = "Observaciones",
            readOnly = true,
            minLines = 3,
            maxLines = 3
        )*/

        Spacer(modifier = Modifier.height(32.dp))
        Spacer(modifier = Modifier.weight(1f))

        // ======================= BOTÓN FINALIZAR =======================
        Button(
            enabled = if(uiState.selectedActividad.endTime != null) false else true,
            onClick = {
                viewModel.UpdateActividad(context, actividad.DocEntry, otherReason)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(buttonHeight)
                .padding(horizontal = padding_res)
                .clip(RoundedCornerShape(0.dp)),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                //containerColor = Color(0xFFFC6A68),
                containerColor = Color(0xFF01398D),
                contentColor = Color.White
            )
        ) {
            //Text(text = "Cerrar Detalle", fontSize = bodyFontSize.sp)
            if (uiState.isCreating) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Finalizando...",fontSize = bodyFontSize.sp)
            } else {
                Text(
                    text = if (uiState.selectedActividad.endTime != null) "Actividad Finalizada" else "Finalizar Actividad",
                    fontSize = bodyFontSize.sp
                )
            }
            if (uiState.createSuccess) {
                Log.d("ActividadViewModel", "Actividad finalizada con éxito")
                onClose()
                viewModel.resetCreateState()
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageCarousel(
    images: List<Uri>,
    modifier: Modifier = Modifier
) {
    if (images.isEmpty()) return

    val pagerState = rememberPagerState(pageCount = { images.size })

    Column(modifier = modifier) {
        // Carousel principal
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Box {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    AsyncImage(
                        model = images[page],
                        contentDescription = "Evidencia ${page + 1}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        error = painterResource(id = android.R.drawable.ic_menu_report_image),
                        placeholder = painterResource(id = android.R.drawable.ic_menu_gallery)
                    )
                }

                // Overlay con información de página
                if (images.size > 1) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp)
                            .background(
                                Color.Black.copy(alpha = 0.6f),
                                RoundedCornerShape(16.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "${pagerState.currentPage + 1} / ${images.size}",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Botones de navegación (solo si hay más de una imagen)
                if (images.size > 1) {
                    // Botón anterior
                    if (pagerState.currentPage > 0) {
                        IconButton(
                            onClick = {
                                // Navegar a la página anterior
                                // Note: En un contexto real necesitarías usar LaunchedEffect con coroutines
                            },
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(start = 8.dp)
                                .background(
                                    Color.Black.copy(alpha = 0.5f),
                                    CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChevronLeft,
                                contentDescription = "Anterior",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    // Botón siguiente
                    if (pagerState.currentPage < images.size - 1) {
                        IconButton(
                            onClick = {
                                // Navegar a la siguiente página
                                // Note: En un contexto real necesitarías usar LaunchedEffect con coroutines
                            },
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 8.dp)
                                .background(
                                    Color.Black.copy(alpha = 0.5f),
                                    CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = "Siguiente",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Indicadores de página (dots) - solo si hay más de una imagen
        if (images.size > 1) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(images.size) { index ->
                    val isSelected = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .size(if (isSelected) 10.dp else 8.dp)
                            .background(
                                color = if (isSelected)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                                shape = CircleShape
                            )
                            .animateContentSize()
                    )

                    if (index < images.size - 1) {
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Información adicional
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.PhotoLibrary,
                contentDescription = "Evidencias",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (images.size == 1) "1 evidencia adjuntada" else "${images.size} evidencias adjuntadas",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// ======================= VERSIÓN CON NAVEGACIÓN FUNCIONAL =======================
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageCarouselWithNavigation(
    images: List<String>,
    modifier: Modifier = Modifier
) {
    if (images.isEmpty()) return

    val pagerState = rememberPagerState(pageCount = { images.size })
    val scope = rememberCoroutineScope()

    Column(modifier = modifier) {
        // Carousel principal
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Box {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    AsyncImage(
                        model = images[page],
                        contentDescription = "Evidencia ${page + 1}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        error = painterResource(id = android.R.drawable.ic_menu_report_image),
                        placeholder = painterResource(id = android.R.drawable.ic_menu_gallery)
                    )
                }

                // Overlay con información de página
                if (images.size > 1) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp)
                            .background(
                                Color.Black.copy(alpha = 0.6f),
                                RoundedCornerShape(16.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "${pagerState.currentPage + 1} / ${images.size}",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Botones de navegación (solo si hay más de una imagen)
                if (images.size > 1) {
                    // Botón anterior
                    if (pagerState.currentPage > 0) {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                }
                            },
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(start = 8.dp)
                                .background(
                                    Color.Black.copy(alpha = 0.5f),
                                    CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChevronLeft,
                                contentDescription = "Anterior",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    // Botón siguiente
                    if (pagerState.currentPage < images.size - 1) {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            },
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 8.dp)
                                .background(
                                    Color.Black.copy(alpha = 0.5f),
                                    CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = "Siguiente",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Indicadores de página (dots) - solo si hay más de una imagen
        if (images.size > 1) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(images.size) { index ->
                    val isSelected = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .size(if (isSelected) 10.dp else 8.dp)
                            .background(
                                color = if (isSelected)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                                shape = CircleShape
                            )
                            .animateContentSize()
                            .clickable {
                                scope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            }
                    )

                    if (index < images.size - 1) {
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Información adicional
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.PhotoLibrary,
                contentDescription = "Evidencias",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (images.size == 1) "1 evidencia adjuntada" else "${images.size} evidencias adjuntadas",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// ======================= FUNCIONES AUXILIARES =======================
@RequiresApi(Build.VERSION_CODES.O)
private fun formatDateTime(dateTime: LocalDateTime?): String {
    if (dateTime != null) {
        return "${dateTime.dayOfMonth}/${dateTime.monthValue}/${dateTime.year} a las ${formatTime(dateTime)}"
    }
    return ""
}

@RequiresApi(Build.VERSION_CODES.O)
private fun formatTime(dateTime: LocalDateTime?): String {
    if (dateTime != null) {
        return String.format("%02d:%02d", dateTime.hour, dateTime.minute)
    }
    return ""
}