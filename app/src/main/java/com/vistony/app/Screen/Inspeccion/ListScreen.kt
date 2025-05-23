package com.vistony.app.Screen.Inspeccion

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vistony.app.Entidad.Inspeccion
import com.vistony.app.Screen.Generic.CustomSearchText
import com.vistony.app.ViewModel.InspectionViewModel
import com.vistony.app.Screen.Generic.Drawers.CustomDrawer
import com.vistony.app.Screen.Generic.Drawers.BottomBar
import com.vistony.app.Screen.Generic.Drawers.RightCurtainDrawer
import com.vistony.app.Screen.Generic.TopBar
import com.vistony.app.ViewModel.LoginViewModel
import com.vistony.app.ViewModel.OTViewModel
import com.vistony.app.ViewModel.OperarioViewModel
import com.vistony.app.ViewModel.SharedViewModel
import com.vistony.app.ui.theme.theme.Dimensions
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import kotlin.math.absoluteValue

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
    id: String = "prueba"
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

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { CustomDrawer(navController = navController, id) }
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
                        }
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
                            containerColor = Color(0xFFFC6A68),
                            contentColor = Color.White
                        )
                    ){
                        Icon(Icons.Filled.Add, contentDescription = "")

                    }

                }
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
            text = "Inspección de Pallets",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Evalua tus procedimientos en Producción",
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
            items(nuevalista) { item ->
                TarjetaInspeccion(
                    inspection = item,
                    inspectionViewModel = inspViewModel,
                    navController = navController,
                    id = id,
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
    function: (Inspeccion) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    val estado by inspectionViewModel.isLoading.collectAsState()
    //val paradaState by paradaViewModel.paradas.collectAsState()
    val context = LocalContext.current

    val backgroundColor = remember(inspection.OT) {
        val colors = listOf(
            Color(0xFFCACACA),
            Color(0xFFC6C6C6),
            Color(0xFFB1B1B1),
            Color(0xFFAEAEAE),
            Color(0xFFA1A1A1)

        )
        colors[inspection.OT.hashCode().absoluteValue % colors.size]
    }


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
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Columna imagen / círculo
            Box(
                modifier = Modifier
                    .weight(0.7f)
                    .size(75.dp)
                    .clip(CircleShape)
                    .background(backgroundColor)
                    .clickable {},
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
                val cadena = inspection.U_Usuario.split(" ")[1]

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
                    .weight(3f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = inspection.OT,
                    fontSize = 12.sp,
                    lineHeight = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B2733)
                )
                Text(
                    text = inspection.U_Usuario,
                    fontSize = 12.sp,
                    lineHeight = 14.sp,
                    color = Color(0xFF455A64),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${inspection.U_Fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))} - ${
                        inspection.U_Turno
                    }",
                    fontSize = 12.sp,
                    lineHeight = 14.sp,
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

    /*if (showDialog) {
        when (estado) {
            EstadoInspeccion.Cargando -> {
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

            EstadoInspeccion.Exitoso -> {
                CustomAlertDialog(
                    showDialog = showDialog,
                    title = "Éxito",
                    message = paradaState.paradaResponse.data,
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

            is EstadoInspeccion.Error -> {
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
    }*/
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
