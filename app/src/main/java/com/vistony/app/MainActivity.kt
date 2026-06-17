package com.vistony.app

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.vistony.app.ViewModel.SharedViewModel
import com.vistony.app.ui.theme.theme.AppTheme
import com.vistony.app.Screen.Admin.DashboardAdmin
import com.vistony.app.Screen.Inspeccion.DetalleScreen
import com.vistony.app.Screen.Inspeccion.ListScreen
import com.vistony.app.Screen.Login2
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
import com.vistony.app.Screen.muestraProduccion.MisOFScreen
import com.vistony.app.Screen.muestraProduccion.DetalleMezclaScreen
import com.vistony.app.Screen.Muestra.RegistroLlegadaScreen
import com.vistony.app.Screen.Muestra.RegistroLlegadaTodosScreen
import com.vistony.app.Screen.muestraProduccion.NuevaMuestraScreen
import com.vistony.app.ViewModel.LoginViewModel
import com.vistony.app.ViewModel.TemperaturaViewModel
import com.vistony.app.ViewModel.MuestraViewModel
import com.vistony.app.clean.core.utils.ZebraDW
import com.vistony.app.clean.core.utils.ZebraDWComunication
import com.vistony.app.clean.core.utils.ZebraDWReceiver
import com.vistony.app.clean.presentation.view.pages.ManuFacturingOrderPage
import com.vistony.app.clean.presentation.viewmodels.ScanViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
     private val userState = UserState()
    private val zebraDWComunication = ZebraDWComunication()
    //private val receiver = ZebraDWReceiver()
    private val zebraDW: ZebraDW = ZebraDW()
    //private val scanReceiver = ZebraDWReceiver()
    private val scanReceiver = ZebraDWReceiver()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        // Registrar receiver dinámicamente
        val intentFilter = IntentFilter(ZebraDW.PROFILE_INTENT_ACTION).apply {
            addCategory(Intent.CATEGORY_DEFAULT)
        }

        // Android 13+ requiere permiso en runtime para poder mostrar notificaciones
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!hasPermission) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(scanReceiver, intentFilter, Context.RECEIVER_EXPORTED)
        } else {
            registerReceiver(scanReceiver, intentFilter)
        }

        zebraDW.createDataWedgeProfile(this)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        
        // Ocultar la barra de estado (status bar)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController?.let { controller ->
            // Ocultar la barra de estado
            controller.hide(WindowInsetsCompat.Type.statusBars())
            // Hacer que la barra de estado se oculte de forma persistente
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        setContent {
            AppTheme() {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val loginViewModel: LoginViewModel = hiltViewModel()
                    var navController = rememberNavController()
                    val sharedViewModel: SharedViewModel = hiltViewModel()
                    val temperaturaViewModel: TemperaturaViewModel = hiltViewModel()
                    val muestraViewModel: MuestraViewModel = hiltViewModel()
                    var usuario = "70131373"
                    val user by loginViewModel.userData.collectAsState()
                    LaunchedEffect(user) {
                        user?.let { userState.setUser(it) }
                    }
                    val scanViewModel: ScanViewModel = hiltViewModel()


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
                                //LoginScreen(navController)
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
                                    userState = userState,
                                    onLogout = {
                                        loginViewModel.clearUserData()
                                        navController.navigate("login") {
                                            popUpTo(0) { inclusive = true }
                                        }
                                    }
                                )
                            }
                            composable("manufacturingOrder",) {
                                ManuFacturingOrderPage(navController, id = it.arguments?.getString("user") ?: "",userState = userState,
                                    scanViewModel = scanViewModel
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
                            composable("registroLlegada") {
                                RegistroLlegadaScreen(
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
                            composable("registroLlegadaTodos") {
                                RegistroLlegadaTodosScreen(
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
                        composable("entregaMuestra") {
                            MisOFScreen(
                                currentUser = userState.currentUser,
                                muestraViewModel = muestraViewModel,
                                onNavigateToAdd = { navController.navigate("nuevaMuestra") },
                                onOrderClick = { docEntry -> navController.navigate("entregaMuestraDetalle/$docEntry") },
                                onTomarACargo = { /*TODO*/ }
                            )
                        }
                        composable(
                            "entregaMuestraDetalle/{docEntry}",
                            arguments = listOf(navArgument("docEntry") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val docEntry = backStackEntry.arguments?.getInt("docEntry") ?: 0
                            DetalleMezclaScreen(
                                docEntry = docEntry,
                                muestraViewModel = muestraViewModel,
                                onBackClick = { navController.popBackStack() },
                                onRegistrarMuestraClick = {
                                    val detalle = muestraViewModel.muestraProduccionDetalle.value
                                    if (detalle != null) {
                                        navController.navigate(
                                            "nuevaMuestra?numOf=${detalle.ordenFabricacion}&numEn=${detalle.ordenEnvase}&type=${detalle.type}"
                                        )
                                    }
                                }
                            )
                        }
                        composable(
                            "nuevaMuestra?numOf={numOf}&numEn={numEn}&type={type}",
                            arguments = listOf(
                                navArgument("numOf") { type = NavType.StringType; nullable = true; defaultValue = null },
                                navArgument("numEn") { type = NavType.StringType; nullable = true; defaultValue = null },
                                navArgument("type") { type = NavType.StringType; nullable = true; defaultValue = null }
                            )
                        ) { backStackEntry ->
                            NuevaMuestraScreen(
                                numOf = backStackEntry.arguments?.getString("numOf"),
                                numEn = backStackEntry.arguments?.getString("numEn"),
                                type = backStackEntry.arguments?.getString("type"),
                                currentUser = userState.currentUser,
                                muestraViewModel = muestraViewModel,
                                onBackClick = { navController.popBackStack() },
                                onNotificationClick = { /*TODO*/ },
                                onEnviarClick = { navController.popBackStack() },
                                onCancelarClick = { navController.popBackStack() }
                            )
                        }
                    }

                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            unregisterReceiver(scanReceiver)
        } catch (e: IllegalArgumentException) {
            // Receiver ya no registrado
        }
    }

    /*override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        Log.e("REOS", "MainActivity-onNewIntent: action=${intent.action}")
        Log.e("REOS", "MainActivity-onNewIntent: extras=${intent.extras?.keySet()?.joinToString()}")

        if (intent.action == ZebraDW.PROFILE_INTENT_ACTION) {
            Log.e("REOS", "MainActivity-onNewIntent: ACCIÓN CORRECTA detectada")

            if (intent.hasExtra(ZebraDWComunication.DATAWEDGE_SCAN_EXTRA_DATA_STRING)) {
                val scannedData = intent.getStringExtra(ZebraDWComunication.DATAWEDGE_SCAN_EXTRA_DATA_STRING)
                val labelType = intent.getStringExtra(ZebraDWComunication.DATAWEDGE_SCAN_EXTRA_LABEL_TYPE)

                Log.e("REOS", "MainActivity-onNewIntent-PayLoad: $scannedData")
                Log.e("REOS", "MainActivity-onNewIntent-Type: $labelType")

                val intentData = Intent().apply {
                    putExtra("SCAN_DATA", scannedData)
                    putExtra("LABEL_TYPE", labelType)
                }
                ObservableObject.instance.updateValue(intentData)
            } else {
                Log.e("REOS", "MainActivity-onNewIntent: NO tiene SCAN_DATA_STRING")
            }
        } else {
            Log.e("REOS", "MainActivity-onNewIntent: Acción NO es de escaneo: ${intent.action}")
        }
    }*/



}
