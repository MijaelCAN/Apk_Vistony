package com.vistony.app.ui.theme.Screen.Inspeccion

import android.os.Build
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.vistony.app.R
import com.vistony.app.ui.theme.Screen.Generic.CustomButton
import com.vistony.app.ui.theme.Screen.Generic.CustomDrawer
import com.vistony.app.ui.theme.Screen.Generic.CustomOutlinedTextField
import com.vistony.app.ui.theme.Screen.Generic.DateOutlinedTextField
import com.vistony.app.ui.theme.Screen.Generic.Detalle
import com.vistony.app.ui.theme.Screen.Generic.TableCell
import com.vistony.app.ui.theme.Screen.Generic.TableHeaderCell
import com.vistony.app.ui.theme.Screen.Generic.TopBar
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    navController: NavController
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { CustomDrawer(navController = navController) }
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
                    BodyList(navController)
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BodyList(navController: NavController) {

    var showDialog by remember { mutableStateOf(false) }
    var item by remember { mutableStateOf(Inspeccion()) }
    var conformidad = rememberSaveable { mutableStateOf("") }
    val expanded = remember { mutableStateOf(false) }
    val options = listOf("Iniciado", "En Proceso", "Finalizado")

    val data = listOf(
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
                text = "LISTA DE INSPECCIONES",
                modifier = Modifier.padding(end = 8.dp, top = 16.dp),
                color = Color.Black
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DateOutlinedTextField(modifier = Modifier.weight(1f), "Fecha Inicio")
                Spacer(modifier = Modifier.width(130.dp))
                DateOutlinedTextField(modifier = Modifier.weight(1f), "Fecha Final")
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
                        conformidad.value = ""
                    }) {
                    CustomOutlinedTextField(
                        modifier = Modifier.menuAnchor(),
                        value = conformidad.value,
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
                                    conformidad.value = option
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
                    onClick = { navController.navigate("homeParada") })
            }
            Spacer(modifier = Modifier.height(32.dp))
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                item {
                    Row {
                        TableHeaderCell(text = "N°", modifier = Modifier.weight(0.5f))
                        TableHeaderCell(text = "Nombre", modifier = Modifier.weight(1f))
                        TableHeaderCell(text = "Estado", modifier = Modifier.weight(1f))
                        TableHeaderCell(text = "Fecha", modifier = Modifier.weight(1f))
                        TableHeaderCell(text = "Hora Insp", modifier = Modifier.weight(1f))
                        TableHeaderCell(text = "", modifier = Modifier.weight(1f))
                    }
                }
                if (data.isNotEmpty()) {
                    items(data) { row ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(0.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TableCell(text = row.id.toString(), modifier = Modifier.weight(0.5f))
                            TableCell(text = row.nombre, modifier = Modifier.weight(1f))
                            TableCell(text = row.estado, modifier = Modifier.weight(1f))
                            TableCell(text = row.fecha_ins, modifier = Modifier.weight(1f))
                            TableCell(text = row.hora_ins, modifier = Modifier.weight(1f))
                            TableCell(value = 1) {
                                IconButton(
                                    onClick = {
                                        showDialog = true
                                        item = Inspeccion(
                                            row.id,
                                            row.nombre,
                                            row.estado,
                                            row.fecha_ins,
                                            row.hora_ins
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
            )
        }
    }
}

data class Inspeccion(
    val id: Int = 0,
    val nombre: String = "",
    val estado: String = "",
    val fecha_ins: String = "",
    val hora_ins: String = ""
)

