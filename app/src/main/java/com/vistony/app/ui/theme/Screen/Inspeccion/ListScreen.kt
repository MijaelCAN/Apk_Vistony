package com.vistony.app.ui.theme.Screen.Inspeccion

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.vistony.app.R
import com.vistony.app.ui.theme.Screen.Generic.CustomDrawer
import com.vistony.app.ui.theme.Screen.Generic.TopBar
import kotlinx.coroutines.launch

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
                    BodyList()
                }
            }
        )
    }
}

@Composable
fun BodyList() {

    /*val data = listOf(
        listOf("1", "John Doe", "Aceptado", "14/08/2023", "123:00"),
        listOf("2", "Jane Smith", "Rechazado", "14/08/2023", "14:00"),
        listOf("3", "Sam Wilson", "Aceptado", "14/08/2023", "09:00"),
    )*/
    val data = emptyList<List<String>>()
    //val data = emptyList<String>()
    /*LazyColumn {
        // Encabezados
        item {
            Row {
                Text("Header 1", modifier = Modifier.weight(1f))
                Text("Header 2", modifier = Modifier.weight(1f))
            }
        }
        // Filas de datos
        items(data){row->
            Row {
                row.forEach { item ->
                    Text(item, modifier = Modifier.weight(1f))
                }
            }
        }
    }*/

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
            Spacer(modifier = Modifier.height(32.dp))
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                item {
                    Row {
                        Text("N°", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                        Text("Nombre", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                        Text("Estado", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                        Text("Fecha", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                        Text(
                            "Hora Insp",
                            modifier = Modifier.weight(1f),
                            fontWeight = FontWeight.Bold
                        )
                        Text("", modifier = Modifier.weight(1f))
                    }
                    Divider()
                }
                if (data.isNotEmpty()) {
                    items(data) { row ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(0.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            row.forEach { item ->
                                Text(
                                    text = item,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(4.dp),
                                    textAlign = TextAlign.Start
                                )
                            }
                            IconButton(
                                onClick = { /*TODO*/ },
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
                        Divider()
                    }
                } else {
                    item {
                        Text(
                            text = "No hay Inspecciones",
                            modifier = Modifier.fillMaxWidth().padding(end = 8.dp, top = 16.dp),
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
