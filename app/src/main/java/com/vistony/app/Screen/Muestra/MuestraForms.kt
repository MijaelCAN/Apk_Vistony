package com.vistony.app.Screen.Muestra

import android.util.Log
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ModalBottomSheet
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.LaunchedEffect
import com.vistony.app.Entidad.MaterialEmpleado
import com.vistony.app.Entidad.InspeccionDimensional
import com.vistony.app.Entidad.CheckListInspeccion
import com.vistony.app.Screen.Generic.*
import com.vistony.app.Screen.Generic.SuccessColor
import com.vistony.app.Screen.Generic.WarningColor
import com.vistony.app.Screen.Generic.ErrorColor
import com.vistony.app.ViewModel.MuestraViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialForm(
    muestraViewModel: MuestraViewModel,
    padding: androidx.compose.ui.unit.Dp
) {
    val formState by muestraViewModel.materialFormState.collectAsState()
    val materiales by muestraViewModel.materialesTemporales.collectAsState()
    
    // Estado para el modal de detalle
    var selectedMaterial by remember { mutableStateOf<MaterialEmpleado?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    
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
                    onClick = {
                        selectedMaterial = material
                        scope.launch {
                            sheetState.show()
                        }
                    },
                    onDelete = { muestraViewModel.eliminarMaterial(material.id) }
                )
            }
        }
    }
    
    // Modal para mostrar detalle del Material
    selectedMaterial?.let { material ->
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch {
                    sheetState.hide()
                }.invokeOnCompletion {
                    selectedMaterial = null
                }
            },
            sheetState = sheetState,
            containerColor = Color(0xFFF7F7F7)
        ) {
            MaterialDetailModal(
                material = material,
                onClose = {
                    scope.launch {
                        sheetState.hide()
                    }.invokeOnCompletion {
                        selectedMaterial = null
                    }
                }
            )
        }
    }
}

