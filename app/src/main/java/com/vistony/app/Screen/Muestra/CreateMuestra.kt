package com.vistony.app.Screen.Muestra

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vistony.app.Entidad.UserResponse
import com.vistony.app.Screen.Generic.*
import com.vistony.app.ViewModel.MuestraViewModel
import com.vistony.app.ui.theme.theme.Dimensions
import java.time.LocalTime

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateMuestra(
    onNavigateBack: () -> Unit,
    onNavigateToSuccess: () -> Unit,
    muestraViewModel: MuestraViewModel,
    currentUser: UserResponse
) {
    val context = LocalContext.current
    val activity = context as Activity
    val windowSize = calculateWindowSizeClass(context)
    val padding_res = Dimensions.getPadding(windowSize.widthSizeClass)
    val buttonHeight = Dimensions.getButtonHeight(windowSize.widthSizeClass)

    val pasoActual by muestraViewModel.pasoActual.collectAsState()
    val isCreating by muestraViewModel.isCreating.collectAsState()
    val successMessage by muestraViewModel.successMessage.collectAsState()
    val errorMessage by muestraViewModel.errorMessage.collectAsState()
    val isEditMode by muestraViewModel.isEditMode.collectAsState()
    val currentMuestraId by muestraViewModel.currentMuestraId.collectAsState()

    // Limpiar mensajes al entrar a la pantalla
    LaunchedEffect(Unit) {
        muestraViewModel.clearMessages()
    }

    // Cargar datos de la muestra si está en modo edición
    LaunchedEffect(isEditMode, currentMuestraId) {
        if (isEditMode && currentMuestraId != null) {
            android.util.Log.d("CreateMuestra", "Cargando datos de muestra para edición - ID: $currentMuestraId")
            muestraViewModel.obtenerMuestraCompletaPorId(currentMuestraId!!)
        }
    }

    // Navegar a éxito cuando se registre exitosamente
    LaunchedEffect(successMessage) {
        successMessage?.let { message ->
            if (isEditMode) {
                // Si está en modo edición, navegar a éxito
                onNavigateToSuccess()
            } else {
                // Si es nuevo registro, ir al paso 2 para completar módulos
                muestraViewModel.irAPaso(2)
            }
            // Limpiar el mensaje después de usarlo
            muestraViewModel.clearMessages()
        }
    }

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
        MuestraHeader(
            title = "Registrar Muestra",
            subtitle = "Control de calidad - Soplado",
            actionButton = {
                IconButton(
                    onClick = onNavigateBack,
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
            },
            modifier = Modifier.padding(horizontal = padding_res, vertical = 16.dp)
        )

        // Wizard Steps
        MuestraWizardSteps(
            pasoActual = pasoActual,
            onPasoClick = { paso -> muestraViewModel.irAPaso(paso) },
            modifier = Modifier.padding(horizontal = padding_res, vertical = 16.dp)
        )

        // Contenido del formulario
        MuestraCard(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = padding_res, vertical = 16.dp),
            elevation = 8.dp,
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                when (pasoActual) {
                    1 -> CabeceraForm(muestraViewModel, padding_res, isEditMode)
                    2 -> MaterialForm(muestraViewModel, padding_res)
                    3 -> InspeccionDimensionalForm(muestraViewModel, padding_res)
                    4 -> CheckListForm(muestraViewModel, padding_res)
                    5 -> EvaluacionForm(muestraViewModel, padding_res)
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Navegación del wizard
                MuestraWizardNavigation(
                    pasoActual = pasoActual,
                    onAnterior = { muestraViewModel.pasoAnterior() },
                    onSiguiente = { muestraViewModel.siguientePaso() },
                    onFinalizar = { 
                        if (pasoActual == 1) {
                            // Paso 1: Solo crear cabecera
                            muestraViewModel.crearMuestra(currentUser)
                        } else {
                            // Otros pasos: Actualizar muestra existente
                            //muestraViewModel.actualizarMuestra(currentMuestraId ?: "")
                        }
                    },
                    isCreating = isCreating,
                    buttonHeight = buttonHeight,
                    isEditMode = isEditMode
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun MuestraWizardSteps(
    pasoActual: Int,
    onPasoClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    data class StepInfo(
        val title: String,
        val icon: ImageVector,
        val isEnabled: Boolean
    )
    
    val steps = listOf(
        StepInfo("Información General", Icons.Default.Info, true),      // Paso 1 - Activo
        StepInfo("Material Empleado", Icons.Default.Build, false),     // Paso 2 - Inactivo
        StepInfo("Inspección Dimensional", Icons.Default.Straighten, false), // Paso 3 - Inactivo
        StepInfo("Check List", Icons.Default.Checklist, true),         // Paso 4 - Activo
        StepInfo("Evaluación", Icons.Default.Assessment, false)        // Paso 5 - Inactivo
    )
    val scrollState = rememberScrollState()

    // Auto-scroll al paso actual
    LaunchedEffect(pasoActual) {
        val stepWidth = 120.dp.value // Ancho aproximado de cada paso
        val scrollPosition = (pasoActual - 1) * stepWidth
        scrollState.animateScrollTo(scrollPosition.toInt())
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        // Contenedor principal que se adapta al tamaño de pantalla
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState),
                horizontalArrangement = Arrangement.Center, // Centrar en tablets
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Espaciador inicial solo en móviles
                Spacer(modifier = Modifier.width(16.dp))
                
                steps.forEachIndexed { index, stepInfo ->
                    val stepNumber = index + 1
                    val isActive = pasoActual == stepNumber
                    val isCompleted = pasoActual > stepNumber
                    val isStepEnabled = stepInfo.isEnabled
                    
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .then(
                                if (isStepEnabled) {
                                    Modifier.clickable { onPasoClick(stepNumber) }
                                } else {
                                    Modifier
                                }
                            )
                            .padding(horizontal = 8.dp)
                            .width(80.dp) // Ancho un poco mayor para tablets
                    ) {
                        // Indicador de progreso
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    color = when {
                                        !isStepEnabled -> Color(0xFFF3F4F6) // Gris claro para inactivos
                                        isCompleted -> Color(0xFF10B981)
                                        isActive -> PrimaryMuestraColor
                                        else -> Color(0xFFE5E7EB)
                                    },
                                    shape = CircleShape
                                )
                                .then(
                                    if (isActive && isStepEnabled) {
                                        Modifier.border(
                                            width = 3.dp,
                                            color = PrimaryMuestraColor.copy(alpha = 0.3f),
                                            shape = CircleShape
                                        )
                                    } else {
                                        Modifier
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isCompleted && isStepEnabled) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Completado",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            } else {
                                Icon(
                                    imageVector = stepInfo.icon,
                                    contentDescription = stepInfo.title,
                                    tint = when {
                                        !isStepEnabled -> Color(0xFF9CA3AF) // Gris para inactivos
                                        isActive -> Color.White
                                        else -> Color(0xFF9CA3AF)
                                    },
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
     
                        Spacer(modifier = Modifier.height(8.dp))
     
                        // Título del paso
                        Text(
                            text = stepInfo.title,
                            fontSize = 11.sp,
                            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
                            color = when {
                                !isStepEnabled -> Color(0xFF9CA3AF) // Gris para inactivos
                                isCompleted -> Color(0xFF10B981)
                                isActive -> PrimaryMuestraColor
                                else -> Color(0xFF6B7280)
                            },
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            lineHeight = 12.sp,
                            maxLines = 2
                        )
                    }
                    
                    // Espaciador entre pasos
                    if (index < steps.size - 1) {
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                }
                
                // Espaciador final solo en móviles
                Spacer(modifier = Modifier.width(16.dp))
            }
        }
    }
}

@Composable
fun MuestraWizardNavigation(
    pasoActual: Int,
    onAnterior: () -> Unit,
    onSiguiente: () -> Unit,
    onFinalizar: () -> Unit,
    isCreating: Boolean,
    buttonHeight: androidx.compose.ui.unit.Dp,
    isEditMode: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // BOTÓN IZQUIERDO: Anterior o vacío
        if (pasoActual > 1) {
            MuestraButton(
                text = "Anterior",
                onClick = onAnterior,
                icon = Icons.Default.ArrowBack,
                variant = ButtonVariant.Outlined,
                modifier = Modifier.weight(1f),
                buttonHeight = buttonHeight
            )
            Spacer(modifier = Modifier.width(16.dp))
        }

        // BOTÓN DERECHO: Siguiente o Finalizar/Registrar
        when {
            // Modo edición en paso 1: Solo Siguiente
            isEditMode && pasoActual == 1 -> {
                MuestraButton(
                    text = "Siguiente",
                    onClick = onSiguiente,
                    icon = Icons.Default.ArrowForward,
                    modifier = Modifier.weight(1f),
                    buttonHeight = buttonHeight
                )
            }
            // Pasos intermedios (2-4): Siguiente
            pasoActual in 2..4 -> {
                MuestraButton(
                    text = "Siguiente",
                    onClick = onSiguiente,
                    icon = Icons.Default.ArrowForward,
                    modifier = Modifier.weight(1f),
                    buttonHeight = buttonHeight
                )
            }
            // Paso final (5) o paso 1 en creación: Finalizar/Registrar
            else -> {
                MuestraButton(
                    text = when {
                        isCreating -> "Registrando..."
                        isEditMode -> "Actualizar"
                        pasoActual == 1 -> "Registrar Muestra"
                        else -> "Finalizar"
                    },
                    onClick = onFinalizar,
                    icon = Icons.Default.Check,
                    enabled = !isCreating,
                    isLoading = isCreating,
                    modifier = Modifier.weight(1f),
                    buttonHeight = buttonHeight
                )
            }
        }
    }
}

@Composable
fun CabeceraForm(
    muestraViewModel: MuestraViewModel,
    padding: androidx.compose.ui.unit.Dp,
    isEditMode: Boolean
) {
    val formState by muestraViewModel.cabeceraFormState.collectAsState()
    
    // Consultar producto automáticamente cuando cambie el código
    LaunchedEffect(formState.codigo) {
        if (formState.codigo.isNotEmpty() && formState.codigo.length >= 3) {
            if(!isEditMode) muestraViewModel.consultarProducto(formState.codigo)
        } else if (formState.codigo.isEmpty()) {
            // Limpiar producto si se borra el código
            val current = muestraViewModel.cabeceraFormState.value
            muestraViewModel.updateCabecera("producto", "")
        }
    }
    
    // Determinar turno automáticamente basado en la hora actual
    LaunchedEffect(Unit) {
        val horaActual = LocalTime.now()
        val turno = when {
            horaActual.isAfter(LocalTime.of(6, 0)) && horaActual.isBefore(LocalTime.of(18, 0)) -> "Mañana"
            else -> "Tarde"
        }
        
        // Solo actualizar si el turno está vacío o es diferente
        val current = muestraViewModel.cabeceraFormState.value
        if (current.turno.isEmpty() || current.turno != turno) {
            muestraViewModel.updateCabecera("turno", turno)
        }
    }
    
    MuestraSectionTitle(
        title = "Información General",
        subtitle = "Datos principales de la muestra",
        icon = Icons.Default.Info
    )
    
    Spacer(modifier = Modifier.height(24.dp))
    
    // Código y Lote
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        MuestraTextField(
            value = formState.codigo,
            onValueChange = { muestraViewModel.updateCabecera("codigo", it) },
            label = "N° Orden de Fabricacion",
            enabled = !isEditMode,
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
            onValueChange = { muestraViewModel.updateCabecera("lote", it) },
            label = "Lote",
            enabled = false,
            readOnly = true,
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
    MuestraTextField(
        value = formState.producto,
        onValueChange = { /* No permitir edición manual */ },
        label = "Descripción de Producto",
        enabled = false, // Solo lectura
        readOnly = true,
        maxLines = 2,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Science,
                contentDescription = null,
                tint = Color(0xFF9CA3AF),
                modifier = Modifier.size(20.dp)
            )
        }
    )
    
    Spacer(modifier = Modifier.height(16.dp))
    
    // Embalaje y Máquina
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        /*MuestraTextField(
            value = formState.embalaje,
            onValueChange = { muestraViewModel.updateCabecera("embalaje", it) },
            label = "Embalaje",
            enabled = !isEditMode,
            modifier = Modifier.weight(1f),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Inventory2,
                    contentDescription = null,
                    tint = Color(0xFF9CA3AF),
                    modifier = Modifier.size(20.dp)
                )
            }
        )*/
        
        MuestraTextField(
            value = formState.maquina,
            onValueChange = { muestraViewModel.updateCabecera("maquina", it) },
            label = "Máquina",
            enabled = false,
            readOnly = true,
            modifier = Modifier.weight(1f),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.PrecisionManufacturing,
                    contentDescription = null,
                    tint = Color(0xFF9CA3AF),
                    modifier = Modifier.size(20.dp)
                )
            }
        )
    }
    
    Spacer(modifier = Modifier.height(16.dp))
    
    // Turno
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        MuestraTextField(
            value = formState.turno,
            onValueChange = { /* No permitir edición manual */ },
            label = "Turno",
            enabled = false, // Solo lectura
            readOnly = true,
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
        
        /*MuestraTextField(
            value = formState.ot,
            onValueChange = { muestraViewModel.updateCabecera("ot", it) },
            label = "OT",
            enabled = !isEditMode,
            modifier = Modifier.weight(1f),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Work,
                    contentDescription = null,
                    tint = Color(0xFF9CA3AF),
                    modifier = Modifier.size(20.dp)
                )
            }
        )*/
    }
    
    Spacer(modifier = Modifier.height(16.dp))
    
    // Auxiliar y Encargado de Producción
    /*Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        MuestraTextField(
            value = formState.auxiliar,
            onValueChange = { muestraViewModel.updateCabecera("auxiliar", it) },
            label = "Auxiliar",
            enabled = !isEditMode,
            modifier = Modifier.weight(1f),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color(0xFF9CA3AF),
                    modifier = Modifier.size(20.dp)
                )
            }
        )
        
        MuestraTextField(
            value = formState.encargadoProduccion,
            onValueChange = { muestraViewModel.updateCabecera("encargadoProduccion", it) },
            label = "Encargado Producción",
            enabled = !isEditMode,
            modifier = Modifier.weight(1f),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.SupervisorAccount,
                    contentDescription = null,
                    tint = Color(0xFF9CA3AF),
                    modifier = Modifier.size(20.dp)
                )
            }
        )
    }*/
}
