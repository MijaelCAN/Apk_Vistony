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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
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
        drawerContent = { CustomDrawer(navController = navController, id) }
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
                    BodyList(navController, listViewModel, id)
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
                            TableCell(text = row.U_Fecha, modifier = Modifier.weight(1f))
                            TableCell(text = row.U_Turno, modifier = Modifier.weight(1f))
                            TableCell(text = row.U_Usuario, modifier = Modifier.weight(1f))
                            TableCell(value = 1) {
                                IconButton(
                                    onClick = {
                                        showDialog = true
                                        item = row
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
                titulo = "Detalle de Inspección",
                onDismiss = { showDialog = false },
                data = item,
                content = {
                    // REEMPLAZAR AQUI CON EL CONTENIDO DE LA PANTALLA
                    /*Column() {
                        Text(
                            text = "Inspeccion de: ${item.OT}",
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = "Fecha: ${item.U_Fecha}")
                        Text(text = "Turno: ${item.U_Turno}")
                        Text(text = "Usuario: ${item.U_Usuario}")
                    }*/
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Número de OT
                        Text(
                            text = "OT: ${item.OT}",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold
                        )

                        // Fecha y Turno
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Fecha: ${item.U_Fecha}",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "Turno: ${item.U_Turno}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        // Peso, Etiqueta, Lote, Limpieza, Sellado, Encogimiento, Rótulo, Pallet
                        DetalleCheckList(
                            "Peso",
                            item.U_Peso_Check,
                            item.U_Peso_Comment
                        )
                        DetalleCheckList(
                            "Etiqueta",
                            item.U_Etiq_Check,
                            item.U_Etiq_Comment
                        )
                        DetalleCheckList(
                            "Lote",
                            item.U_Lot_Check,
                            item.U_Lot_Comment
                        )
                        DetalleCheckList(
                            "Limpieza",
                            item.U_Limp_Check,
                            item.U_Limp_Comment
                        )
                        DetalleCheckList(
                            "Sellado",
                            item.U_Sell_Check,
                            item.U_Sell_Comment
                        )
                        DetalleCheckList(
                            "Encogimiento",
                            item.U_Enc_Check,
                            item.U_Enc_Comment
                        )
                        DetalleCheckList(
                            "Rótulo",
                            item.U_Rotulo_Check,
                            item.U_Rotulo_Comment
                        )
                        DetalleCheckList(
                            "Pallet",
                            item.U_Palet_Check,
                            item.U_Palet_Comment
                        )

                        // Conformidad y Usuario
                        Text(
                            text = "Conformidad: ${if (item.U_Conformidad == "Y") "CONFORME" else "NO CONFORME"}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "Usuario: ${item.U_Usuario}",
                            style = MaterialTheme.typography.bodySmall
                        )

                        // Cantidad y Maquinista
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Cantidad: ${item.U_Cantidad}",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "Maquinista: ${item.U_Maquinista}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        // Separador
                        Divider(modifier = Modifier.padding(vertical = 8.dp))

                        // Botón para cerrar o regresar
                        Button(
                            onClick = { showDialog = false },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text(text = "Cerrar")
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun DetalleCheckList(
    label: String,
    check: String,
    comment: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.weight(0.5f),
            text = label,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold
        )
        /*Text(
            modifier = Modifier.width(40.dp),
            text = check,
            style = MaterialTheme.typography.bodySmall,
            color = when (check) {
                "Y" -> Color.Green
                "N" -> Color.Red
                else -> Color.Gray
            }
        )*/
        if (check == "Y") {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = "Check",
                tint = Color.Green,modifier = Modifier.width(40.dp)
            )
        } else {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Close",
                tint = Color.Gray,
                modifier = Modifier.width(40.dp)
            )
        }
        Text(
            modifier = Modifier.weight(0.5f),
            text = comment,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

