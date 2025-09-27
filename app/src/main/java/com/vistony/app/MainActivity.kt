package com.vistony.app

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vistony.app.Entidad.UserState
import com.vistony.app.Service.ConnectivityObserver
import com.vistony.app.ViewModel.NetworkStatusViewModel
import com.vistony.app.ViewModel.SharedViewModel
import com.vistony.app.ui.theme.theme.AppTheme
import com.vistony.app.Screen.Inspeccion.DetalleScreen
import com.vistony.app.Screen.Inspeccion.ListScreen
import com.vistony.app.Screen.Login2
import com.vistony.app.Screen.LoginScreen
import com.vistony.app.Screen.NoInternetScreen
import com.vistony.app.Screen.Parada.ListParada
import com.vistony.app.Screen.ParadaMantenimiento.BodyActividad
import com.vistony.app.Screen.ParadaMantenimiento.ListActividad
import com.vistony.app.ViewModel.LoginViewModel
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
                                ListParada(navController = navController, id = it.arguments?.getString("user") ?: "", userState = userState)
                            }
                            composable("paradaMantenimiento"){
                                ListActividad(
                                    navController = navController,
                                    sharedViewModel = sharedViewModel,
                                    userState = userState
                                )
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
