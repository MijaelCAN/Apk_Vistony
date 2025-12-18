package com.vistony.app.Screen.Parada

import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
//import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.TurnLeft
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.Card
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.vistony.app.Entidad.Actividad
import com.vistony.app.Entidad.ListaRequest
import com.vistony.app.Entidad.Parada
import com.vistony.app.Entidad.UserState
import com.vistony.app.Extras.formatoServidor
import com.vistony.app.Screen.Generic.CustomAlertDialog
import com.vistony.app.Screen.Generic.CustomSearchText
import com.vistony.app.Screen.Generic.DialogType
import com.vistony.app.Screen.Generic.Drawers.BottomBar
import com.vistony.app.Screen.Generic.Drawers.BottomCurtainDrawer
import com.vistony.app.Screen.Generic.FilterButtonsRow
import com.vistony.app.Screen.Generic.Drawers.CustomDrawer
import com.vistony.app.Screen.Generic.Drawers.RightCurtainDrawer
import com.vistony.app.Screen.Generic.TopBar
import com.vistony.app.Screen.Inspeccion.backGroundLigth
import com.vistony.app.Screen.ParadaMantenimiento.TarjetaActividad
import com.vistony.app.ViewModel.ActividadViewModel
import com.vistony.app.ViewModel.EstadoParada
import com.vistony.app.ViewModel.LoginViewModel
import com.vistony.app.ViewModel.ParadaViewModel
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ListParada(
    navController: NavController,
    paradaViewModel: ParadaViewModel = hiltViewModel(),
    actividadViewModel: ActividadViewModel = hiltViewModel(),
    id: String,
    userState: UserState,
    onLogout: () -> Unit
) {
    val loginViewModel = hiltViewModel<LoginViewModel>(LocalContext.current as ComponentActivity)

    //val role by loginViewModel.userRole.collectAsState()
    val role = userState.currentUser.role

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val systemUiController = rememberSystemUiController()
    //val topBarColor = Color(0xFFF7F7F7)
    val topBarColor = backGroundLigth

    var paradaSeleccionada by remember { mutableStateOf<Parada?>(null) }
    var showDrawerDetalle by remember { mutableStateOf(false) }
    var showDrawerHome by remember { mutableStateOf(false) }
    var showDrawerActivity by remember { mutableStateOf(false) }

    var selected by remember { mutableStateOf(if (role == "mantenimiento") "Iniciado" else "Todos") }
    val actividades by actividadViewModel.actividades.collectAsState()

    // Estado para pull-to-refresh
    var isRefreshing by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    
    // Función para refrescar datos
    fun refreshData() {
        if (userState.currentUser.dni.isNotEmpty()) {
            paradaViewModel.obtenerParadas(
                ListaRequest(
                    formatoServidor(paradaViewModel.fechaIni.value),
                    formatoServidor(paradaViewModel.fechaFin.value),
                    "T",
                    userState.currentUser.dni
                )
            )
        }
    }

    // Inicializar paradas con DNI del usuario
    LaunchedEffect(userState.currentUser.dni) {
        if (userState.currentUser.dni.isNotEmpty()) {
            refreshData()
        }
    }
    val coroutineScope = rememberCoroutineScope()

    // Estado de pull-to-refresh
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            coroutineScope.launch{
                isRefreshing = true
                refreshData()
                // Simular un pequeño delay para mostrar el indicador
                kotlinx.coroutines.delay(500)
                isRefreshing = false
            }
        }
    )

    SideEffect {
        systemUiController.setStatusBarColor(
            color = topBarColor,
            darkIcons = true
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            CustomDrawer(
                navController = navController,
                id = id,
                userState = userState,
                onLogout = onLogout
            )
        }
    ) {
        ModalBottomSheetLayout(
            modifier = Modifier.fillMaxWidth(),
            sheetState = bottomSheetState,
            sheetContent = { BottomBar("parada", userState.currentUser) }
        ) {
            Scaffold(
                topBar = {
                    TopBar(
                        "",
                        color = Color(0XFFF7F7F7),
                        colorContent = Color.Black,
                        navController = navController,
                        onMenuClick = { scope.launch { drawerState.open() } },
                        onBottomMenuClick = {
                            scope.launch {
                                if (bottomSheetState.isVisible) {
                                    bottomSheetState.hide()
                                } else {
                                    bottomSheetState.show()
                                }
                            }
                        },
                        viewModel = loginViewModel
                    )
                },
                floatingActionButton = {
                    IconButton(
                        modifier = Modifier.size(60.dp),
                        onClick = { showDrawerHome = true },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color(0xFFFC6A68),
                            contentColor = Color.White
                        )
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "")

                    }
                },
                content = { paddingValues ->
                    BodyListParada(
                        navController,
                        id,
                        paradaViewModel,
                        actividadViewModel,
                        role = role,
                        selected = selected,
                        onSelectedChange = { selected = it },
                        listState = listState,
                        pullRefreshState = pullRefreshState,
                        isRefreshing = isRefreshing,
                        paddingValues,
                        function = { it ->
                            Log.d("TAG", "DetalleParada-CallBack: $it")
                            paradaSeleccionada = it
                            //if (it.FechaHoraFin.isNullOrEmpty()) {
                                showDrawerDetalle = true
                            //}
                            /*if (it.Usuario.isEmpty()) {
                                showDrawerDetalle = true
                            } else {
                                if (actividades.none { it.paradaDocEntry == paradaSeleccionada?.DocEntry }) {
                                    showDrawerActivity = true
                                } else {
                                    showDrawerDetalle = true
                                }
                            }*/
                        }
                    )
                },
                containerColor = Color(0XFFF7F7F7)
            )
        }

    }

    // ============================ FORMULARIO DETALLE PARADA ================================= //
    RightCurtainDrawer(
        visible = showDrawerDetalle,
        onClose = { showDrawerDetalle = false },
        animationDuration = 300 // ms, ajusta velocidad
    ) {
        IconButton(onClick = { showDrawerDetalle = false }) {
            Icon(Icons.Default.TurnLeft, contentDescription = "Cerrar")
        }
        Column(Modifier.padding(start = 24.dp, end = 24.dp, top = 32.dp)) {
            DetalleParada(
                id = id,
                role = role,
                parada = paradaSeleccionada,
                paradaViewModel = paradaViewModel,
                actividadViewModel = actividadViewModel,
                actividades = actividades,
                onClose = {
                    showDrawerDetalle = false
                    selected = it
                },
                onOpenActivity = { showDrawerActivity = true }
            )
        }
    }

    // ============================ FORMULARIO REGISTRO PARADA ================================= //
    RightCurtainDrawer(
        visible = showDrawerHome,
        onClose = { showDrawerHome = false },
        animationDuration = 300 // ms, ajusta velocidad
    ) {
        IconButton(onClick = { showDrawerHome = false }) {
            Icon(Icons.Default.TurnLeft, contentDescription = "Cerrar")
        }
        Column(Modifier.padding(start = 24.dp, end = 24.dp, top = 32.dp)) {
            // =========== CONTENIDO =============== //
            Log.d("TAG", "id: $id")
            BodyParada(navController, paradaViewModel, id)
        }
    }

    // ========================== FORMULARIO REGISTRO ACTIVIDAD ================================= //

    BottomCurtainDrawer(
        visible = showDrawerActivity,
        onClose = { showDrawerActivity = false },
        animationDuration = 300
    ) {
        IconButton(onClick = { showDrawerActivity = false }) {
            Icon(Icons.Default.TurnLeft, contentDescription = "Cerrar")
        }
        Column(Modifier.padding(start = 24.dp, end = 24.dp, top = 32.dp)) {
            // ======= CONTENIDO ========== //
            /*BodyActividad(
                //paradaSeleccionada,
                navController,
                paradaViewModel,
                actividadViewModel,
                id,
                onClose = { showDrawerActivity = false },
            )*/
        }
    }


}

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BodyListParada(
    navController: NavController,
    id: String,
    paradaViewModel: ParadaViewModel,
    actividadViewModel: ActividadViewModel,
    role: String?, // listOf("produccion", "mantenimiento")
    selected: String,
    onSelectedChange: (String) -> Unit,
    listState: LazyListState,
    pullRefreshState: PullRefreshState,
    isRefreshing: Boolean,
    paddingValues: PaddingValues,
    function: (Parada) -> Unit
) {

    var searchText by remember { mutableStateOf("") }
    val paradas by paradaViewModel.listParadas.collectAsState()
    val actividades by actividadViewModel.actividades.collectAsState()
    //var selected by remember { mutableStateOf(if(role == "mantenimiento") "Iniciado" else "Todos" ) }
    /*val options = listOf("Todos", "Iniciado", "Finalizado")
    val optionsMantenimiento = listOf("Iniciado", "Actividades")*/

    val options = if (role == "mantenimiento") {
        listOf("Iniciado", "Asignados")
    } else {
        listOf("Todos", "Iniciado", "Finalizado")
    }


    val listaFiltradaParadas = remember(paradas.data, selected, searchText) {
        paradas.data.filter { parada ->
            // Filtro por estado
            val estadoFilter = when (selected) {
                "Iniciado" -> parada.FechaHoraFin.isNullOrBlank()
                "Finalizado" -> !parada.FechaHoraFin.isNullOrBlank()
                else -> true
            }

            // Filtro por texto de búsqueda
            val searchFilter = searchText.isEmpty() || listOf(
                parada.Maquina,
                parada.FechaHoraInicio,
                parada.FechaHoraFin ?: ""
            ).any { it.contains(searchText, ignoreCase = true) }

            estadoFilter && searchFilter
        }
    }
    val listaFiltradaActividad = remember(paradas.data, searchText) {
        paradas.data.filter { parada ->
            // Filtro por texto de búsqueda
            val searchFilter = searchText.isEmpty() || listOf(
                parada.Maquina,
                parada.FechaHoraInicio,
                parada.FechaHoraFin ?: ""
            ).any { it.contains(searchText, ignoreCase = true) }

            searchFilter
        }

    }
    val listaFiltrada: List<ListItem> =
        remember(paradas.data, actividades, selected, searchText, role) {
            if (role == "mantenimiento") {
                when (selected) {
                    "Iniciado" -> listaFiltradaParadas
                        .filter { parada -> parada.FechaHoraFin.isNullOrBlank() }
                        .map { ListItem.ParadaItem(it) }

                    "Asignados" -> listaFiltradaActividad
                        .filter { parada -> parada.Usuario.isNotEmpty() }
                        .map { ListItem.ParadaItem(it) }

                    else -> emptyList()
                }
            } else {
                listaFiltradaParadas.map { ListItem.ParadaItem(it) }
            }
        }




    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
            .wrapContentSize()
            .background(Color(0xFFf7f7f7))
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Paradas de máquinas",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Revisa las paradas en un Rango de tiempo",
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        CustomSearchText(
            text = searchText,
            onTextChange = { searchText = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            containerColor = Color.White,
            searchIcon = Icons.Default.Search,
            placeholderText = "Buscar..."
        )

        Spacer(modifier = Modifier.height(8.dp))
        /*if(role == "mantenimiento"){
            FilterButtonsRow(
                options = optionsMantenimiento,
                selectedOption = selected,
                onOptionSelected = { selected = it }
            )
        }else{
            FilterButtonsRow(
                options = options,
                selectedOption = selected,
                onOptionSelected = { selected = it }
            )
        }*/
        FilterButtonsRow(
            options = options,
            selectedOption = selected,
            onOptionSelected = { onSelectedChange(it) }
        )

        Spacer(modifier = Modifier.height(8.dp))
        if (listaFiltrada.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 8.dp, top = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay Paradas, Registre una nueva.",
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .pullRefresh(pullRefreshState)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    state = listState
                ) {
                    items(listaFiltrada.asReversed()) { item ->
                        when (item) {
                            is ListItem.ParadaItem -> TarjetaParada(
                                parada = item.parada,
                                paradaViewModel = paradaViewModel,
                                navController = navController,
                                id = id,
                                function = { function(item.parada) }
                            )

                            is ListItem.ActividadItem -> TarjetaActividad(
                                actividad = item.actividad,
                                id = id,
                                actividadViewModel = actividadViewModel,
                                function = {
                                    /* función para actividad */
                                }
                            )
                        }
                    }
                }
                
                // Indicador de pull-to-refresh
                PullRefreshIndicator(
                    refreshing = isRefreshing,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TarjetaParada(
    parada: Parada,
    paradaViewModel: ParadaViewModel,
    navController: NavController,
    id: String,
    function: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    val estado by paradaViewModel.estadoParada.collectAsState()
    val paradaState by paradaViewModel.paradas.collectAsState()
    val context = LocalContext.current

    val backgroundColor = remember(parada.Maquina) {
        val colors = listOf(
            Color(0xFFCACACA),
            Color(0xFFC6C6C6),
            Color(0xFFB1B1B1),
            Color(0xFFAEAEAE),
            Color(0xFFA1A1A1)

        )
        colors[parada.Maquina.hashCode().absoluteValue % colors.size]
    }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = 0.dp,
        backgroundColor = Color.Transparent
        /*colors = CardDefaults.cardColors(
            if (parada.FechaHoraFin.isNullOrEmpty()) Color.White else Color(
                0xFFF0F0F0
            )
        )*/
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Columna imagen / círculo
            Box(
                modifier = Modifier
                    //.weight(0.7f)
                    .size(75.dp)
                    .clip(CircleShape)
                    .background(backgroundColor)
                    .clickable { function() },
                contentAlignment = Alignment.Center
            ) {
                // Aquí puedes cargar imagen si tienes url o recurso
                // Por ejemplo con Coil:
                /*
                val painter = rememberAsyncImagePainter(model = parada.imagenUrl)
                Image(
                    painter = painter,
                    contentDescription = "Imagen de la máquina",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                */
                // Por ahora dejamos solo el círculo con color
                val palabras = parada.Maquina.split(" ")
                val cadena = if (palabras.size > 1) palabras[1] else palabras[0]

                Text(
                    text = cadena.take(1).uppercase(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .weight(3f)
                    .clickable { function() },
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = parada.Maquina,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B2733)
                )
                Text(
                    text = parada.Comentario,
                    fontSize = 12.sp,
                    color = Color(0xFF455A64),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${parada.FechaHoraInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))} - ${
                        parada.FechaHoraFin?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) ?: "En curso"
                    }",
                    fontSize = 12.sp,
                    color = Color(0xFF78909C)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Divider(
                    color = Color(0xFFE0E0E0),
                    thickness = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    if (showDialog) {
        when (estado) {
            EstadoParada.Cargando -> {
                CustomAlertDialog(
                    showDialog = showDialog,
                    title = "Cargando",
                    message = "Espere por favor...",
                    confirmButtonText = "",
                    dismissButtonText = null,
                    onDismiss = { showDialog = false },
                    dialogType = DialogType.LOADING,
                )
            }

            EstadoParada.Exitoso -> {
                CustomAlertDialog(
                    showDialog = showDialog,
                    title = "Éxito",
                    message = paradaState.paradaResponse.message,
                    confirmButtonText = "OK",
                    dismissButtonText = null,
                    onConfirm = { paradaViewModel.actualizarEstadoParada(EstadoParada.Idle) },
                    onDismiss = {
                        showDialog = false
                        paradaViewModel.actualizarEstadoParada(EstadoParada.Idle)
                        navController.navigate("listaParada/$id")
                    },
                    dialogType = DialogType.SUCCESS,
                )
            }

            is EstadoParada.Error -> {
                CustomAlertDialog(
                    showDialog = showDialog,
                    title = "Error",
                    message = (estado as EstadoParada.Error).mensaje,
                    confirmButtonText = "OK",
                    dismissButtonText = null,
                    onConfirm = { paradaViewModel.actualizarEstadoParada(EstadoParada.Idle) },
                    onDismiss = {
                        showDialog = false
                        paradaViewModel.actualizarEstadoParada(EstadoParada.Idle)
                    },
                    dialogType = DialogType.ERROR,
                )
            }

            else -> {}
        }
    }
}

sealed class ListItem {
    data class ParadaItem(val parada: Parada) : ListItem()
    data class ActividadItem(val actividad: Actividad) : ListItem()
}