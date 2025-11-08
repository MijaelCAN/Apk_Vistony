package com.vistony.app.Screen.Temperatura

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.ModalDrawer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Thermostat
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.vistony.app.Entidad.Temperatura
import com.vistony.app.Entidad.UserState
import com.vistony.app.Screen.Generic.Drawers.BottomBar
import com.vistony.app.Screen.Generic.Drawers.CustomDrawer
import com.vistony.app.Screen.Generic.PrimaryMuestraColor
import com.vistony.app.Screen.Generic.TemperatureButton
import com.vistony.app.Screen.Generic.TemperatureCard
import com.vistony.app.Screen.Generic.TemperatureEmptyState
import com.vistony.app.Screen.Generic.TemperatureHeader
import com.vistony.app.Screen.Generic.TemperatureIcon
import com.vistony.app.Screen.Generic.TemperatureLoadingCard
import com.vistony.app.Screen.Generic.getTemperatureColor
import com.vistony.app.Screen.Generic.formatTemperature
import com.vistony.app.ViewModel.TemperaturaViewModel
import com.vistony.app.ui.theme.theme.Dimensions
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ListTemperatura(
    onNavigateToCreate: () -> Unit = {},
    onNavigateToDetail: (Temperatura) -> Unit = {},
    onOpenDrawer: () -> Unit = {},
    temperaturaViewModel: TemperaturaViewModel,
    currentUser: com.vistony.app.Entidad.UserResponse,
    userState: UserState,
    navController: NavController,
    onLogout: () -> Unit = {}
) {
    val context = LocalContext.current
    val activity = context as Activity
    val windowSize = calculateWindowSizeClass(context)
    val padding_res = Dimensions.getPadding(windowSize.widthSizeClass)
    val bodyFontSize = Dimensions.getBodyFontSize(windowSize.widthSizeClass)

    val temperaturas by temperaturaViewModel.temperaturas.collectAsState()
    val isLoading by temperaturaViewModel.isLoading.collectAsState()
    val errorMessage by temperaturaViewModel.errorMessage.collectAsState()


    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    val systemUiController = rememberSystemUiController()
    SideEffect {
        //systemUiController.setStatusBarColor(Color(0xFF0957c3))
        systemUiController.setStatusBarColor(Color(0xFFF8FAFF))
    }

    // Cargar datos iniciales
    LaunchedEffect(Unit) {
        temperaturaViewModel.obtenerTemperaturas(
            currentUser.dni,
            LocalDate.now(),
            LocalDate.now()
        )
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
                                Color(0xFFF8FAFF),
                                Color(0xFFE8F2FF)
                            )
                        )
                    )
            ) {
                // Header con gradiente
                TemperatureHeader(
                    title = "Control de Temperatura",
                    subtitle = "Registro de temperaturas",
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
                                onClick = onNavigateToCreate,
                                modifier = Modifier.size(56.dp),
                                containerColor = Color.White,
                                contentColor = PrimaryMuestraColor,
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Agregar temperatura",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    },
                    modifier = Modifier.padding(horizontal = padding_res, vertical = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            TemperatureLoadingCard(
                                modifier = Modifier.padding(horizontal = padding_res)
                            )
                        }
                    }
                    temperaturas.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            TemperatureEmptyState(
                                title = "No hay registros de temperatura",
                                subtitle = "Comienza registrando la primera temperatura de soplado",
                                actionButton = {
                                    TemperatureButton(
                                        text = "Registrar Temperatura",
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
                            items(temperaturas) { temperatura ->
                                TemperatureItemCard(
                                    temperatura = temperatura,
                                    onClick = { onNavigateToDetail(temperatura) },
                                    bodyFontSize = bodyFontSize
                                )
                            }
                        }
                    }
                }

                // Mostrar mensaje de error si existe
                errorMessage?.let { message ->
                    LaunchedEffect(message) {
                        // Aquí podrías mostrar un Snackbar o Toast
                        temperaturaViewModel.clearMessages()
                    }
                }
            }
        }
    }
}

@Composable
fun TemperatureItemCard(
    temperatura: Temperatura,
    onClick: () -> Unit,
    bodyFontSize: Float
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(100),
        label = "scale"
    )

    TemperatureCard (
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable {
                isPressed = true
                onClick()
            }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono de temperatura
            TemperatureIcon(
                temperature = temperatura.temperatura.toDoubleOrNull() ?: 0.0
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
                        text = "OT: ${temperatura.ot}",
                        fontSize = bodyFontSize.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3748)
                    )
                    
                    Text(
                        text = "${formatTemperature(temperatura.temperatura)}°C",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = getTemperatureColor(temperatura.temperatura.toDoubleOrNull() ?: 0.0)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = temperatura.descripcion,
                    fontSize = (bodyFontSize - 2).sp,
                    color = Color(0xFF718096),
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = temperatura.fecha,
                        fontSize = 12.sp,
                        color = Color(0xFFA0AEC0)
                    )
                    
                    Text(
                        text = temperatura.auxiliar,
                        fontSize = 12.sp,
                        color = Color(0xFF667EEA),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
