package com.vistony.app.clean.presentation.view.moleculs

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Factory
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vistony.app.R
import com.vistony.app.clean.domain.model.ApprobationDefaults
import com.vistony.app.clean.domain.model.ManufacturingOrderDetailModel
import com.vistony.app.clean.presentation.view.atoms.DialogM3
import com.vistony.app.clean.presentation.view.atoms.ListItemM3
import com.vistony.app.clean.presentation.view.atoms.ManuFacturingOrderEditTextView
import com.vistony.app.clean.presentation.view.atoms.ManuFacturingOrderTextFieldView
import com.vistony.app.clean.presentation.view.atoms.TextWithDivider
import com.vistony.app.clean.presentation.viewmodels.ManufacturingOrderViewModel
import com.vistony.app.ui.theme.theme.Dimensions
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.unit.sp
import com.vistony.app.clean.presentation.view.atoms.ManuFacturingOrderSpinnerView
import com.vistony.app.clean.presentation.view.atoms.ManuFacturingOrderTextView
import com.vistony.app.clean.presentation.view.atoms.ResultDialog
import com.vistony.app.clean.presentation.viewmodels.ScanViewModel
import kotlinx.coroutines.delay

@Composable
fun ManuFacturingOrderHead(
    viewModel: ManufacturingOrderViewModel  = hiltViewModel(),
    scanViewModel: ScanViewModel  = hiltViewModel()
) {
    val orderCode = viewModel.orderCode.collectAsState()
    val scanData by scanViewModel.scanData.collectAsState()

    LaunchedEffect(scanData) {
        scanData?.let { data ->
            viewModel.onOrderCodeChange(data)
            viewModel.getManufacturingOrder(data)
            viewModel.onDensityChange("0")
            scanViewModel.clearScanData()
        }
    }

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    ManuFacturingOrderTextFieldView(
                        value = "ORDEN DE FABRICACIÓN",
                        color = Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                TextWithDivider("Datos de Orden de Fabricación")
                Spacer(modifier = Modifier.height(8.dp))
                ManuFacturingOrderEditTextView(
                    status = true,
                    text = orderCode.value,
                    label = "Digite o escanee el Nro. de Orden de Fabricación",
                    onClick = {
                        viewModel.onOrderCodeChange(it)
                    },
                    countMaxCharacter = 254,
                    keyboardType = KeyboardType.NumberPassword,
                    onClickLeadingIcon = {
                        viewModel.getManufacturingOrder(it)
                        viewModel.onDensityChange("0")
                    },
                    leadingIconStatus = false,
                    isUsedKeyboardGO = true,
                    eventKeyboardGO = { result ->
                        viewModel.getManufacturingOrder(result)
                        viewModel.onDensityChange("0")
                    },
                    trailingIconStatus = true,
                    onClickTrailingIcon = {
                        viewModel.getManufacturingOrder(it)
                        viewModel.onDensityChange("0")
                    }
                )
            }

}


@Composable
fun ManuFacturingOrderDetail(
    viewModel: ManufacturingOrderViewModel  = hiltViewModel()
) {

    val data = viewModel.manufacturingOrderResponseModel.collectAsState()
    val isLoading = viewModel.isLoadingBody.collectAsState()

    when {
        isLoading.value -> {
            CustomProgressDialog()
        }
        data.value.data.isNotEmpty() -> {
            ManuFacturingOrderDetailBody()
        }
        else -> {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Factory,
                    contentDescription = "Fábrica",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp).size(500.dp),
                )
            }
        }
    }
}

