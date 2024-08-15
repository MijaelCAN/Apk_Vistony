package com.vistony.app.ui.theme.Screen.Parada

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.vistony.app.R
import com.vistony.app.ui.theme.Screen.Generic.CustomDrawer
import com.vistony.app.ui.theme.Screen.Generic.Detalle
import com.vistony.app.ui.theme.Screen.Generic.TableCell
import com.vistony.app.ui.theme.Screen.Generic.TableHeaderCell
import com.vistony.app.ui.theme.Screen.Generic.TopBar
import com.vistony.app.ui.theme.Screen.Inspeccion.Inspeccion
import kotlinx.coroutines.launch

@Composable
fun ListParada(
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
                    BodyListParada()
                }
            }
        )
    }
}

@Composable
fun BodyListParada() {

    var detenerState by rememberSaveable { mutableStateOf(true) }
    var showDialog by remember { mutableStateOf(false) }
    var item by remember { mutableStateOf(Inspeccion()) }


    val data = listOf(
        ListParada(1, "Maquina 1", "10/10/2024", "10/10/2024", "10:10", true),
        ListParada(2, "Maquina 2", "12/08/2024", "12/08/2024", "10:10", false),
        ListParada(3, "Maquina 3", "03/10/2023", "03/10/2023", "10:10", false),
        ListParada(4, "Maquina 4", "22/05/2023", "22/05/2023", "10:10", false),
        ListParada(5, "Maquina 5", "09/01/2023", "09/01/2023", "10:10", true),
        ListParada(6, "Maquina 6", "10/10/2024", "10/10/2024", "10:10", false),
        ListParada(7, "Maquina 7", "12/08/2024", "12/08/2024", "10:10", false),
        ListParada(8, "Maquina 8", "03/10/2023", "03/10/2023", "10:10", false),
        ListParada(9, "Maquina 9", "22/05/2023", "22/05/2023", "10:10", true),

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
                            TableCell(text = row.fec_inicio, modifier = Modifier.weight(1f))
                            TableCell(text = row.fec_final, modifier = Modifier.weight(1f))
                            TableCell(value = 1) {
                                CustomButtonRed("Ver", estado = true, row, Color(0xFF0054A3)) {
                                    showDialog = true
                                }
                            }
                            TableCell(value = 1) {
                                //CustomButtonRed(if(row.estado)?"Detener":"Finalizado", estado = row.estado, row, Color.Red)
                                CustomButtonRed("Detener", estado = row.estado, row, Color.Red)
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

data class ListParada(
    val id: Int = 0,
    val maquina: String = "",
    val fec_inicio: String = "",
    val fec_final: String = "",
    val hora_ins: String = "",
    var estado: Boolean = false
)

@Composable
fun CustomButtonRed(text: String, estado: Boolean, row: ListParada, color: Color,onClick: () -> Unit = {}) {
    //var detenerState by remember { mutableStateOf(estado) }
    Button(
        onClick = {
            if (color == Color.Red) {
                row.estado = false
            } else {
                onClick()
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
}