package com.vistony.app.Screen.Inspeccion

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
import com.vistony.app.Screen.Generic.CustomButton
import com.vistony.app.Screen.Generic.CustomDrawer
import com.vistony.app.Screen.Generic.DateOutlinedTextField
import com.vistony.app.Screen.Generic.Detalle
import com.vistony.app.Screen.Generic.TableCell
import com.vistony.app.Screen.Generic.TableHeaderCell
import com.vistony.app.Screen.Generic.TopBar
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BodyList(navController: NavController, listViewModel: EvalViewModel, id: String) {
    val listState by listViewModel.listInspeccionState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var item by remember { mutableStateOf(Inspeccion()) }

    var selectedDateIni by remember { mutableStateOf(LocalDate.now()) }
    var showDialogDateIni by remember { mutableStateOf(false) }
    var selectedDateFin by remember { mutableStateOf(LocalDate.now()) }
    var showDialogDateFin by remember { mutableStateOf(false) }

    LaunchedEffect(selectedDateIni, selectedDateFin) {
        val newfechaIni = selectedDateIni.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        val newfechaFin = selectedDateFin.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        Log.d("selectedDateIni", selectedDateIni.toString())
        listViewModel.getListInspeccion(newfechaIni, newfechaFin)
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
                val nuevalista = listState.listInspeccion.data.asReversed()
                if (nuevalista.isNotEmpty()) {
                    items(nuevalista) { row ->
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
                            fontWeight = FontWeight.Bold
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