@Composable
fun CustomProgressDialog(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(64.dp),
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Cargando...",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black
            )
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun ManuFacturingOrderDetailBody(
    viewModel: ManufacturingOrderViewModel  = hiltViewModel()
) {
    val listApprobation = ApprobationDefaults.DEFAULT_APPROBATIONS
    val listApprobationHeader = ApprobationDefaults.DEFAULT_APPROBATIONS_HEADER
    val listApprobationClosed = ApprobationDefaults.DEFAULT_APPROBATIONS_CLOSED

    val data = viewModel.manufacturingOrderResponseModel.collectAsState()
    val statusAprobationHeader1 = viewModel.statusAprobationHeader1.collectAsState()
    val context = LocalContext.current
    val activity = context as Activity
    val windowSize = calculateWindowSizeClass(activity)
    val density = viewModel.density.collectAsState()
    val padding_res = Dimensions.getPadding(windowSize.widthSizeClass)
    val isLoadingBodyDetail = viewModel.isLoadingBodyDetail.collectAsState()
    var shouldRefresh by remember { mutableStateOf(false) }
    val orderCode = viewModel.orderCode.collectAsState()
    val statusCorrection = viewModel.statusCorrection.collectAsState()
    val statusDesaprobation = viewModel.statusDesaprobation.collectAsState()
    val reasonForRejectionsResponseModel = viewModel.reasonForRejectionsResponseModel.collectAsState()
    val reasonDesaprobation = viewModel.reasonDesaprobation.collectAsState()
    val observation = viewModel.observation.collectAsState()
    val isVisibleObservation = viewModel.isVisibleObservation.collectAsState()
    val validationMessage = viewModel.validationMessage.collectAsState()

    // ✅ Diálogo de resultado con iconos
    if (isVisibleObservation.value) {
        ResultDialog(
            isSuccess = false,
            message = validationMessage.value.toString(),
            onDismiss = {
                viewModel.setIsVisibleObservation(false)
            }
        )
    }

    // Ejecutar con retraso de 2 segundos
    LaunchedEffect(shouldRefresh) {
        if (shouldRefresh) {
            delay(3000) // 2 segundos
            viewModel.getCalculateDensity(orderCode.value, density.value)
            shouldRefresh = false
        }
    }

    /*LaunchedEffect((isVisibleObservation.value)) {
        if(isVisibleObservation.value){
            Toast.makeText(context,"Debe ingresar peso óptimo y peso máximo diferentes de cero", Toast.LENGTH_LONG).show()
        }
    }*/



    when {
        isLoadingBodyDetail.value -> {
            CustomProgressDialog()
        }

        data.value.data.isNotEmpty() -> {
            data.value.data.forEach {
                Spacer(modifier = Modifier.padding(top = 10.dp))
                ManuFacturingOrderTextView(
                    status=false,
                    text=it.description,
                    label="Descripción de la Orden de Fabricación"
                )
                Spacer(modifier = Modifier.padding(top = 10.dp))
                ManuFacturingOrderSpinnerView(
                    status = true,
                    selectedOption = statusAprobationHeader1.value,
                    label = "Resultado de Análisis Muestra 1 - Tanque",
                    onOptionSelected = { result ->
                        Log.e("REOS","ManuFacturingOrderMoleculs-ManuFacturingOrderDetailBody-result"+result)
                        viewModel.onDocNumChange(it.batchName)
                        viewModel.onOptimalWeightChange(
                            it.detail.firstOrNull()?.optimumWeight ?: "0"
                        )
                        viewModel.onMaximunWeightChange(
                            it.detail.firstOrNull()?.optimumWeight ?: "0"
                        )
                        viewModel.onStatusAprobationHeader1Change(result, "Linea1", it.batchName)
                        shouldRefresh=true
                    },
                    options = listApprobationHeader.map { it.name },

                )
                Spacer(modifier = Modifier.padding(top = 10.dp))

                ManuFacturingOrderSpinnerView(
                    status = true,
                    selectedOption = statusCorrection.value,
                    label = "¿Se ha realizado corrección?",
                    onOptionSelected = { result ->
                        viewModel.onStatusCorrectionChange(result)
                        viewModel.onStatusAprobationHeader1Change(statusAprobationHeader1.value, "Linea1", it.batchName)
                        shouldRefresh=true
                    },
                    options = listApprobationClosed.map { it.name }
                )
                if(statusCorrection.value.equals("Si", ignoreCase = true)) {
                    Spacer(modifier = Modifier.padding(top = 10.dp))
                    ManuFacturingOrderSpinnerView(
                        status = true,
                        selectedOption = reasonDesaprobation.value,
                        label = "Motivo Corrección (Listado)",
                        onOptionSelected = { result ->
                            viewModel.onReasonDesaprobationChange(result)
                            viewModel.onStatusAprobationHeader1Change(
                                statusAprobationHeader1.value,
                                "Linea1",
                                it.batchName
                            )
                            shouldRefresh = true
                        },
                        options = reasonForRejectionsResponseModel.value.data.map { it.name },
                    )
                }
                Spacer(modifier = Modifier.padding(top = 10.dp))
                ManuFacturingOrderEditTextView(
                    status = true,
                    text = observation.value,
                    label = "Observaciones",
                    onClick = { result ->
                        viewModel.onObservationChange( result )
                    },
                    countMaxCharacter = 254,
                    keyboardType = KeyboardType.Text,
                    onClickLeadingIcon = {

                    },
                    trailingIconStatus = false,
                    onClickTrailingIcon = { result ->

                    },
                    trailingIconResourceId = R.drawable.baseline_verified_24,
                    isUsedKeyboardGO = true,
                    eventKeyboardGO = { result ->
                        //viewModel.onObservationChange( result )
                        viewModel.onStatusAprobationHeader1Change(statusAprobationHeader1.value, "Linea1", it.batchName)
                        shouldRefresh=true
                    },
                )
                Spacer(modifier = Modifier.padding(top = 10.dp))
                ManuFacturingOrderEditTextView(
                    status = true,
                    text = density.value,
                    label = "Introduce la densidad",
                    onClick = { result ->
                        viewModel.onDensityChange(result)
                    },
                    countMaxCharacter = 254,
                    keyboardType = KeyboardType.Decimal,
                    onClickLeadingIcon = {
                    },
                    trailingIconStatus = true,
                    onClickTrailingIcon = { result ->

                        viewModel.getCalculateDensity(it.batchName, result)

                    },
                    trailingIconResourceId = R.drawable.baseline_verified_24
                )
                Spacer(modifier = Modifier.padding(top = 10.dp))
                TextWithDivider("Envases")
                ManuFacturingOrderDetailBodyPackaging(it.detail)

            }
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun StatusChip(status: String,value:String="") {

    val context = LocalContext.current
    val activity = context as Activity
    val windowSize = calculateWindowSizeClass(activity)
    val padding_res = Dimensions.getPadding(windowSize.widthSizeClass)
    val bodyFontSize = Dimensions.getBodyFontSize(windowSize.widthSizeClass)

    val (bg, fg, icon) = when (status.lowercase()) {
        "aprobado" -> Triple(Color(0xFFE8F5E9), Color(0xFF2E7D32), Icons.Default.Check)
        "pendiente" -> Triple(Color(0xFFFFF8E1), Color(0xFFF9A825), Icons.Default.Schedule)
        "rechazado" -> Triple(Color(0xFFFFEBEE), Color(0xFFC62828), Icons.Default.Close)
        else -> Triple(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.onSurfaceVariant, Icons.Default.Info)
    }

    AssistChip(
        onClick = { },
        label = { Text(value+": "+status, color = fg, fontSize = bodyFontSize.sp) },
        leadingIcon = { Icon(icon, null, tint = fg) },
        colors = AssistChipDefaults.assistChipColors(containerColor = bg)
    )
}


@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun ManuFacturingOrderDetailBodyPackaging(
    manufacturingOrderDetailModel: List<ManufacturingOrderDetailModel>,
    viewModel: ManufacturingOrderViewModel  = hiltViewModel()
) {

    val context = LocalContext.current
    val activity = context as Activity
    val windowSize = calculateWindowSizeClass(activity)
    val density = viewModel.density.collectAsState()
    val padding_res = Dimensions.getPadding(windowSize.widthSizeClass)
    val bodyFontSize = Dimensions.getBodyFontSize(windowSize.widthSizeClass)
    val statusAprobationHeader1= viewModel.statusAprobationHeader1.collectAsState()



    manufacturingOrderDetailModel.forEach {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding( horizontal =  padding_res),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF7F8FB)
            )
        ) {
            ListItemM3(
                textSize = bodyFontSize.sp,
                headLineContent = it.optimumWeight + "  (Optimo) / " + it.maximumWeight + " (Maximo)",
                suportingContent = {
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        maxItemsInEachRow = Int.MAX_VALUE
                    ) {
                        StatusChip(it.approbationName1, "Muestra 1")
                        StatusChip(it.approbationName2, "Muestra 2 - Linea")
                        StatusChip(it.approbationName3, "Muestra 3 - Calidad")
                    }
                },
                trailingContentClick = { },
                modifier = Modifier.padding(horizontal = 8.dp),
                overLineContent = it.batchName + " " + it.description,
                isUsedLeadingContent = false,
                isUsedTrailingContent = true,
                leadingContent = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick =
                                {
                                }
                        ) {

                            }
                        }

                },
                trailingContent = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            //enabled = if (it.optimumWeight.isNotEmpty() && it.optimumWeight.toFloat() > 0) true else false,
                            enabled = if (statusAprobationHeader1.value.equals("Aprobado", ignoreCase = true)) true else false,
                            onClick = {
                                viewModel.onStatusAprobbation1Change(it.approbationName1, "Linea1")
                                viewModel.onStatusAprobbation2Change(it.approbationName2, "Linea2")
                                viewModel.onStatusAprobbation3Change(it.approbationName3, "Linea3")
                                viewModel.onVisibleDialogEditPackingChange(true)
                                viewModel.onOptimalWeightChange(it.optimumWeight)
                                viewModel.onMaximunWeightChange(it.maximumWeight)
                                viewModel.onDocNumChange(it.batchName)

                            } // Aquí puedes manejar el clic en el icono
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_mode_edit_24),
                                tint =
                                    //if (it.optimumWeight.isNotEmpty() && it.optimumWeight.toFloat() > 0)
                                    if (statusAprobationHeader1.value.equals("Aprobado", ignoreCase = true))
                                    MaterialTheme.colorScheme.secondary else Color.LightGray,
                                contentDescription = "Ver"
                            )
                        }
                    }
                }
            )

        }
        Spacer(modifier= Modifier.height(10.dp))
    }
    
}

