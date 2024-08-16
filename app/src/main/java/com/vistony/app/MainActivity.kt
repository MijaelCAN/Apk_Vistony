package com.vistony.app

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vistony.app.Service.ConnectivityObserver
import com.vistony.app.ViewModel.NetworkStatusViewModel
import com.vistony.app.ViewModel.SharedViewModel
import com.vistony.app.ui.theme.Screen.Inspeccion.DetalleScreen
import com.vistony.app.ui.theme.Screen.Inspeccion.HomeScreen
import com.vistony.app.ui.theme.Screen.Inspeccion.ListScreen
import com.vistony.app.ui.theme.Screen.LoginScreen
import com.vistony.app.ui.theme.Screen.NoInternetScreen
import com.vistony.app.ui.theme.Screen.Parada.HomeParada
import com.vistony.app.ui.theme.Screen.Parada.ListParada
import com.vistony.app.ui.theme.VistonyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContent {
            VistonyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var navController = rememberNavController()
                    val sharedViewModel: SharedViewModel = hiltViewModel()
                    val networkStatusViewModel: NetworkStatusViewModel = hiltViewModel()
                    val status by networkStatusViewModel.status.collectAsState()

                    if (status == ConnectivityObserver.Status.Available) {
                        NavHost(startDestination = "login", navController = navController) {
                            composable("login") {
                                LoginScreen(navController)
                            }
                            composable(
                                route = "home/{user}",
                                arguments = listOf(navArgument("user") { type = NavType.StringType })
                            ) { it ->
                                HomeScreen(navController, sharedViewModel, id = it.arguments?.getString("user") ?: "")
                            }
                            composable(
                                "detalle/{user}",
                                arguments = listOf(navArgument("user") { type = NavType.StringType })
                            ) {
                                DetalleScreen(navController, sharedViewModel, id = it.arguments?.getString("user") ?: "")
                            }
                            composable(
                                route = "listaInsp/{user}",
                                arguments = listOf(navArgument("user") { type = NavType.StringType })
                            ) { it ->
                                ListScreen(navController, id = it.arguments?.getString("user") ?: "")
                            }
                            composable(
                                "homeParada",
                                arguments = listOf(navArgument("user") { type = NavType.StringType })
                            ) {
                                HomeParada(navController = navController, id = it.arguments?.getString("user") ?: "")
                            }
                            composable(
                                "listaParada",
                                arguments = listOf(navArgument("user") { type = NavType.StringType })
                            ) {
                                ListParada(navController = navController, id = it.arguments?.getString("user") ?: "")
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
