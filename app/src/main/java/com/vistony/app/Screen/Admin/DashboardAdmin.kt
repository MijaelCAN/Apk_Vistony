package com.vistony.app.Screen.Admin

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.HomeRepairService
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.vistony.app.Entidad.UserState
import com.vistony.app.Screen.Generic.Drawers.CustomDrawer
import com.vistony.app.Screen.Generic.TopBar
import com.vistony.app.Screen.Inspeccion.backGroundLigth
import com.vistony.app.ViewModel.LoginViewModel
import kotlinx.coroutines.launch

data class ModuleCard(
    val id: String,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val color: Color,
    val route: String
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun DashboardAdmin(
    navController: NavController,
    userState: UserState,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as Activity
    val windowSize = calculateWindowSizeClass(activity)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val loginViewModel: LoginViewModel = hiltViewModel()

    val systemUiController = rememberSystemUiController()
    SideEffect {
        //systemUiController.setStatusBarColor(Color(0xFF0957c3))
        systemUiController.setStatusBarColor(backGroundLigth)
    }

    val currentUser = userState.currentUser
    val userDni = currentUser?.dni ?: ""

    val modules = remember {
        listOf(
            ModuleCard(
                id = "inspeccion",
                title = "Inspección",
                description = "Lista de inspecciones",
                icon = Icons.Default.Checklist,
                color = Color(0xFF4CAF50),
                route = "listaInsp/$userDni"
            ),
            ModuleCard(
                id = "parada",
                title = "Parada Máquina",
                description = "Lista de paradas",
                icon = Icons.Default.Construction,
                color = Color(0xFF2196F3),
                route = "listaParada/$userDni"
            ),
            ModuleCard(
                id = "mantenimiento",
                title = "Mantenimiento",
                description = "Registro de paradas",
                icon = Icons.Default.HomeRepairService,
                color = Color(0xFFFF9800),
                route = "paradaMantenimiento"
            ),
            ModuleCard(
                id = "temperatura",
                title = "Control Temperatura",
                description = "Lista de temperaturas",
                icon = Icons.Default.Thermostat,
                color = Color(0xFFE91E63),
                route = "listaTemperatura"
            ),
            ModuleCard(
                id = "muestra",
                title = "Registro de Muestras",
                description = "Lista de muestras",
                icon = Icons.Default.Science,
                color = Color(0xFF9C27B0),
                route = "listaMuestra"
            )
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            CustomDrawer(
                navController = navController,
                userState = userState,
                id = userDni,
                onLogout = onLogout
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    title = "Panel Administrativo",
                    color = backGroundLigth,
                    colorContent = Color.Black,
                    navController = navController,
                    onMenuClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    },
                    viewModel = loginViewModel
                )
            },
            containerColor = backGroundLigth
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backGroundLigth)
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Bienvenido, ${currentUser?.name ?: "Administrador"}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3748),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Selecciona un módulo para comenzar",
                    fontSize = MaterialTheme.typography.labelMedium.fontSize,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(modules) { module ->
                        ModuleCardItem(
                            module = module,
                            onClick = {
                                navController.navigate(module.route)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ModuleCardItem(
    module: ModuleCard,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        module.color.copy(alpha = 0.1f),
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = module.icon,
                    contentDescription = module.title,
                    tint = module.color,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = module.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3748),
                textAlign = TextAlign.Center
            )

            Text(
                text = module.description,
                fontSize = 12.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

