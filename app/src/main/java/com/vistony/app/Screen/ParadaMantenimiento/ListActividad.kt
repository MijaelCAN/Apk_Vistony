package com.vistony.app.Screen.ParadaMantenimiento

import android.app.Activity
import android.os.Build
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TurnLeft
import androidx.compose.material.icons.filled.WorkOutline
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.vistony.app.Entidad.Actividad
import com.vistony.app.Entidad.Activity2
import com.vistony.app.Entidad.Equipment
import com.vistony.app.Entidad.Machine
import com.vistony.app.Entidad.UserState
import com.vistony.app.Entidad.semiActivity
import com.vistony.app.Screen.Generic.CustomSearchText
import com.vistony.app.Screen.Generic.Drawers.BottomBar
import com.vistony.app.Screen.Generic.Drawers.BottomCurtainDrawer
import com.vistony.app.Screen.Generic.Drawers.CustomDrawer
import com.vistony.app.Screen.Generic.Drawers.RightCurtainDrawer
import com.vistony.app.Screen.Generic.TopBar
import com.vistony.app.Screen.Inspeccion.backGroundLigth
import com.vistony.app.Screen.Inspeccion.blueDarkVistony
import com.vistony.app.Screen.Inspeccion.contentColor
import com.vistony.app.Screen.Inspeccion.redVistony
import com.vistony.app.Screen.Inspeccion.textColorSubTitle
import com.vistony.app.Screen.Inspeccion.textColorTitle
import com.vistony.app.ViewModel.ActividadViewModel
import com.vistony.app.ViewModel.LoginViewModel
import com.vistony.app.ViewModel.OTViewModel
import com.vistony.app.ViewModel.SharedViewModel
import com.vistony.app.ui.theme.theme.Dimensions
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.Duration
import java.time.temporal.ChronoUnit


