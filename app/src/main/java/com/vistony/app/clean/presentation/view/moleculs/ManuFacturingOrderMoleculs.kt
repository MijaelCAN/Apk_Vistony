package com.vistony.app.clean.presentation.view.moleculs

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.vistony.app.clean.presentation.view.atoms.SpinnerM3
import com.vistony.app.clean.presentation.view.atoms.TextWithDivider
import com.vistony.app.clean.presentation.viewmodels.ManufacturingOrderViewModel
import com.vistony.app.ui.theme.theme.Dimensions
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.delay

@Composable
fun ManuFacturingOrderHead(
    viewModel: ManufacturingOrderViewModel  = hiltViewModel()
) {
    val orderCode = viewModel.orderCode.collectAsState()

            Column {
                ManuFacturingOrderTextFieldView(
                    value = "Digite el Nro. de Orden de Fabricación a buscar",
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                ManuFacturingOrderEditTextView(
                    status = true,
                    text = orderCode.value,
                    label = "Nro. Orden de Fabricación",
                    onClick = {
                        viewModel.onOrderCodeChange(it)
                    },
                    countMaxCharacter = 254,
                    keyboardType = KeyboardType.Number,
                    onClickLeadingIcon = {
                        viewModel.getManufacturingOrder(it)
                        viewModel.onDensityChange("0")
                    },
                    leadingIconStatus = true
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
            // Pantalla de carga
            CustomProgressDialog()
        }
        data.value.data.isNotEmpty() -> {
            //TextWithDivider("Lotes Encontrados")
            ManuFacturingOrderDetailBody()
        }
        else -> {
            Icon(
                imageVector = Icons.Filled.Factory,
                contentDescription = "Fábrica",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp).size(500.dp)
                ,
            )
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
    val data = viewModel.manufacturingOrderResponseModel.collectAsState()
    val statusAprobationHeader1 = viewModel.statusAprobationHeader1.collectAsState()
    val context = LocalContext.current
    val activity = context as Activity
    val windowSize = calculateWindowSizeClass(context)
    val density = viewModel.density.collectAsState()
    val padding_res = Dimensions.getPadding(windowSize.widthSizeClass)

    val isLoadingBodyDetail = viewModel.isLoadingBodyDetail.collectAsState()

    when {
        isLoadingBodyDetail.value -> {
            CustomProgressDialog()
        }

        data.value.data.isNotEmpty() -> {
            data.value.data.forEach {
                Row(modifier = Modifier.fillMaxWidth()) {
                    ManuFacturingOrderTextFieldView(
                        value = "Nro. Lote: ${it.batchName}",
                        textAlign = TextAlign.Start,
                        color = Color.Black
                    )
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    ManuFacturingOrderTextFieldView(
                        value = "Descripción: ${it.description}",
                        textAlign = TextAlign.Start,
                        color = Color.Black
                    )
                }
                ManuFacturingOrderEditTextView(
                    status = true,
                    text = density.value,
                    label = "Nueva Densidad",
                    onClick = { result ->
                        viewModel.onDensityChange(result)
                    },
                    countMaxCharacter = 254,
                    keyboardType = KeyboardType.Number,
                    onClickLeadingIcon = { result ->
                        //viewModel.getCalculateDensity(it.batchCode, result)
                    },
                    trailingIconStatus = true,
                    onClickTrailingIcon = { result ->
                        viewModel.getCalculateDensity(it.batchName, result)
                    },
                    trailingIconResourceId = R.drawable.outline_calculate_24
                )
                SpinnerM3(
                    label = "Aprobación de Lote",
                    options = listApprobation.map { it.name },
                    selectedOption = statusAprobationHeader1.value,
                    onOptionSelected = { result ->
                        viewModel.onOptimalWeightChange(
                            it.detail.firstOrNull()?.optimumWeight ?: "0"
                        )
                        viewModel.onMaximunWeightChange(
                            it.detail.firstOrNull()?.optimumWeight ?: "0"
                        )
                        viewModel.onStatusAprobationHeader1Change(result, "Linea1", it.batchName)
                        //activitiesActionViewModel.updateIsErrorSpinnerTypeAction(false)
                    },
                    iconColor = MaterialTheme.colorScheme.secondary,
                    enabled = true,
                    isError = false,
                    errorMessage = "Seleccione Aprobación 2",
                )
                Spacer(modifier = Modifier.padding(top = 10.dp))
                TextWithDivider("Envases")
                Spacer(modifier = Modifier.padding(top = 10.dp))
                ManuFacturingOrderDetailBodyPackaging(it.detail)

            }
        }
    }
}

@Composable
fun StatusChip(status: String) {
    val (bg, fg, icon) = when (status.lowercase()) {
        "aprobado" -> Triple(Color(0xFFE8F5E9), Color(0xFF2E7D32), Icons.Default.Check)
        "pendiente" -> Triple(Color(0xFFFFF8E1), Color(0xFFF9A825), Icons.Default.Schedule)
        "rechazado" -> Triple(Color(0xFFFFEBEE), Color(0xFFC62828), Icons.Default.Close)
        else -> Triple(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.onSurfaceVariant, Icons.Default.Info)
    }

    AssistChip(
        onClick = { },
        label = { Text(status, color = fg) },
        leadingIcon = { Icon(icon, null, tint = fg) },
        colors = AssistChipDefaults.assistChipColors(containerColor = bg)
    )
}


@Composable
fun ManuFacturingOrderDetailBodyPackaging(
    manufacturingOrderDetailModel: List<ManufacturingOrderDetailModel>,
    viewModel: ManufacturingOrderViewModel  = hiltViewModel()
) {
    val density=viewModel.density.collectAsState()
    manufacturingOrderDetailModel.forEach {
        ListItemM3(
            headLineContent = it.optimumWeight + "  (Optimo) / " + it.maximumWeight + " (Maximo)",
            suportingContent = {
                Row {
                    /*Text(
                        text = it.approbationName1,
                        color = getApprovalColor(it.approbationName1)
                    )*/
                    StatusChip(it.approbationName1)
                    /*Text(text = " / ")
                    Text(
                        text = it.approbationName2,
                        color = getApprovalColor(it.approbationName2)
                    )*/
                    StatusChip(it.approbationName2)
                    /*Text(text = " / ")
                    Text(
                        text = it.approbationName3,
                        color = getApprovalColor(it.approbationName3)
                    )*/
                    StatusChip(it.approbationName3)
                }
            },
            trailingContentClick = {  },
            modifier = Modifier.padding(horizontal = 8.dp),
            overLineContent = it.batchName+" "+it.description ,
            isUsedLeadingContent = false,
            isUsedTrailingContent = true,
            leadingContent = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick =
                        //onDetailClick(client)
                        {
                        } // Aquí puedes manejar el clic en el icono
                    ) {

                    }
                }

            },
            trailingContent = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        enabled = if(it.optimumWeight.isNotEmpty() && it.optimumWeight.toFloat()>0) true else false,
                        onClick = {
                            viewModel.onStatusAprobbation1Change(it.approbationName1,"Linea1")
                            viewModel.onStatusAprobbation2Change(it.approbationName2,"Linea2")
                            viewModel.onStatusAprobbation3Change(it.approbationName3,"Linea3")
                            viewModel.onVisibleDialogEditPackingChange(true)
                            viewModel.onOptimalWeightChange(it.optimumWeight)
                            viewModel.onMaximunWeightChange(it.maximumWeight)
                            viewModel.onDocNumChange(it.batchName)

                        } // Aquí puedes manejar el clic en el icono
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_mode_edit_24),
                            tint = if(it.optimumWeight.isNotEmpty() && it.optimumWeight.toFloat()>0) Color.Red else Color.LightGray ,
                            contentDescription = "Ver"
                        )
                    }
                }
            }
        )
        TextWithDivider("")
        //Spacer(modifier = Modifier.height(10.dp))
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
                //viewModel.getManufacturingOrder(orderCode.value)
                //viewModel.getCalculateDensity(orderCode.value, density.value)
                shouldRefresh = true // Activar el retraso
            },
            confirmText = "Guardar",
            dismissText = "Cancelar",
            showConfirmButton = true,
            content = {
                SpinnerM3(
                    label = "Estado de Aprobación (Muestra 1)",
                    options = listApprobation.map { it.name },
                    selectedOption = statusAprobbation1.value,
                    onOptionSelected = { result ->
                        viewModel.onStatusAprobbation1Change(result,"Linea1")
                        //viewModel.onLineNumChange("Linea1")
                    },
                    iconColor = MaterialTheme.colorScheme.secondary,
                    enabled = true,
                    isError = false,
                    errorMessage = "Seleccione Aprobación",
                )
                SpinnerM3(
                    label = "Estado de Aprobación (Muestra 2 - Linea)",
                    options = listApprobation.map { it.name },
                    selectedOption = statusAprobbation2.value,
                    onOptionSelected = { result ->
                        viewModel.onStatusAprobbation2Change(result,"Linea2")
                        //viewModel.onLineNumChange("Linea2")
                    },
                    iconColor = MaterialTheme.colorScheme.secondary,
                    enabled = true,
                    isError = false,
                    errorMessage = "Seleccione Aprobación",
                )
                SpinnerM3(
                    label = "Estado de Aprobación (Calidad)",
                    options = listApprobation.map { it.name },
                    selectedOption = statusAprobbation3.value,
                    onOptionSelected = { result ->
                        viewModel.onStatusAprobbation3Change(result,"Linea3")
                        //viewModel.onLineNumChange("Linea3")
                    },
                    iconColor = MaterialTheme.colorScheme.secondary,
                    enabled = true,
                    isError = false,
                    errorMessage = "Seleccione Aprobación",
                )
            }
        )
    }
}
