package com.vistony.app.ui.theme.Screen.Generic

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.FormatLineSpacing
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDrawer(navController: NavController) {
    var selectedItem by remember { mutableStateOf<String?>(null) }
    ModalDrawerSheet(
        modifier = Modifier.background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0054A3).copy(alpha = 0.2f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Filled.Person,
                        contentDescription = null,
                        modifier = Modifier.size(70.dp),
                        tint = Color(0xFF0054A3)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Nombre de Usuario",
                        color = Color.Black,
                        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    )
                }
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0054A3).copy(alpha = 0.2f))
            ) {
                DrawerMenuItem(
                    icon = Icons.Filled.Checklist,
                    text = "Inspección",
                    onClick = { selectedItem = "Inspección" }
                )
                if (selectedItem == "Inspección") {
                    // Sub-menu items for Inspección
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.White)
                            .padding(start = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        DrawerMenuItem(
                            icon = Icons.Filled.List,
                            text = "Lista Inspecciones", onClick = { navController.navigate("listaInsp") }
                        )
                        Button(
                            onClick = { navController.navigate("home/123456") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 16.dp,
                                    end = 16.dp
                                ),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF0054A3),
                                contentColor = Color.White
                            )
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = "Nueva Inspección"
                                )
                                Spacer(modifier = Modifier.width(8.dp)) // Add space between icon and text
                                Text("NUEVA INSPECCIÓN")
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                DrawerMenuItem(
                    icon = Icons.Filled.Construction,
                    text = "Parada Máquina",
                    onClick = { selectedItem = "Parada Máquina" }
                )
                if (selectedItem == "Parada Máquina") {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.White)
                            .padding(start = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        DrawerMenuItem(
                            icon = Icons.Filled.List,
                            text = "Lista de Paradas",
                            onClick = { /* Action for Notificaciones */ }
                        )
                        Button(
                            onClick = { navController.navigate("homeParada") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 16.dp,
                                    end = 16.dp
                                ),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF167E05),
                                contentColor = Color.White
                            )
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = "Nueva Parada"
                                )
                                Spacer(modifier = Modifier.width(8.dp)) // Add space between icon and text
                                Text("NUEVA PARADA")
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
                DrawerMenuItem(
                    icon = Icons.Filled.Settings,
                    text = "Configuración",
                    onClick = { navController.navigate("configuracion") }
                )
            }
        }
    }
}

@Composable
fun DrawerMenuItem(icon: ImageVector, text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = text, modifier = Modifier.size(24.dp), tint = Color.Black)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text)
    }
}