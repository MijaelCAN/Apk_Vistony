package com.vistony.app.Screen.Parada

import android.os.Build
import android.util.Log
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vistony.app.Entidad.Parada
import com.vistony.app.Extras.formatoFecha
import com.vistony.app.Extras.formatoServidor
import com.vistony.app.R
import com.vistony.app.Screen.Generic.CustomAlertDialog
import com.vistony.app.Screen.Generic.CustomButton
import com.vistony.app.Screen.Generic.CustomDrawer
import com.vistony.app.Screen.Generic.CustomOutlinedTextField
import com.vistony.app.Screen.Generic.DateOutlinedTextField
import com.vistony.app.Screen.Generic.Detalle
import com.vistony.app.Screen.Generic.DialogType
import com.vistony.app.Screen.Generic.TableCell
import com.vistony.app.Screen.Generic.TableHeaderCell
import com.vistony.app.Screen.Generic.TableScreen
import com.vistony.app.Screen.Generic.TopBar
import com.vistony.app.ViewModel.EstadoParada
import com.vistony.app.ViewModel.ParadaViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ListParada(
    navController: NavController,
    paradaViewModel: ParadaViewModel = hiltViewModel(),
    id: String
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { CustomDrawer(navController = navController, id = id) }
    ) {
        Scaffold(
            topBar = {
                TopBar("Lista de Paradas", navController = navController, onMenuClick = {
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
                        .background(color = Color(0xFF0B4FAF)),
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
                    BodyListParada(navController, id, paradaViewModel)
                }
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BodyListParada(navController: NavController, id: String, paradaViewModel: ParadaViewModel) {

    var fechaFinal by rememberSaveable { mutableStateOf<LocalDateTime?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var showDialogDetalle by remember { mutableStateOf(false) }
    var item by remember { mutableStateOf(Parada()) }
    var txt_estado = rememberSaveable { mutableStateOf("") }

    var selectedDateIni by remember { mutableStateOf(LocalDate.now()) }
    var showDialogDateIni by remember { mutableStateOf(false) }
    var selectedDateFin by remember { mutableStateOf(LocalDate.now()) }
    var showDialogDateFin by remember { mutableStateOf(false) }

    val expanded = remember { mutableStateOf(false) }
    val options = listOf("Iniciado", "Finalizado")

    val paradas by paradaViewModel.listParadas.collectAsState()
    val estado  by paradaViewModel.estadoParada.collectAsState()

    val data = listOf(
        Parada(1, "Maquina 1", "10/10/2024", "10/10/2024", "10:10", ""),
        Parada(2, "Maquina 2", "12/08/2024", "", "10:10", ""),
        Parada(3, "Maquina 3", "03/10/2023", "03/10/2023", "10:10", ""),
        Parada(4, "Maquina 4", "22/05/2023", "22/05/2023", "10:10", ""),
        Parada(5, "Maquina 5", "09/01/2023", "", "10:10", ""),
        Parada(6, "Maquina 6", "10/10/2024", "", "10:10", ""),
        Parada(7, "Maquina 7", "12/08/2024", "12/08/2024", "10:10", ""),
        Parada(8, "Maquina 8", "03/10/2023", "03/10/2023", "10:10", ""),
        Parada(9, "Maquina 9", "22/05/2023", "", "10:10", ""),

        )

    //val data = emptyList<List<String>>()

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
            Text(
                text = "LISTA DE PARADAS",
                modifier = Modifier.padding(end = 8.dp, top = 16.dp),
                color = Color.Black
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DateOutlinedTextField(
                    modifier = Modifier.weight(1f),
                    "Fecha Inicio",
                    selectedDate = selectedDateIni,
                    onDateChange = { selectedDateIni = it },
                    showDialog = showDialogDateIni,
                    onShowDialogChange = { showDialogDateIni = it }
                )
                Spacer(modifier = Modifier.width(130.dp))
                DateOutlinedTextField(
                    modifier = Modifier.weight(1f),
                    "Fecha Final",
                    selectedDate = selectedDateFin,
                    onDateChange = { selectedDateFin = it },
                    showDialog = showDialogDateFin,
                    onShowDialogChange = { showDialogDateFin = it }
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ExposedDropdownMenuBox(
                    modifier = Modifier.width(200.dp),
                    expanded = expanded.value,
                    onExpandedChange = {
                        expanded.value = !expanded.value
                        txt_estado.value = ""
                    }) {
                    CustomOutlinedTextField(
                        modifier = Modifier.menuAnchor(),
                        value = txt_estado.value,
                        onValueChange = { },
                        label = "Estado",
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
                        readOnly = true
                    )
                    ExposedDropdownMenu(
                        modifier = Modifier
                            .background(Color.White)
                            .clip(RoundedCornerShape(8.dp)),
                        expanded = expanded.value,
                        onDismissRequest = { expanded.value = false }) {
                        options.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option, color = Color.Black) },
                                onClick = {
                                    txt_estado.value = option
                                    expanded.value = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.width(60.dp))
                CustomButton(
                    "Registro Nuevo",
                    modifier = Modifier.weight(1f),
                    Icons.Filled.Add,
                    onClick = { navController.navigate("homeParada/${id}") },
                    Color(0xFF299203)
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                item {
                    Row {
                        TableHeaderCell(text = "ID", modifier = Modifier.weight(0.5f))
                        TableHeaderCell(text = "Maquina", modifier = Modifier.weight(1f))
                        TableHeaderCell(text = "Fecha Inicio", modifier = Modifier.weight(1f))
                        TableHeaderCell(text = "Fecha Final", modifier = Modifier.weight(1f))
                        TableHeaderCell(text = "Detalle", modifier = Modifier.weight(1f))
                        TableHeaderCell(text = "Finalizar", modifier = Modifier.weight(1f))
                    }
                }
                val listaParadas = paradas.data
                if (data.isNotEmpty()) {
                    items(data) { row ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(0.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TableCell(text = row.id.toString(), modifier = Modifier.weight(0.5f))
                            TableCell(text = row.maquina, modifier = Modifier.weight(1f))
                            TableCell(text = row.fechaInicio, modifier = Modifier.weight(1f))
                            TableCell(
                                text = if (row.fechaFinal.isNullOrEmpty()) "En curso" else row.fechaFinal,
                                modifier = Modifier.weight(1f)
                            )
                            TableCell(value = 1) {
                                Button(
                                    modifier = Modifier.width(100.dp),
                                    onClick = { showDialogDetalle = true; item = row },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF0B4FAF),
                                        contentColor = Color.White,
                                        disabledContainerColor = Color.Gray,
                                        disabledContentColor = Color.White
                                    )
                                ) {
                                    Text("Ver")
                                }
                            }
                            TableCell(value = 1) {
                                if (row.fechaFinal.isNullOrEmpty()) { // Verificar si la parada está iniciada
                                    Button(
                                        modifier = Modifier.width(100.dp),
                                        onClick = {
                                            fechaFinal = LocalDateTime.now()
                                            row.fechaFinal = formatoServidor(fechaFinal!!)
                                            paradaViewModel.detenerParada(row)
                                            showDialog = true
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color.Red,
                                            contentColor = Color.White,
                                            disabledContainerColor = Color.Gray,
                                            disabledContentColor = Color.White
                                        )
                                    ) {
                                        Text("Detener")
                                    }
                                } else {
                                    Text(
                                        "Finalizado",
                                        color = Color.Gray
                                    ) // Mostrar texto si está finalizada
                                }
                            }

                        }
                    }
                } else {
                    item {
                        Text(
                            text = "No hay registros de Paradas",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 8.dp, top = 16.dp),
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Log.d("TAG", "BodyListParada: ${paradas}")
            Detalle(
                isVisible = showDialogDetalle,
                onDismiss = { showDialogDetalle = false },
                data = item,
                content = {
                    Column() {
                        Text(
                            text = "Inspeccion de: ${item.maquina}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                        Text(
                            text = "Descripcion: ${item.motivoParada}",
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = "Fecha: ${item.fechaInicio}", fontWeight = FontWeight.Bold)
                        Text(text = "Fecha: ${item.fechaFinal}", fontWeight = FontWeight.Bold)
                        Text(text = "Hora: ")
                    }
                }
            )
            if (showDialog){
                when(estado){
                    EstadoParada.Cargando ->
                        CustomAlertDialog(
                            showDialog = true,
                            title = "Cargando",
                            message = "Espere por favor...",
                            confirmButtonText = "",
                            dismissButtonText = null,
                            onDismiss = { showDialog = false },
                            dialogType = DialogType.LOADING
                        )
                    is EstadoParada.Error -> TODO()
                    EstadoParada.Exitoso -> {
                        CustomAlertDialog(
                            showDialog = true,
                            title = "Exitoso",
                            message = "Parada finalizada a las ${formatoFecha(fechaFinal!!)}",
                            confirmButtonText = "Aceptar",
                            dismissButtonText = null,
                            onDismiss = { showDialog = false },
                            dialogType = DialogType.SUCCESS
                        )
                    }
                    EstadoParada.Idle -> TODO()
                }
            }
        }
    }
}

/*
@Composable
fun CustomButtonRed(
    text: String,
    estado: Boolean,
    row: Parada,
    color: Color,
    onClick: @Composable () -> Unit = {}
) {
    //var detenerState by remember { mutableStateOf(estado) }

    var isVisibilidad by remember { mutableStateOf(false) }
    Button(
        onClick = {
            if (color == Color.Red) {
                row.estado = false
            } else {
                isVisibilidad = true
            }
        },
        modifier = Modifier.width(100.dp),
        enabled = row.estado,
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = Color.White,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.White
        )
    ) {
        Text(text)
    }
    if (isVisibilidad) {
        DialogEspera(onDismiss = { isVisibilidad = false })
    }else{

    }

}
*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogEspera(onDismiss: () -> Unit = {}) {
    var showSuccess by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(5000) // Wait for 5 seconds
        showSuccess = true
    }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        modifier = Modifier.size(200.dp), content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(25.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                if (showSuccess) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircleOutline,
                            contentDescription = "Success",
                            tint = Color.Green
                        )
                        Text(text = "Exitoso")
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFF0054A3))
                        Text(text = "Espera")
                    }
                }
            }
        }
    )
}