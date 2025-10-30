package com.vistony.app.Screen.Muestra

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vistony.app.Entidad.MuestraCompleta
import com.vistony.app.Screen.Generic.*
import com.vistony.app.ViewModel.MuestraViewModel
import com.vistony.app.ui.theme.theme.Dimensions
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun EditMuestra(
    muestra: MuestraCompleta,
    onNavigateBack: () -> Unit,
    onNavigateToSuccess: () -> Unit,
    muestraViewModel: MuestraViewModel
) {
    //val uiState by muestraViewModel.uiState.collectAsState()
    val successMessage by muestraViewModel.successMessage.collectAsState()
    val errorMessage by muestraViewModel.errorMessage.collectAsState()
    
    // Cargar la muestra en el ViewModel para edición
    LaunchedEffect(muestra.cabecera.id) {
        muestraViewModel.cargarMuestraParaEdicion(muestra)
    }
    
    // Navegar a éxito cuando se actualice exitosamente
    LaunchedEffect(successMessage) {
        if (successMessage != null) {
            onNavigateToSuccess()
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF8FAFC),
                        Color(0xFFF1F5F9)
                    )
                )
            )
    ) {
        // Header
        MuestraHeader(
            title = "Editar Muestra",
            subtitle = "Completar secciones faltantes",
            actionButton = {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = Color.White.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Atrás",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        )
        
        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            // Información de la muestra
            MuestraCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Información General",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Código: ${muestra.cabecera.codigo}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF374151)
                        )
                        
                        /*MuestraStatusChip(
                            estado = muestra.getEstadoGeneral(),
                            color = when {
                                muestra.getCompletitudPorcentaje() == 100 -> Color(0xFF10B981)
                                muestra.getCompletitudPorcentaje() >= 75 -> Color(0xFFF59E0B)
                                muestra.getCompletitudPorcentaje() >= 50 -> Color(0xFFEF4444)
                                else -> Color(0xFF6B7280)
                            }
                        )*/
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Lote: ${muestra.cabecera.lote}",
                        fontSize = 12.sp,
                        color = Color(0xFF6B7280)
                    )
                    
                    Text(
                        text = "Producto: ${muestra.cabecera.producto}",
                        fontSize = 12.sp,
                        color = Color(0xFF6B7280)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Secciones editables
            val seccionesPendientes = muestra.getSeccionesPendientes()
            
            // Materiales
            if (!muestra.materialesCompletado || seccionesPendientes.contains("Materiales")) {
                MuestraSectionCard(
                    title = "Formulación de Material Empleado",
                    isCompleted = muestra.materialesCompletado,
                    onEditClick = { /* Navegar a edición de materiales */ },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            
            // Inspecciones Dimensionales
            if (!muestra.inspeccionesCompletado || seccionesPendientes.contains("Inspecciones")) {
                MuestraSectionCard(
                    title = "Inspección Dimensional",
                    isCompleted = muestra.inspeccionesCompletado,
                    onEditClick = { /* Navegar a edición de inspecciones */ },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            
            // CheckLists
            if (!muestra.checkListsCompletado || seccionesPendientes.contains("CheckLists")) {
                MuestraSectionCard(
                    title = "Check List de Inspección",
                    isCompleted = muestra.checkListsCompletado,
                    onEditClick = { /* Navegar a edición de checklists */ },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            
            // Evaluación de Producción
            if (!muestra.evaluacionCompletado || seccionesPendientes.contains("Evaluación")) {
                MuestraSectionCard(
                    title = "Evaluación de la Producción",
                    isCompleted = muestra.evaluacionCompletado,
                    onEditClick = { /* Navegar a edición de evaluación */ },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            
            // Indicador de progreso completo
            MuestraProgressIndicator(
                muestra = muestra,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
    
    // Mostrar mensaje de error si existe
    errorMessage?.let { message ->
        LaunchedEffect(message) {
            muestraViewModel.clearMessages()
        }
    }
}

@Composable
fun MuestraSectionCard(
    title: String,
    isCompleted: Boolean,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = tween(100),
        label = "scale"
    )
    
    MuestraCard(
        modifier = modifier
            .scale(scale)
            .clickable {
                isPressed = true
                onEditClick()
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Icono de estado
                Icon(
                    imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.Schedule,
                    contentDescription = null,
                    tint = if (isCompleted) Color(0xFF10B981) else Color(0xFF6B7280),
                    modifier = Modifier.size(24.dp)
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF111827)
                    )
                    
                    Text(
                        text = if (isCompleted) "Completado" else "Pendiente",
                        fontSize = 12.sp,
                        color = if (isCompleted) Color(0xFF10B981) else Color(0xFF6B7280)
                    )
                }
            }
            
            // Botón de acción
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (isCompleted) Color(0xFF10B981) else Color(0xFF4F46E5)
            ) {
                Text(
                    text = if (isCompleted) "Ver" else "Completar",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}




