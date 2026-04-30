package com.vistony.app.Screen.Inspeccion

import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.vistony.app.ViewModel.OTViewModel
import com.vistony.app.ViewModel.OperarioViewModel
import com.vistony.app.ViewModel.SharedViewModel
import com.vistony.app.Screen.Generic.Drawers.CustomDrawer
import com.vistony.app.Screen.Generic.CustomOutlinedTextField
import com.vistony.app.Screen.Generic.TopBar
import com.vistony.app.ViewModel.InspectionViewModel
import com.vistony.app.ui.theme.theme.Dimensions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    viewModel: OperarioViewModel = hiltViewModel(),
    otViewModel: OTViewModel = hiltViewModel(),
    inspectionViewModel: InspectionViewModel = hiltViewModel(),
    id: String = "20304050",
    onClose: () -> Unit
) {

    val context = LocalContext.current
    val activity = context as Activity
    val windowSize = calculateWindowSizeClass(context)
    val padding_res = Dimensions.getPadding(windowSize.widthSizeClass)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val pagerState = rememberPagerState(pageCount = {2})

    /*ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { CustomDrawer(navController = navController, id = id) }
    ) {
        Scaffold(
            topBar = {
                TopBar("Inspección de Pallet", navController = navController, onMenuClick = {
                    scope.launch {
                        drawerState.open()
                    }
                })
            }
        ) { paddingValues ->
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFE6E9F1))

            ) {
                val screenWidth = maxWidth
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding_res)
                        //.border(2.dp, Color.White, RoundedCornerShape(24.dp))
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.White.copy(alpha = 0.44f)),
                    //.graphicsLayer { alpha = 0.44f },
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BodyIspeccion(
                        navController,
                        viewModel,
                        otViewModel,
                        sharedViewModel,
                        id
                    ) {
                        scope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    }
                    BodyDetalle(navController, viewModel, sharedViewModel, id)
                }
            }
        }
    }

    HorizontalPager(
        state = pagerState,
        userScrollEnabled = false // para controlar solo con botones
    ) { page ->
        when (page) {
            0 -> BodyIspeccion(navController, viewModel, otViewModel, sharedViewModel, id){ scope.launch { pagerState.animateScrollToPage(1) }}
            1 -> BodyDetalle(navController, inspectionViewModel, sharedViewModel, id)
        }
    }*/


    /*Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(600.dp) // Ajusta según tu diálogo
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        // Barra con botón para cambiar página
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = when (pagerState.currentPage) {
                    0 -> "Inspección"
                    1 -> "Detalle"
                    else -> ""
                },
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Cerrar")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
            userScrollEnabled = false // para controlar solo con botones
        ) { page ->
            when (page) {
                0 -> BodyIspeccion(navController, viewModel, otViewModel, sharedViewModel, id){ scope.launch { pagerState.animateScrollToPage(1) }}
                1 -> BodyDetalle(navController, inspectionViewModel, sharedViewModel, id)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botón para deslizar a la siguiente o anterior página
        Button(
            onClick = {
                if (pagerState.currentPage == 0) {
                    // Deslizar a Detalle
                    scope.launch { pagerState.animateScrollToPage(1) }
                } else {
                    // Volver a Inspección
                    scope.launch {
                        pagerState.animateScrollToPage(0)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (pagerState.currentPage == 0) "Ver Detalle" else "Volver a Inspección"
            )
        }
    }*/
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalMaterial3Api
@Composable
fun BodyIspeccion(
    navController: NavController,
    viewModel: OperarioViewModel,
    otViewModel: OTViewModel,
    sharedViewModel: SharedViewModel,
    id: String,
    onNext: () -> Unit
) {
    var turno by remember { mutableStateOf("") }
    var ot by rememberSaveable { mutableStateOf("") }
    var new_ot by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var um by rememberSaveable { mutableStateOf("") }
    var cantidad by rememberSaveable { mutableStateOf("") }
    var linea by rememberSaveable { mutableStateOf("") }
    val newLinea = remember { mutableStateOf("Seleccione") }
    val operador = rememberSaveable { mutableStateOf("") }
    val fecha by remember { mutableStateOf(SimpleDateFormat("dd/MM/yyyy").format(Date())) }
    val newfecha by remember { mutableStateOf(SimpleDateFormat("yyyyMMdd").format(Date())) }
    val expanded = remember { mutableStateOf(false) }
    val expandedLine = remember { mutableStateOf(false) }

    val operarioState = viewModel._operarioState
    val lineaState = viewModel._lineaState
    val otState = otViewModel._otState

    var showDialog by remember { mutableStateOf(false) }

    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    //operarioState.operarioResponse?.data?.map { it.Nonbre } ?: emptyList()
    //val options = operarioState.operarioResponse?.data?.map { Operario(it.ID, it.Nonbre) } ?: emptyList()
    val options = operarioState.operarioResponse?.data?.map { it.ID to it.Nonbre } ?: emptyList()
    var operarioId by remember { mutableStateOf("") }


    val context = LocalContext.current
    val activity = context as Activity
    val windowSize = calculateWindowSizeClass(context)

    val padding_res = Dimensions.getPadding(windowSize.widthSizeClass)
    val buttonHeight = Dimensions.getButtonHeight(windowSize.widthSizeClass)
    val bodyFontSize = Dimensions.getBodyFontSize(windowSize.widthSizeClass)


    val scanLauncher = rememberLauncherForActivityResult(
        contract = ScanContract(),
        onResult = { reslt ->
            //ot = reslt.contents ?: "Sin Lectura"
            val scannedText = reslt.contents ?: "Sin Lectura"
            val startIndex = scannedText.indexOf("(10)") + 4
            val endIndex = scannedText.indexOf("(17)")
            if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
                new_ot = scannedText.substring(startIndex, endIndex)
                ot = new_ot
            } else {
                ot = "Codigo de barra, no válido"
            }

        }
    )


    val currentOt by rememberUpdatedState(ot)
    LaunchedEffect(currentOt) {
        if (currentOt.isNotEmpty()) {
            otViewModel.getCodigoBarra(currentOt)

            um = otState.productoResponse?.data?.UM.toString()
            description = otState.productoResponse?.data?.Producto.toString()
            linea = otState.productoResponse?.data?.Linea.toString()
            newLinea.value = lineaState.lineaResponse?.data?.find { it.ID == linea }?.Descripcion ?: newLinea.value
            Log.i("VER", "ENTRO AL NO VACIO")
        } else {
            um = ""
            description = ""
            linea = ""
            Log.i("VER", "ENTRO AL VACIO")
        }
    }
    um = otState.productoResponse?.data?.UM.orEmpty()
    description = otState.productoResponse?.data?.Producto.orEmpty()
    linea = otState.productoResponse?.data?.Linea.orEmpty()
    newLinea.value =
        lineaState.lineaResponse?.data?.find { it.ID == linea }?.Descripcion ?: newLinea.value



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize()
    ) {
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = "Registro de Inspección",
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
        )
        Text(
            text = "Seleccione todos los campos y registre una nueva Inspección",
            fontSize = 12.sp,
        )
        Spacer(Modifier.height(25.dp))


        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            CustomOutlinedTextField(
                modifier = Modifier.weight(1f),
                value = fecha,
                label = "Fecha",
                readOnly = true
            )
            Spacer(modifier = Modifier.width(8.dp))
            CustomOutlinedTextField(
                modifier = Modifier.weight(1f),
                value = if (LocalTime.now()
                        .isBefore(LocalTime.parse("18:00:00")) && LocalTime.now()
                        .isAfter(LocalTime.parse("06:00:00"))
                ) {
                    turno = "Mañana"
                    "MAÑANA"
                } else if (LocalTime.now()
                        .isAfter(LocalTime.parse("18:00:00")) || LocalTime.now()
                        .isBefore(LocalTime.parse("06:00:00"))
                ) {
                    turno = "Noche"
                    "NOCHE"
                } else {
                    ""
                },
                onValueChange = { turno = it },
                label = "Turno",
                readOnly = true
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        CustomOutlinedTextField( // ---- OT ----
            value = ot,
            onValueChange = { ot = it },
            label = "OT",
            trailingIcon = {
                IconButton(onClick = { scanLauncher.launch(ScanOptions()) }
                ) {
                    Icon(Icons.Filled.CameraAlt, contentDescription = "Camera")
                }
            },
            keyboardOption = KeyboardOptions().copy(keyboardType = KeyboardType.Number),
            readOnly = false
        )
        Spacer(modifier = Modifier.height(8.dp))
        CustomOutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = "Descripción",
            readOnly = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CustomOutlinedTextField(
                modifier = Modifier.weight(1f),
                value = if (um === null) " " else um,
                onValueChange = { um = it },
                label = "UM",
                readOnly = true
            )
            Spacer(modifier = Modifier.width(8.dp))
            CustomOutlinedTextField(
                modifier = Modifier.weight(1f),
                value = cantidad,
                onValueChange = { cantidad = it.filter { char -> char.isDigit() } },
                label = "Cant. de cajas y/o Pallets muestreados",
                keyboardOption = KeyboardOptions().copy(keyboardType = KeyboardType.Number),
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        //val options = operarioState.operarioResponse?.data?.map { it.Nonbre } ?: emptyList()
        val filteredOptions = remember { mutableStateOf(options) }

        LaunchedEffect(operador.value) {
            if (operador.value.isEmpty()) {
                filteredOptions.value = options
                expanded.value = false
            } else {
                expanded.value = true
                filteredOptions.value =
                    withContext(Dispatchers.Default) { // En segundo plano
                        //options.filter { it.Nonbre.contains(operador.value, ignoreCase = true) }
                        options.filter { it.second.contains(operador.value, ignoreCase = true) }
                    }
            }
        }

        /*GenericDropdownMenu(
            label = "Maquinista Encargado",
            options = filteredOptions.value,
            selectedValue = operador.value,
            onValueChange = { operador.value = it },
            onCodeChange = { /*maquinaId = it*/ },
            expanded = expanded.value,
            onExpandedChange = { expanded.value = it }
        )*/

        ExposedDropdownMenuBox(
            expanded = expanded.value,
            onExpandedChange = { expanded.value = !expanded.value }
        ) {
            CustomOutlinedTextField(
                modifier = Modifier.menuAnchor(),
                value = operador.value,
                onValueChange = {newValue ->
                    operador.value = newValue.filter { char-> char.isLetter() }
                    /*filterOptions(newValue)*/},
                label = "Maquinista Encargado",
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
                readOnly = false
            )
            ExposedDropdownMenu(
                modifier = Modifier
                    .background(Color.White)
                    .clip(RoundedCornerShape(8.dp)),
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }) {
                filteredOptions.value.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.second, color = Color.Black) },
                        onClick = {
                            operador.value = option.second
                            operarioId = option.first
                            expanded.value = false
                        }
                    )
                }
            }
        }

        /*val options = operarioState.operarioResponse?.data?.map { it.Nonbre } ?: emptyList()
        CustomSpinner(expanded, options, operador, textFieldSize)*/
        Spacer(modifier = Modifier.height(8.dp))
        CustomOutlinedTextField(
            value = linea,
            onValueChange = { linea = it },
            label = "Línea",
            readOnly = true
        )


        Spacer(modifier = Modifier.weight(1f))
        BotonH(
            ot,
            description,
            um,
            cantidad,
            turno,
            linea,
            operador.value,
            operarioId,
            newfecha,
            navController,
            sharedViewModel,
            id,
            buttonHeight,
            padding_res,
            bodyFontSize,
            onNext
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun BotonH(
    ot: String,
    description: String,
    um: String,
    cantidad: String,
    turno: String,
    linea: String,
    operador: String,
    operarioId: String,
    fecha: String,
    navController: NavController,
    sharedViewModel: SharedViewModel,
    id: String,
    buttonHeight: Dp,
    padding_res: Dp,
    bodyFontSize: Float,
    onNext: () -> Unit,
) {
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val stateButton =
        ot.isNotEmpty() && description.isNotEmpty() && um.isNotEmpty() && cantidad.isNotEmpty() && operador.isNotEmpty()

    LaunchedEffect(ot, description, um, cantidad, turno, fecha, linea, operador, operarioId,id) {
        sharedViewModel.turno = turno
        sharedViewModel.ot = ot
        sharedViewModel.description = description
        sharedViewModel.um = um
        sharedViewModel.cantidad = cantidad
        sharedViewModel.linea = linea
        sharedViewModel.operador = operador
        sharedViewModel.fecha = fecha
        sharedViewModel.usuario = id
    }

    Button(
        enabled = stateButton && !isLoading,
        onClick = {
            isLoading = true
            Log.e("vista", sharedViewModel.fecha + " " + sharedViewModel.turno)
            scope.launch {
                try {
                    onNext()
                } finally {
                    isLoading = false
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(buttonHeight)
            .padding(horizontal = padding_res)
            .clip(RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFC6A68),
            contentColor = Color.White,
            disabledContainerColor = Color(0xFFD1D5DB),
            disabledContentColor = Color.White
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = "Procesando...", fontSize = bodyFontSize.sp)
        } else {
            Text(text = "Inspección", fontSize = bodyFontSize.sp)
        }
    }

}


