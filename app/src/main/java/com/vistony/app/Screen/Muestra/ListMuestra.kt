package com.vistony.app.Screen.Muestra

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.vistony.app.Entidad.MuestraCabecera
import com.vistony.app.Entidad.UserResponse
import com.vistony.app.Entidad.UserState
import com.vistony.app.Screen.Generic.Drawers.BottomBar
import com.vistony.app.Screen.Generic.Drawers.CustomDrawer
import com.vistony.app.Screen.Generic.MuestraButton
import com.vistony.app.Screen.Generic.MuestraCard
import com.vistony.app.Screen.Generic.MuestraEmptyState
import com.vistony.app.Screen.Generic.MuestraHeader
import com.vistony.app.Screen.Generic.MuestraLoadingCard
import com.vistony.app.Screen.Generic.DateOutlinedTextField
import com.vistony.app.ViewModel.MuestraViewModel
import com.vistony.app.ui.theme.theme.Dimensions
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ListMuestra(
    onNavigateToCreate: () -> Unit = {},
    onNavigateToDetail: (MuestraCabecera) -> Unit = {},
    onNavigateToEdit: (MuestraCabecera) -> Unit = {},
    onOpenDrawer: () -> Unit = {},
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

    val muestras by muestraViewModel.muestras.collectAsState()
    val isLoading by muestraViewModel.isLoading.collectAsState()
    val errorMessage by muestraViewModel.errorMessage.collectAsState()

    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    // Estados para filtros
    var fechaInicio by remember { mutableStateOf<LocalDateTime?>(LocalDateTime.now()) }
    var fechaFin by remember { mutableStateOf<LocalDateTime?>(LocalDateTime.now()) }
    var showDialogDateIni by remember { mutableStateOf(false) }
    var showDialogDateFin by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    // Filtrar muestras localmente por búsqueda
    val muestrasFiltradas = remember(muestras, searchText) {
        if (searchText.isBlank()) {
            muestras
        } else {
            muestras.filter { muestra ->
                muestra.lote.contains(searchText, ignoreCase = true) ||
                muestra.codigo.contains(searchText, ignoreCase = true) ||
                muestra.auxiliar.contains(searchText, ignoreCase = true)
            }
        }
    }

    // Cargar muestras al iniciar
    LaunchedEffect(Unit) {
        muestraViewModel.obtenerMuestras(currentUser.dni)
    }

    // Cargar muestras cuando cambien las fechas
    LaunchedEffect(fechaInicio, fechaFin) {
        if (fechaInicio != null && fechaFin != null) {
            val fechaInicioStr = fechaInicio!!.toLocalDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
            val fechaFinStr = fechaFin!!.toLocalDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
            muestraViewModel.obtenerMuestrasConFechas(currentUser.dni, fechaInicioStr, fechaFinStr)
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
                    title = "Registro de Muestras",
                    subtitle = "Control de calidad - Soplado",
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
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            FloatingActionButton(
                                onClick = {
                                    muestraViewModel.resetAllForms()
                                    onNavigateToCreate()
                                },
                                modifier = Modifier.size(56.dp),
                                containerColor = Color.White,
                                contentColor = Color(0xFF4F46E5)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Agregar muestra",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    },
                    modifier = Modifier.padding(horizontal = padding_res, vertical = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Filtros: Selector de fechas y búsqueda
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = padding_res),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        // Selector de fechas
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            DateOutlinedTextField(
                                modifier = Modifier.weight(1f),
                                texto = "Fecha Inicio",
                                readonly = true,
                                selectedDate = fechaInicio,
                                onDateChange = { fechaInicio = it },
                                showDialog = showDialogDateIni,
                                onShowDialogChange = { showDialogDateIni = it }
                            )
                            DateOutlinedTextField(
                                modifier = Modifier.weight(1f),
                                texto = "Fecha Fin",
                                readonly = true,
                                selectedDate = fechaFin,
                                onDateChange = { fechaFin = it },
                                showDialog = showDialogDateFin,
                                onShowDialogChange = { showDialogDateFin = it }
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Campo de búsqueda
                        OutlinedTextField(
                            value = searchText,
                            onValueChange = { searchText = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text(
                                    text = "Buscar por lote, código o auxiliar (DNI)",
                                    color = Color(0xFF9CA3AF),
                                    fontSize = 14.sp
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Buscar",
                                    tint = Color(0xFF9CA3AF)
                                )
                            },
                            trailingIcon = {
                                if (searchText.isNotEmpty()) {
                                    IconButton(onClick = { searchText = "" }) {
                                        Icon(
                                            imageVector = Icons.Default.FilterList,
                                            contentDescription = "Limpiar",
                                            tint = Color(0xFF9CA3AF)
                                        )
                                    }
                                }
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFFF9FAFB),
                                unfocusedContainerColor = Color(0xFFF9FAFB),
                                focusedBorderColor = Color(0xFF4F46E5),
                                unfocusedBorderColor = Color(0xFFE5E7EB)
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            MuestraLoadingCard(
                                modifier = Modifier.padding(horizontal = padding_res)
                            )
                        }
                    }
                    muestrasFiltradas.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            MuestraEmptyState(
                                title = "No hay muestras registradas",
                                subtitle = "Comienza registrando la primera muestra de soplado",
                                actionButton = {
                                    MuestraButton(
                                        text = "Registrar Muestra",
                                        onClick = onNavigateToCreate,
                                        icon = Icons.Default.Add,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                },
                                modifier = Modifier.padding(horizontal = padding_res)
                            )
                        }
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = padding_res),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(muestrasFiltradas) { muestra ->
                                MuestraItemCard(
                                    muestra = muestra,
                                    onViewClick = {
                                        // Obtener muestra completa y navegar al detalle
                                        muestraViewModel.obtenerMuestraCompletaPorId(muestra.id)
                                        onNavigateToDetail(muestra)
                                    },
                                    onEditClick = {
                                        // Obtener muestra completa y cargar para edición
                                        muestraViewModel.obtenerMuestraCompletaPorId(muestra.id)
                                        //onNavigateToEdit(muestra)
                                        muestraViewModel.activarModoEdicion(muestra.id)
                                        onNavigateToCreate()
                                        muestraViewModel.irAPaso(4)
                                    },
                                    bodyFontSize = bodyFontSize
                                )
                            }
                        }
                    }
                }
            }

            // Mostrar mensaje de error si existe
            errorMessage?.let { message ->
                LaunchedEffect(message) {
                    // Aquí podrías mostrar un Snackbar o Toast
                    muestraViewModel.clearMessages()
                }
            }
        }
    }
}