@OptIn(
    ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterialApi::class,
    ExperimentalMaterial3Api::class
)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ListActividad(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    viewModel: ActividadViewModel = hiltViewModel(),
    otViewModel: OTViewModel = hiltViewModel(),
    id: String = "prueba",
    userState: UserState,
    onLogout: () -> Unit = {}
) {
    val loginViewModel = hiltViewModel<LoginViewModel>(LocalContext.current as ComponentActivity)


    val context = LocalContext.current
    val activity = context as Activity
    val windowSize = calculateWindowSizeClass(context)
    val padding_res = Dimensions.getPadding(windowSize.widthSizeClass)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    var actividadSeleccionada by remember { mutableStateOf(Activity2()) }
    var showDrawerDetalle by remember { mutableStateOf(false) }
    var showDrawerHome by remember { mutableStateOf(false) }
    var showDrawerActivity by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState(pageCount = {2})

    val systemUiController = rememberSystemUiController()
    val topBarColor = backGroundLigth

    // ============================ ESTADO GENERAL DE LA UI ===========================
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onUserChange(userState.currentUser)
        viewModel.getAllActividades(userState.currentUser)
    }

    SideEffect {
        systemUiController.setStatusBarColor(
            color = topBarColor,
            darkIcons = true
        )
    }

    // Observar cambios y mostrar Toasts
    LaunchedEffect(uiState.createSuccess) {
        if (uiState.createSuccess) {
            Toast.makeText(
                context,
                "Actividad creada exitosamente",
                Toast.LENGTH_LONG
            ).show()
            viewModel.resetCreateState()
        }
    }

    LaunchedEffect(uiState.createError) {
        uiState.createError?.let { error ->
            Toast.makeText(
                context,
                "Error: $error",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { CustomDrawer(navController = navController, id, userState = userState, onLogout = onLogout) }
    ) {
        ModalBottomSheetLayout(
            modifier = Modifier.fillMaxWidth(),
            sheetState = bottomSheetState,
            sheetContent = { BottomBar("mantenimiento", userState.currentUser) }
        ) {
            Scaffold(
                topBar = {
                    TopBar(
                        "",
                        //color = Color(0XFFF7F7F7),
                        color = backGroundLigth,
                        colorContent = contentColor,
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
                content = { paddingValues ->
                    /*BodyActividad(
                        paddingValues = paddingValues,
                        userState = userState,
                        navController = navController,
                        id = id
                    ) {

                    }*/
                    BodyListActividad(
                        paddingValues = paddingValues,
                        navController = navController,
                        viewModel = viewModel,
                        onCreateClick = { showDrawerActivity = true },
                        function = {
                            actividadSeleccionada = Activity2(
                                DocEntry = it.DocEntry,
                                description_OT = it.U_description_OT,
                                area = it.U_area,
                                equipment = Equipment(name = it.U_equipment),
                                machine = Machine(name = it.U_machine),
                                userName = it.U_userName,
                                userId = it.U_userId
                            )
                            viewModel.getDetailActivity(it.DocEntry,context)
                            showDrawerDetalle = true
                        }
                    )
                },
                floatingActionButton = {
                    IconButton(
                        modifier = Modifier.size(60.dp),
                        onClick = {
                            //navController.navigate("home/${id}")
                            showDrawerActivity = true
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color(0xFF01398D),
                            contentColor = Color.White
                        )
                    ){
                        Icon(Icons.Filled.Add, contentDescription = "")

                    }

                },
                //containerColor = Color(0XFFF7F7F7)
                containerColor = backGroundLigth
            )
        }


        // ============================ FORMULARIO DETALLE ACTIVIDAD ================================= //
        RightCurtainDrawer(
            visible = showDrawerDetalle,
            onClose = { showDrawerDetalle = false },
            animationDuration = 400 // ms, ajusta velocidad
        ) {
            IconButton(
                enabled = !uiState.isCreating,
                onClick = {
                    viewModel.resetCreateState()
                    showDrawerDetalle = false
                }
            ) {
                Icon(Icons.Default.TurnLeft, contentDescription = "Cerrar")
            }
            Column(Modifier.padding(start = 24.dp, end = 24.dp, top = 32.dp)) {
                DetalleActividad(
                    viewModel = viewModel,
                    //actividad = actividadSeleccionada,
                    onClose = {showDrawerDetalle = false},
                    userState = userState
                )
            }
        }


        // ============================ FORMULARIO REGISTRO INSPECCIÓN ================================= //
        /*RightCurtainDrawer(
            visible = showDrawerHome,
            onClose = { showDrawerHome = false },
            animationDuration = 400 // ms, ajusta velocidad
        ) {
            IconButton(onClick = {showDrawerHome = false}) {
                Icon(Icons.Default.TurnLeft, contentDescription = "Cerrar")
            }
            Column(Modifier.padding(start = 24.dp, end = 24.dp, top = 32.dp)) {
                //BodyIspeccion(navController, viewModel, otViewModel, sharedViewModel,id)

                HorizontalPager(
                    state = pagerState,
                    userScrollEnabled = false // para controlar solo con botones
                ) { page ->
                    when (page) {
                        0 -> BodyIspeccion(navController, viewModel, otViewModel, sharedViewModel, id){ scope.launch { pagerState.animateScrollToPage(1) }}
                        1 -> BodyDetalle(navController, inspViewModel, sharedViewModel, id){ scope.launch { pagerState.animateScrollToPage(0) }}
                    }
                }
            }
        }*/
        // ========================== FORMULARIO REGISTRO ACTIVIDAD ================================= //

        BottomCurtainDrawer(
            visible = showDrawerActivity,
            onClose = { showDrawerActivity = false },
            animationDuration = 300
        ) {
            IconButton(
                enabled = !uiState.isCreating,
                onClick = {
                    viewModel.onResetSelectedActividad()
                    viewModel.resetCreateState()
                    showDrawerActivity = false}
            ) {
                Icon(Icons.Default.TurnLeft, contentDescription = "Cerrar")
            }
            Column(Modifier.padding(start = 24.dp, end = 24.dp, top = 32.dp)) {
                // ======= CONTENIDO ========== //
                BodyActividad(
                    userState = userState,
                    navController = navController,
                    viewModel = viewModel,
                    onClose = { showDrawerActivity = false },
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BodyListActividad(
    paddingValues: PaddingValues,
    navController: NavController,
    viewModel: ActividadViewModel,
    function: (semiActivity) -> Unit,
    onCreateClick: () -> Unit = {}
) {

    val uiState by viewModel.uiState.collectAsState()
    val listActividades = uiState.actividades
    var searchText by remember { mutableStateOf("") }
    val nuevalista = listActividades.asReversed()

    val listaFiltrada = remember(nuevalista, searchText) {
        nuevalista.filter { _actividad ->

            // Filtro por texto de búsqueda
            val searchFilter = searchText.isEmpty() || listOf(
                _actividad.U_area ?: "",
                _actividad.U_description_OT ?: "",
                _actividad.U_equipment ?: "",
            ).any { it.contains(searchText, ignoreCase = true) }

            searchFilter
        }
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
            .wrapContentSize()
            .background(backGroundLigth)
    ) {

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Mis Actividades",
            color = textColorTitle,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Actividades registradas en este período",
            color = textColorSubTitle,
            fontSize = 12.sp
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            androidx.compose.material3.TextButton(
                onClick = { viewModel.getAllActividades(viewModel.uiState.value.userCurrent) }
            ) {
                Text("↻ Actualizar", fontSize = 12.sp, color = Color(0xFF01398D))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Solo mostrar la barra de búsqueda si no está cargando
        if (!uiState.isLoading) {
            CustomSearchText(
                text = searchText,
                onTextChange = { searchText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                containerColor = Color.White,
                searchIcon = Icons.Default.Search,
                placeholderText = "Buscar actividad..."
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
        
        // Mostrar diferentes estados según el estado de la UI
        when {
            uiState.isLoading -> {
                // Estado de carga
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(48.dp),
                            color = blueDarkVistony,
                            strokeWidth = 4.dp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Cargando actividades...",
                            color = textColorSubTitle,
                            fontSize = 14.sp
                        )
                    }
                }
            }
            uiState.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.WorkOutline,
                            contentDescription = "Error",
                            modifier = Modifier.size(48.dp),
                            tint = Color(0xFFEF5350).copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No se pudieron cargar las actividades",
                            color = textColorTitle,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        androidx.compose.material3.TextButton(
                            onClick = { viewModel.getAllActividades(viewModel.uiState.value.userCurrent) }
                        ) {
                            Text("Reintentar", color = Color(0xFF01398D))
                        }
                    }
                }
            }
            listaFiltrada.isEmpty() -> {
                // Estado vacío
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.WorkOutline,
                            contentDescription = "Sin actividades",
                            modifier = Modifier.size(50.dp),
                            tint = textColorSubTitle.copy(alpha = 0.2f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No tiene registros en este período",
                            color = textColorTitle,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        androidx.compose.material3.Button(
                            onClick = onCreateClick,
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF01398D),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text("Registrar actividad", fontSize = 13.sp)
                        }
                    }
                }
            }
            else -> {
                // Estado con datos - mostrar lista
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    itemsIndexed(listaFiltrada) { _, item ->
                        TarjetaActividad(
                            actividad = item,
                            viewModel = viewModel,
                            navController = navController,
                            function = { function(item) }
                        )
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TarjetaActividad(
    actividad: semiActivity,
    viewModel: ActividadViewModel,
    navController: NavController,
    function: (semiActivity) -> Unit
){
    val imageUrls = actividad.U_imageUrls
    var currentImageIndex by remember(actividad.DocEntry) { mutableStateOf(0) }
    var isCarouselPaused by remember(actividad.DocEntry) { mutableStateOf(false) }

    LaunchedEffect(actividad.DocEntry, isCarouselPaused) {
        if (imageUrls.size > 1 && !isCarouselPaused) {
            while (true) {
                delay(7000)
                if (!isCarouselPaused) {
                    currentImageIndex = (currentImageIndex + 1) % imageUrls.size
                }
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { function(actividad) },
        shape = RoundedCornerShape(8.dp),
        elevation = 0.dp,
        backgroundColor = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(8.dp),
        ) {
            // Columna imagen / círculo
            Box(
                modifier = Modifier
                    .size(75.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (imageUrls.isNotEmpty()) {
                    AsyncImage(
                        model = imageUrls[currentImageIndex],
                        contentDescription = "Imagen de la actividad",
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable { isCarouselPaused = !isCarouselPaused },
                        contentScale = ContentScale.Crop
                    )
                } else {
                    val letra = actividad.U_area.firstOrNull()?.uppercaseChar()?.toString() ?: "A"
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(blueDarkVistony, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = letra,
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))


            Column(
                modifier = Modifier.fillMaxHeight().weight(3f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.fillMaxHeight().padding(bottom = 20.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    // Diferenciador visual de estado
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        val chipColor = when {
                            actividad.U_status == "waiting_photos_upload" -> Color(0xFFF57F17)
                            isActivityFinished(actividad) -> Color(0xFF4CAF50)
                            else -> Color(0xFFFF9800)
                        }
                        val chipLabel = when {
                            actividad.U_status == "waiting_photos_upload" -> "Subiendo fotos"
                            isActivityFinished(actividad) -> "Finalizada"
                            else -> "En progreso"
                        }
                        Box(
                            modifier = Modifier
                                .background(chipColor.copy(alpha = 0.12f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .background(chipColor, CircleShape)
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    text = chipLabel,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = chipColor
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "- ", fontSize = 10.sp, color = chipColor, fontWeight = FontWeight.Bold)
                        LiveTimer(
                            startTime = actividad.U_InitialHour,
                            finishTime = actividad.U_FinalHour,
                            color = chipColor
                        )
                    }
                    
                    Text(
                        text = actividad.U_area + " - " + actividad.U_equipment,
                        fontSize = 12.sp,
                        lineHeight = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = blueDarkVistony
                    )
                    Text(
                        text = actividad.U_userName,
                        fontSize = 12.sp,
                        lineHeight = 14.sp,
                        color = blueDarkVistony,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = actividad.U_description_OT,
                        fontSize = 12.sp,
                        lineHeight = 14.sp,
                        color = Color(0xFF78909C)
                    )
                    
                    // Cronómetro/Tiempo
                    /*Spacer(modifier = Modifier.height(1.dp))
                    if (isActivityFinished(actividad)) {
                        Text(
                            text = "Finalizada - ${formatTimeDuration(actividad.U_InitialHour, actividad.U_FinalHour)}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = getActivityStatusColor(actividad)
                        )
                    } else {
                        LiveTimer(
                            startTime = actividad.U_InitialHour,
                            color = getActivityStatusColor(actividad)
                        )
                    }*/
                }

                Spacer(modifier = Modifier.height(2.dp))

                Divider(
                    color = Color(0xFFE0E0E0),
                    thickness = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            /*Icon(
                imageVector = if (inspection.U_Conformidad == "Y") Icons.Filled.CheckCircle else Icons.Filled.Error,
                contentDescription = null,
                tint = if (inspection.U_Conformidad == "Y") greenVistony else redVistony,
                modifier = Modifier.size(22.dp).weight(1f)
            )*/

        }
    }
}

// Composable para cronómetro en tiempo real
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LiveTimer(
    startTime: String?,
    finishTime: String? = null,
    color: Color
) {

    var currentTime by remember { mutableStateOf(LocalDateTime.now()) }
    val endTime = if (finishTime.isNullOrEmpty()) {
        currentTime
    } else {
        LocalDateTime.parse(finishTime, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
    }
    
    LaunchedEffect(Unit) {
        while (true) {
            currentTime = LocalDateTime.now()
            delay(1000) // Actualizar cada segundo
        }
    }
    
    Text(
        text = " ${formatTimeDuration(startTime, endTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))}",
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium,
        color = color
    )
}

// Funciones auxiliares para el cálculo de tiempo
@RequiresApi(Build.VERSION_CODES.O)
fun isActivityFinished(actividad: semiActivity): Boolean {
    val isFinished = !actividad.U_FinalHour.isNullOrBlank()
    android.util.Log.d("ActivityStatus", "DocEntry: ${actividad.DocEntry}, U_FinalHour: '${actividad.U_FinalHour}', isFinished: $isFinished")
    return isFinished
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatTimeDuration(startTime: String?, endTime: String?): String {
    if (startTime.isNullOrBlank()) return "--"
    
    return try {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val start = LocalDateTime.parse(startTime, formatter)
        val end = if (endTime.isNullOrBlank()) LocalDateTime.now() else LocalDateTime.parse(endTime, formatter)
        
        val duration = Duration.between(start, end)
        val hours = duration.toHours()
        val minutes = duration.toMinutesPart()
        
        val result = when {
            hours > 0 -> "${hours}h ${minutes}m"
            minutes > 0 -> "${minutes}m"
            else -> "< 1m"
        }
        
        android.util.Log.d("TimeDuration", "startTime: $startTime, endTime: $endTime, result: $result")
        result
    } catch (e: Exception) {
        android.util.Log.e("TimeDuration", "Error parsing time: ${e.message}")
        "--"
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun getActivityStatusText(actividad: semiActivity): String {
    return if (isActivityFinished(actividad)) {
        "Finalizada - ${formatTimeDuration(actividad.U_InitialHour, actividad.U_FinalHour)}"
    } else {
        "En progreso - ${formatTimeDuration(actividad.U_InitialHour, null)}"
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun getActivityStatusColor(actividad: semiActivity): Color {
    return if (isActivityFinished(actividad)) {
        Color(0xFF4CAF50) // Verde para finalizada
    } else {
        Color(0xFFFF9800) // Naranja para en progreso
    }
}

// Funciones para mostrar mensajes (puedes usar Snackbar o Toast)
fun showSuccessMessage(message: String) {
    // Implementación con Snackbar o Toast
}

fun showErrorMessage(message: String) {
    // Implementación con Snackbar o Toast
}