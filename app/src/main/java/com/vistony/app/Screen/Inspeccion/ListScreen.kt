package com.vistony.app.Screen.Inspeccion

import android.app.Activity
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CleanHands
import androidx.compose.material.icons.filled.Compress
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TurnLeft
import androidx.compose.material.icons.filled.Warehouse
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.vistony.app.Entidad.Inspeccion
import com.vistony.app.Entidad.UserState
import com.vistony.app.Screen.Generic.CustomSearchText
import com.vistony.app.ViewModel.InspectionViewModel
import com.vistony.app.Screen.Generic.Drawers.CustomDrawer
import com.vistony.app.Screen.Generic.Drawers.BottomBar
import com.vistony.app.Screen.Generic.Drawers.RightCurtainDrawer
import com.vistony.app.Screen.Generic.Recursos.UnsplashImages
import com.vistony.app.Screen.Generic.TopBar
import com.vistony.app.ViewModel.LoginViewModel
import com.vistony.app.ViewModel.OTViewModel
import com.vistony.app.ViewModel.OperarioViewModel
import com.vistony.app.ViewModel.SharedViewModel
import com.vistony.app.ui.theme.theme.Dimensions
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

val redVistony = Color(0xFFD6001C)
val blueDarkVistony = Color(0xFF253746)
val blueLightVistony = Color(0xFF0957c3)
val greenVistony = Color(0xFF98BC3D)


