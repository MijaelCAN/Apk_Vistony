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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.rememberModalBottomSheetState
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
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterialApi::class,
    ExperimentalMaterial3Api::class
)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RegistroLlegadaTodosScreen(
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

    val registros by muestraViewModel.registrosLlegada.collectAsState()
    val isLoading by muestraViewModel.isLoading.collectAsState()

    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    
    // Estado para BottomSheet de detalle
    var selectedRegistro by remember { mutableStateOf<RegistroLlegada?>(null) }
    val detailSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    
    // Estados para filtros de fecha
    var fechaInicio by remember { mutableStateOf<LocalDateTime?>(LocalDateTime.now().withHour(0).withMinute(0)) }
    var fechaFin by remember { mutableStateOf<LocalDateTime?>(LocalDateTime.now().withHour(23).withMinute(59)) }
    var showDialogFechaInicio by remember { mutableStateOf(false) }
    var showDialogFechaFin by remember { mutableStateOf(false) }
    
    // Filtrar registros por fecha
    val registrosFiltrados = remember(registros, fechaInicio, fechaFin) {
        if (fechaInicio == null && fechaFin == null) {
            registros
        } else {
            registros.filter { registro ->
                try {
                    val fechaRegistro = LocalDateTime.parse("${registro.fechaRegistro}T${registro.horaRegistro}:00")
                    val cumpleInicio = fechaInicio == null || !fechaRegistro.isBefore(fechaInicio)
                    val cumpleFin = fechaFin == null || !fechaRegistro.isAfter(fechaFin)
                    cumpleInicio && cumpleFin
                } catch (e: Exception) {
                    false
                }
            }
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
                    title = "Todos los Registros",
                    subtitle = "Registro de Llegada - Control de calidad",
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
                    actionButton = {
                        IconButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    color = Color.White.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(8.dp)
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

                Spacer(modifier = Modifier.height(16.dp))

                // Filtros de fecha
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = padding_res),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Filtrar por Fecha",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B)
                        )
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Fecha Inicio
                            DateOutlinedTextField(
                                modifier = Modifier.weight(1f),
                                texto = "Fecha Inicio",
                                readonly = true,
                                selectedDate = fechaInicio,
                                onDateChange = { fechaInicio = it },
                                showDialog = showDialogFechaInicio,
                                onShowDialogChange = { showDialogFechaInicio = it }
                            )
                            
                            // Fecha Fin
                            DateOutlinedTextField(
                                modifier = Modifier.weight(1f),
                                texto = "Fecha Fin",
                                readonly = true,
                                selectedDate = fechaFin,
                                onDateChange = { fechaFin = it },
                                showDialog = showDialogFechaFin,
                                onShowDialogChange = { showDialogFechaFin = it }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Lista de registros
                if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFF4F46E5))
                    }
                } else if (registrosFiltrados.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize().background(Color.Transparent),
                        contentAlignment = Alignment.Center
                    ) {
                        MuestraEmptyState(
                            icon = Icons.Default.Science,
                            title = "No hay registros",
                            subtitle = "No se encontraron registros para las fechas seleccionadas"
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = padding_res),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(registrosFiltrados) { registro ->
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
                        
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
    
    
    // BottomSheet para detalle
    selectedRegistro?.let { registro ->
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
                registro = registro,
                onClose = {
                    scope.launch {
                        detailSheetState.hide()
                    }.invokeOnCompletion {
                        selectedRegistro = null
                    }
                }
            )
        }
    }
}

