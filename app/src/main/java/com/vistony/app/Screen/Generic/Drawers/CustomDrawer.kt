package com.vistony.app.Screen.Generic.Drawers

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.HomeRepairService
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.vistony.app.Entidad.UserState
import com.vistony.app.R

@Composable
fun CustomDrawer( //CustomDrawer - ProfessionalDrawer
    navController: NavController,
    id: String,
    userState: UserState,
    modifier: Modifier = Modifier
) {
    var selectedItem by remember { mutableStateOf<String?>(null) }
    var expandedItems by remember { mutableStateOf(setOf<String>()) }

    val menuItems = remember(userState.currentUser?.role) {
        listOf(
            DrawerItem(
                id = "inspeccion",
                icon = Icons.Default.Checklist,
                label = "Inspección",
                subItems = listOf(
                    DrawerSubItem("Lista Inspecciones") { navController.navigate("listaInsp/$id") },
                ),
                // Solo visible para ciertos roles
                visibleForRoles = setOf("operador", "admin")
            ),
            DrawerItem(
                id = "parada_maquina",
                icon = Icons.Default.Construction,
                label = "Parada Máquina",
                subItems = listOf(
                    DrawerSubItem("Lista de Paradas") { navController.navigate("listaParada/$id") },
                ),
                visibleForRoles = setOf("operador", "admin")
            ),
            DrawerItem(
                id = "mantenimiento",
                icon = Icons.Default.HomeRepairService,
                label = "Mantenimiento",
                subItems = listOf(
                    DrawerSubItem("Registro de Paradas") { navController.navigate("paradaMantenimiento") },
                ),
                visibleForRoles = setOf("mantenimiento", "admin")
            ),
            DrawerItem(
                id = "configuracion",
                icon = Icons.Default.Settings,
                label = "Configuración",
                subItems = emptyList(),
                visibleForRoles = setOf("admin", "supervisor")
            )
        ).filter { item ->
            item.visibleForRoles.isEmpty() ||
                    userState.hasAnyRole(*item.visibleForRoles.toTypedArray())
        }
    }

    ModalDrawerSheet(
        modifier = modifier
            .background(Color(0xFFF7F7F7))
            //.fillMaxSize()
    ) {
        // ===============================  HEADER =========================================== //
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val painter = rememberAsyncImagePainter(model = userState.currentUser?.avatar ?: "")

                // Imagen circular (avatar)
                Image(
                    painter = painter,
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = userState.currentUser?.name ?: "Anónimo",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Dirección",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = userState.currentUser?.role ?: "Generico",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                        )
                    }
                }
            }
        }

        // MENÚ PRINCIPAL
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color(0xFFF7F7F7))
                .padding(horizontal = 8.dp)
        ) {
            /*val menuItems = listOf(
                DrawerItem(
                    id = "inspeccion",
                    icon = Icons.Default.Checklist,
                    label = "Inspección",
                    subItems = listOf(
                        DrawerSubItem("Lista Inspecciones") { navController.navigate("listaInsp/$id") },
                        //DrawerSubItem("Nueva Inspección") { navController.navigate("home/$id") }
                    )
                ),
                DrawerItem(
                    id = "parada_maquina",
                    icon = Icons.Default.Construction,
                    label = "Parada Máquina",
                    subItems = listOf(
                        DrawerSubItem("Lista de Paradas") { navController.navigate("listaParada/$id") },
                        //DrawerSubItem("Nueva Parada") { navController.navigate("homeParada/$id") }
                    )
                ),
                DrawerItem(
                    id = "configuracion",
                    icon = Icons.Default.Settings,
                    label = "Configuración",
                    subItems = emptyList()
                )
            )*/

            items(menuItems) { item ->
                if (item.visibleForRoles.isEmpty() || userState.hasAnyRole(*item.visibleForRoles.toTypedArray())) {
                    DrawerMenuItem(
                        item = item,
                        isSelected = selectedItem == item.id,
                        isExpanded = expandedItems.contains(item.id),
                        onClick = {
                            selectedItem = item.id
                            if (item.subItems.isNotEmpty()) {
                                expandedItems = if (expandedItems.contains(item.id))
                                    expandedItems - item.id
                                else
                                    expandedItems + item.id
                            } else {
                                expandedItems = expandedItems - item.id
                            }
                        }
                    )
                    if (expandedItems.contains(item.id)) {
                        item.subItems.forEachIndexed { index, subItem ->
                            Divider(color = Color.LightGray, thickness = 0.5.dp)
                            DrawerSubMenuItem(
                                subItem = subItem,
                                modifier = Modifier.padding(start = 56.dp),
                                onClick = {
                                    selectedItem = item.id
                                    subItem.onClick()
                                }
                            )
                        }
                    }
                    Divider(color = Color.LightGray, thickness = 0.5.dp)
                }
            }
        }
    }
}

data class DrawerItem(
    val id: String,
    val icon: ImageVector,
    val label: String,
    val subItems: List<DrawerSubItem> = emptyList(),
    val visibleForRoles: Set<String> = emptySet()
)

data class DrawerSubItem(
    val label: String,
    val onClick: () -> Unit
)

@Composable
fun DrawerMenuItem(
    item: DrawerItem,
    isSelected: Boolean,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) Color(0xFFFC6A68).copy(alpha = 0.2f) else Color.Transparent
    val contentColor = if (isSelected) Color(0xFFFC6A68) else Color.Black

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icono dentro de fondo redondeado
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(
                    color = if (isSelected) Color(0xFFFC6A68).copy(alpha = 0.3f) else Color(
                        0xFFE0E0E0
                    ),
                    shape = RoundedCornerShape(10.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.label,
                tint = contentColor,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = item.label,
            color = contentColor,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        if (item.subItems.isNotEmpty()) {
            Icon(
                imageVector = if (isExpanded) Icons.Default.ExpandMore else Icons.Default.NavigateNext,
                contentDescription = if (isExpanded) "Expandido" else "Expandir",
                tint = contentColor
            )
        }
    }
}

@Composable
fun DrawerSubMenuItem(
    subItem: DrawerSubItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = subItem.label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black
        )
    }
}

