package com.vistony.app.ui.theme.Screen.Inspeccion

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
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
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(color = Color(0xFF0B4FAF)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    item {
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
                    }
                    item {
                        BodyList()
                    }
                }
            }
        )
    }
}

@Composable
fun BodyList() {

    val data = listOf(
        listOf("1", "John Doe"),
        listOf("2", "Jane Smith"),
        listOf("3", "Sam Wilson"),
        // Agrega más registros según sea necesario
    )
    LazyColumn {
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
    }
}
