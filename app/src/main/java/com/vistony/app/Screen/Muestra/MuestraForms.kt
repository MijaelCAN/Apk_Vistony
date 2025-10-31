package com.vistony.app.Screen.Muestra

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vistony.app.Entidad.MaterialEmpleado
import com.vistony.app.Entidad.InspeccionDimensional
import com.vistony.app.Entidad.CheckListInspeccion
import com.vistony.app.Screen.Generic.*
import com.vistony.app.ViewModel.MuestraViewModel

@Composable
fun MaterialForm(
    muestraViewModel: MuestraViewModel,
    padding: androidx.compose.ui.unit.Dp
) {
    val formState by muestraViewModel.materialFormState.collectAsState()
    val materiales by muestraViewModel.materialesTemporales.collectAsState()
    
    MuestraSectionTitle(
        title = "Formulación de Material Empleado",
        subtitle = "Registre los materiales utilizados",
        icon = Icons.Default.Build
    )
    
    Spacer(modifier = Modifier.height(24.dp))
    
    // Formulario de material
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Nuevo Material",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Material y Marca
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MuestraTextField(
                    value = formState.material,
                    onValueChange = { muestraViewModel.updateMaterial("material", it) },
                    label = "Material",
                    modifier = Modifier.weight(1f),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Science,
                            contentDescription = null,
                            tint = Color(0xFF9CA3AF),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                )
                
                MuestraTextField(
                    value = formState.marca,
                    onValueChange = { muestraViewModel.updateMaterial("marca", it) },
                    label = "Marca",
                    modifier = Modifier.weight(1f),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.BrandingWatermark,
                            contentDescription = null,
                            tint = Color(0xFF9CA3AF),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Código y Lote
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MuestraTextField(
                    value = formState.codigo,
                    onValueChange = { muestraViewModel.updateMaterial("codigo", it) },
                    label = "Código",
                    modifier = Modifier.weight(1f),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.QrCode,
                            contentDescription = null,
                            tint = Color(0xFF9CA3AF),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                )
                
                MuestraTextField(
                    value = formState.lote,
                    onValueChange = { muestraViewModel.updateMaterial("lote", it) },
                    label = "Lote",
                    modifier = Modifier.weight(1f),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Inventory,
                            contentDescription = null,
                            tint = Color(0xFF9CA3AF),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            MuestraButton(
                text = "Agregar Material",
                onClick = { muestraViewModel.agregarMaterial() },
                icon = Icons.Default.Add,
                enabled = formState.isFormValid,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
    
    Spacer(modifier = Modifier.height(24.dp))
    
    // Lista de materiales agregados
    if (materiales.isNotEmpty()) {
        Text(
            text = "Materiales Agregados (${materiales.size})",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF111827)
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            materiales.forEach { material ->
                MaterialItemCard(
                    material = material,
                    onDelete = { muestraViewModel.eliminarMaterial(material.id) }
                )
            }
        }
    }
}

@Composable
fun MaterialItemCard(
    material: MaterialEmpleado,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                tint = Color(0xFF4F46E5),
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
            }
            
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = Color(0xFFEF4444),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun InspeccionDimensionalForm(
    muestraViewModel: MuestraViewModel,
    padding: androidx.compose.ui.unit.Dp
) {
    val formState by muestraViewModel.inspeccionFormState.collectAsState()
    val inspecciones by muestraViewModel.inspeccionesTemporales.collectAsState()
    
    MuestraSectionTitle(
        title = "Inspección Dimensional",
        subtitle = "Mediciones por cavidad",
        icon = Icons.Default.Straighten
    )
    
    Spacer(modifier = Modifier.height(24.dp))
    
    // Formulario de inspección
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Nueva Inspección",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Hora y Parámetros de máquina
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MuestraTextField(
                    value = formState.horaInspeccion,
                    onValueChange = { muestraViewModel.updateInspeccion("horaInspeccion", it) },
                    label = "Hora de Inspección",
                    modifier = Modifier.weight(1f),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = Color(0xFF9CA3AF),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                )
                
                MuestraTextField(
                    value = formState.numeroCavidad,
                    onValueChange = { muestraViewModel.updateInspeccion("numeroCavidad", it) },
                    label = "N° Cavidad",
                    modifier = Modifier.weight(1f),
                    keyboardType = KeyboardType.Number,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Numbers,
                            contentDescription = null,
                            tint = Color(0xFF9CA3AF),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Temperaturas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MuestraTextField(
                    value = formState.temperaturaChiller,
                    onValueChange = { muestraViewModel.updateInspeccion("temperaturaChiller", it) },
                    label = "T° Chiller",
                    modifier = Modifier.weight(1f),
                    keyboardType = KeyboardType.Decimal,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Thermostat,
                            contentDescription = null,
                            tint = Color(0xFF9CA3AF),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                )
                
                MuestraTextField(
                    value = formState.temperaturaCiclo,
                    onValueChange = { muestraViewModel.updateInspeccion("temperaturaCiclo", it) },
                    label = "T° Ciclo",
                    modifier = Modifier.weight(1f),
                    keyboardType = KeyboardType.Decimal,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Thermostat,
                            contentDescription = null,
                            tint = Color(0xFF9CA3AF),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Peso
            MuestraTextField(
                value = formState.peso,
                onValueChange = { muestraViewModel.updateInspeccion("peso", it) },
                label = "Peso",
                keyboardType = KeyboardType.Decimal,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Scale,
                        contentDescription = null,
                        tint = Color(0xFF9CA3AF),
                        modifier = Modifier.size(20.dp)
                    )
                }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Medidas de Diámetro de Rosca
            Text(
                text = "Diámetro de Rosca",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MuestraTextField(
                    value = formState.diametroRoscaMedida1,
                    onValueChange = { muestraViewModel.updateInspeccion("diametroRoscaMedida1", it) },
                    label = "Medida 1",
                    modifier = Modifier.weight(1f),
                    keyboardType = KeyboardType.Decimal
                )
                
                MuestraTextField(
                    value = formState.diametroRoscaMedida2,
                    onValueChange = { muestraViewModel.updateInspeccion("diametroRoscaMedida2", it) },
                    label = "Medida 2",
                    modifier = Modifier.weight(1f),
                    keyboardType = KeyboardType.Decimal
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Altura de Boca
            Text(
                text = "Altura de Boca",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MuestraTextField(
                    value = formState.alturaBocaMedida1,
                    onValueChange = { muestraViewModel.updateInspeccion("alturaBocaMedida1", it) },
                    label = "M1",
                    modifier = Modifier.weight(1f),
                    keyboardType = KeyboardType.Decimal
                )
                
                MuestraTextField(
                    value = formState.alturaBocaMedida2,
                    onValueChange = { muestraViewModel.updateInspeccion("alturaBocaMedida2", it) },
                    label = "M2",
                    modifier = Modifier.weight(1f),
                    keyboardType = KeyboardType.Decimal
                )
                
                MuestraTextField(
                    value = formState.alturaBocaMedida3,
                    onValueChange = { muestraViewModel.updateInspeccion("alturaBocaMedida3", it) },
                    label = "M3",
                    modifier = Modifier.weight(1f),
                    keyboardType = KeyboardType.Decimal
                )
                
                MuestraTextField(
                    value = formState.alturaBocaMedida4,
                    onValueChange = { muestraViewModel.updateInspeccion("alturaBocaMedida4", it) },
                    label = "M4",
                    modifier = Modifier.weight(1f),
                    keyboardType = KeyboardType.Decimal
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Observaciones
            MuestraTextField(
                value = formState.observacion,
                onValueChange = { muestraViewModel.updateInspeccion("observacion", it) },
                label = "Observaciones",
                minLines = 2,
                maxLines = 3,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Notes,
                        contentDescription = null,
                        tint = Color(0xFF9CA3AF),
                        modifier = Modifier.size(20.dp)
                    )
                }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            MuestraButton(
                text = "Agregar Inspección",
                onClick = { muestraViewModel.agregarInspeccion() },
                icon = Icons.Default.Add,
                enabled = formState.isFormValid,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
    
    Spacer(modifier = Modifier.height(24.dp))
    
    // Lista de inspecciones agregadas
    if (inspecciones.isNotEmpty()) {
        Text(
            text = "Inspecciones Agregadas (${inspecciones.size})",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF111827)
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            inspecciones.forEach { inspeccion ->
                InspeccionItemCard(
                    inspeccion = inspeccion,
                    onDelete = { muestraViewModel.eliminarInspeccion(inspeccion.id) }
                )
            }
        }
    }
}

@Composable
fun InspeccionItemCard(
    inspeccion: InspeccionDimensional,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Straighten,
                contentDescription = "Inspección",
                tint = Color(0xFF4F46E5),
                modifier = Modifier.size(20.dp)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Cavidad ${inspeccion.numeroCavidad} - ${inspeccion.horaInspeccion}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
                Text(
                    text = "Peso: ${inspeccion.peso}g | T°: ${inspeccion.temperaturaChiller}°C",
                    fontSize = 12.sp,
                    color = Color(0xFF6B7280)
                )
            }
            
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = Color(0xFFEF4444),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun CheckListForm(
    muestraViewModel: MuestraViewModel,
    padding: Dp
) {
    val formState by muestraViewModel.checkListFormState.collectAsState()
    val checkLists by muestraViewModel.checkListsTemporales.collectAsState()
    
    MuestraSectionTitle(
        title = "Check List de Inspección",
        subtitle = "Verificación de calidad",
        icon = Icons.Default.Checklist
    )
    
    Spacer(modifier = Modifier.height(24.dp))
    
    // Formulario de check list
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Nuevo Check List",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Hora de check list
            /*MuestraTextField(
                value = formState.horaCheckList,
                onValueChange = { muestraViewModel.updateCheckList("horaCheckList", it) },
                label = "Hora de Check List",
                enabled = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = Color(0xFF9CA3AF),
                        modifier = Modifier.size(20.dp)
                    )
                }
            )*/
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Criterios de evaluación
            Text(
                text = "Criterios de Evaluación",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MuestraCriterioSelector(
                    label = "Testeado",
                    selectedValue = formState.testeado,
                    onValueChange = { muestraViewModel.updateCheckList("testeado", it) }
                )
                
                MuestraCriterioSelector(
                    label = "Estabilidad",
                    selectedValue = formState.estabilidad,
                    onValueChange = { muestraViewModel.updateCheckList("estabilidad", it) }
                )
                
                MuestraCriterioSelector(
                    label = "Tonalidad",
                    selectedValue = formState.tonalidad,
                    onValueChange = { muestraViewModel.updateCheckList("tonalidad", it) }
                )
                
                MuestraCriterioSelector(
                    label = "Visor Uniforme",
                    selectedValue = formState.visorUniforme,
                    onValueChange = { muestraViewModel.updateCheckList("visorUniforme", it) }
                )
                
                MuestraCriterioSelector(
                    label = "Correcta Costura",
                    selectedValue = formState.correctaCostura,
                    onValueChange = { muestraViewModel.updateCheckList("correctaCostura", it) }
                )
                
                MuestraCriterioSelector(
                    label = "Libre de Ovulamiento",
                    selectedValue = formState.libreOvulamiento,
                    onValueChange = { muestraViewModel.updateCheckList("libreOvulamiento", it) }
                )
                
                MuestraCriterioSelector(
                    label = "Libre de Contaminación",
                    selectedValue = formState.libreContaminacion,
                    onValueChange = { muestraViewModel.updateCheckList("libreContaminacion", it) }
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Observaciones
            MuestraTextField(
                value = formState.observacion,
                onValueChange = { muestraViewModel.updateCheckList("observacion", it) },
                label = "Observaciones",
                minLines = 2,
                maxLines = 3,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Notes,
                        contentDescription = null,
                        tint = Color(0xFF9CA3AF),
                        modifier = Modifier.size(20.dp)
                    )
                }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            MuestraButton(
                text = "Agregar Check List",
                onClick = { muestraViewModel.agregarCheckList() },
                icon = Icons.Default.Add,
                enabled = formState.isFormValid,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
    
    Spacer(modifier = Modifier.height(24.dp))
    
    // Lista de check lists agregados
    if (checkLists.isNotEmpty()) {
        Text(
            text = "Check Lists Agregados (${checkLists.size})",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF111827)
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            checkLists.forEach { checkList ->
                CheckListItemCard(
                    checkList = checkList,
                    onDelete = { muestraViewModel.eliminarCheckList(checkList.id) }
                )
            }
        }
    }
}

@Composable
fun CheckListItemCard(
    checkList: CheckListInspeccion,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Checklist,
                contentDescription = "Check List",
                tint = Color(0xFF4F46E5),
                modifier = Modifier.size(20.dp)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Check List - ${checkList.horaCheckList}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
                Text(
                    text = "Testeado: ${checkList.testeado} | Estabilidad: ${checkList.estabilidad}",
                    fontSize = 12.sp,
                    color = Color(0xFF6B7280)
                )
            }
            
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = Color(0xFFEF4444),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun EvaluacionForm(
    muestraViewModel: MuestraViewModel,
    padding: androidx.compose.ui.unit.Dp
) {
    val formState by muestraViewModel.evaluacionFormState.collectAsState()
    
    MuestraSectionTitle(
        title = "Evaluación de la Producción",
        subtitle = "Criterios finales de evaluación",
        icon = Icons.Default.Assessment
    )
    
    Spacer(modifier = Modifier.height(24.dp))
    
    // Estado general
    MuestraTextField(
        value = formState.estado,
        onValueChange = { muestraViewModel.updateEvaluacion("estado", it) },
        label = "Estado General",
        placeholder = "Ej: Aprobado, Observado, Rechazado",
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Assessment,
                contentDescription = null,
                tint = Color(0xFF9CA3AF),
                modifier = Modifier.size(20.dp)
            )
        }
    )
    
    Spacer(modifier = Modifier.height(16.dp))
    
    // Criterios de evaluación
    Text(
        text = "Criterios de Evaluación",
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF111827)
    )
    
    Spacer(modifier = Modifier.height(12.dp))
    
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        MuestraCriterioSelector(
            label = "Paletas",
            selectedValue = formState.paletas,
            onValueChange = { muestraViewModel.updateEvaluacion("paletas", it) }
        )
        
        MuestraCriterioSelector(
            label = "Bolsas",
            selectedValue = formState.bolsas,
            onValueChange = { muestraViewModel.updateEvaluacion("bolsas", it) }
        )
    }
    
    Spacer(modifier = Modifier.height(16.dp))
    
    // Criterios de evaluación detallados
    MuestraTextField(
        value = formState.criteriosEvaluacion,
        onValueChange = { muestraViewModel.updateEvaluacion("criteriosEvaluacion", it) },
        label = "Criterios de Evaluación Detallados",
        placeholder = "Describa los criterios específicos utilizados",
        minLines = 3,
        maxLines = 5,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Notes,
                contentDescription = null,
                tint = Color(0xFF9CA3AF),
                modifier = Modifier.size(20.dp)
            )
        }
    )
}
