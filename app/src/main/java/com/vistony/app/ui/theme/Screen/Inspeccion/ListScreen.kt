package com.vistony.app.ui.theme.Screen.Inspeccion

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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vistony.app.Entidad.Inspeccion
import com.vistony.app.R
import com.vistony.app.ViewModel.EvalViewModel
import com.vistony.app.ui.theme.Screen.Generic.CustomButton
import com.vistony.app.ui.theme.Screen.Generic.CustomDrawer
import com.vistony.app.ui.theme.Screen.Generic.DateOutlinedTextField
import com.vistony.app.ui.theme.Screen.Generic.Detalle
import com.vistony.app.ui.theme.Screen.Generic.TableCell
import com.vistony.app.ui.theme.Screen.Generic.TableHeaderCell
import com.vistony.app.ui.theme.Screen.Generic.TopBar
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    navController: NavController,
    listViewModel: EvalViewModel = hiltViewModel(),
    id: String = "prueba"
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { CustomDrawer(navController = navController,id) }
    ) {
        Scaffold(
            topBar = {
                TopBar("Lista de Inspección", navController = navController, onMenuClick = {
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
                    BodyList(navController, listViewModel,id)
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BodyList(navController: NavController, listViewModel: EvalViewModel, id: String) {
    val listState by listViewModel.listInspeccionState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var item by remember { mutableStateOf(Inspeccion()) }
    var txt_estado = rememberSaveable { mutableStateOf("") }
    val expanded = remember { mutableStateOf(false) }
    val options = listOf("Iniciado", "Finalizado")

    var selectedDateIni by remember { mutableStateOf(LocalDate.now()) }
    var showDialogDateIni by remember { mutableStateOf(false) }
    var selectedDateFin by remember { mutableStateOf(LocalDate.now()) }
    var showDialogDateFin by remember { mutableStateOf(false) }
    var lista_inspecciones by remember { mutableStateOf(emptyList<Inspeccion>()) }

    /*val data2 = listOf(
        Inspeccion(1, "Inspecion 1", "Aprobado", "10/10/2024", "10:10"),
        Inspeccion(2, "Inspecion 2", "Desaprobado", "12/08/2024", "10:10"),
        Inspeccion(3, "Inspecion 3", "Aprobado", "03/10/2023", "10:10"),
        Inspeccion(4, "Inspecion 4", "Desaprobado", "22/05/2023", "10:10"),
        Inspeccion(5, "Inspecion 5", "Aprobado", "09/01/2023", "10:10"),
        Inspeccion(6, "Inspecion 6", "Desaprobado", "10/10/2024", "10:10"),
        Inspeccion(7, "Inspecion 7", "Aprobado", "12/08/2024", "10:10"),
        Inspeccion(8, "Inspecion 8", "Desaprobado", "03/10/2023", "10:10"),
        Inspeccion(9, "Inspecion 9", "Aprobado", "22/05/2023", "10:10"),
        Inspeccion(10, "Inspecion 10", "Desaprobado", "09/01/2023", "10:10"),
        Inspeccion(11, "Inspecion 11", "Aprobado", "10/10/2024", "10:10"),
        Inspeccion(12, "Inspecion 12", "Desaprobado", "12/08/2024", "10:10"),
        Inspeccion(13, "Inspecion 13", "Aprobado", "03/10/2023", "10:10"),
        Inspeccion(14, "Inspecion 14", "Desaprobado", "22/05/2023", "10:10"),
        Inspeccion(15, "Inspecion 15", "Aprobado", "09/01/2023", "10:10"),
        Inspeccion(16, "Inspecion 16", "Desaprobado", "10/10/2024", "10:10"),
    )*/
    LaunchedEffect(Unit) { // Or any key that changes on every recomposition
        val newfechaIni = selectedDateIni.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        val newfechaFin = selectedDateFin.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        Log.d("selectedDateIni", selectedDateIni.toString())
        listViewModel.getListInspeccion(newfechaIni, newfechaFin)
        lista_inspecciones = listState.listInspeccion.data
    }
    lista_inspecciones = listState.listInspeccion.data

    LaunchedEffect(selectedDateIni, selectedDateFin) {
        val newfechaIni = selectedDateIni.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        val newfechaFin = selectedDateFin.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        Log.d("selectedDateIni", selectedDateIni.toString())
        listViewModel.getListInspeccion(newfechaIni, newfechaFin)
        lista_inspecciones = listState.listInspeccion.data
    }
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
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                /*
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
                }*/
                Spacer(modifier = Modifier.width(430.dp))
                CustomButton(
                    "Registro Nuevo",
                    modifier = Modifier.width(200.dp),
                    Icons.Filled.Add,
                    onClick = { navController.navigate("home/${id}") },
                    Color(0xFF299203)
                )
            }
            Text(
                text = "LISTA DE INSPECCIONES",
                modifier = Modifier.padding(end = 8.dp, top = 16.dp),
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(32.dp))
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
                    onShowDialogChange = { showDialogDateIni= it }
                )
                Spacer(modifier = Modifier.width(130.dp))
                DateOutlinedTextField(
                    modifier = Modifier.weight(1f),
                    "Fecha Final",
                    selectedDate = selectedDateFin,
                    onDateChange = { selectedDateFin = it },
                    showDialog = showDialogDateFin,
                    onShowDialogChange = { showDialogDateFin= it }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                item {
                    Row {
                        TableHeaderCell(text = "OT", modifier = Modifier.weight(1f))
                        TableHeaderCell(text = "FECHA", modifier = Modifier.weight(1f))
                        TableHeaderCell(text = "TURNO", modifier = Modifier.weight(1f))
                        TableHeaderCell(text = "USUARIO", modifier = Modifier.weight(1f))
                        TableHeaderCell(text = "", modifier = Modifier.weight(1f))
                    }
                }

                if (lista_inspecciones.isNotEmpty()) {
                    items(lista_inspecciones) { row ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(0.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TableCell(text = row.OT, modifier = Modifier.weight(1f))
                            TableCell(text = row.Fecha, modifier = Modifier.weight(1f))
                            TableCell(text = row.Turno, modifier = Modifier.weight(1f))
                            TableCell(text = row.Usuario, modifier = Modifier.weight(1f))
                            TableCell(value = 1) {
                                IconButton(
                                    onClick = {
                                        showDialog = true
                                        item = Inspeccion(
                                            row.OT,
                                            row.Fecha,
                                            row.Turno,
                                            row.Usuario
                                        )
                                    },
                                    modifier = Modifier.width(100.dp),
                                    colors = IconButtonDefaults.iconButtonColors(
                                        containerColor = Color(0xFF0054A3),
                                        contentColor = Color.White
                                    )
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                    ) {
                                        Icon(Icons.Filled.Search, contentDescription = null)
                                        Text(text = "Ver")
                                    }
                                }
                            }

                        }
                    }
                } else {
                    item {
                        Text(
                            text = "No hay Inspecciones",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 8.dp, top = 16.dp),
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            Detalle(
                isVisible = showDialog,
                onDismiss = { showDialog = false },
                data = item,
                content = {
                    Column() {
                        Text(
                            text = "Inspeccion de: ${item.OT}",
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                        )
                        Text(text = "Fecha: ${item.Fecha}")
                        Text(text = "Turno: ${item.Turno}")
                        Text(text = "Usuario: ${item.Usuario}")
                    }
                }
            )
        }
    }
}


