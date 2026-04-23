package com.vistony.app.Screen.Muestra

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.vistony.app.Entidad.RegistroLlegada
import com.vistony.app.Entidad.UserResponse
import com.vistony.app.Entidad.UserState
import com.vistony.app.Screen.Generic.Drawers.BottomBar
import com.vistony.app.Screen.Generic.Drawers.CustomDrawer
import com.vistony.app.Screen.Generic.*
import com.vistony.app.ViewModel.MuestraViewModel
import com.vistony.app.ui.theme.theme.Dimensions
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterialApi::class,
    ExperimentalMaterial3Api::class
)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RegistroLlegadaScreen(
    muestraViewModel: MuestraViewModel,
    currentUser: UserResponse,
    userState: UserState,
    navController: NavController,
    onLogout: () -> Unit = {}
) {
    val context = LocalContext.current
    val activity = context as Activity
    val windowSize = calculateWindowSizeClass(context)
    val padding_res = Dimensions.getPadding(windowSize.widthSizeClass)
    val bodyFontSize = Dimensions.getBodyFontSize(windowSize.widthSizeClass)

    val formState by muestraViewModel.registroLlegadaFormState.collectAsState()
    val registros by muestraViewModel.registrosLlegada.collectAsState()
    val successMessage by muestraViewModel.successMessage.collectAsState()
    val errorMessage by muestraViewModel.errorMessage.collectAsState()
    val isCreating by muestraViewModel.isCreating.collectAsState()
    val isConfirming by muestraViewModel.isConfirming.collectAsState()
    val productoEncontrado by muestraViewModel.productoEncontrado.collectAsState()

    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    
    // Estado para BottomSheet de detalle
    var selectedRegistro by remember { mutableStateOf<RegistroLlegada?>(null) }
    val detailSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    
    // Estado para controlar el modal de éxito
    var showSuccessModal by remember { mutableStateOf(false) }
    
    // Detectar cuando se completa el envío exitosamente
    LaunchedEffect(isCreating, successMessage) {
        if (!isCreating && successMessage != null) {
            showSuccessModal = true
            // Auto-cerrar después de 3 segundos
            kotlinx.coroutines.delay(3000)
            showSuccessModal = false
            muestraViewModel.clearMessages()
        }
    }

    // Cargar registros al iniciar
    LaunchedEffect(Unit) {
        // Formatear fechas en formato yyyyMMdd (ejemplo: 20260101, 20260206)
        val fechaInicio = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        val fechaFin = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        muestraViewModel.obtenerRegistrosLlegada(fechaInicio, fechaFin, currentUser.dni)
    }

    // Manejar mensajes de error (sin modal, solo toast o snackbar)
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            kotlinx.coroutines.delay(5000)
            muestraViewModel.clearMessages()
        }
    }

    // Consultar producto cuando cambia el número de orden
    LaunchedEffect(formState.numeroOrdenFabricacion) {
        if (formState.numeroOrdenFabricacion.isNotEmpty() && formState.numeroOrdenFabricacion.length >= 3) {
            muestraViewModel.consultarProductoRegistroLlegada(formState.numeroOrdenFabricacion)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            CustomDrawer(
                navController = navController,
                id = currentUser.dni,
                userState = userState,
                onLogout = onLogout
            )
        }
    ) {
        ModalBottomSheetLayout(
            modifier = Modifier.fillMaxWidth(),
            sheetState = bottomSheetState,
            sheetContent = { BottomBar("parada", currentUser) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFF8FAFC),
                                Color(0xFFE2E8F0)
                            )
                        )
                    )
            ) {
                // Header con gradiente
                MuestraHeader(
                    title = "Registro de Llegada",
                    subtitle = "Control de calidad de producción",
                    menuButton = {
                        IconButton(
                            onClick = { scope.launch { drawerState.open() } },
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    color = Color.White.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menú",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    },
                    modifier = Modifier.padding(horizontal = padding_res, vertical = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Contenido principal
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = padding_res),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Formulario de registro
                    item {
                        RegistroLlegadaForm(
                            formState = formState,
                            muestraViewModel = muestraViewModel,
                            onUpdateField = { field, value ->
                                muestraViewModel.updateRegistroLlegada(field, value)
                            },
                            onRegistrar = {
                                muestraViewModel.agregarRegistroLlegada(currentUser)
                            },
                            windowSize = windowSize.widthSizeClass,
                            padding = padding_res
                        )
                    }

                    // Lista de registros
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Lista de Registros",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E293B)
                            )
                            Text(
                                text = "Ver Todos",
                                fontSize = 14.sp,
                                color = Color(0xFF6B7280),
                                modifier = Modifier.clickable { 
                                    navController.navigate("registroLlegadaTodos")
                                }
                            )
                        }
                    }

                    // Mostrar solo los primeros 3-4 registros
                    val registrosLimitados = registros.take(4)
                    if (registrosLimitados.isEmpty()) {
                        item {
                            MuestraEmptyState(
                                icon = Icons.Default.Science,
                                title = "No hay registros",
                                subtitle = "Los registros de llegada aparecerán aquí"
                            )
                        }
                    } else {
                        items(registrosLimitados) { registro ->
                            RegistroLlegadaItemCard(
                                registro = registro,
                                onClick = {
                                    selectedRegistro = registro
                                    scope.launch {
                                        detailSheetState.show()
                                    }
                                }
                            )
                        }
                    }

                    // Espacio al final
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
    
    // Alert de producto encontrado
    MuestraInfoAlert(
        message = productoEncontrado,
        onDismiss = {
            muestraViewModel.clearMessages()
        }
    )
    
    // Modal de carga y éxito
    MuestraLoadingModal(
        isLoading = isCreating,
        isSuccess = showSuccessModal,
        successMessage = successMessage ?: "¡Registro enviado exitosamente!",
        loadingMessage = "Enviando registro de llegada...",
        onDismiss = {
            showSuccessModal = false
            muestraViewModel.clearMessages()
        }
    )
    
    // BottomSheet para detalle
    selectedRegistro?.let { registro ->
        // Sincronizar con el estado actualizado de la lista (por si se confirmó)
        val registroActualizado = registros.find {
            it.numeroOrdenFabricacion == registro.numeroOrdenFabricacion &&
            it.numeroMuestra == registro.numeroMuestra
        } ?: registro

        ModalBottomSheet(
            onDismissRequest = {
                scope.launch {
                    detailSheetState.hide()
                }.invokeOnCompletion {
                    selectedRegistro = null
                }
            },
            sheetState = detailSheetState,
            containerColor = Color(0xFFF7F7F7)
        ) {
            RegistroLlegadaDetailSheet(
                registro = registroActualizado,
                onClose = {
                    scope.launch {
                        detailSheetState.hide()
                    }.invokeOnCompletion {
                        selectedRegistro = null
                    }
                },
                onConfirmar = { reg ->
                    val fechaIso = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                    muestraViewModel.confirmarRecepcion(reg, fechaIso)
                },
                isConfirming = isConfirming
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroLlegadaForm(
    formState: MuestraViewModel.RegistroLlegadaFormState,
    muestraViewModel: MuestraViewModel,
    onUpdateField: (String, String) -> Unit,
    onRegistrar: () -> Unit,
    windowSize: WindowWidthSizeClass,
    padding: androidx.compose.ui.unit.Dp
) {
    // Obtener muestras disponibles del API
    val muestrasDisponibles by muestraViewModel.muestrasDisponibles.collectAsState()
    
    // Usar las muestras del API si están disponibles, sino usar lista vacía
    val numerosMuestra = if (muestrasDisponibles.isNotEmpty()) {
        muestrasDisponibles
    } else {
        emptyList()
    }
    
    var expandedMuestra by remember { mutableStateOf(false) }
    
    // Determinar si es mobile o tablet
    val isMobile = windowSize == WindowWidthSizeClass.Compact

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // N° ORDEN DE FABRICACIÓN y N° DE MUESTRA
            if (isMobile) {
                // Mobile: uno debajo del otro
                Column {
                    Text(
                        text = "N° ORDEN DE FABRICACIÓN",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF6B7280),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    MuestraTextField(
                        value = formState.numeroOrdenFabricacion,
                        onValueChange = { onUpdateField("numeroOrdenFabricacion", it) },
                        label = "",
                        placeholder = "260002070",
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Description,
                                contentDescription = null,
                                tint = Color(0xFF9CA3AF),
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        onImeAction = {
                            if (formState.numeroOrdenFabricacion.isNotEmpty()) {
                                muestraViewModel.consultarProductoRegistroLlegada(formState.numeroOrdenFabricacion)
                            }
                        }
                    )
                }
                
                // N° DE MUESTRA - Dropdown
                Column {
                    Text(
                        text = "N° DE MUESTRA",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF6B7280),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    ExposedDropdownMenuBox(
                        expanded = expandedMuestra,
                        onExpandedChange = { expandedMuestra = !expandedMuestra }
                    ) {
                        OutlinedTextField(
                            value = formState.numeroMuestra,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("") },
                            placeholder = { Text("Muestra 01") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMuestra)
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Numbers,
                                    contentDescription = null,
                                    tint = Color(0xFF9CA3AF),
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedBorderColor = Color(0xFF4F46E5),
                                unfocusedBorderColor = Color(0xFFE5E7EB)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = expandedMuestra,
                            onDismissRequest = { expandedMuestra = false }
                        ) {
                            if (numerosMuestra.isEmpty()) {
                                DropdownMenuItem(
                                    text = { Text("No hay muestras disponibles") },
                                    onClick = { expandedMuestra = false },
                                    enabled = false
                                )
                            } else {
                                numerosMuestra.forEach { muestra ->
                                    DropdownMenuItem(
                                        text = { Text(muestra) },
                                        onClick = {
                                            onUpdateField("numeroMuestra", muestra)
                                            expandedMuestra = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                // Tablet: en la misma fila
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "N° ORDEN DE FABRICACIÓN",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF6B7280),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        MuestraTextField(
                            value = formState.numeroOrdenFabricacion,
                            onValueChange = { onUpdateField("numeroOrdenFabricacion", it) },
                            label = "",
                            placeholder = "OF-2024-0",
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Description,
                                    contentDescription = null,
                                    tint = Color(0xFF9CA3AF),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        )
                    }
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "N° DE MUESTRA",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF6B7280),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        ExposedDropdownMenuBox(
                            expanded = expandedMuestra,
                            onExpandedChange = { expandedMuestra = !expandedMuestra }
                        ) {
                            OutlinedTextField(
                                value = formState.numeroMuestra,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("") },
                                placeholder = { Text("Muestra 01") },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMuestra)
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Numbers,
                                        contentDescription = null,
                                        tint = Color(0xFF9CA3AF),
                                        modifier = Modifier.size(20.dp)
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedBorderColor = Color(0xFF4F46E5),
                                    unfocusedBorderColor = Color(0xFFE5E7EB)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                            ExposedDropdownMenu(
                                expanded = expandedMuestra,
                                onDismissRequest = { expandedMuestra = false }
                            ) {
                                if (numerosMuestra.isEmpty()) {
                                    DropdownMenuItem(
                                        text = { Text("No hay muestras disponibles") },
                                        onClick = { expandedMuestra = false },
                                        enabled = false
                                    )
                                } else {
                                    numerosMuestra.forEach { muestra ->
                                        DropdownMenuItem(
                                            text = { Text(muestra) },
                                            onClick = {
                                                onUpdateField("numeroMuestra", muestra)
                                                expandedMuestra = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // DESCRIPCIÓN DEL PRODUCTO
            Column {
                Text(
                    text = "DESCRIPCIÓN DEL PRODUCTO",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF6B7280),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                MuestraTextField(
                    value = formState.descripcionProducto,
                    onValueChange = { onUpdateField("descripcionProducto", it) },
                    label = "",
                    placeholder = "Envase Polietileno 500ml - Azul",
                    enabled = false, // Solo lectura, se completa automáticamente
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Inventory,
                            contentDescription = null,
                            tint = Color(0xFF9CA3AF),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                )
            }

            // MÁQUINA
            Column {
                Text(
                    text = "MÁQUINA",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF6B7280),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                MuestraTextField(
                    value = formState.maquina,
                    onValueChange = { onUpdateField("maquina", it) },
                    label = "",
                    placeholder = "Soplado S-04 (Línea B)",
                    enabled = false, // Solo lectura, se completa automáticamente
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Build,
                            contentDescription = null,
                            tint = Color(0xFF9CA3AF),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón Registrar Muestra
            MuestraButton(
                text = "Registrar Muestra",
                onClick = onRegistrar,
                icon = Icons.Default.Add,
                enabled = formState.isFormValid,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RegistroLlegadaItemCard(
    registro: RegistroLlegada,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = Color(0xFF4F46E5).copy(alpha = 0.1f),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Science,
                    contentDescription = "Muestra",
                    tint = Color(0xFF4F46E5),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Información
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "${registro.numeroMuestra} - ${registro.numeroOrdenFabricacion}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatearTiempoRelativo(registro.fechaRegistro),
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280)
                )
                Spacer(modifier = Modifier.height(6.dp))
                // Chip de estado
                val esConfirmado = !registro.fechaConfirmacion.isNullOrEmpty()
                val estadoColor = colorEstado(registro.estado)
                Box(
                    modifier = Modifier
                        .background(
                            color = estadoColor.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = registro.estado,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = estadoColor
                    )
                }
            }

            // Flecha
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Ver detalle",
                tint = Color(0xFF9CA3AF),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroLlegadaDetailSheet(
    registro: RegistroLlegada,
    onClose: () -> Unit,
    onConfirmar: ((RegistroLlegada) -> Unit)? = null,
    isConfirming: Boolean = false
) {
    val esConfirmado = !registro.fechaConfirmacion.isNullOrEmpty()
    val estadoColor = colorEstado(registro.estado)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Detalle del Registro",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )

            IconButton(onClick = onClose) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cerrar",
                    tint = Color(0xFF6B7280)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Chip de estado
        Box(
            modifier = Modifier
                .background(
                    color = estadoColor.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 12.dp, vertical = 5.dp)
        ) {
            Text(
                text = registro.estado,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = estadoColor
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Información del registro
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DetailRow(
                label = "N° Orden de Fabricación",
                value = registro.numeroOrdenFabricacion,
                icon = Icons.Default.Description
            )

            DetailRow(
                label = "N° de Muestra",
                value = registro.numeroMuestra,
                icon = Icons.Default.Numbers
            )

            DetailRow(
                label = "Descripción del Producto",
                value = registro.descripcionProducto,
                icon = Icons.Default.Inventory
            )

            DetailRow(
                label = "Código de Producto",
                value = registro.codigoProducto,
                icon = Icons.Default.Tag
            )

            DetailRow(
                label = "Fecha y Hora de Registro",
                value = formatearFechaHora(registro.fechaRegistro),
                icon = Icons.Default.CalendarToday
            )

            DetailRow(
                label = "Registrado por",
                value = registro.nombre.ifEmpty { registro.userRegister },
                icon = Icons.Default.Person
            )

            val esRechazado = registro.estado == "Rechazado"

            if (!registro.fechaAprobacion.isNullOrEmpty()) {
                DetailRow(
                    label = if (esRechazado) "Fecha de Rechazo" else "Fecha de Aprobación",
                    value = formatearFechaHora(registro.fechaAprobacion),
                    icon = if (esRechazado) Icons.Default.ThumbDown else Icons.Default.ThumbUp
                )
            }

            if (!registro.fechaConfirmacion.isNullOrEmpty()) {
                DetailRow(
                    label = "Fecha de Confirmación",
                    value = formatearFechaHora(registro.fechaConfirmacion),
                    icon = Icons.Default.CheckCircle
                )
            }
        }

        // Tiempos calculados
        val esRechazado = registro.estado == "Rechazado"
        val tiempoAprobacion = calcularTiempo(registro.fechaRegistro, registro.fechaAprobacion)
        val tiempoConfirmacion = calcularTiempo(registro.fechaAprobacion, registro.fechaConfirmacion)

        if (tiempoAprobacion != null || tiempoConfirmacion != null) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Tiempos",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6B7280)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (tiempoAprobacion != null) {
                    TiempoCard(
                        label = if (esRechazado) "Tiempo de Rechazo" else "Tiempo de Aprobación",
                        valor = tiempoAprobacion,
                        color = if (esRechazado) Color(0xFFDC2626) else Color(0xFF7C3AED),
                        modifier = Modifier.weight(1f)
                    )
                }
                if (tiempoConfirmacion != null) {
                    TiempoCard(
                        label = "Tiempo de Confirmación",
                        valor = tiempoConfirmacion,
                        color = Color(0xFF0369A1),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Botón de confirmación — solo visible si no está confirmado y se pasó el callback
        if (!esConfirmado && onConfirmar != null) {
            val esAprobado = registro.estado == "Aprobado" || esRechazado
            Button(
                onClick = { onConfirmar(registro) },
                enabled = esAprobado && !isConfirming,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF16A34A),
                    disabledContainerColor = Color(0xFF16A34A).copy(alpha = 0.5f)
                )
            ) {
                if (isConfirming) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Confirmando...",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Marcar como Recibido",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun DetailRow(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color(0xFF4F46E5),
                modifier = Modifier.size(20.dp)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    fontSize = 12.sp,
                    color = Color(0xFF6B7280)
                )
                Text(
                    text = value.ifEmpty { "No especificado" },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatearTiempoRelativo(fechaRegistro: String): String {
    return try {
        // Parsear el formato ISO 8601 con timezone: "2026-02-06T02:22:58-05:00"
        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        val fechaHora = java.time.ZonedDateTime.parse(fechaRegistro, formatter)
        val ahora = java.time.ZonedDateTime.now()
        val minutos = ChronoUnit.MINUTES.between(fechaHora, ahora)
        
        when {
            minutos < 1 -> "Hace un momento"
            minutos < 60 -> "Hace $minutos ${if (minutos == 1L) "minuto" else "minutos"}"
            else -> {
                val horas = ChronoUnit.HOURS.between(fechaHora, ahora)
                if (horas < 24) {
                    "Hace $horas ${if (horas == 1L) "hora" else "horas"}"
                } else {
                    val dias = ChronoUnit.DAYS.between(fechaHora, ahora)
                    "Hace $dias ${if (dias == 1L) "día" else "días"}"
                }
            }
        }
    } catch (e: Exception) {
        "Fecha no disponible"
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun parsearFecha(fecha: String): java.time.LocalDateTime? {
    val parserConEspacioYFraccion = java.time.format.DateTimeFormatterBuilder()
        .appendPattern("yyyy-MM-dd HH:mm:ss")
        .optionalStart()
        .appendFraction(java.time.temporal.ChronoField.NANO_OF_SECOND, 0, 9, true)
        .optionalEnd()
        .toFormatter()
    return try {
        java.time.ZonedDateTime.parse(fecha, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toLocalDateTime()
    } catch (e1: Exception) {
        try {
            java.time.LocalDateTime.parse(fecha, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        } catch (e2: Exception) {
            try {
                java.time.LocalDateTime.parse(fecha, parserConEspacioYFraccion)
            } catch (e3: Exception) {
                try {
                    java.time.LocalDateTime.parse(fecha, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
                } catch (e4: Exception) { null }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun calcularTiempo(desde: String?, hasta: String?): String? {
    if (desde.isNullOrEmpty() || hasta.isNullOrEmpty()) return null
    val dtDesde = parsearFecha(desde) ?: return null
    val dtHasta = parsearFecha(hasta) ?: return null
    val minutos = java.time.Duration.between(dtDesde, dtHasta).toMinutes()
    if (minutos < 0) return null
    val horas = minutos / 60
    val mins = minutos % 60
    return when {
        horas > 0 -> "${horas}h ${mins}min"
        else -> "${mins}min"
    }
}

@Composable
fun TiempoCard(label: String, valor: String, color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(color = color.copy(alpha = 0.1f), shape = RoundedCornerShape(12.dp))
            .border(width = 1.dp, color = color.copy(alpha = 0.3f), shape = RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = valor,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = color
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = color.copy(alpha = 0.8f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

fun colorEstado(estado: String): androidx.compose.ui.graphics.Color = when (estado) {
    "Aprobado"   -> Color(0xFF16A34A)
    "Rechazado"  -> Color(0xFFDC2626)
    "Confirmado" -> Color(0xFF9F25EB)
    else         -> Color(0xFFD97706) // Pendiente y cualquier otro
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatearFechaHora(fechaRegistro: String): String {
    val formatoSalida = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    val parserConEspacioYFraccion = java.time.format.DateTimeFormatterBuilder()
        .appendPattern("yyyy-MM-dd HH:mm:ss")
        .optionalStart()
        .appendFraction(java.time.temporal.ChronoField.NANO_OF_SECOND, 0, 9, true)
        .optionalEnd()
        .toFormatter()
    return try {
        java.time.ZonedDateTime.parse(fechaRegistro, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            .format(formatoSalida)
    } catch (e1: Exception) {
        try {
            java.time.LocalDateTime.parse(fechaRegistro, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                .format(formatoSalida)
        } catch (e2: Exception) {
            try {
                java.time.LocalDateTime.parse(fechaRegistro, parserConEspacioYFraccion)
                    .format(formatoSalida)
            } catch (e3: Exception) {
                try {
                    java.time.LocalDateTime.parse(fechaRegistro, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
                        .format(formatoSalida)
                } catch (e4: Exception) {
                    fechaRegistro
                }
            }
        }
    }
}