val backGroundLigth = Color(0XFFF7F7F7) // 0xFF5c6a83
val textColorTitle = blueDarkVistony
val textColorSubTitle = blueDarkVistony
val contentColor = blueDarkVistony


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterialApi::class,
    ExperimentalMaterial3Api::class
)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ListScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    inspViewModel: InspectionViewModel = hiltViewModel(),
    viewModel: OperarioViewModel = hiltViewModel(),
    otViewModel: OTViewModel = hiltViewModel(),
    id: String = "prueba",
    userState: UserState
) {
    val loginViewModel = hiltViewModel<LoginViewModel>(LocalContext.current as ComponentActivity)
    val role by loginViewModel.userRole.collectAsState()

    val context = LocalContext.current
    val activity = context as Activity
    val windowSize = calculateWindowSizeClass(context)
    val padding_res = Dimensions.getPadding(windowSize.widthSizeClass)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    var inspSeleccionada by remember { mutableStateOf<Inspeccion?>(null) }
    var showDrawerDetalle by remember { mutableStateOf(false) }
    var showDrawerHome by remember { mutableStateOf(false) }
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
            sheetContent = { BottomBar("inspection",userState.currentUser) }
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
                        BodyList(
                            paddingValues =paddingValues,
                            navController = navController,
                            inspViewModel = inspViewModel,
                            id = id,
                            function = {
                                inspSeleccionada = it
                                showDrawerDetalle = true
                            }
                        )
                },
                floatingActionButton = {
                    IconButton(
                        modifier = Modifier.size(60.dp),
                        onClick = {
                            //navController.navigate("home/${id}")
                            showDrawerHome = true
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


        // ============================ FORMULARIO DETALLE INSPECCIÓN ================================= //
        RightCurtainDrawer(
            visible = showDrawerDetalle,
            onClose = { showDrawerDetalle = false },
            animationDuration = 400 // ms, ajusta velocidad
        ) {
            IconButton(onClick = {showDrawerDetalle = false}) {
                Icon(Icons.Default.TurnLeft, contentDescription = "Cerrar")
            }
            Column(Modifier.padding(start = 24.dp, end = 24.dp, top = 32.dp)) {
                DetalleInspeccion(inspection = inspSeleccionada, onClose = {})
            }
        }


        // ============================ FORMULARIO REGISTRO INSPECCIÓN ================================= //
        RightCurtainDrawer(
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
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BodyList(
    id: String,
    paddingValues: PaddingValues,
    navController: NavController,
    inspViewModel: InspectionViewModel,
    function: (Inspeccion) -> Unit
) {
    val listState by inspViewModel.listInspeccionState.collectAsState()
    var searchText by remember { mutableStateOf("") }
    val nuevalista = listState.listInspeccion.data.asReversed()

    val listaFiltrada = remember(nuevalista, searchText) {
        nuevalista.filter { _inspection ->

            // Filtro por texto de búsqueda
            val searchFilter = searchText.isEmpty() || listOf(
                _inspection.OT,
                _inspection.U_Usuario,
                _inspection.U_Fecha ?: ""
            ).any { it.contains(searchText, ignoreCase = true) }

            searchFilter
        }
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
            .wrapContentSize()
            //.background(Color(0xFFf7f7f7))
            .background(backGroundLigth)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Inspección de Pallets",
            color = textColorTitle,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Evalua tus procedimientos en Producción",
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
            placeholderText = "Buscar..."
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            itemsIndexed(listaFiltrada) { index,item ->
                val imageUrl = UnsplashImages.urls[index % UnsplashImages.urls.size]
                TarjetaInspeccion(
                    inspection = item,
                    inspectionViewModel = inspViewModel,
                    navController = navController,
                    id = id,
                    imageUrl = imageUrl,
                    function = { function(item) }
                )
            }
        }
        /*Detalle(
            isVisible = showDialog,
            titulo = "Detalle de Inspección",
            onDismiss = { showDialog = false },
            data = item,
            content = {}
        )*/
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TarjetaInspeccion(
    inspection: Inspeccion,
    inspectionViewModel: InspectionViewModel,
    navController: NavController,
    id: String,
    imageUrl: String,
    function: (Inspeccion) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { function(inspection) },
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
                        text = inspection.OT,
                        fontSize = 12.sp,
                        lineHeight = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = blueDarkVistony
                    )
                    Text(
                        text = inspection.U_Usuario,
                        fontSize = 12.sp,
                        lineHeight = 14.sp,
                        color = blueDarkVistony,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${inspection.U_Fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))} - ${inspection.U_Turno}",
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
            Icon(
                imageVector = if (inspection.U_Conformidad == "Y") Icons.Filled.CheckCircle else Icons.Filled.Error,
                contentDescription = null,
                tint = if (inspection.U_Conformidad == "Y") greenVistony else redVistony,
                modifier = Modifier.size(22.dp).weight(1f)
            )

        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TarjetaInspeccion2(
    inspection: Inspeccion,
    inspectionViewModel: InspectionViewModel,
    navController: NavController,
    id: String,
    imageUrl: String,
    function: (Inspeccion) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { function(inspection) }
    ) {
        // Tarjeta principal con diseño diagonal
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        ) {
            val width = size.width
            val height = size.height

            // Fondo blanco (ocupa mayor proporción)
            drawRect(
                color = Color.White,
                size = Size(width, height)
            )

            // Sección roja diagonal (de abajo hacia arriba)
            val path = Path().apply {
                moveTo(width * 0.1f, 0f) // Esquina inferior izquierda
                lineTo(width * 0.5f, 0f) // Punto superior (30% del ancho)
                lineTo(0f, 0f) // Esquina superior izquierda
                close()
            }

            drawPath(
                path = path,
                color = Color(0xFFD32F2F) // redVistony
            )

            // Borde de la tarjeta
            /*drawRect(
                color = Color(0xFFBDBDBD),
                size = Size(width, height),
                style = Stroke(width = 2.dp.toPx())
            )*/
        }

        // Contenido de la tarjeta
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Sección roja con imagen circular
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .padding(2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Imagen de la máquina",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Sección blanca con contenido
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Información principal
                Column {
                    Text(
                        text = inspection.OT,
                        fontSize = 14.sp,
                        lineHeight = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1976D2) // redVistony
                    )
                    Text(
                        text = inspection.U_Usuario,
                        fontSize = 12.sp,
                        color = Color(0xFF1976D2), // blueLightVistony
                        maxLines = 1,
                        lineHeight = 14.sp,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${inspection.U_Fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))} - ${inspection.U_Turno}",
                        fontSize = 11.sp,
                        lineHeight = 14.sp,
                        color = Color(0xFF78909C)
                    )
                }

                // Línea divisoria y estado
               /* Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Divider(
                        color = Color(0xFFD32F2F),
                        thickness = 1.dp,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Icon(
                        imageVector = if (inspection.U_Conformidad == "Y") Icons.Filled.CheckCircle else Icons.Filled.Error,
                        contentDescription = null,
                        tint = if (inspection.U_Conformidad == "Y") Color(0xFF4CAF50) else Color(0xFFD32F2F), // greenVistony : redVistony
                        modifier = Modifier.size(20.dp)
                    )
                }*/
            }
        }

        // Clavos en las esquinas
        NailCorner(
            modifier = Modifier.align(Alignment.TopStart),
            offsetX = 8.dp,
            offsetY = 8.dp
        )
        NailCorner(
            modifier = Modifier.align(Alignment.TopEnd),
            offsetX = (-8).dp,
            offsetY = 8.dp
        )
        NailCorner(
            modifier = Modifier.align(Alignment.BottomStart),
            offsetX = 8.dp,
            offsetY = (-8).dp
        )
        NailCorner(
            modifier = Modifier.align(Alignment.BottomEnd),
            offsetX = (-8).dp,
            offsetY = (-8).dp
        )
    }
}

