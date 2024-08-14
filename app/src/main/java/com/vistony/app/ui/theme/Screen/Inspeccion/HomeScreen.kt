package com.vistony.app.ui.theme.Screen.Inspeccion

import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.journeyapps.barcodescanner.CaptureActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.vistony.app.R
import com.vistony.app.ViewModel.OTViewModel
import com.vistony.app.ViewModel.OperarioViewModel
import com.vistony.app.ViewModel.SharedViewModel
import com.vistony.app.ui.theme.Screen.Generic.CustomDrawer
import com.vistony.app.ui.theme.Screen.Generic.CustomOutlinedTextField
import com.vistony.app.ui.theme.Screen.Generic.TopBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    viewModel: OperarioViewModel = hiltViewModel(),
    otViewModel: OTViewModel = hiltViewModel()
) {
    /*val scrollState = rememberScrollState()
    Scaffold(
        topBar = { TopBar(navController = navController) },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(color = Color(0xFF0B4FAF))
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Box(
                modifier = Modifier
                    .width(610.dp)
                    .height(210.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(Color.White)
            ) {
                Image(
                    modifier = Modifier.size(width = 600.dp, height = 200.dp),
                    painter = painterResource(id = R.drawable.vistony),
                    contentDescription = "Logo"
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            BodyHome(navController, viewModel, otViewModel, sharedViewModel)
        }
    }*/
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { CustomDrawer(navController = navController) }
    ) {
        Scaffold(
            topBar = {
                TopBar("Inspección de Pallet", navController = navController, onMenuClick = {
                    scope.launch {
                        drawerState.open()
                    }
                })
            },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(color = Color(0xFF0B4FAF))
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Box(
                        modifier = Modifier
                            .width(610.dp)
                            .height(210.dp)
                            .clip(RoundedCornerShape(25.dp))
                            .background(Color.White)
                    ) {
                        Image(
                            modifier = Modifier.size(width = 600.dp, height = 200.dp),
                            painter = painterResource(id = R.drawable.vistony),
                            contentDescription = "Logo"
                        )
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                    BodyHome(navController, viewModel, otViewModel, sharedViewModel)
                }
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalMaterial3Api
@Composable
fun BodyHome(
    navController: NavController,
    viewModel: OperarioViewModel,
    otViewModel: OTViewModel,
    sharedViewModel: SharedViewModel
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


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .wrapContentSize()
            .clip(RoundedCornerShape(25.dp))
            .background(color = Color(0xFFFFFFFF))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize()
                .padding(vertical = 16.dp, horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                Spacer(modifier = Modifier.width(16.dp))
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
            CustomOutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = "Descripción",
                readOnly = true
            )
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
                Spacer(modifier = Modifier.width(16.dp))
                CustomOutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = cantidad,
                    onValueChange = { cantidad = it.filter { char -> char.isDigit() } },
                    label = "Cant. de cajas y/o Pallets",
                    keyboardOption = KeyboardOptions().copy(keyboardType = KeyboardType.Number),
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val options = operarioState.operarioResponse?.data?.map { it.Nonbre } ?: emptyList()
                val filteredOptions = remember { mutableStateOf(options) }

                LaunchedEffect(operador.value) {
                    if (operador.value.isEmpty()) {
                        filteredOptions.value = options
                        expanded.value = false
                    } else {
                        expanded.value = true
                        filteredOptions.value =
                            withContext(Dispatchers.Default) { // En segundo plano
                                options.filter { it.contains(operador.value, ignoreCase = true) }
                            }
                    }
                }

                ExposedDropdownMenuBox(
                    modifier = Modifier.weight(1f),
                    expanded = expanded.value,
                    onExpandedChange = { expanded.value = !expanded.value
                    }) {
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
                                text = { Text(option, color = Color.Black) },
                                onClick = {
                                    operador.value = option
                                    expanded.value = false
                                }
                            )
                        }
                    }
                }


                /*val onClickAction = remember { { expanded.value = true } }
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .weight(1f)
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = { onClickAction() })
                        }
                ) {
                    CustomOutlinedTextField(
                        modifier = Modifier
                            //.weight(1f)
                            .onGloballyPositioned { textFieldSize = it.size.toSize() },
                        value = operador.value,
                        onValueChange = { operador.value = it },
                        label = "Maquinista Encargado",
                        trailingIcon = {
                            IconButton(onClick = { expanded.value = true }) {
                                Icon(Icons.Filled.ArrowDropDown, contentDescription = "Expandir")
                            }
                        },
                        readOnly = true
                    )
                }*/
                /*val options = operarioState.operarioResponse?.data?.map { it.Nonbre } ?: emptyList()
                CustomSpinner(expanded, options, operador, textFieldSize)*/
                Spacer(modifier = Modifier.width(16.dp))
                CustomOutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = linea,
                    onValueChange = { linea = it },
                    label = "Línea",
                    readOnly = true
                )
                //--------------------- Linea ----------------------------

                /*ExposedDropdownMenuBox(
                    modifier = Modifier.weight(1f),
                    expanded = expandedLine.value,
                    onExpandedChange = { expandedLine.value = !expandedLine.value }) {
                    CustomOutlinedTextField(
                        modifier = Modifier.menuAnchor(),
                        value = newLinea.value,
                        onValueChange = {},
                        label = "Línea",
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
                        readOnly = true
                    )
                    val optionsLinea =
                        lineaState.lineaResponse?.data?.map { it.Descripcion } ?: emptyList()


                    ExposedDropdownMenu(
                        modifier = Modifier
                            .background(Color.White)
                            .clip(RoundedCornerShape(8.dp)),
                        expanded = expandedLine.value,
                        onDismissRequest = { expandedLine.value = false }) {
                        optionsLinea.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option, color = Color.Black) },
                                onClick = {
                                    newLinea.value = option
                                    expandedLine.value = false
                                }
                            )
                        }
                    }
                }*/

            }
            Spacer(modifier = Modifier.height(30.dp))
            BotonH(
                ot,
                description,
                um,
                cantidad,
                turno,
                linea,
                operador.value,
                newfecha,
                navController,
                sharedViewModel
            )
            /*DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.width(200.dp),
                offset = DpOffset(0.dp, 0.dp),
                properties = PopupProperties(focusable = true)
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            operador = option
                            expanded = false
                        }
                    )
                }
            }*/
            /*AlertDialog(
                modifier = Modifier
                    .width(400.dp)
                    .height(300.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(Color.White),
                onDismissRequest = {showDialog = false},
                title = {},
                text = {},
                confirmButton = {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 48.dp, vertical = 0.dp),
                        onClick = { navController.navigate("home") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0054A3))
                    ) {
                        Text(text = "Cerrar", color = Color.White)
                    }
                })*/

        }
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
    fecha: String,
    navController: NavController,
    sharedViewModel: SharedViewModel
) {
    //var stateButton by remember { mutableStateOf(false) }
    val stateButton =
        ot.isNotEmpty() && description.isNotEmpty() && um.isNotEmpty() && cantidad.isNotEmpty() && operador.isNotEmpty() && linea.isNotEmpty()
    LaunchedEffect(ot, description, um, cantidad, turno, fecha, linea, operador) {
        sharedViewModel.turno = turno
        sharedViewModel.ot = ot
        sharedViewModel.description = description
        sharedViewModel.um = um
        sharedViewModel.cantidad = cantidad
        sharedViewModel.linea = linea
        sharedViewModel.operador = operador
        sharedViewModel.fecha = fecha
        //stateButton = ot.isNotEmpty() && description.isNotEmpty() && um.isNotEmpty() && cantidad.isNotEmpty()
    }
    val buttonColors = ButtonDefaults.elevatedButtonColors(
        containerColor = if (stateButton) Color(0xFF0054A3) else Color(0XFF9C9B9B),
        contentColor = if (stateButton) Color(0xFFC9C9C9) else Color.White,
        disabledContentColor = Color.White,
        disabledContainerColor = Color(0XFF9C9B9B)
    )

    ElevatedButton(
        modifier = Modifier
            .width(300.dp)
            .height(58.dp),
        enabled = stateButton,
        onClick = {
            Log.e("vista", sharedViewModel.fecha + " " + sharedViewModel.turno)
            navController.navigate("detalle")
        },
        colors = buttonColors,
        shape = RoundedCornerShape(18.dp)
    ) {
        Text(text = "Inspección")
    }
}
/*
Dosimetría
Sonometría
Iluminación
Estrés Térmico
Estrés por Frio
Confort Térmico
Radiación UV
Radiación Electromagnética
Vibración
Velocidad del Aire
Humedad Relativa
 */