@Composable
fun MaterialItemCard(
    material: MaterialEmpleado,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
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
            
            Icon(
                imageVector = Icons.Default.Visibility,
                contentDescription = "Ver detalle",
                tint = Color(0xFF4F46E5),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun MaterialDetailModal(
    material: MaterialEmpleado,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Detalle del Material",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )
            
            IconButton(onClick = onClose) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cerrar",
                    tint = Color(0xFF6B7280)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Información del material
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DetailRow(
                label = "Material",
                value = material.material,
                icon = Icons.Default.Science
            )
            
            DetailRow(
                label = "Marca",
                value = material.marca,
                icon = Icons.Default.BrandingWatermark
            )
            
            DetailRow(
                label = "Código",
                value = material.codigo,
                icon = Icons.Default.QrCode
            )
            
            DetailRow(
                label = "Lote",
                value = material.lote,
                icon = Icons.Default.Inventory
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun DetailRow(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isError: Boolean = false
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isError) Color(0xFFFFEBEE) else Color(0xFFF9FAFB)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isError) Color(0xFFDC2626) else Color(0xFF4F46E5),
                modifier = Modifier.size(20.dp)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
                Text(
                    text = label,
                    fontSize = 12.sp,
                    color = Color(0xFF6B7280)
                )
                Text(
                    text = value.ifEmpty { "No especificado" },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isError) Color(0xFFDC2626) else Color(0xFF111827)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InspeccionDimensionalForm(
    muestraViewModel: MuestraViewModel,
    padding: androidx.compose.ui.unit.Dp,
    isCompletado: Boolean = false
) {
    val formState by muestraViewModel.inspeccionFormState.collectAsState()
    val inspecciones by muestraViewModel.inspeccionesTemporales.collectAsState()
    val cabeceraFormState by muestraViewModel.cabeceraFormState.collectAsState()
    val especificacion by muestraViewModel.especificacionSoplado.collectAsState()
    val currentMuestraId by muestraViewModel.currentMuestraId.collectAsState()
    val muestrasCompletas by muestraViewModel.muestrasCompletas.collectAsState()

    // Cuando la muestra ya está completada, a veces las listas "temporales" vienen vacías.
    // En ese caso, mostramos las inspecciones desde el cache de `muestrasCompletas`.

    // Estado para el modal de detalle
    var selectedInspeccion by remember { mutableStateOf<InspeccionDimensional?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    // Track previous muestraId to detect changes
    var previousMuestraId by remember { mutableStateOf<String?>(null) }

    // Resetear el formulario cuando cambia la muestra actual (solo cuando cambia, no en cada recomposición)
    LaunchedEffect(currentMuestraId) {
        if (currentMuestraId != null && currentMuestraId != previousMuestraId) {
            // Resetear el formulario cuando se abre para una nueva muestra
            muestraViewModel.resetInspeccionForm()
            previousMuestraId = currentMuestraId
        } else if (currentMuestraId == null) {
            // Si no hay muestra seleccionada, resetear también
            previousMuestraId = null
        }
    }

    // Obtener especificaciones cuando se ingresa al formulario o cambia el código de producto
    LaunchedEffect(cabeceraFormState.codigo) {
        if (cabeceraFormState.codigo.isNotEmpty()) {
            muestraViewModel.obtenerEspecificacionSoplado(cabeceraFormState.codigo)
        }
    }

    // Función helper para formatear rango
    fun formatearRango(min: String, max: String, unidad: String = ""): String {
        if (min.isEmpty() || max.isEmpty()) return ""
        val minFormateado = min.toDoubleOrNull()?.let {
            if (it % 1.0 == 0.0) it.toInt().toString() else String.format("%.1f", it)
        } ?: min
        val maxFormateado = max.toDoubleOrNull()?.let {
            if (it % 1.0 == 0.0) it.toInt().toString() else String.format("%.1f", it)
        } ?: max
        return "($minFormateado$unidad - $maxFormateado$unidad)"
    }

    // Función helper para validar valor
    fun validarValor(valor: String, min: String, max: String): Boolean {
        if (valor.isBlank() || min.isEmpty() || max.isEmpty()) return true
        return muestraViewModel.validarRango(valor, min, max)
    }

    MuestraSectionTitle(
        title = "Inspección Dimensional",
        subtitle = "Mediciones por cavidad",
        icon = Icons.Default.Straighten
    )

    Spacer(modifier = Modifier.height(24.dp))

    // Si está completado, mostrar solo el aviso y el listado
    if (isCompletado) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3CD)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = WarningColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "La muestra está completada. Solo se permite visualización.",
                    fontSize = 14.sp,
                    color = Color(0xFF856404),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    } else {
        // Formulario de inspección (solo se muestra si NO está completado)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Nueva Inspección",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Lógica para N° Cavidad
                val listaVacia = inspecciones.isEmpty()
                val ultimoNumeroCavidad =
                    inspecciones.mapNotNull { it.numeroCavidad.toIntOrNull() }.maxOrNull()
                val siguienteNumeroCavidad = if (ultimoNumeroCavidad != null) {
                    (ultimoNumeroCavidad + 1).toString()
                } else {
                    ""
                }

                // Auto-completar número de cavidad si hay registros previos
                LaunchedEffect(ultimoNumeroCavidad) {
                    if (ultimoNumeroCavidad != null && !listaVacia) {
                        muestraViewModel.updateInspeccion("numeroCavidad", siguienteNumeroCavidad)
                    }
                }

                // Valor a mostrar en el campo
                val valorMostrar = if (listaVacia) {
                    formState.numeroCavidad
                } else {
                    siguienteNumeroCavidad
                }

                // Hora y Parámetros de máquina
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    /*MuestraTextField(
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
                )*/

                    MuestraTextField(
                        value = valorMostrar,
                        onValueChange = { nuevoValor ->
                            if (listaVacia && !isCompletado) {
                                // Solo permitir 1 o 5 si la lista está vacía
                                if (nuevoValor.isEmpty() || nuevoValor == "1" || nuevoValor == "5") {
                                    muestraViewModel.updateInspeccion("numeroCavidad", nuevoValor)
                                }
                            }
                            // Si hay registros, no permitir edición (ya está deshabilitado)
                        },
                        label = "N° Cavidad",
                        modifier = Modifier.weight(1f),
                        keyboardType = KeyboardType.Number,
                        enabled = listaVacia && !isCompletado, // Solo habilitado si la lista está vacía y no está completado
                        readOnly = !listaVacia || isCompletado, // Solo lectura si hay registros o está completado
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

                // Mensaje informativo cuando está deshabilitado
                if (!listaVacia && ultimoNumeroCavidad != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Número de cavidad asignado automáticamente: $siguienteNumeroCavidad",
                        fontSize = 12.sp,
                        color = Color(0xFF6B7280),
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Medidas de Diámetro de Rosca
                Text(
                    text = "Parametros de Máquina",
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
                        value = formState.temperaturaChiller,
                        onValueChange = {
                            muestraViewModel.updateInspeccion(
                                "temperaturaChiller",
                                it
                            )
                        },
                        label = "T° Chiller",
                        modifier = Modifier.weight(1f),
                        keyboardType = KeyboardType.Decimal,
                        enabled = !isCompletado,
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
                        onValueChange = {
                            muestraViewModel.updateInspeccion(
                                "temperaturaCiclo",
                                it
                            )
                        },
                        label = "T° Ciclo",
                        modifier = Modifier.weight(1f),
                        keyboardType = KeyboardType.Decimal,
                        enabled = !isCompletado,
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
                val pesoValido =
                    especificacion?.let { validarValor(formState.peso, it.pesoMin, it.pesoMax) }
                        ?: true
                val pesoRango =
                    especificacion?.let { formatearRango(it.pesoMin, it.pesoMax, "g") } ?: ""
                MuestraTextField(
                    value = formState.peso,
                    onValueChange = { muestraViewModel.updateInspeccion("peso", it) },
                    label = "Peso $pesoRango",
                    keyboardType = KeyboardType.Decimal,
                    enabled = !isCompletado,
                    isError = !pesoValido,
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
                val diametroRoscaRango = especificacion?.let {
                    formatearRango(
                        it.diametroRoscaMin,
                        it.diametroRoscaMax,
                        "mm"
                    )
                } ?: ""
                val diametroRosca1Valido = especificacion?.let {
                    validarValor(
                        formState.diametroRoscaMedida1,
                        it.diametroRoscaMin,
                        it.diametroRoscaMax
                    )
                } ?: true
                val diametroRosca2Valido = especificacion?.let {
                    validarValor(
                        formState.diametroRoscaMedida2,
                        it.diametroRoscaMin,
                        it.diametroRoscaMax
                    )
                } ?: true
                val diametroRoscaErrores =
                    listOf(!diametroRosca1Valido, !diametroRosca2Valido).count { it }

                Text(
                    text = "Diámetro de Rosca $diametroRoscaRango",
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
                        onValueChange = {
                            muestraViewModel.updateInspeccion(
                                "diametroRoscaMedida1",
                                it
                            )
                        },
                        label = "Medida 1",
                        modifier = Modifier.weight(1f),
                        keyboardType = KeyboardType.Decimal,
                        isError = !diametroRosca1Valido
                    )

                    MuestraTextField(
                        value = formState.diametroRoscaMedida2,
                        onValueChange = {
                            muestraViewModel.updateInspeccion(
                                "diametroRoscaMedida2",
                                it
                            )
                        },
                        label = "Medida 2",
                        modifier = Modifier.weight(1f),
                        keyboardType = KeyboardType.Decimal,
                        isError = !diametroRosca2Valido
                    )
                }

                // Mensaje de error para Diámetro de Rosca
                if (diametroRoscaErrores > 0) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$diametroRoscaErrores ${if (diametroRoscaErrores == 1) "medida no se encuentra" else "medidas no se encuentran"} dentro del parámetro",
                        fontSize = 12.sp,
                        color = Color(0xFFDC2626),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Altura de Boca
                val alturaBocaRango =
                    especificacion?.let { formatearRango(it.alturaBocaMin, it.alturaBocaMax, "mm") }
                        ?: ""
                val alturaBoca1Valido = especificacion?.let {
                    validarValor(
                        formState.alturaBocaMedida1,
                        it.alturaBocaMin,
                        it.alturaBocaMax
                    )
                } ?: true
                val alturaBoca2Valido = especificacion?.let {
                    validarValor(
                        formState.alturaBocaMedida2,
                        it.alturaBocaMin,
                        it.alturaBocaMax
                    )
                } ?: true
                val alturaBoca3Valido = especificacion?.let {
                    validarValor(
                        formState.alturaBocaMedida3,
                        it.alturaBocaMin,
                        it.alturaBocaMax
                    )
                } ?: true
                val alturaBoca4Valido = especificacion?.let {
                    validarValor(
                        formState.alturaBocaMedida4,
                        it.alturaBocaMin,
                        it.alturaBocaMax
                    )
                } ?: true
                val alturaBocaErrores = listOf(
                    !alturaBoca1Valido,
                    !alturaBoca2Valido,
                    !alturaBoca3Valido,
                    !alturaBoca4Valido
                ).count { it }

                Text(
                    text = "Altura de Boca $alturaBocaRango",
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
                        onValueChange = {
                            muestraViewModel.updateInspeccion(
                                "alturaBocaMedida1",
                                it
                            )
                        },
                        label = "M1",
                        modifier = Modifier.weight(1f),
                        keyboardType = KeyboardType.Decimal,
                        isError = !alturaBoca1Valido
                    )

                    MuestraTextField(
                        value = formState.alturaBocaMedida2,
                        onValueChange = {
                            muestraViewModel.updateInspeccion(
                                "alturaBocaMedida2",
                                it
                            )
                        },
                        label = "M2",
                        modifier = Modifier.weight(1f),
                        keyboardType = KeyboardType.Decimal,
                        isError = !alturaBoca2Valido
                    )

                    MuestraTextField(
                        value = formState.alturaBocaMedida3,
                        onValueChange = {
                            muestraViewModel.updateInspeccion(
                                "alturaBocaMedida3",
                                it
                            )
                        },
                        label = "M3",
                        modifier = Modifier.weight(1f),
                        keyboardType = KeyboardType.Decimal,
                        isError = !alturaBoca3Valido
                    )

                    MuestraTextField(
                        value = formState.alturaBocaMedida4,
                        onValueChange = {
                            muestraViewModel.updateInspeccion(
                                "alturaBocaMedida4",
                                it
                            )
                        },
                        label = "M4",
                        modifier = Modifier.weight(1f),
                        keyboardType = KeyboardType.Decimal,
                        isError = !alturaBoca4Valido
                    )
                }

                // Mensaje de error para Altura de Boca
                if (alturaBocaErrores > 0) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$alturaBocaErrores ${if (alturaBocaErrores == 1) "medida no se encuentra" else "medidas no se encuentran"} dentro del parámetro",
                        fontSize = 12.sp,
                        color = Color(0xFFDC2626),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Diámetro de Precinto
                val diametroPrecintoRango = especificacion?.let {
                    formatearRango(
                        it.diametroPrecintoMin,
                        it.diametroPrecintoMax,
                        "mm"
                    )
                } ?: ""
                val diametroPrecinto1Valido = especificacion?.let {
                    validarValor(
                        formState.diametroPrecintoMedida1,
                        it.diametroPrecintoMin,
                        it.diametroPrecintoMax
                    )
                } ?: true
                val diametroPrecinto2Valido = especificacion?.let {
                    validarValor(
                        formState.diametroPrecintoMedida2,
                        it.diametroPrecintoMin,
                        it.diametroPrecintoMax
                    )
                } ?: true
                val diametroPrecinto3Valido = especificacion?.let {
                    validarValor(
                        formState.diametroPrecintoMedida3,
                        it.diametroPrecintoMin,
                        it.diametroPrecintoMax
                    )
                } ?: true
                val diametroPrecintoErrores = listOf(
                    !diametroPrecinto1Valido,
                    !diametroPrecinto2Valido,
                    !diametroPrecinto3Valido
                ).count { it }

                Text(
                    text = "Diametro de Precinto $diametroPrecintoRango",
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
                        value = formState.diametroPrecintoMedida1,
                        onValueChange = {
                            muestraViewModel.updateInspeccion(
                                "diametroPrecintoMedida1",
                                it
                            )
                        },
                        label = "M1",
                        modifier = Modifier.weight(1f),
                        keyboardType = KeyboardType.Decimal,
                        isError = !diametroPrecinto1Valido
                    )

                    MuestraTextField(
                        value = formState.diametroPrecintoMedida2,
                        onValueChange = {
                            muestraViewModel.updateInspeccion(
                                "diametroPrecintoMedida2",
                                it
                            )
                        },
                        label = "M2",
                        modifier = Modifier.weight(1f),
                        keyboardType = KeyboardType.Decimal,
                        isError = !diametroPrecinto2Valido
                    )

                    MuestraTextField(
                        value = formState.diametroPrecintoMedida3,
                        onValueChange = {
                            muestraViewModel.updateInspeccion(
                                "diametroPrecintoMedida3",
                                it
                            )
                        },
                        label = "M3",
                        modifier = Modifier.weight(1f),
                        keyboardType = KeyboardType.Decimal,
                        isError = !diametroPrecinto3Valido
                    )
                }

                // Mensaje de error para Diámetro de Precinto
                if (diametroPrecintoErrores > 0) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$diametroPrecintoErrores ${if (diametroPrecintoErrores == 1) "medida no se encuentra" else "medidas no se encuentran"} dentro del parámetro",
                        fontSize = 12.sp,
                        color = Color(0xFFDC2626),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Altura Total
                val alturaTotalRango = especificacion?.let {
                    formatearRango(
                        it.alturaTotalMin,
                        it.alturaTotalMax,
                        "mm"
                    )
                } ?: ""
                val alturaTotal1Valido = especificacion?.let {
                    validarValor(
                        formState.alturaTotalMedida1,
                        it.alturaTotalMin,
                        it.alturaTotalMax
                    )
                } ?: true
                val alturaTotal2Valido = especificacion?.let {
                    validarValor(
                        formState.alturaTotalMedida2,
                        it.alturaTotalMin,
                        it.alturaTotalMax
                    )
                } ?: true
                val alturaTotalErrores =
                    listOf(!alturaTotal1Valido, !alturaTotal2Valido).count { it }

                Text(
                    text = "Altura Total $alturaTotalRango",
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
                        value = formState.alturaTotalMedida1,
                        onValueChange = {
                            muestraViewModel.updateInspeccion(
                                "alturaTotalMedida1",
                                it
                            )
                        },
                        label = "M1",
                        modifier = Modifier.weight(1f),
                        keyboardType = KeyboardType.Decimal,
                        isError = !alturaTotal1Valido
                    )

                    MuestraTextField(
                        value = formState.alturaTotalMedida2,
                        onValueChange = {
                            muestraViewModel.updateInspeccion(
                                "alturaTotalMedida2",
                                it
                            )
                        },
                        label = "M2",
                        modifier = Modifier.weight(1f),
                        keyboardType = KeyboardType.Decimal,
                        isError = !alturaTotal2Valido
                    )
                }

                // Mensaje de error para Altura Total
                if (alturaTotalErrores > 0) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$alturaTotalErrores ${if (alturaTotalErrores == 1) "medida no se encuentra" else "medidas no se encuentran"} dentro del parámetro",
                        fontSize = 12.sp,
                        color = Color(0xFFDC2626),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Diametro Interno
                val diametroInternoRango = especificacion?.let {
                    formatearRango(
                        it.diametroInternoMin,
                        it.diametroInternoMax,
                        "mm"
                    )
                } ?: ""
                val diametroInterno1Valido = especificacion?.let {
                    validarValor(
                        formState.diametroInternoMedida1,
                        it.diametroInternoMin,
                        it.diametroInternoMax
                    )
                } ?: true
                val diametroInterno2Valido = especificacion?.let {
                    validarValor(
                        formState.diametroInternoMedida2,
                        it.diametroInternoMin,
                        it.diametroInternoMax
                    )
                } ?: true
                val diametroInternoErrores =
                    listOf(!diametroInterno1Valido, !diametroInterno2Valido).count { it }

                Text(
                    text = "Diametro Interno $diametroInternoRango",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MuestraTextField(
                        value = formState.diametroInternoMedida1,
                        onValueChange = {
                            muestraViewModel.updateInspeccion(
                                "diametroInternoMedida1",
                                it
                            )
                        },
                        label = "M1",
                        modifier = Modifier.weight(1f),
                        keyboardType = KeyboardType.Decimal,
                        isError = !diametroInterno1Valido
                    )

                    MuestraTextField(
                        value = formState.diametroInternoMedida2,
                        onValueChange = {
                            muestraViewModel.updateInspeccion(
                                "diametroInternoMedida2",
                                it
                            )
                        },
                        label = "M2",
                        modifier = Modifier.weight(1f),
                        keyboardType = KeyboardType.Decimal,
                        isError = !diametroInterno2Valido
                    )
                }

                // Mensaje de error para Diámetro Interno
                if (diametroInternoErrores > 0) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$diametroInternoErrores ${if (diametroInternoErrores == 1) "medida no se encuentra" else "medidas no se encuentran"} dentro del parámetro",
                        fontSize = 12.sp,
                        color = Color(0xFFDC2626),
                        modifier = Modifier.padding(start = 4.dp)
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

                if (isCompletado) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3CD)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = WarningColor,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "La muestra está completada. Solo se permite visualización.",
                                fontSize = 14.sp,
                                color = Color(0xFF856404),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                } else {
                    MuestraButton(
                        text = "Agregar Inspección",
                        onClick = { muestraViewModel.agregarInspeccion() },
                        icon = Icons.Default.Add,
                        enabled = formState.isFormValid && !isCompletado,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(24.dp))

    // Lista de inspecciones agregadas
    if (inspecciones.isNotEmpty()) {
        Log.i("Inspecciones", "Total Inspecciones: ${inspecciones.size}")
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
                    onClick = {
                        selectedInspeccion = inspeccion
                        scope.launch {
                            sheetState.show()
                        }
                    },
                    onDelete = { muestraViewModel.eliminarInspeccion(inspeccion.id) }
                )
            }
        }
    }

    // Modal para mostrar detalle de la Inspección
    selectedInspeccion?.let { inspeccion ->
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch {
                    sheetState.hide()
                }.invokeOnCompletion {
                    selectedInspeccion = null
                }
            },
            sheetState = sheetState,
            containerColor = Color(0xFFF7F7F7)
        ) {
            InspeccionDetailModal(
                inspeccion = inspeccion,
                muestraViewModel = muestraViewModel,
                onClose = {
                    scope.launch {
                        sheetState.hide()
                    }.invokeOnCompletion {
                        selectedInspeccion = null
                    }
                }
            )
        }
    }
}

    @Composable
    fun InspeccionItemCard(
        inspeccion: InspeccionDimensional,
        onClick: () -> Unit,
        onDelete: () -> Unit
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },
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

                Icon(
                    imageVector = Icons.Default.Visibility,
                    contentDescription = "Ver detalle",
                    tint = Color(0xFF4F46E5),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }

    @Composable
    fun InspeccionDetailModal(
        inspeccion: InspeccionDimensional,
        muestraViewModel: MuestraViewModel,
        onClose: () -> Unit
    ) {
        val especificacion by muestraViewModel.especificacionSoplado.collectAsState()
        val cabeceraFormState by muestraViewModel.cabeceraFormState.collectAsState()

        // Obtener especificaciones si no están cargadas
        LaunchedEffect(cabeceraFormState.codigo) {
            if (cabeceraFormState.codigo.isNotEmpty() && especificacion == null) {
                muestraViewModel.obtenerEspecificacionSoplado(cabeceraFormState.codigo)
            }
        }

        // Función helper para formatear rango
        fun formatearRango(min: String, max: String, unidad: String = ""): String {
            if (min.isEmpty() || max.isEmpty()) return ""
            val minFormateado = min.toDoubleOrNull()?.let {
                if (it % 1.0 == 0.0) it.toInt().toString() else String.format("%.1f", it)
            } ?: min
            val maxFormateado = max.toDoubleOrNull()?.let {
                if (it % 1.0 == 0.0) it.toInt().toString() else String.format("%.1f", it)
            } ?: max
            return "($minFormateado$unidad - $maxFormateado$unidad)"
        }

        // Función helper para validar valor
        fun validarValor(valor: String, min: String, max: String): Boolean {
            if (valor.isBlank() || min.isEmpty() || max.isEmpty()) return true
            return muestraViewModel.validarRango(valor, min, max)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Detalle de Inspección Dimensional",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryMuestraColor
                )

                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = Color(0xFF6B7280)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Información general
            Text(
                text = "Cavidad ${inspeccion.numeroCavidad} - ${inspeccion.horaInspeccion}",
                fontSize = 14.sp,
                color = Color(0xFF6B7280),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Parámetros de máquina
            Text(
                text = "Parámetros de Máquina",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryMuestraColor,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                // Temperatura Chiller
                Column {
                    Text(
                        text = "Temperatura Chiller",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF374151),
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF9FAFB),
                        border = BorderStroke(1.dp, Color(0xFFD1D5DB))
                    ) {
                        Row(
                            modifier = Modifier
                                .defaultMinSize(minHeight = 56.dp)
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Thermostat,
                                contentDescription = null,
                                tint = Color(0xFF6B7280),
                                modifier = Modifier.size(24.dp).padding(end = 12.dp)
                            )
                            Text(
                                text = "${inspeccion.temperaturaChiller}°C",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color(0xFF9CA3AF)
                            )
                        }
                    }
                }

                // Temperatura Ciclo
                Column {
                    Text(
                        text = "Temperatura Ciclo",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF374151),
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF9FAFB),
                        border = BorderStroke(1.dp, Color(0xFFD1D5DB))
                    ) {
                        Row(
                            modifier = Modifier
                                .defaultMinSize(minHeight = 56.dp)
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Thermostat,
                                contentDescription = null,
                                tint = Color(0xFF6B7280),
                                modifier = Modifier.size(24.dp).padding(end = 12.dp)
                            )
                            Text(
                                text = "${inspeccion.temperaturaCiclo}°C",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color(0xFF9CA3AF)
                            )
                        }
                    }
                }

                // Peso
                val pesoValido =
                    especificacion?.let { validarValor(inspeccion.peso, it.pesoMin, it.pesoMax) }
                        ?: true
                val pesoRango =
                    especificacion?.let { formatearRango(it.pesoMin, it.pesoMax, "g") } ?: ""
                Column {
                    Text(
                        text = "Peso $pesoRango",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (!pesoValido) Color(0xFFDC2626) else Color(0xFF374151),
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = if (!pesoValido) Color(0xFFFFEBEE) else Color(0xFFF9FAFB),
                        border = BorderStroke(
                            width = 1.dp,
                            color = if (!pesoValido) Color(0xFFDC2626) else Color(0xFFD1D5DB)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .defaultMinSize(minHeight = 56.dp)
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Scale,
                                contentDescription = null,
                                tint = if (!pesoValido) Color(0xFFDC2626) else Color(0xFF6B7280),
                                modifier = Modifier.size(24.dp).padding(end = 12.dp)
                            )
                            Text(
                                text = "${inspeccion.peso}g",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                color = if (!pesoValido) Color(0xFFDC2626) else Color(0xFF9CA3AF)
                            )
                        }
                    }
                }
            }

            // Diámetro de Rosca
            val diametroRoscaRango = especificacion?.let {
                formatearRango(
                    it.diametroRoscaMin,
                    it.diametroRoscaMax,
                    "mm"
                )
            } ?: ""
            val diametroRosca1Valido = especificacion?.let {
                validarValor(
                    inspeccion.diametroRoscaMedida1,
                    it.diametroRoscaMin,
                    it.diametroRoscaMax
                )
            } ?: true
            val diametroRosca2Valido = especificacion?.let {
                validarValor(
                    inspeccion.diametroRoscaMedida2,
                    it.diametroRoscaMin,
                    it.diametroRoscaMax
                )
            } ?: true

            Text(
                text = "Diámetro de Rosca $diametroRoscaRango",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryMuestraColor,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Medida 1
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Medida 1",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (!diametroRosca1Valido) Color(0xFFDC2626) else Color(0xFF374151),
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = if (!diametroRosca1Valido) Color(0xFFFFEBEE) else Color(0xFFF9FAFB),
                        border = BorderStroke(
                            width = 1.dp,
                            color = if (!diametroRosca1Valido) Color(0xFFDC2626) else Color(
                                0xFFD1D5DB
                            )
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .defaultMinSize(minHeight = 56.dp)
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = inspeccion.diametroRoscaMedida1.ifEmpty { "N/A" },
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                color = if (!diametroRosca1Valido) Color(0xFFDC2626) else Color(
                                    0xFF9CA3AF
                                )
                            )
                        }
                    }
                }

                // Medida 2
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Medida 2",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (!diametroRosca2Valido) Color(0xFFDC2626) else Color(0xFF374151),
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = if (!diametroRosca2Valido) Color(0xFFFFEBEE) else Color(0xFFF9FAFB),
                        border = BorderStroke(
                            width = 1.dp,
                            color = if (!diametroRosca2Valido) Color(0xFFDC2626) else Color(
                                0xFFD1D5DB
                            )
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .defaultMinSize(minHeight = 56.dp)
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = inspeccion.diametroRoscaMedida2.ifEmpty { "N/A" },
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                color = if (!diametroRosca2Valido) Color(0xFFDC2626) else Color(
                                    0xFF9CA3AF
                                )
                            )
                        }
                    }
                }
            }

            // Altura de Boca
            val alturaBocaRango =
                especificacion?.let { formatearRango(it.alturaBocaMin, it.alturaBocaMax, "mm") }
                    ?: ""
            val alturaBoca1Valido = especificacion?.let {
                validarValor(
                    inspeccion.alturaBocaMedida1,
                    it.alturaBocaMin,
                    it.alturaBocaMax
                )
            } ?: true
            val alturaBoca2Valido = especificacion?.let {
                validarValor(
                    inspeccion.alturaBocaMedida2,
                    it.alturaBocaMin,
                    it.alturaBocaMax
                )
            } ?: true
            val alturaBoca3Valido = especificacion?.let {
                validarValor(
                    inspeccion.alturaBocaMedida3,
                    it.alturaBocaMin,
                    it.alturaBocaMax
                )
            } ?: true
            val alturaBoca4Valido = especificacion?.let {
                validarValor(
                    inspeccion.alturaBocaMedida4,
                    it.alturaBocaMin,
                    it.alturaBocaMax
                )
            } ?: true

            Text(
                text = "Altura de Boca $alturaBocaRango",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryMuestraColor,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    inspeccion.alturaBocaMedida1 to alturaBoca1Valido,
                    inspeccion.alturaBocaMedida2 to alturaBoca2Valido,
                    inspeccion.alturaBocaMedida3 to alturaBoca3Valido,
                    inspeccion.alturaBocaMedida4 to alturaBoca4Valido
                ).forEachIndexed { index, (medida, esValido) ->
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "M${index + 1}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (!esValido) Color(0xFFDC2626) else Color(0xFF374151),
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = if (!esValido) Color(0xFFFFEBEE) else Color(0xFFF9FAFB),
                            border = BorderStroke(
                                width = 1.dp,
                                color = if (!esValido) Color(0xFFDC2626) else Color(0xFFD1D5DB)
                            )
                        ) {
                            Box(
                                modifier = Modifier
                                    .defaultMinSize(minHeight = 56.dp)
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = medida.ifEmpty { "N/A" },
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = if (!esValido) Color(0xFFDC2626) else Color(0xFF9CA3AF)
                                )
                            }
                        }
                    }
                }
            }

            // Diámetro de Precinto
            if (inspeccion.diametroPrecintoMedida1.isNotEmpty() ||
                inspeccion.diametroPrecintoMedida2.isNotEmpty() ||
                inspeccion.diametroPrecintoMedida3.isNotEmpty()
            ) {
                val diametroPrecintoRango = especificacion?.let {
                    formatearRango(
                        it.diametroPrecintoMin,
                        it.diametroPrecintoMax,
                        "mm"
                    )
                } ?: ""
                val diametroPrecinto1Valido = especificacion?.let {
                    validarValor(
                        inspeccion.diametroPrecintoMedida1,
                        it.diametroPrecintoMin,
                        it.diametroPrecintoMax
                    )
                } ?: true
                val diametroPrecinto2Valido = especificacion?.let {
                    validarValor(
                        inspeccion.diametroPrecintoMedida2,
                        it.diametroPrecintoMin,
                        it.diametroPrecintoMax
                    )
                } ?: true
                val diametroPrecinto3Valido = especificacion?.let {
                    validarValor(
                        inspeccion.diametroPrecintoMedida3,
                        it.diametroPrecintoMin,
                        it.diametroPrecintoMax
                    )
                } ?: true

                Text(
                    text = "Diámetro de Precinto $diametroPrecintoRango",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryMuestraColor,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        inspeccion.diametroPrecintoMedida1 to diametroPrecinto1Valido,
                        inspeccion.diametroPrecintoMedida2 to diametroPrecinto2Valido,
                        inspeccion.diametroPrecintoMedida3 to diametroPrecinto3Valido
                    ).forEachIndexed { index, (medida, esValido) ->
                        if (medida.isNotEmpty()) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "M${index + 1}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (!esValido) Color(0xFFDC2626) else Color(0xFF374151),
                                    modifier = Modifier.padding(bottom = 6.dp)
                                )
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (!esValido) Color(0xFFFFEBEE) else Color(0xFFF9FAFB),
                                    border = BorderStroke(
                                        width = 1.dp,
                                        color = if (!esValido) Color(0xFFDC2626) else Color(
                                            0xFFD1D5DB
                                        )
                                    )
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .defaultMinSize(minHeight = 56.dp)
                                            .padding(horizontal = 16.dp, vertical = 12.dp),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        Text(
                                            text = medida,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Normal,
                                            color = if (!esValido) Color(0xFFDC2626) else Color(
                                                0xFF9CA3AF
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Altura Total
            if (inspeccion.alturaTotalMedida1.isNotEmpty() || inspeccion.alturaTotalMedida2.isNotEmpty()) {
                val alturaTotalRango = especificacion?.let {
                    formatearRango(
                        it.alturaTotalMin,
                        it.alturaTotalMax,
                        "mm"
                    )
                } ?: ""
                val alturaTotal1Valido = especificacion?.let {
                    validarValor(
                        inspeccion.alturaTotalMedida1,
                        it.alturaTotalMin,
                        it.alturaTotalMax
                    )
                } ?: true
                val alturaTotal2Valido = especificacion?.let {
                    validarValor(
                        inspeccion.alturaTotalMedida2,
                        it.alturaTotalMin,
                        it.alturaTotalMax
                    )
                } ?: true

                Text(
                    text = "Altura Total $alturaTotalRango",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryMuestraColor,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        inspeccion.alturaTotalMedida1 to alturaTotal1Valido,
                        inspeccion.alturaTotalMedida2 to alturaTotal2Valido
                    ).forEachIndexed { index, (medida, esValido) ->
                        if (medida.isNotEmpty()) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "M${index + 1}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (!esValido) Color(0xFFDC2626) else Color(0xFF374151),
                                    modifier = Modifier.padding(bottom = 6.dp)
                                )
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (!esValido) Color(0xFFFFEBEE) else Color(0xFFF9FAFB),
                                    border = BorderStroke(
                                        width = 1.dp,
                                        color = if (!esValido) Color(0xFFDC2626) else Color(
                                            0xFFD1D5DB
                                        )
                                    )
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .defaultMinSize(minHeight = 56.dp)
                                            .padding(horizontal = 16.dp, vertical = 12.dp),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        Text(
                                            text = medida,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Normal,
                                            color = if (!esValido) Color(0xFFDC2626) else Color(
                                                0xFF9CA3AF
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Diámetro Interno
            if (inspeccion.diametroInternoMedida1.isNotEmpty() || inspeccion.diametroInternoMedida2.isNotEmpty()) {
                val diametroInternoRango = especificacion?.let {
                    formatearRango(
                        it.diametroInternoMin,
                        it.diametroInternoMax,
                        "mm"
                    )
                } ?: ""
                val diametroInterno1Valido = especificacion?.let {
                    validarValor(
                        inspeccion.diametroInternoMedida1,
                        it.diametroInternoMin,
                        it.diametroInternoMax
                    )
                } ?: true
                val diametroInterno2Valido = especificacion?.let {
                    validarValor(
                        inspeccion.diametroInternoMedida2,
                        it.diametroInternoMin,
                        it.diametroInternoMax
                    )
                } ?: true

                Text(
                    text = "Diámetro Interno $diametroInternoRango",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryMuestraColor,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        inspeccion.diametroInternoMedida1 to diametroInterno1Valido,
                        inspeccion.diametroInternoMedida2 to diametroInterno2Valido
                    ).forEachIndexed { index, (medida, esValido) ->
                        if (medida.isNotEmpty()) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "M${index + 1}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (!esValido) Color(0xFFDC2626) else Color(0xFF374151),
                                    modifier = Modifier.padding(bottom = 6.dp)
                                )
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (!esValido) Color(0xFFFFEBEE) else Color(0xFFF9FAFB),
                                    border = BorderStroke(
                                        width = 1.dp,
                                        color = if (!esValido) Color(0xFFDC2626) else Color(
                                            0xFFD1D5DB
                                        )
                                    )
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .defaultMinSize(minHeight = 56.dp)
                                            .padding(horizontal = 16.dp, vertical = 12.dp),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        Text(
                                            text = medida,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Normal,
                                            color = if (!esValido) Color(0xFFDC2626) else Color(
                                                0xFF9CA3AF
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Observaciones
            if (inspeccion.observacion.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Observaciones",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB))
                ) {
                    Text(
                        text = inspeccion.observacion,
                        fontSize = 14.sp,
                        color = Color(0xFF374151),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CheckListForm(
        muestraViewModel: MuestraViewModel,
        padding: Dp,
        isCompletado: Boolean = false
    ) {
        val formState by muestraViewModel.checkListFormState.collectAsState()
        val checkLists by muestraViewModel.checkListsTemporales.collectAsState()

        // Estado para el modal de detalle
        var selectedCheckList by remember { mutableStateOf<CheckListInspeccion?>(null) }
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        val scope = rememberCoroutineScope()

        MuestraSectionTitle(
            title = "Check List de Inspección",
            subtitle = "Verificación de calidad",
            icon = Icons.Default.Checklist
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Si está completado, mostrar solo el aviso y el listado
        if (isCompletado) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3CD)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = WarningColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "La muestra está completada. Solo se permite visualización.",
                        fontSize = 14.sp,
                        color = Color(0xFF856404),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        } else {
            // Formulario de check list (solo se muestra si NO está completado)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
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
                            onValueChange = {
                                muestraViewModel.updateCheckList(
                                    "visorUniforme",
                                    it
                                )
                            }
                        )

                        MuestraCriterioSelector(
                            label = "Correcta Costura",
                            selectedValue = formState.correctaCostura,
                            onValueChange = {
                                muestraViewModel.updateCheckList(
                                    "correctaCostura",
                                    it
                                )
                            }
                        )

                        MuestraCriterioSelector(
                            label = "Libre de Ovalamiento",
                            selectedValue = formState.libreOvulamiento,
                            onValueChange = {
                                muestraViewModel.updateCheckList(
                                    "libreOvulamiento",
                                    it
                                )
                            }
                        )

                        MuestraCriterioSelector(
                            label = "Libre de Contaminación",
                            selectedValue = formState.libreContaminacion,
                            onValueChange = {
                                muestraViewModel.updateCheckList(
                                    "libreContaminacion",
                                    it
                                )
                            }
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
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Lista de check lists agregados formState.isFormValid &&
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
                checkLists.sortedByDescending { it.horaCheckList }.forEach { checkList ->
                        CheckListItemCard(
                        checkList = checkList,
                        onClick = {
                            selectedCheckList = checkList
                            scope.launch {
                                sheetState.show()
                            }
                        },
                        onDelete = { muestraViewModel.eliminarCheckList(checkList.id) }
                    )
                }
            }
        }

        // Modal para mostrar detalle del CheckList
        selectedCheckList?.let { checkList ->
            ModalBottomSheet(
                onDismissRequest = {
                    scope.launch {
                        sheetState.hide()
                    }.invokeOnCompletion {
                        selectedCheckList = null
                    }
                },
                sheetState = sheetState,
                containerColor = Color(0xFFF7F7F7)
            ) {
                CheckListDetailModal(
                    checkList = checkList,
                    onClose = {
                        scope.launch {
                            sheetState.hide()
                        }.invokeOnCompletion {
                            selectedCheckList = null
                        }
                    }
                )
            }
        }
    }

    @Composable
    fun CheckListItemCard(
        checkList: CheckListInspeccion,
        onClick: () -> Unit,
        onDelete: () -> Unit
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },
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

                Icon(
                    imageVector = Icons.Default.Visibility,
                    contentDescription = "Ver detalle",
                    tint = Color(0xFF4F46E5),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }

    @Composable
    fun CheckListDetailModal(
        checkList: CheckListInspeccion,
        onClose: () -> Unit
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Detalle del Check List",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )

                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = Color(0xFF6B7280)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Hora del CheckList
            Text(
                text = "Hora: ${checkList.horaCheckList}",
                fontSize = 14.sp,
                color = Color(0xFF6B7280),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Título de criterios
            Text(
                text = "Criterios de Evaluación",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Lista de criterios
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CheckListDetailItem(
                    label = "Testeado",
                    value = checkList.testeado
                )

                CheckListDetailItem(
                    label = "Estabilidad",
                    value = checkList.estabilidad
                )

                CheckListDetailItem(
                    label = "Tonalidad",
                    value = checkList.tonalidad
                )

                CheckListDetailItem(
                    label = "Visor Uniforme",
                    value = checkList.visorUniforme
                )

                CheckListDetailItem(
                    label = "Correcta Costura",
                    value = checkList.correctaCostura
                )

                CheckListDetailItem(
                    label = "Libre de Ovalamiento",
                    value = checkList.libreOvulamiento
                )

                CheckListDetailItem(
                    label = "Libre de Contaminación",
                    value = checkList.libreContaminacion
                )
            }

            // Observaciones
            if (checkList.observacion.isNotEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Observaciones",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB))
                ) {
                    Text(
                        text = checkList.observacion,
                        fontSize = 14.sp,
                        color = Color(0xFF374151),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    @Composable
    fun CheckListDetailItem(
        label: String,
        value: String
    ) {
        val (backgroundColor, textColor, icon) = when (value.uppercase()) {
            "APROBADO" -> Triple(
                Color(0xFF10B981).copy(alpha = 0.1f),
                Color(0xFF10B981),
                Icons.Default.CheckCircle
            )

            "OBSERVADO" -> Triple(
                Color(0xFFF59E0B).copy(alpha = 0.1f),
                Color(0xFFF59E0B),
                Icons.Default.Warning
            )

            "RECHAZADO" -> Triple(
                Color(0xFFEF4444).copy(alpha = 0.1f),
                Color(0xFFEF4444),
                Icons.Default.Cancel
            )

            else -> Triple(
                Color(0xFFE5E7EB),
                Color(0xFF6B7280),
                Icons.Default.Info
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = backgroundColor)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = textColor,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = label,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF374151)
                    )
                    Text(
                        text = value.ifEmpty { "No especificado" },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun EvaluacionForm(
        muestraViewModel: MuestraViewModel,
        padding: androidx.compose.ui.unit.Dp,
        isCompletado: Boolean = false
    ) {
        val formState by muestraViewModel.evaluacionFormState.collectAsState()
        val currentMuestraId by muestraViewModel.currentMuestraId.collectAsState()

        // Lista de equipos disponibles
        val equipos = listOf("SOP 1", "SOP 2", "SOP 3", "SOP 4", "SOP 5", "Enlaynadora")
        var expandedEquipo by remember { mutableStateOf(false) }

        MuestraSectionTitle(
            title = "Evaluación de la Producción",
            subtitle = "Criterios finales de evaluación",
            icon = Icons.Default.Assessment
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Formulario de evaluación
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
                    text = "Cerrar Registro",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Selector de Equipo
                ExposedDropdownMenuBox(
                    expanded = expandedEquipo && !isCompletado,
                    onExpandedChange = { if (!isCompletado) expandedEquipo = !expandedEquipo }
                ) {
                    OutlinedTextField(
                        value = formState.equipo,
                        onValueChange = {},
                        readOnly = true,
                        enabled = !isCompletado,
                        label = { Text("Equipo") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEquipo)
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Build,
                                contentDescription = null,
                                tint = Color(0xFF9CA3AF),
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = Color(0xFF4F46E5),
                            unfocusedBorderColor = Color(0xFFE5E7EB)
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = expandedEquipo && !isCompletado,
                        onDismissRequest = { expandedEquipo = false }
                    ) {
                        equipos.forEach { equipo ->
                            DropdownMenuItem(
                                text = { Text(equipo) },
                                onClick = {
                                    if (!isCompletado) {
                                        muestraViewModel.updateEvaluacion("equipo", equipo)
                                        expandedEquipo = false
                                    }
                                },
                                enabled = !isCompletado
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Criterios de evaluación
                Text(
                    text = "Criterios de Evaluación",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Paletas
                Text(
                    text = "Paletas",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF374151),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MuestraTextField(
                        value = formState.paletasAprobadas,
                        onValueChange = { muestraViewModel.updateEvaluacion("paletasAprobadas", it) },
                        label = "Aprobadas",
                        placeholder = "0",
                        keyboardType = androidx.compose.ui.text.input.KeyboardType.Number,
                        enabled = !isCompletado,
                        modifier = Modifier.weight(1f),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = SuccessColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    )

                    MuestraTextField(
                        value = formState.paletasObservadas,
                        onValueChange = { muestraViewModel.updateEvaluacion("paletasObservadas", it) },
                        label = "Observadas",
                        placeholder = "0",
                        keyboardType = androidx.compose.ui.text.input.KeyboardType.Number,
                        enabled = !isCompletado,
                        modifier = Modifier.weight(1f),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = WarningColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    )

                    MuestraTextField(
                        value = formState.paletasRechazadas,
                        onValueChange = { muestraViewModel.updateEvaluacion("paletasRechazadas", it) },
                        label = "Rechazadas",
                        placeholder = "0",
                        keyboardType = androidx.compose.ui.text.input.KeyboardType.Number,
                        enabled = !isCompletado,
                        modifier = Modifier.weight(1f),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Cancel,
                                contentDescription = null,
                                tint = ErrorColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Bolsas
                Text(
                    text = "Bolsas",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF374151),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MuestraTextField(
                        value = formState.bolsasAprobadas,
                        onValueChange = { muestraViewModel.updateEvaluacion("bolsasAprobadas", it) },
                        label = "Aprobadas",
                        placeholder = "0",
                        keyboardType = androidx.compose.ui.text.input.KeyboardType.Number,
                        enabled = !isCompletado,
                        modifier = Modifier.weight(1f),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = SuccessColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    )

                    MuestraTextField(
                        value = formState.bolsasObservadas,
                        onValueChange = { muestraViewModel.updateEvaluacion("bolsasObservadas", it) },
                        label = "Observadas",
                        placeholder = "0",
                        keyboardType = androidx.compose.ui.text.input.KeyboardType.Number,
                        enabled = !isCompletado,
                        modifier = Modifier.weight(1f),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = WarningColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    )

                    MuestraTextField(
                        value = formState.bolsasRechazadas,
                        onValueChange = { muestraViewModel.updateEvaluacion("bolsasRechazadas", it) },
                        label = "Rechazadas",
                        placeholder = "0",
                        keyboardType = androidx.compose.ui.text.input.KeyboardType.Number,
                        enabled = !isCompletado,
                        modifier = Modifier.weight(1f),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Cancel,
                                contentDescription = null,
                                tint = ErrorColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Criterios de evaluación detallados
                MuestraTextField(
                    value = formState.criteriosEvaluacion,
                    onValueChange = { muestraViewModel.updateEvaluacion("criteriosEvaluacion", it) },
                    label = "Criterios de Evaluación Detallados",
                    placeholder = "Describa los criterios específicos utilizados",
                    enabled = !isCompletado,
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

                Spacer(modifier = Modifier.height(16.dp))

                // Botón para cerrar registro
                if (isCompletado) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFD4EDDA)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = SuccessColor,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "La evaluación ya fue registrada. La muestra está completada.",
                                fontSize = 14.sp,
                                color = Color(0xFF155724),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                } else {
                    MuestraButton(
                        text = "Cerrar Registro",
                        onClick = {
                            if (currentMuestraId.isNullOrEmpty()) {
                                // El error se manejará en la función crearEvaluacionProduccion
                                muestraViewModel.crearEvaluacionProduccion("")
                            } else {
                                muestraViewModel.crearEvaluacionProduccion(currentMuestraId!!)
                            }
                        },
                        icon = Icons.Default.CheckCircle,
                        enabled = formState.isFormValid && !isCompletado,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }

