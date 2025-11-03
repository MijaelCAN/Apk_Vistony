package com.vistony.app

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vistony.app.Entidad.UserState
import com.vistony.app.Service.ConnectivityObserver
import com.vistony.app.ViewModel.NetworkStatusViewModel
import com.vistony.app.ViewModel.SharedViewModel
import com.vistony.app.ui.theme.theme.AppTheme
import com.vistony.app.Screen.Admin.DashboardAdmin
import com.vistony.app.Screen.Inspeccion.DetalleScreen
import com.vistony.app.Screen.Inspeccion.ListScreen
import com.vistony.app.Screen.Login2
import com.vistony.app.Screen.LoginScreen
import com.vistony.app.Screen.NoInternetScreen
import com.vistony.app.Screen.NoModulesScreen
import com.vistony.app.Screen.Parada.ListParada
import com.vistony.app.Screen.ParadaMantenimiento.ListActividad
import com.vistony.app.Screen.Temperatura.ListTemperatura
import com.vistony.app.Screen.Temperatura.CreateTemperatura
import com.vistony.app.Screen.Temperatura.DetailTemperaturaWithViewModel
import com.vistony.app.Screen.Muestra.ListMuestra
import com.vistony.app.Screen.Muestra.CreateMuestra
import com.vistony.app.Screen.Muestra.DetailMuestra
import com.vistony.app.Screen.Muestra.EditMuestra
import com.vistony.app.ViewModel.LoginViewModel
import com.vistony.app.ViewModel.TemperaturaViewModel
import com.vistony.app.ViewModel.MuestraViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
     private val userState = UserState()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContent {
            AppTheme() {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val loginViewModel: LoginViewModel = hiltViewModel()
                    var navController = rememberNavController()
                    val sharedViewModel: SharedViewModel = hiltViewModel()
                    val networkStatusViewModel: NetworkStatusViewModel = hiltViewModel()
                    val temperaturaViewModel: TemperaturaViewModel = hiltViewModel()
                    val muestraViewModel: MuestraViewModel = hiltViewModel()
                    val status by networkStatusViewModel.status.collectAsState()
                    var usuario = "70131373"
                    val user by loginViewModel.userData.collectAsState()
                    LaunchedEffect(user) {
                        user?.let { userState.setUser(it) }
                    }

                    if (status == ConnectivityObserver.Status.Available) {
                        NavHost(startDestination = "login", navController = navController) {
                            composable("login") {
                                Login2(navController,loginViewModel, userState)
                                //LoginScreen(navController)
                            }
                            composable("noModules") {
                                NoModulesScreen(
                                    navController = navController,
                                    onLogout = {
                                        loginViewModel.clearUserData()
                                    }
                                )
                            }
                            composable("dashboardAdmin") {
                                DashboardAdmin(
                                    navController = navController,
                                    userState = userState,
                                    onLogout = {
                                        loginViewModel.clearUserData()
                                        navController.navigate("login") {
                                            popUpTo(0) { inclusive = true }
                                        }
                                    }
                                )
                            }
                            composable(
                                "detalle/{user}",
                                arguments = listOf(navArgument("user") { type = NavType.StringType })
                            ) {
                                DetalleScreen(navController, sharedViewModel, id = it.arguments?.getString("user") ?: "", userState = userState)
                            }
                            composable(
                                route = "listaInsp/{user}",
                                arguments = listOf(navArgument("user") { type = NavType.StringType })
                            ) { it ->
                                ListScreen(navController, sharedViewModel, id = it.arguments?.getString("user") ?: "", userState = userState)
                            }
                            composable("reporte",) {
                                LoginScreen(navController)
                            }
                            composable(
                                "listaParada/{user}",
                                arguments = listOf(navArgument("user") { type = NavType.StringType })
                            ) {
                                ListParada(
                                    navController = navController,
                                    id = it.arguments?.getString("user") ?: "",
                                    userState = userState,
                                    onLogout = {
                                        loginViewModel.clearUserData()
                                        navController.navigate("login") {
                                            popUpTo(0) { inclusive = true }
                                        }
                                    }
                                )
                            }
                            composable("paradaMantenimiento"){
                                ListActividad(
                                    navController = navController,
                                    sharedViewModel = sharedViewModel,
                                    userState = userState
                                )
                            }
                            composable("listaTemperatura") {
                                ListTemperatura(
                                    onNavigateToCreate = { navController.navigate("createTemperatura") },
                                    onNavigateToDetail = { temperatura ->
                                        // Pasar la temperatura como argumento serializado
                                        navController.navigate("detailTemperatura/${temperatura.id}")
                                    },
                                    onOpenDrawer = { /* El drawer se maneja automáticamente */ },
                                    temperaturaViewModel = temperaturaViewModel,
                                    currentUser = userState.currentUser,
                                    userState = userState,
                                    navController = navController,
                                    onLogout = {
                                        loginViewModel.clearUserData()
                                        navController.navigate("login") {
                                            popUpTo(0) { inclusive = true }
                                        }
                                    }
                                )
                            }
                            composable("createTemperatura") {
                                CreateTemperatura(
                                    onNavigateBack = { navController.popBackStack() },
                                    onNavigateToSuccess = { 
                                        navController.popBackStack()
                                        navController.navigate("listaTemperatura")
                                    },
                                    temperaturaViewModel = temperaturaViewModel,
                                    currentUser = userState.currentUser
                                )
                            }
                            composable(
                                "detailTemperatura/{temperaturaId}",
                                arguments = listOf(navArgument("temperaturaId") { type = NavType.StringType })
                            ) { backStackEntry ->
                                val temperaturaId = backStackEntry.arguments?.getString("temperaturaId") ?: ""
                                DetailTemperaturaWithViewModel(
                                    temperaturaId = temperaturaId,
                                    onNavigateBack = { navController.popBackStack() },
                                    temperaturaViewModel = temperaturaViewModel
                                )
                            }
                            composable("listaMuestra") {
                                ListMuestra(
                                    onNavigateToCreate = { 
                                        //muestraViewModel.resetAllForms() // Resetear para nuevo registro
                                        navController.navigate("createMuestra") 
                                    },
                                    onNavigateToDetail = { muestra ->
                                        navController.navigate("detailMuestra/${muestra.id}")
                                    },
                                    onNavigateToEdit = { muestra ->
                                        // Obtener muestra completa y cargar para edición
                                        muestraViewModel.obtenerMuestraCompletaPorId(muestra.id)
                                        navController.navigate("editMuestra/${muestra.id}")
                                    },
                                    onOpenDrawer = { /* El drawer se maneja automáticamente */ },
                                    muestraViewModel = muestraViewModel,
                                    currentUser = userState.currentUser,
                                    userState = userState,
                                    navController = navController,
                                    onLogout = {
                                        loginViewModel.clearUserData()
                                        navController.navigate("login") {
                                            popUpTo(0) { inclusive = true }
                                        }
                                    }
                                )
                            }
                            composable("createMuestra") {
                                CreateMuestra(
                                    onNavigateBack = { navController.popBackStack() },
                                    onNavigateToSuccess = { 
                                        // Limpiar el stack y navegar a la lista
                                        navController.navigate("listaMuestra") {
                                            popUpTo("listaMuestra") { inclusive = false }
                                        }
                                    },
                                    muestraViewModel = muestraViewModel,
                                    currentUser = userState.currentUser
                                )
                            }
                            composable(
                                "detailMuestra/{muestraId}",
                                arguments = listOf(navArgument("muestraId") { type = NavType.StringType })
                            ) { backStackEntry ->
                                val muestraId = backStackEntry.arguments?.getString("muestraId") ?: ""
                                val muestra = muestraViewModel.obtenerMuestraPorId(muestraId)
                                if (muestra != null) {
                                    DetailMuestra(
                                        muestra = muestra,
                                        onNavigateBack = { navController.popBackStack() },
                                        onNavigateToEdit = {
                                            // Activar modo edición y navegar a CreateMuestra
                                            muestraViewModel.activarModoEdicion(muestraId)
                                            navController.navigate("createMuestra")
                                        }
                                    )
                                } else {
                                    // Mostrar pantalla de error
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Muestra no encontrada",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF2D3748)
                                        )
                                    }
                                }
                            }
                            composable(
                                "editMuestra/{muestraId}",
                                arguments = listOf(navArgument("muestraId") { type = NavType.StringType })
                            ) { backStackEntry ->
                                val muestraId = backStackEntry.arguments?.getString("muestraId") ?: ""
                                val muestra = muestraViewModel.obtenerMuestraPorId(muestraId)
                                if (muestra != null) {
                                    EditMuestra(
                                        muestra = muestra,
                                        onNavigateBack = { navController.popBackStack() },
                                        onNavigateToSuccess = {
                                            navController.navigate("listaMuestra") {
                                                popUpTo("listaMuestra") { inclusive = false }
                                            }
                                        },
                                        muestraViewModel = muestraViewModel
                                    )
                                } else {
                                    // Mostrar pantalla de error
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Muestra no encontrada",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF2D3748)
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        NoInternetScreen()
                    }

                }
            }
        }
    }

}
