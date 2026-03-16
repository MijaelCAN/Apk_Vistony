package com.vistony.app.Screen.Muestra

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vistony.app.Entidad.MuestraCompleta
import com.vistony.app.Screen.Generic.*
import com.vistony.app.Screen.Generic.SuccessColor
import com.vistony.app.Screen.Generic.WarningColor
import com.vistony.app.Screen.Generic.ErrorColor
import com.vistony.app.ViewModel.MuestraViewModel
import com.vistony.app.ui.theme.theme.Dimensions

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetailMuestra(
    muestra: MuestraCompleta,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: () -> Unit = {}
) {
    val context = LocalContext.current
    val activity = context as Activity
    val windowSize = calculateWindowSizeClass(context)
    val padding_res = Dimensions.getPadding(windowSize.widthSizeClass)
    val bodyFontSize = Dimensions.getBodyFontSize(windowSize.widthSizeClass)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF8FAFC),
                        Color(0xFFE2E8F0)
                    )
                )
            )
    ) {
        // Header con gradiente
        MuestraDetailHeader(
            muestra = muestra,
            onBackClick = onNavigateBack,
            padding = padding_res
        )

        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = padding_res, vertical = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Información general
            MuestraInfoCard(
                title = "Información General",
                icon = Icons.Default.Info,
                content = {
                    MuestraInfoGrid(
                        items = listOf(
                            "Código" to muestra.cabecera.codigo,
                            "Lote" to muestra.cabecera.lote,
                            "Embalaje" to muestra.cabecera.embalaje,
                            "Código Máquina" to muestra.cabecera.codigoMaquina,
                            "Máquina" to muestra.cabecera.maquina,
                            "Turno" to muestra.cabecera.turno,
                            "Producto" to muestra.cabecera.producto,
                            "Auxiliar" to muestra.cabecera.auxiliar,
                            "Encargado" to muestra.cabecera.encargadoProduccion,
                            "Fecha" to muestra.cabecera.fechaRegistro,
                            "Estado" to muestra.cabecera.estado
                        )
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Materiales empleados
            if (muestra.materiales.isNotEmpty()) {
                MuestraInfoCard(
                    title = "Materiales Empleados (${muestra.materiales.size})",
                    icon = Icons.Default.Build,
                    content = {
                        muestra.materiales.forEach { material ->
                            MaterialDetailItem(material = material)
                            if (material != muestra.materiales.last()) {
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Inspecciones dimensionales
            if (muestra.inspeccionesDimensionales.isNotEmpty()) {
                MuestraInfoCard(
                    title = "Inspecciones Dimensionales (${muestra.inspeccionesDimensionales.size})",
                    icon = Icons.Default.Straighten,
                    content = {
                        muestra.inspeccionesDimensionales.forEach { inspeccion ->
                            InspeccionDetailItem(inspeccion = inspeccion)
                            if (inspeccion != muestra.inspeccionesDimensionales.last()) {
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Check lists
            if (muestra.checkLists.isNotEmpty()) {
                MuestraInfoCard(
                    title = "Check Lists (${muestra.checkLists.size})",
                    icon = Icons.Default.Checklist,
                    content = {
                        muestra.checkLists.forEach { checkList ->
                            CheckListDetailItem(checkList = checkList)
                            if (checkList != muestra.checkLists.last()) {
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Evaluación de producción
            muestra.evaluacionProduccion?.let { evaluacion ->
                MuestraInfoCard(
                    title = "Evaluación de Producción",
                    icon = Icons.Default.Assessment,
                    content = {
                        EvaluacionDetailItem(evaluacion = evaluacion)
                    }
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Botón de editar
            MuestraButton(
                text = "Editar Muestra",
                onClick = onNavigateToEdit,
                icon = Icons.Default.Edit,
                modifier = Modifier.fillMaxWidth(),
                buttonHeight = 48.dp
            )
        }
    }
}

@Composable
fun MuestraDetailHeader(
    muestra: MuestraCompleta,
    onBackClick: () -> Unit,
    padding: androidx.compose.ui.unit.Dp
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = padding, vertical = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(PrimaryMuestraColor, SecondaryMuestraColor)
                    )
                )
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = Color.White.copy(alpha = 0.2f),
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Atrás",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "Detalle de Muestra",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Código: ${muestra.cabecera.codigo}",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }

                MuestraStatusChip(
                    estado = muestra.cabecera.estado,
                    isLarge = true
                )
            }
        }
    }
}

@Composable
fun MuestraInfoCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    MuestraCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = 4.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = PrimaryMuestraColor.copy(alpha = 0.1f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = PrimaryMuestraColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        content()
    }
}

@Composable
fun MuestraInfoGrid(
    items: List<Pair<String, String>>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                rowItems.forEach { (label, value) ->
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = label,
                            fontSize = 12.sp,
                            color = Color(0xFF6B7280),
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = value,
                            fontSize = 14.sp,
                            color = Color(0xFF111827),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                // Rellenar con espacio vacío si hay un número impar de elementos
                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun MaterialDetailItem(
    material: com.vistony.app.Entidad.MaterialEmpleado
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Science,
                contentDescription = "Material",
                tint = PrimaryMuestraColor,
                modifier = Modifier.size(20.dp)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = material.material,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
                Text(
                    text = "${material.marca} - ${material.codigo}",
                    fontSize = 12.sp,
                    color = Color(0xFF6B7280)
                )
                Text(
                    text = "Lote: ${material.lote}",
                    fontSize = 12.sp,
                    color = Color(0xFF6B7280)
                )
            }
        }
    }
}

@Composable
fun InspeccionDetailItem(
    inspeccion: com.vistony.app.Entidad.InspeccionDimensional
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Straighten,
                    contentDescription = "Inspección",
                    tint = PrimaryMuestraColor,
                    modifier = Modifier.size(20.dp)
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Text(
                    text = "Cavidad ${inspeccion.numeroCavidad} - ${inspeccion.horaInspeccion}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Peso",
                        fontSize = 10.sp,
                        color = Color(0xFF6B7280)
                    )
                    Text(
                        text = "${inspeccion.peso}g",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )
                }
                
                Column {
                    Text(
                        text = "T° Chiller",
                        fontSize = 10.sp,
                        color = Color(0xFF6B7280)
                    )
                    Text(
                        text = "${inspeccion.temperaturaChiller}°C",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )
                }
                
                Column {
                    Text(
                        text = "T° Ciclo",
                        fontSize = 10.sp,
                        color = Color(0xFF6B7280)
                    )
                    Text(
                        text = "${inspeccion.temperaturaCiclo}°C",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )
                }
            }
            
            if (inspeccion.observacion.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Obs: ${inspeccion.observacion}",
                    fontSize = 12.sp,
                    color = Color(0xFF6B7280),
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }
        }
    }
}

@Composable
fun CheckListDetailItem(
    checkList: com.vistony.app.Entidad.CheckListInspeccion
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Checklist,
                    contentDescription = "Check List",
                    tint = PrimaryMuestraColor,
                    modifier = Modifier.size(20.dp)
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Text(
                    text = "Check List - ${checkList.horaCheckList}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            val criterios = listOf(
                "Testeado" to checkList.testeado,
                "Estabilidad" to checkList.estabilidad,
                "Tonalidad" to checkList.tonalidad,
                "Visor Uniforme" to checkList.visorUniforme,
                "Correcta Costura" to checkList.correctaCostura,
                "Libre Ovulamiento" to checkList.libreOvulamiento,
                "Libre Contaminación" to checkList.libreContaminacion
            )
            
            criterios.chunked(2).forEach { rowCriterios ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    rowCriterios.forEach { (criterio, valor) ->
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = criterio,
                                fontSize = 10.sp,
                                color = Color(0xFF6B7280)
                            )
                            MuestraChip(
                                text = valor,
                                isSelected = true,
                                color = when (valor) {
                                    "Aprobado" -> Color(0xFF10B981)
                                    "Observado" -> Color(0xFFF59E0B)
                                    "Rechazado" -> Color(0xFFEF4444)
                                    else -> Color(0xFF6B7280)
                                }
                            )
                        }
                    }
                }
            }
            
            if (checkList.observacion.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Obs: ${checkList.observacion}",
                    fontSize = 12.sp,
                    color = Color(0xFF6B7280),
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }
        }
    }
}

@Composable
fun EvaluacionDetailItem(
    evaluacion: com.vistony.app.Entidad.EvaluacionProduccion
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Assessment,
                    contentDescription = "Evaluación",
                    tint = PrimaryMuestraColor,
                    modifier = Modifier.size(20.dp)
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Text(
                    text = "Evaluación de Producción",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Paletas
            Text(
                text = "Paletas",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Aprobadas",
                        fontSize = 10.sp,
                        color = Color(0xFF6B7280)
                    )
                    Text(
                        text = evaluacion.paletasAprobadas.ifEmpty { "0" },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = SuccessColor
                    )
                }
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Observadas",
                        fontSize = 10.sp,
                        color = Color(0xFF6B7280)
                    )
                    Text(
                        text = evaluacion.paletasObservadas.ifEmpty { "0" },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = WarningColor
                    )
                }
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Rechazadas",
                        fontSize = 10.sp,
                        color = Color(0xFF6B7280)
                    )
                    Text(
                        text = evaluacion.paletasRechazadas.ifEmpty { "0" },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = ErrorColor
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Bolsas
            Text(
                text = "Bolsas",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Aprobadas",
                        fontSize = 10.sp,
                        color = Color(0xFF6B7280)
                    )
                    Text(
                        text = evaluacion.bolsasAprobadas.ifEmpty { "0" },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = SuccessColor
                    )
                }
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Observadas",
                        fontSize = 10.sp,
                        color = Color(0xFF6B7280)
                    )
                    Text(
                        text = evaluacion.bolsasObservadas.ifEmpty { "0" },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = WarningColor
                    )
                }
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Rechazadas",
                        fontSize = 10.sp,
                        color = Color(0xFF6B7280)
                    )
                    Text(
                        text = evaluacion.bolsasRechazadas.ifEmpty { "0" },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = ErrorColor
                    )
                }
            }
            
            if (evaluacion.criteriosEvaluacion.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Criterios: ${evaluacion.criteriosEvaluacion}",
                    fontSize = 12.sp,
                    color = Color(0xFF6B7280),
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }
        }
    }
}

@Composable
fun MuestraStatusChip(
    estado: String,
    isLarge: Boolean = false
) {
    val (backgroundColor, contentColor) = when (estado) {
        "Completado" -> Color(0xFF10B981) to Color.White
        "En Proceso" -> Color(0xFFF59E0B) to Color.White
        "Cancelado" -> Color(0xFFEF4444) to Color.White
        else -> Color(0xFF6B7280) to Color.White
    }
    
    val padding = if (isLarge) 12.dp else 8.dp
    val fontSize = if (isLarge) 14.sp else 12.sp
    
    Box(
        modifier = Modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = padding, vertical = padding)
    ) {
        Text(
            text = estado,
            fontSize = fontSize,
            fontWeight = FontWeight.Medium,
            color = contentColor
        )
    }
}




