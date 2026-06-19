package com.vistony.app.Screen.muestraProduccion

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vistony.app.Entidad.UserResponse
import com.vistony.app.Entidad.UserState
import com.vistony.app.Screen.Generic.Drawers.CustomDrawer
import com.vistony.app.ViewModel.MuestraViewModel
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MisOFScreen(
    navController: NavController,
    userState: UserState,
    modifier: Modifier = Modifier,
    currentUser: UserResponse = UserResponse(),
    muestraViewModel: MuestraViewModel = hiltViewModel(),
    onNavigateToAdd: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onOrderClick: (Int) -> Unit = {},
    onTomarACargo: (Int) -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val esCalidad = currentUser.role.equals("ASEG. CALIDAD", ignoreCase = true)
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val filters = listOf("Todas", "En análisis", "Aprobados", "Rechazados", "Pendientes")
    var selectedFilter by remember { mutableStateOf("Todas") }

    val muestrasProduccion by muestraViewModel.muestrasProduccion.collectAsState()
    val isLoading by muestraViewModel.isLoading.collectAsState()
    val numPendientes = muestrasProduccion.count { it.status == "PENDIENTE" }

    @RequiresApi(Build.VERSION_CODES.O)
    fun recargar() {
        val fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        muestraViewModel.obtenerMuestrasProduccion(fecha, fecha)
    }

    LaunchedEffect(Unit) {
        recargar()
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isLoading,
        onRefresh = { recargar() }
    )

    val orders = muestrasProduccion
        .filter { muestra ->
            when (selectedFilter) {
                "En análisis" -> muestra.status == "EN_ANALISIS"
                "Aprobados" -> muestra.status == "APROBADO"
                "Rechazados" -> muestra.status == "RECHAZADO"
                "Pendientes" -> muestra.status == "PENDIENTE"
                else -> true
            }
        }
        .map { muestra ->
            OrdenFabricacionUI(
                docEntry = muestra.docEntry,
                id = muestra.ordenEnvase,
                product = muestra.descripcion,
                lote = "Lote ${muestra.lote}",
                status = muestra.status,
                version = muestra.version,
                intentos = muestra.counter,
                tiempoCorriendo = calcularTiempoCorriendo(
                    status = muestra.status,
                    dateRegister = muestra.dateRegister,
                    dateStartAnalysis = muestra.dateStartAnalysis,
                    dateEndAnalysis = muestra.dateEndAnalysis
                )
            )
        }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            CustomDrawer(
                navController = navController,
                id = currentUser.dni,
                userState = userState,
                onLogout = onLogout
            )
        }
    ) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .border(1.dp, Color.Gray, CircleShape)
                            .clickable { scope.launch { drawerState.open() } },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Text(
                        text = "Mis Muestras",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 12.dp)
                    )
                    BadgedBox(
                        badge = {
                            Badge(
                                containerColor = if (numPendientes > 0) Color(0xFFD32F2F) else Color.Transparent,
                                contentColor = Color.White,
                                modifier = Modifier.offset(x = (-4).dp, y = 4.dp)
                            ) {
                                Text(""+ numPendientes)
                            }
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .border(1.dp, Color.Gray, CircleShape)
                                .clickable { onNotificationClick() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = null,
                                tint = if (numPendientes > 0)Color(0xFFFBC02D) else Color.LightGray,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
                HorizontalDivider(color = Color.Black, thickness = 1.dp)
            }
        },
        floatingActionButton = {
            if (!esCalidad) {
                FloatingActionButton(
                    onClick = onNavigateToAdd,
                    containerColor = Color(0xFF1A1A1A),
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color.White)
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filters) { filter ->
                    FilterChipItem(
                        label = filter,
                        isSelected = filter == selectedFilter,
                        onClick = { selectedFilter = filter }
                    )
                }
            }

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "Listado de Muestras (" + orders.size + ")",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(8.dp))
                DashedLine()
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(pullRefreshState)
            ) {
                if (orders.isEmpty() && !isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay muestras pendientes para hoy",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                } else if (orders.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(orders) { order ->
                            OFCard(
                                order = order,
                                esCalidad = esCalidad,
                                onClick = { onOrderClick(order.docEntry) },
                                onTomarACargo = { onTomarACargo(order.docEntry) }
                            )
                        }
                    }
                }

                PullRefreshIndicator(
                    refreshing = isLoading,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }
    }
    }
}

