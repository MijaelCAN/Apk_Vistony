package com.vistony.app.clean.presentation.view.moleculs

import android.app.Activity
import android.util.Log
import androidx.cardview.widget.CardView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Factory
import androidx.compose.material3.Card
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vistony.app.R
import com.vistony.app.clean.domain.model.ApprobationDefaults
import com.vistony.app.clean.domain.model.ManufacturingOrderDetailModel
import com.vistony.app.clean.presentation.view.atoms.CardM3
import com.vistony.app.clean.presentation.view.atoms.DialogM3
import com.vistony.app.clean.presentation.view.atoms.GroupedLazyColumn
import com.vistony.app.clean.presentation.view.atoms.ListItemM3
import com.vistony.app.clean.presentation.view.atoms.ManuFacturingOrderEditTextView
import com.vistony.app.clean.presentation.view.atoms.ManuFacturingOrderTextFieldView
import com.vistony.app.clean.presentation.view.atoms.SpinnerM3
import com.vistony.app.clean.presentation.view.atoms.TextWithDivider
import com.vistony.app.clean.presentation.viewmodels.ManufacturingOrderViewModel
import com.vistony.app.ui.theme.theme.Dimensions
import androidx.compose.material.icons.filled.Factory
import androidx.compose.material3.Icon
import androidx.compose.ui.text.style.TextAlign

@Composable
fun ManuFacturingOrderHead(
    viewModel: ManufacturingOrderViewModel  = hiltViewModel()
) {
   // CardM3(
    //    contentBody = {
            Column {
                ManuFacturingOrderTextFieldView(
                    value = "Digite el Nro. de Orden de Fabricación a buscar",
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                ManuFacturingOrderEditTextView(
                    status = true,
                    text = "250010207",
                    label = "Nro. Orden de Fabricación",
                    onClick = {
                        viewModel.onOrderCodeChange(it)
                    },
                    countMaxCharacter = 254,
                    keyboardType = KeyboardType.Number,
                    onClickLeadingIcon = {
                        viewModel.getManufacturingOrder(it)
                    },
                    leadingIconStatus = true
                )
            }
     //   },
      //  isUsedTextDivider = false
   // )
}


@Composable
fun ManuFacturingOrderDetail(
    viewModel: ManufacturingOrderViewModel  = hiltViewModel()
) {

    val data = viewModel.manufacturingOrderResponseModel.collectAsState()

    when {
        data.value.data.isNotEmpty() -> {
            TextWithDivider("Lotes Encontrados")
            ManuFacturingOrderDetailBody()
        }
        else -> {
            /*Image(
                painter = painterResource(id =Icons.Filled.Factory),
                contentDescription = "No data",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable {
                        Log.d("ManuFacturingOrderDetail", "Image clicked")
                    },
                alignment = Alignment.Center
            )*/
            Icon(
                imageVector = Icons.Filled.Factory,
                contentDescription = "Fábrica",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp).size(400.dp)
                ,
                //alignment = Alignment.Center
            )
            ManuFacturingOrderTextFieldView(
                value = "No se encontraron lotes.",
            )

        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun ManuFacturingOrderDetailBody(
    viewModel: ManufacturingOrderViewModel  = hiltViewModel()
) {
    val listApprobation =  ApprobationDefaults.DEFAULT_APPROBATIONS
    val data = viewModel.manufacturingOrderResponseModel.collectAsState()
    val statusAprobationHeader1 = viewModel.statusAprobationHeader1.collectAsState()
    val context = LocalContext.current
    val activity = context as Activity
    val windowSize = calculateWindowSizeClass(context)

    val padding_res = Dimensions.getPadding(windowSize.widthSizeClass)
    data.value.data.forEach {
        /*CardM3(
            contentBody = {*/
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding_res)
                //.border(2.dp, Color.White, RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White.copy(alpha = 0.44f)),
            //.graphicsLayer { alpha = 0.44f },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                text = "",
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
                trailingIconResourceId = R.drawable.outline_send_24
            )
            SpinnerM3(
                label = "Aprobación 1",
                options = listApprobation.map { it.name },
                selectedOption = statusAprobationHeader1.value,
                onOptionSelected = { result ->
                    viewModel.onOptimalWeightChange(it.detail.firstOrNull()?.optimumWeight ?: "0")
                    viewModel.onMaximunWeightChange(it.detail.firstOrNull()?.optimumWeight ?: "0")
                    viewModel.onStatusAprobationHeader1Change(result, "Linea1", it.batchName)
                    //activitiesActionViewModel.updateIsErrorSpinnerTypeAction(false)
                },
                iconColor = MaterialTheme.colorScheme.secondary,
                enabled = true,
                isError = false,
                errorMessage = "Seleccione Aprobación 2",
            )
            Spacer(modifier = Modifier.padding(top = 10.dp))

            //},
            //contentActions = {
            TextWithDivider("Envases")
            Spacer(modifier = Modifier.padding(top = 10.dp))
            ManuFacturingOrderDetailBodyPackaging(it.detail)
            //},
            //textDivider = "Envases"
            //)
        }
    }
}
@Composable
fun getApprovalColor(status: String): Color {
    return when (status.lowercase()) {
        "aprobado" -> Color(0xFF4CAF50) // Verde
        "rechazado" -> Color(0xFFF44336) // Rojo
        "pendiente" -> Color(0xFFFFC107) // Amarillo
        else -> MaterialTheme.colorScheme.onSurface // Color por defecto
    }
}