@Composable
fun ManufacturingOrderCustomDialog(
    viewModel: ManufacturingOrderViewModel  = hiltViewModel()
)
{
    val listApprobation =  ApprobationDefaults.DEFAULT_APPROBATIONS
    val isVisibleDialogEditPacking = viewModel.isVisibleDialogEditPacking.collectAsState()
    val statusAprobbation1 = viewModel.statusAprobbation1.collectAsState()
    val statusAprobbation2 = viewModel.statusAprobbation2.collectAsState()
    val statusAprobbation3 = viewModel.statusAprobbation3.collectAsState()
    val orderCode = viewModel.orderCode.collectAsState()
    val density = viewModel.density.collectAsState()
    var shouldRefresh by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val activity = context as Activity
    val isStatusApprobationContainer1 = viewModel.isStatusApprobationContainer1.collectAsState()
    val isStatusApprobationContainer2 = viewModel.isStatusApprobationContainer2.collectAsState()
    val isStatusApprobationContainer3 = viewModel.isStatusApprobationContainer3.collectAsState()
    val statusAprobationHeader1 = viewModel.statusAprobationHeader1.collectAsState()


    // Ejecutar con retraso de 2 segundos
    LaunchedEffect(shouldRefresh) {
        if (shouldRefresh) {
            delay(2000) // 2 segundos
            viewModel.getCalculateDensity(orderCode.value, density.value)
            shouldRefresh = false
        }
    }

    if(isVisibleDialogEditPacking.value)
    {
        DialogM3(
            title = "Selecciona una opción",
            subtitle = "Selecciona el tipo de aprobacion",
            onDismiss = {
                viewModel.onVisibleDialogEditPackingChange(false)
            },
            onConfirm = {
                viewModel.saveStatusAprobation()
                viewModel.onVisibleDialogEditPackingChange(false)
                shouldRefresh = true // Activar el retraso
            },
            confirmText = "Guardar",
            dismissText = "Cancelar",
            showConfirmButton = true,
            content = {
                ManuFacturingOrderSpinnerView(
                    status = false,
                    selectedOption = statusAprobbation1.value,
                    label = "Estado de Aprobación (Muestra 1)",
                    onOptionSelected = { result ->
                        viewModel.onStatusAprobbation1Change(result,"Linea1")
                        //viewModel.statusApprobationContainer2Change(true)
                    },
                    options = listApprobation.map { it.name },
                    activity = activity
                    )
                Spacer(modifier = Modifier.height(10.dp))
                ManuFacturingOrderSpinnerView(
                    status = statusAprobationHeader1.value.equals("Aprobado", ignoreCase = true),
                    selectedOption = statusAprobbation2.value,
                    label = "Muestra 2 - Inicio de envasado",
                    onOptionSelected = { result ->
                        viewModel.onStatusAprobbation2Change(result,"Linea2")
                        //viewModel.statusApprobationContainer3Change(true)
                    },
                    options = listApprobation.map { it.name },
                    activity = activity
                )
                Spacer(modifier = Modifier.height(10.dp))
                ManuFacturingOrderSpinnerView(
                    //status = isStatusApprobationContainer3.value,
                    status = statusAprobationHeader1.value.equals("Aprobado", ignoreCase = true),
                    selectedOption = statusAprobbation3.value,
                    label = "Muestra 3 - Final de envasado",
                    onOptionSelected = { result ->
                        viewModel.onStatusAprobbation3Change(result,"Linea3")
                    },
                    options = listApprobation.map { it.name },
                    activity = activity
                )
            }
        )
    }
}