@Composable
private fun FilterChipItem(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) Color(0xFF262626) else Color.White,
        border = if (!isSelected) BorderStroke(1.dp, Color.Black) else null
    ) {
        Text(
            text = label,
            color = if (isSelected) Color.White else Color.Black,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun DashedLine() {
    Canvas(
        Modifier
            .fillMaxWidth()
            .height(1.dp)
    ) {
        drawLine(
            color = Color.Gray.copy(alpha = 0.5f),
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        )
    }
}

@Composable
private fun OFCard(
    order: OrdenFabricacionUI,
    esCalidad: Boolean,
    onClick: () -> Unit,
    onTomarACargo: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "OF  ${order.id}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (esCalidad && order.tiempoCorriendo != null) {
                        Text(
                            text = order.tiempoCorriendo,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                    StatusBadge(status = order.status)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = order.product,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF424242)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = order.lote,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                val detail = buildString {
                    order.version?.let { append(it) }
                    order.intentos?.let { append(" · $it intentos") }
                    order.extraInfo?.let { append(it) }
                }
                Text(
                    text = detail,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.End
                )
            }

            if (esCalidad && order.status == "PENDIENTE") {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onTomarACargo() },
                    color = Color(0xFF212121),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Box(
                        modifier = Modifier.padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Tomar a cargo",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusBadge(status: String) {
    val (bgColor, textColor) = when (status) {
        "EN_ANALISIS" -> Color(0xFFD7E3F4) to Color(0xFF3C5A81)
        "APROBADO" -> Color(0xFFDFF0E0) to Color(0xFF568057)
        "PENDIENTE" -> Color(0xFFFAF3E0) to Color(0xFF8B7A4D)
        "RECHAZADO" -> Color(0xFFF9E0E0) to Color(0xFF9E4B4B)
        else -> Color.LightGray to Color.DarkGray
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(1.dp, textColor.copy(alpha = 0.3f))
    ) {
        Text(
            text = status,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

private data class OrdenFabricacionUI(
    val docEntry: Int,
    val id: String,
    val product: String,
    val lote: String,
    val status: String,
    val version: String? = null,
    val intentos: Int? = null,
    val extraInfo: String? = null,
    val tiempoCorriendo: String? = null
)

// Tiempo corriendo (visible solo para ASEG. CALIDAD): pendiente desde que se registró,
// en análisis desde que se inició, aprobado/rechazado desde que finalizó el análisis
@RequiresApi(Build.VERSION_CODES.O)
private fun calcularTiempoCorriendo(
    status: String,
    dateRegister: String,
    dateStartAnalysis: String?,
    dateEndAnalysis: String?
): String? {
    val fechaBase = when (status) {
        "PENDIENTE" -> dateRegister
        "EN_ANALISIS" -> dateStartAnalysis ?: dateRegister
        "APROBADO", "RECHAZADO" -> dateEndAnalysis ?: return null
        else -> return null
    }

    return try {
        val duracion = Duration.between(Instant.parse(fechaBase), Instant.now())
        formatearDuracion(duracion)
    } catch (e: Exception) {
        null
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun formatearDuracion(duracion: Duration): String {
    val totalMinutos = duracion.toMinutes().coerceAtLeast(0)
    val dias = totalMinutos / (24 * 60)
    val horas = (totalMinutos / 60) % 24
    val minutos = totalMinutos % 60

    return when {
        dias > 0 -> "${dias}d ${horas}h"
        horas > 0 -> "${horas}h ${minutos}m"
        else -> "${minutos}m"
    }
}