@Composable
fun ManuFacturingOrderDetailBodyPackaging(
    manufacturingOrderDetailModel: List<ManufacturingOrderDetailModel>,
    viewModel: ManufacturingOrderViewModel  = hiltViewModel()
) {

    manufacturingOrderDetailModel.forEach {
        ListItemM3(
            headLineContent = it.optimumWeight + " Kg. (Optimo) / " + it.maximumWeight + " Kg. (Maximo)",
            suportingContent = {
                Row {
                    Text(
                        text = it.approbationName1,
                        color = getApprovalColor(it.approbationName1)
                    )
                    Text(text = " / ")
                    Text(
                        text = it.approbationName2,
                        color = getApprovalColor(it.approbationName2)
                    )
                    Text(text = " / ")
                    Text(
                        text = it.approbationName3,
                        color = getApprovalColor(it.approbationName3)
                    )
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
                            tint = MaterialTheme.colorScheme.secondary,
                            contentDescription = "Ver"
                        )
                    }
                }
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
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
            },
            confirmText = "Guardar",
            dismissText = "Cancelar",
            showConfirmButton = true,
            content = {
                SpinnerM3(
                    label = "Aprobación 1",
                    options = listApprobation.map { it.name },
                    selectedOption = statusAprobbation1.value,
                    onOptionSelected = { result ->
                        viewModel.onStatusAprobbation1Change(result,"Linea1")
                        //viewModel.onLineNumChange("Linea1")
                    },
                    iconColor = MaterialTheme.colorScheme.secondary,
                    enabled = true,
                    isError = false,
                    errorMessage = "Seleccione Aprobación 1",
                )
                SpinnerM3(
                    label = "Aprobación 2",
                    options = listApprobation.map { it.name },
                    selectedOption = statusAprobbation2.value,
                    onOptionSelected = { result ->
                        viewModel.onStatusAprobbation2Change(result,"Linea2")
                        //viewModel.onLineNumChange("Linea2")
                    },
                    iconColor = MaterialTheme.colorScheme.secondary,
                    enabled = true,
                    isError = false,
                    errorMessage = "Seleccione Aprobación 2",
                )
                SpinnerM3(
                    label = "Aprobación 3",
                    options = listApprobation.map { it.name },
                    selectedOption = statusAprobbation3.value,
                    onOptionSelected = { result ->
                        viewModel.onStatusAprobbation3Change(result,"Linea3")
                        //viewModel.onLineNumChange("Linea3")
                    },
                    iconColor = MaterialTheme.colorScheme.secondary,
                    enabled = true,
                    isError = false,
                    errorMessage = "Seleccione Aprobación 3",
                )
            }
        )
    }
}