@Composable
fun NailCorner(
    modifier: Modifier = Modifier,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 0.dp
) {
    Box(
        modifier = modifier
            .offset(x = offsetX, y = offsetY)
            .size(8.dp)
            .clip(CircleShape)
            .background(Color(0xFF424242))
    ) {
        // Efecto de brillo del clavo
        Box(
            modifier = Modifier
                .size(3.dp)
                .offset(x = 1.dp, y = 1.dp)
                .clip(CircleShape)
                .background(Color(0xFF757575))
        )
    }
}

@Composable
fun DetalleInspeccion(
    inspection: Inspeccion?,
    onClose: () -> Unit // Para manejar el botón cerrar
) {
    if (inspection == null) return

    // Determinar colores y textos según conformidad
    val conformidadOK = inspection.U_Conformidad == "Y"
    val conformidadColor = if (conformidadOK) Color(0xFF4CAF50) else Color(0xFFF44336)
    val conformidadText = if (conformidadOK) "CONFORME" else "NO CONFORME"
    val conformidadIcon = if (conformidadOK) Icons.Filled.CheckCircle else Icons.Filled.Error


    val checkListItems = listOf(
        CheckListItem("Peso", inspection.U_Peso_Check, inspection.U_Peso_Comment, Icons.Filled.Balance),
        CheckListItem("Etiqueta", inspection.U_Etiq_Check, inspection.U_Etiq_Comment, Icons.Filled.LocalOffer),
        CheckListItem("Lote", inspection.U_Lot_Check, inspection.U_Lot_Comment, Icons.Filled.Dns),
        CheckListItem("Limpieza", inspection.U_Limp_Check, inspection.U_Limp_Comment, Icons.Filled.CleanHands),
        CheckListItem("Sellado", inspection.U_Sell_Check, inspection.U_Sell_Comment, Icons.Filled.Lock),
        CheckListItem("Encogimiento", inspection.U_Enc_Check, inspection.U_Enc_Comment, Icons.Filled.Compress),
        CheckListItem("Rótulo", inspection.U_Rotulo_Check, inspection.U_Rotulo_Comment, Icons.Filled.Label),
        CheckListItem("Pallet", inspection.U_Palet_Check, inspection.U_Palet_Comment, Icons.Filled.Warehouse)
    )


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = "Detalle de Inspección",
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
        )
        Text(
            text = "Inspección realizada el ${inspection?.U_Fecha!!.split(" ")[0]}",
            fontSize = 12.sp,
        )
        Spacer(Modifier.height(25.dp))


        // Encabezado con icono y usuario
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Usuario",
                modifier = Modifier.size(40.dp)
            )
            Spacer(Modifier.width(10.dp))
            Column {
                Text(
                    text = inspection.U_Usuario,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Maquinista: ${inspection.U_Maquinista}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Card principal con OT, estado y fecha
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "OT: ${inspection.OT}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Fecha: ${inspection.U_Fecha.split(" ")[0]}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "Turno: ${inspection.U_Turno}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                // Estado de conformidad
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = conformidadIcon,
                        contentDescription = conformidadText,
                        tint = conformidadColor,
                        modifier = Modifier.size(40.dp)
                    )
                    Text(
                        text = conformidadText,
                        color = conformidadColor,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        if (inspection.U_Conformidad_Comment.isNotBlank()) {
            //Spacer(Modifier.height(8.dp))
            Surface(
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.errorContainer,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = inspection.U_Conformidad_Comment,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }

        // Checklist visual
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = "Checklist",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
        )
        //Spacer(Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            checkListItems.forEach { item ->
                DetalleCheckListItem(item)
            }
        }


        // Cantidad
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Cantidad: ${inspection.U_Cantidad}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun DetalleCheckListItem(item: CheckListItem) {
    Card(
        shape = RoundedCornerShape(10.dp),
        //elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(Color.Transparent)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono representativo del item
            Icon(
                imageVector = item.icon,
                contentDescription = item.label,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )

            Spacer(Modifier.width(10.dp))

            // Nombre del item
            Text(
                text = item.label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            Spacer(Modifier.width(10.dp))

            // Comentario (en un chip o badge si quieres)
            if (item.comment.isNotBlank()) {
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = item.comment,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
            Spacer(Modifier.width(10.dp))
            // Estado (Check o Close)
            Icon(
                imageVector = if (item.check == "Y") Icons.Filled.CheckCircle else Icons.Filled.Cancel,
                contentDescription = if (item.check == "Y") "Correcto" else "Incorrecto",
                tint = if (item.check == "Y") Color(0xFF4CAF50) else Color(0xFFF44336),
                modifier = Modifier.size(28.dp)
            )
        }
    }
}



data class CheckListItem(
    val label: String,
    val check: String,
    val comment: String,
    val icon: ImageVector // Puedes asociar un icono diferente a cada item si quieres
)
