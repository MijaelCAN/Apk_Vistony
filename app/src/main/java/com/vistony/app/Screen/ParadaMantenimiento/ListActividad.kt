package com.vistony.app.Screen.ParadaMantenimiento

import android.app.Activity
import android.os.Build
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
import androidx.compose.material.rememberModalBottomSheetState
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.vistony.app.Entidad.Actividad
import com.vistony.app.Entidad.Activity2
import com.vistony.app.Entidad.UserState
import com.vistony.app.Screen.Generic.CustomSearchText
import com.vistony.app.Screen.Generic.Drawers.BottomBar
import com.vistony.app.Screen.Generic.Drawers.BottomCurtainDrawer
import com.vistony.app.Screen.Generic.Drawers.CustomDrawer
import com.vistony.app.Screen.Generic.Drawers.RightCurtainDrawer
import com.vistony.app.Screen.Generic.Recursos.UnsplashImages
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
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


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
    userState: UserState
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

    SideEffect {
        systemUiController.setStatusBarColor(
            color = topBarColor,
            darkIcons = true
        )
    }


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { CustomDrawer(navController = navController, id, userState = userState) }
    ) {
        ModalBottomSheetLayout(
            modifier = Modifier.fillMaxWidth(),
            sheetState = bottomSheetState,
            sheetContent = { BottomBar("inspection") }
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
                        }
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
                        function = {
                            actividadSeleccionada = it
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
                            containerColor = redVistony,
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
            IconButton(onClick = {showDrawerDetalle = false}) {
                Icon(Icons.Default.TurnLeft, contentDescription = "Cerrar")
            }
            Column(Modifier.padding(start = 24.dp, end = 24.dp, top = 32.dp)) {
                DetalleActividad(
                    viewModel = viewModel,
                    actividad = actividadSeleccionada,
                    onClose = {},
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
            IconButton(onClick = {showDrawerActivity = false}) {
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
    function: (Activity2) -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()
    val listActividades = uiState.actividades
    var searchText by remember { mutableStateOf("") }
    val nuevalista = listActividades.asReversed()

    val listaFiltrada = remember(nuevalista, searchText) {
        nuevalista.filter { _actividad ->

            // Filtro por texto de búsqueda
            val searchFilter = searchText.isEmpty() || listOf(
                _actividad.description ?: "",
                _actividad.equipment.name ?: "",
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
            text = "Se lista las actividades desarrolladas en este horario",
            color = textColorSubTitle,
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
            placeholderText = "Buscar actividad..."
        )

        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            itemsIndexed(listaFiltrada) { index,item ->
                val imageUrl = UnsplashImages.urls[index % UnsplashImages.urls.size]
                TarjetaActividad(
                    actividad = item,
                    viewModel = viewModel,
                    navController = navController,
                    imageUrl = imageUrl,
                    function = { function(item) }
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TarjetaActividad(
    actividad: Activity2,
    viewModel: ActividadViewModel,
    navController: NavController,
    imageUrl: String,
    function: (Activity2) -> Unit
){
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
                    //.weight(0.7f)
                    .size(75.dp)
                    .clip(CircleShape)
                    .clickable {},
                contentAlignment = Alignment.Center
            ) {

                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Imagen de la máquina",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
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
                    Text(
                        text = actividad.area + " - " + actividad.equipment.name,
                        fontSize = 12.sp,
                        lineHeight = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = blueDarkVistony
                    )
                    Text(
                        text = actividad.userName,
                        fontSize = 12.sp,
                        lineHeight = 14.sp,
                        color = blueDarkVistony,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${actividad.startTime?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))} - ${actividad.reason.name}",
                        fontSize = 12.sp,
                        lineHeight = 14.sp,
                        color = Color(0xFF78909C)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

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