@Composable
fun MuestraItemCard(
    muestra: MuestraCabecera,
    onViewClick: () -> Unit,
    onEditClick: () -> Unit,
    bodyFontSize: Float
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(100),
        label = "scale"
    )

    MuestraCard(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable {
                isPressed = true
                when (muestra.estado) {
                    "Completado" -> onViewClick()
                    else -> onEditClick()
                }
            }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono de muestra
            MuestraIcon(
                estado = muestra.estado
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Información principal
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Código: ${muestra.codigo}",
                        fontSize = bodyFontSize.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )

                    MuestraStatusChip(
                        estado = muestra.estado
                    )
                }

                Spacer(modifier = Modifier.height(0.dp))

                Text(
                    text = muestra.producto,
                    fontSize = (bodyFontSize - 2).sp,
                    color = Color(0xFF6B7280),
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(0.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    /*MuestraInfoChip(
                        icon = Icons.Default.Science,
                        text = "Lote: ${muestra.lote}",
                        color = Color(0xFF4F46E5)
                    )*/
                    Text(
                        text = "Lote: ${muestra.lote}",
                        fontSize = 14.sp,
                        color = Color(0xFF6B7280)
                    )
                    
                    Text(
                        text = muestra.fechaRegistro,
                        fontSize = 12.sp,
                        color = Color(0xFF9CA3AF)
                    )
                }

                Spacer(modifier = Modifier.height(0.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Auxiliar: ${muestra.auxiliar}",
                        fontSize = 12.sp,
                        color = Color(0xFF6B7280)
                    )
                }
            }
        }
    }
}

@Composable
fun MuestraIcon(
    estado: String,
    size: androidx.compose.ui.unit.Dp = 60.dp
) {
    val color = when (estado) {
        "Completado" -> Color(0xFF10B981)
        "En Proceso" -> Color(0xFFF59E0B)
        "Cancelado" -> Color(0xFFEF4444)
        else -> Color(0xFF6B7280)
    }
    
    Box(
        modifier = Modifier
            .size(size)
            .background(
                color = color.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Science,
            contentDescription = "Muestra",
            modifier = Modifier.size(size * 0.5f),
            tint = color
        )
    }
}

@Composable
fun MuestraStatusChip(
    estado: String,
) {
    val (backgroundColor, contentColor) = when (estado) {
        "Completado" -> Color(0xFF10B981) to Color.White
        "En Proceso" -> Color(0xFFF59E0B) to Color.White
        "Cancelado" -> Color(0xFFEF4444) to Color.White
        else -> Color(0xFF6B7280) to Color.White
    }
    
    /*Box(
        modifier = Modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = estado,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = contentColor
        )
    }*/
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = backgroundColor.copy(alpha = 0.1f),
        border = BorderStroke(1.dp, backgroundColor)
    ) {
        Text(
            text = estado,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color = backgroundColor,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}
