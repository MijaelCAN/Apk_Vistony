package com.vistony.app

import android.content.Context
import android.net.ConnectivityManager
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vistony.app.Service.ConnectivityObserver
import com.vistony.app.ViewModel.NetworkStatusViewModel
import com.vistony.app.ViewModel.SharedViewModel
import com.vistony.app.ui.theme.Screen.DetalleScreen
import com.vistony.app.ui.theme.Screen.HomeScreen
import com.vistony.app.ui.theme.Screen.LoginScreen
import com.vistony.app.ui.theme.Screen.NoInternetScreen
import com.vistony.app.ui.theme.VistonyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

                    if(status == ConnectivityObserver.Status.Available){
                        NavHost(startDestination = "login",navController =navController){
                            composable("login"){
                                LoginScreen(navController)
                            }
                            composable("home"){
                                HomeScreen(navController,sharedViewModel)
                            }
                            composable("detalle"){
                                DetalleScreen(navController,sharedViewModel)
                            }
                        }
                    }else{
                        NoInternetScreen()
                    }
                }
            }
        }
    }
    public fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}
