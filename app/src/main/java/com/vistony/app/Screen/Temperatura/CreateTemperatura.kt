package com.vistony.app.Screen.Temperatura

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vistony.app.Screen.Generic.DateOutlinedTextField
import com.vistony.app.Screen.Generic.TemperatureButton
import com.vistony.app.Screen.Generic.TemperatureCard
import com.vistony.app.Screen.Generic.TemperatureHeader
import com.vistony.app.Screen.Generic.TemperatureSectionTitle
import com.vistony.app.Screen.Generic.TemperatureTextField
import com.vistony.app.Screen.Generic.TimeOutlinedTextField
import com.vistony.app.ViewModel.TemperaturaViewModel
import com.vistony.app.ui.theme.theme.Dimensions
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateTemperatura(
    onNavigateBack: () -> Unit,
    onNavigateToSuccess: () -> Unit,
    temperaturaViewModel: TemperaturaViewModel,
    currentUser: com.vistony.app.Entidad.UserResponse
) {
    val context = LocalContext.current
    val activity = context as Activity
    val windowSize = calculateWindowSizeClass(context)
    val padding_res = Dimensions.getPadding(windowSize.widthSizeClass)
    val buttonHeight = Dimensions.getButtonHeight(windowSize.widthSizeClass)

    val formState by temperaturaViewModel.formState.collectAsState()
    val isCreating by temperaturaViewModel.isCreating.collectAsState()
    val isConsultingOT by temperaturaViewModel.isConsultingOT.collectAsState()
    val successMessage by temperaturaViewModel.successMessage.collectAsState()
    val errorMessage by temperaturaViewModel.errorMessage.collectAsState()

    // Llenar el campo auxiliar con el nombre del usuario al iniciar
    LaunchedEffect(currentUser) {
        /*if (formState.auxiliar.isEmpty() && currentUser.name.isNotEmpty()) {
            temperaturaViewModel.updateAuxiliar(currentUser.name)
        }*/
    }

    // Navegar a éxito cuando se registre exitosamente
    LaunchedEffect(successMessage) {
        successMessage?.let { message ->
            if (message.isNotEmpty()) {
                onNavigateToSuccess()
                // Limpiar el mensaje después de navegar para evitar navegación automática al re-entrar
                kotlinx.coroutines.delay(100)
                temperaturaViewModel.clearMessages()
            }
        }
    }

    // Mostrar errores en un Snackbar
    var snackbarHostState by remember { mutableStateOf(SnackbarHostState()) }

    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            if (message.isNotEmpty()) {
                snackbarHostState.showSnackbar(
                    message = message,
                    duration = SnackbarDuration.Long
                )
                // Limpiar el mensaje después de mostrarlo
                kotlinx.coroutines.delay(1000)
                temperaturaViewModel.clearMessages()
            }
        }
    }

    Box(
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
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header con gradiente
            TemperatureHeader(
                title = "Registrar Temperatura",
                subtitle = "Control de temperatura",
                actionButton = {
                    IconButton(
                        onClick = onNavigateBack,
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
                },
                modifier = Modifier.padding(horizontal = padding_res, vertical = 16.dp)
            )

            // Formulario
            TemperatureCard(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = padding_res, vertical = 16.dp),
                elevation = 8.dp,
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Título del formulario
                    TemperatureSectionTitle(
                        title = "Información del Registro",
                        subtitle = "Complete todos los campos requeridos",
                        icon = Icons.Default.Thermostat
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Campos del formulario
                    TemperatureFormFields(
                        formState = formState,
                        isConsultingOT = isConsultingOT,
                        onFechaChange = temperaturaViewModel::updateFecha,
                        onOTChange = temperaturaViewModel::updateOT,
                        onDescripcionChange = {},//temperaturaViewModel::updateDescripcion,
                        onHoraChange = temperaturaViewModel::updateHora,
                        onTemperaturaChange = temperaturaViewModel::updateTemperatura,
                        onAuxiliarChange = temperaturaViewModel::updateAuxiliar,
                        onObservacionesChange = temperaturaViewModel::updateObservaciones,
                        onProductoSeleccionado = temperaturaViewModel::seleccionarProducto
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Botón de registro
                    TemperatureButton(
                        text = if (isCreating) "Registrando..." else "Registrar Temperatura",
                        onClick = { temperaturaViewModel.registrarTemperatura(currentUser.dni) },
                        enabled = formState.isFormValid && !isCreating,
                        isLoading = isCreating,
                        icon = Icons.Default.Thermostat,
                        buttonHeight = buttonHeight
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // Snackbar para mostrar errores
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    //.align(Alignment.BottomCenter)
                    .padding(16.dp)
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TemperatureFormFields(
    formState: com.vistony.app.ViewModel.TemperaturaViewModel.TemperaturaFormState,
    isConsultingOT: Boolean = false,
    onFechaChange: (String) -> Unit,
    onOTChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onHoraChange: (String) -> Unit,
    onTemperaturaChange: (String) -> Unit,
    onAuxiliarChange: (String) -> Unit,
    onObservacionesChange: (String) -> Unit,
    onProductoSeleccionado: (com.vistony.app.Entidad.ConsultaOTItem) -> Unit
) {
    // Fecha
    DateOutlinedTextField(
        selectedDate = LocalDateTime.now(),
        onDateChange = { date ->
            onFechaChange(date?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) ?: "")
        },
        showDialog = false,
        onShowDialogChange = {},
        texto = "Fecha de Registro",
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(16.dp))

    // OT
    TemperatureTextField(
        value = formState.ot,
        onValueChange = onOTChange,
        label = "Número de OT",
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Work,
                contentDescription = null,
                tint = Color(0xFFA0AEC0),
                modifier = Modifier.size(20.dp)
            )
        },
        trailingIcon = {
            if (isConsultingOT) {
                CircularProgressIndicator(
                    color = Color(0xFF667EEA),
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            } else if (formState.ot.isNotEmpty() && formState.descripcion.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Producto cargado",
                    tint = Color(0xFF10B981),
                    modifier = Modifier.size(20.dp)
                )
            }
        },
        keyboardType = KeyboardType.Text,
        enabled = !isConsultingOT
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Descripción del producto
    if (formState.descripcion.isEmpty()) {
        // No hay productos aún
        TemperatureTextField(
            value = "Ingrese el número de OT",
            onValueChange = {},
            label = "Descripción de la OT",
            readOnly = true,
            enabled = false,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = null,
                    tint = Color(0xFFA0AEC0),
                    modifier = Modifier.size(20.dp)
                )
            }
        )
    } else if (formState.descripcion.size == 1 && formState.productoSeleccionado != null) {
        // Hay solo un producto y está seleccionado
        TemperatureTextField(
            value = formState.productoSeleccionado!!.producto,
            onValueChange = {},
            label = "Descripción de la OT",
            readOnly = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = null,
                    tint = Color(0xFF10B981),
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Producto seleccionado",
                    tint = Color(0xFF10B981),
                    modifier = Modifier.size(20.dp)
                )
            },
            minLines = 2,
            maxLines = 3
        )
    } else if (formState.descripcion.size > 1) {
        // Hay múltiples productos - mostrar dropdown
        ProductoDropdown(
            productos = formState.descripcion,
            productoSeleccionado = formState.productoSeleccionado,
            onProductoSeleccionado = onProductoSeleccionado
        )
    } else {
        // Hay productos pero ninguno seleccionado
        TemperatureTextField(
            value = "Seleccione un producto",
            onValueChange = {},
            label = "Descripción de la OT",
            readOnly = true,
            enabled = false,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = null,
                    tint = Color(0xFFFF9800),
                    modifier = Modifier.size(20.dp)
                )
            }
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Hora
    TimeOutlinedTextField(
        selectedTime = LocalDateTime.parse("${formState.fecha}T${formState.hora}"),
        onTimeChange = { time ->
            onHoraChange(time?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: "")
        },
        showDialog = false,
        onShowDialogChange = {},
        texto = "Hora de Registro",
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Temperatura
    TemperatureTextField(
        value = formState.temperatura,
        onValueChange = onTemperaturaChange,
        label = "Temperatura (°C)",
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Thermostat,
                contentDescription = null,
                tint = Color(0xFFA0AEC0),
                modifier = Modifier.size(20.dp)
            )
        },
        keyboardType = KeyboardType.Decimal
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Auxiliar
    /*TemperatureTextField(
        value = formState.auxiliar,
        onValueChange = onAuxiliarChange,
        label = "Auxiliar Responsable",
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = Color(0xFFA0AEC0),
                modifier = Modifier.size(20.dp)
            )
        }
    )*/

    Spacer(modifier = Modifier.height(16.dp))

    // Observaciones
    TemperatureTextField(
        value = formState.observaciones,
        onValueChange = onObservacionesChange,
        label = "Observaciones (Opcional)",
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Notes,
                contentDescription = null,
                tint = Color(0xFFA0AEC0),
                modifier = Modifier.size(20.dp)
            )
        },
        minLines = 2,
        maxLines = 4
    )
}

@Composable
fun ProductoDropdown(
    productos: List<com.vistony.app.Entidad.ConsultaOTItem>,
    productoSeleccionado: com.vistony.app.Entidad.ConsultaOTItem?,
    onProductoSeleccionado: (com.vistony.app.Entidad.ConsultaOTItem) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    
    Box(modifier = Modifier.fillMaxWidth()) {
        TemperatureTextField(
            value = productoSeleccionado?.producto ?: "Seleccione un producto",
            onValueChange = {},
            label = "Descripción de la OT",
            readOnly = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = null,
                    tint = if (productoSeleccionado != null) Color(0xFF10B981) else Color(0xFFFF9800),
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Cerrar lista" else "Mostrar lista",
                    tint = Color(0xFFA0AEC0),
                    modifier = Modifier.size(20.dp)
                )
            },
            modifier = Modifier.clickable { expanded = !expanded },
        )
        
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            productos.forEach { producto ->
                DropdownMenuItem(
                    text = {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = producto.producto,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "OT: ${producto.ot}",
                                color = Color(0xFF6B7280),
                                fontSize = 12.sp
                            )
                        }
                    },
                    onClick = {
                        onProductoSeleccionado(producto)
                        expanded = false
                    }
                )
            }
        }
    }
}