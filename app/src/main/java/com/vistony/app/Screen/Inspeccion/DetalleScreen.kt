package com.vistony.app.Screen.Inspeccion

import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CleanHands
import androidx.compose.material.icons.filled.Compress
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Warehouse
import androidx.compose.material.Card
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.vistony.app.Entidad.Evaluacion
import com.vistony.app.Entidad.UserState
import com.vistony.app.R
import com.vistony.app.Screen.Generic.CompactCommentField
import com.vistony.app.Screen.Generic.CustomAlertDialog
import com.vistony.app.ViewModel.InspectionViewModel
import com.vistony.app.ViewModel.SharedViewModel
import com.vistony.app.Screen.Generic.Drawers.CustomDrawer
import com.vistony.app.Screen.Generic.CustomOutlinedTextField2
import com.vistony.app.Screen.Generic.CustomTriStateCheckbox
import com.vistony.app.Screen.Generic.DialogType
import com.vistony.app.Screen.Generic.SelectableOutlinedRow
import com.vistony.app.Screen.Generic.TopBar
import com.vistony.app.ViewModel.EstadoInspeccion
import com.vistony.app.ViewModel.EstadoParada
import com.vistony.app.ui.theme.theme.Dimensions
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetalleScreen(
    navController: NavHostController,
    sharedViewModel: SharedViewModel,
    viewModel: InspectionViewModel = hiltViewModel(),
    id: String,
    userState: UserState
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { CustomDrawer(navController = navController, id = id, userState) }) {
        Scaffold(
            topBar = { /*TopBar("Detalle de Inspección", navController = navController, onMenuClick = {
                scope.launch { drawerState.open() }
            }) */}
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .background(color = Color(0xFF0B4FAF))
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Box(
                    modifier = Modifier
                        .width(610.dp)
                        .height(210.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                ) {
                    Image(
                        modifier = Modifier.size(width = 600.dp, height = 200.dp),
                        painter = painterResource(id = R.drawable.vistony),
                        contentDescription = "Logo"
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                //BodyDetalle(navController, viewModel, sharedViewModel,id)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalTextApi::class)
@ExperimentalMaterial3Api
@Composable
fun BodyDetalle(
    navController: NavController,
    viewModel: InspectionViewModel,
    sharedViewModel: SharedViewModel,
    id: String,
    onBack: () -> Unit
) {
    val checkItems = remember{
        mutableStateListOf(
            CheckItem("Peso", "PESO", Icons.Filled.Balance, ToggleableState.Off, ""),
            CheckItem("Etiqueta", "ETIQ.", Icons.Filled.LocalOffer, ToggleableState.Off, ""),
            CheckItem("Lote", "LOT.", Icons.Filled.Dns, ToggleableState.Off, ""),
            CheckItem("Limpieza", "LIMP.", Icons.Filled.CleanHands, ToggleableState.Off, ""),
            CheckItem("Sellado", "SELL.", Icons.Filled.Lock, ToggleableState.Off, ""),
            CheckItem("Encogimiento", "ENCOG.", Icons.Filled.Compress, ToggleableState.Off, ""),
            CheckItem("Rótulo", "RÓTULO", Icons.Filled.Label, ToggleableState.Off, ""),
            CheckItem("Pallet", "PALLET", Icons.Filled.Warehouse, ToggleableState.Off, "")
        )
    }

    /*var pesoCheck by rememberSaveable { mutableStateOf(ToggleableState.Off) }
    var pesoComment by rememberSaveable { mutableStateOf("") }
    var etiqCheck by rememberSaveable { mutableStateOf(ToggleableState.Off) }
    var etiqComment by rememberSaveable { mutableStateOf("") }
    var lotCheck by rememberSaveable { mutableStateOf(ToggleableState.Off) }
    var lotComment by rememberSaveable { mutableStateOf("") }
    var limpCheck by rememberSaveable { mutableStateOf(ToggleableState.Off) }
    var limpComment by rememberSaveable { mutableStateOf("") }
    var sellCheck by rememberSaveable { mutableStateOf(ToggleableState.Off) }
    var sellComment by rememberSaveable { mutableStateOf("") }
    var encCheck by rememberSaveable { mutableStateOf(ToggleableState.Off) }
    var encComment by rememberSaveable { mutableStateOf("") }
    var rotuloCheck by rememberSaveable { mutableStateOf(ToggleableState.Off) }
    var rotuloComment by rememberSaveable { mutableStateOf("") }
    var paletCheck by rememberSaveable { mutableStateOf(ToggleableState.Off) }
    var paletComment by rememberSaveable { mutableStateOf("") }*/
    var conformidad = rememberSaveable { mutableStateOf("CONFORME") }
    var conformidadComment by rememberSaveable { mutableStateOf("") }


    fun toggleStateToString(state: ToggleableState): String {
        return when (state) {
            ToggleableState.On -> "Y"
            ToggleableState.Off -> "N"
            ToggleableState.Indeterminate -> "X"
        }
    }

    fun buildEvaluacion(): Evaluacion {
        return Evaluacion(
            U_Fecha = sharedViewModel.fecha,
            U_Turno = sharedViewModel.turno,
            U_OT = sharedViewModel.ot,

            U_Peso_Check = toggleStateToString(checkItems[0].state),
            U_Peso_Comment = checkItems[0].comment,

            U_Etiq_Check = toggleStateToString(checkItems[1].state),
            U_Etiq_Comment = checkItems[1].comment,

            U_Lot_Check = toggleStateToString(checkItems[2].state),
            U_Lot_Comment = checkItems[2].comment,

            U_Limp_Check = toggleStateToString(checkItems[3].state),
            U_Limp_Comment = checkItems[3].comment,

            U_Sell_Check = toggleStateToString(checkItems[4].state),
            U_Sell_Comment = checkItems[4].comment,

            U_Enc_Check = toggleStateToString(checkItems[5].state),
            U_Enc_Comment = checkItems[5].comment,

            U_Rotulo_Check = toggleStateToString(checkItems[6].state),
            U_Rotulo_Comment = checkItems[6].comment,

            U_Palet_Check = toggleStateToString(checkItems[7].state),
            U_Palet_Comment = checkItems[7].comment,

            // ###############
            /*U_Etiq_Check = when (etiqCheck) {
                ToggleableState.On -> "Y"
                ToggleableState.Off -> "N"
                ToggleableState.Indeterminate -> "X"
            },
            U_Etiq_Comment = etiqComment,
            U_Lot_Check = when (lotCheck) {
                ToggleableState.On -> "Y"
                ToggleableState.Off -> "N"
                ToggleableState.Indeterminate -> "X"
            },
            U_Lot_Comment = lotComment,
            U_Limp_Check = when (limpCheck) {
                ToggleableState.On -> "Y"
                ToggleableState.Off -> "N"
                ToggleableState.Indeterminate -> "X"
            },
            U_Limp_Comment = limpComment,
            U_Sell_Check = when (sellCheck) {
                ToggleableState.On -> "Y"
                ToggleableState.Off -> "N"
                ToggleableState.Indeterminate -> "X"
            },
            U_Sell_Comment = sellComment,
            U_Enc_Check = when (encCheck) {
                ToggleableState.On -> "Y"
                ToggleableState.Off -> "N"
                ToggleableState.Indeterminate -> "X"
            },
            U_Enc_Comment = encComment,
            U_Rotulo_Check = when (rotuloCheck) {
                ToggleableState.On -> "Y"
                ToggleableState.Off -> "N"
                ToggleableState.Indeterminate -> "X"
            },
            U_Rotulo_Comment = rotuloComment,
            U_Palet_Check = when (paletCheck) {
                ToggleableState.On -> "Y"
                ToggleableState.Off -> "N"
                ToggleableState.Indeterminate -> "X"
            },
            U_Palet_Comment = paletComment,*/

            U_Conformidad = if (conformidad.value == "CONFORME") "Y" else "N",
            U_Conformidad_Comment = conformidadComment,
            U_Usuario = sharedViewModel.usuario,
            U_Cantidad = sharedViewModel.cantidad,
            U_Maquinista = sharedViewModel.operador,
        )

    }



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = "Evaluación de Inspección",
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
        )
        Text(
            text = "Seleccione los criterios a evaluar y añada un comentario en cada campo",
            fontSize = 12.sp,
            lineHeight = 14.sp
        )
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = "OBSERVACIONES",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Checklist visual y dinámico
        checkItems.forEachIndexed { index, item ->
            CheckItemCard(
                label = item.label,
                icon = item.icon,
                state = item.state,
                comment = item.comment,
                onStateChange = { newState ->
                    val currentItem = checkItems[index]
                    checkItems[index] = checkItems[index].copy(state = newState)
                    //checkItems[index] = item.copy(state = newState, comment = if (newState == ToggleableState.Off) "" else item.comment)
                    //checkItems[index] = item.copy(state = newState, comment = if (newState == ToggleableState.Off) "" else currentItem.comment)
                },
                onCommentChange = { newComment ->
                    //checkItems[index] = item.copy(comment = newComment)
                    checkItems[index] = checkItems[index].copy(comment = newComment)
                }
            )
        }

        //------------------------------ EVALUACION ----------------------------------------
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = "EVALUACIÓN",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
        )
        Spacer(modifier = Modifier.height(8.dp))
        var selected by remember { mutableStateOf(true) }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SelectableOutlinedRow(
                modifier = Modifier.weight(1f),
                label = if(selected) "CONFORME" else "NO CONFORME",
                selected = selected,
                onClick = {
                    selected = !selected
                    conformidad.value = if(selected) "CONFORME" else "NO CONFORME"
                }
            )

            Spacer(modifier = Modifier.width(16.dp))
            CompactCommentField(
                text = conformidadComment,
                onTextChange = { conformidadComment = it },
                placeholder = "Observacion",
                enabled = true,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(32.dp)) // En caso d eque no haya espacio suficiente
        Spacer(modifier = Modifier.weight(1f)) // Expande el espacio restante
        BotonD(
            navController = navController,
            viewModel,
            sharedViewModel,
            buildEvaluacion(),
            conformidad,
            id = id,
            onBack
        )

        Spacer(modifier = Modifier.height(32.dp))

    }
}

@Composable
fun CheckItemCard(
    label: String,
    icon: ImageVector,
    state: ToggleableState,
    comment: String,
    onStateChange: (ToggleableState) -> Unit,
    onCommentChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = 0.dp,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(80.dp)
            )
            Spacer(Modifier.width(10.dp))
            TriStateCheckbox(
                state = state,
                onClick = {
                    val newState = when (state) {
                        ToggleableState.Off -> ToggleableState.On
                        ToggleableState.On -> ToggleableState.Indeterminate
                        ToggleableState.Indeterminate -> ToggleableState.Off
                    }
                    onStateChange(newState)
                    if (newState == ToggleableState.Off) onCommentChange("")
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = when (state) {
                        ToggleableState.On -> Color(0xFF4CAF50)
                        ToggleableState.Indeterminate -> Color(0xFFFFC107)
                        ToggleableState.Off -> Color.Gray
                    },
                    uncheckedColor = Color.Gray,
                    checkmarkColor = Color.White
                )
            )

            Spacer(Modifier.width(12.dp))
            CompactCommentField(
                text = if (state != ToggleableState.Off) comment else "",
                onTextChange = onCommentChange,
                placeholder = when (state) {
                    ToggleableState.On -> "Comentario"
                    ToggleableState.Indeterminate -> "Observación"
                    else -> ""
                },
                enabled = state != ToggleableState.Off,
                modifier = Modifier.weight(1f)
            )
        }
    }
}



@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BotonD(
    navController: NavController,
    viewModel: InspectionViewModel,
    sharedViewModel: SharedViewModel,
    buildEvaluacion: Evaluacion,
    conformidad: MutableState<String>,
    id: String,
    onBack: () -> Unit
) {

    val evalState by viewModel.evalState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var dialogTitle by remember { mutableStateOf("") }
    var dialogMessage by remember { mutableStateOf(evalState.evalResponde.data) }

    val context = LocalContext.current
    val activity = context as Activity
    val windowSize = calculateWindowSizeClass(context)

    val padding_res = Dimensions.getPadding(windowSize.widthSizeClass)
    val buttonHeight = Dimensions.getButtonHeight(windowSize.widthSizeClass)
    val bodyFontSize = Dimensions.getBodyFontSize(windowSize.widthSizeClass)

    Row(modifier = Modifier.fillMaxWidth()){
        Button(
            onClick = { onBack() },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .height(buttonHeight)
                .padding(horizontal = padding_res)
                .clip(RoundedCornerShape(0.dp)),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.LightGray,
                contentColor = Color.Black
            )
        ) {
            Text(text = "Volver", fontSize = bodyFontSize.sp )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = {
                val evaluacion = buildEvaluacion
                viewModel.EnviarEvaluacion(evaluacion)
                showDialog = true
                Log.e("Evaluacion", evaluacion.toString())
            },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .height(buttonHeight)
                .padding(horizontal = padding_res)
                .clip(RoundedCornerShape(0.dp)),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFC6A68),
                contentColor = Color.White
            )
        ) {
            Text(text = "ENVIAR", fontSize = bodyFontSize.sp )
        }
    }


    when(isLoading){
        EstadoInspeccion.Cargando -> {
            CustomAlertDialog(
                showDialog = showDialog,
                title = "Cargando",
                message = "Enviando...",
                confirmButtonText = "",
                dismissButtonText = null,
                onDismiss = {showDialog = false}
            )
        }
        EstadoInspeccion.Exitoso -> {
            CustomAlertDialog(
                showDialog = showDialog,
                title = "Envio Exitoso",
                message = evalState.evalResponde.data,
                icon = Icons.Default.Check,
                confirmButtonText = "OK",
                dismissButtonText = null,
                onConfirm = { viewModel.actualizarEstadoInspeccion(EstadoInspeccion.Idle) },
                onDismiss = {
                    showDialog = false
                    viewModel.actualizarEstadoInspeccion(EstadoInspeccion.Idle)
                    navController.navigate("listaInsp/$id")
                },
                dialogType = DialogType.SUCCESS,
            )
        }
        is EstadoInspeccion.Error -> {
            CustomAlertDialog(
                showDialog = showDialog,
                title = "Error",
                message = (isLoading as EstadoParada.Error).mensaje,
                confirmButtonText = "OK",
                dismissButtonText = null,
                onConfirm = { viewModel.actualizarEstadoInspeccion(EstadoInspeccion.Idle) },
                onDismiss = {
                    showDialog = false
                    viewModel.actualizarEstadoInspeccion(EstadoInspeccion.Idle)
                },
                dialogType = DialogType.ERROR,
            )
        }
        else -> {}
    }

    if (evalState.state != null) {
        LaunchedEffect(evalState.state) {
            dialogTitle = if (evalState.state) "Envio Exitoso" else "Enviando"
            dialogMessage = if (evalState.state) evalState.evalResponde.data else "Cargando..."
            /*dialogMessage = if (evalState.state) {
                evalState.evalResponde.data
            } else {
                "Hubo un problema con la solicitud."
            }*/
        }
    }

}


data class CheckItem(
    val label: String,
    val shortLabel: String,
    val icon: ImageVector,
    var state: ToggleableState,
    var comment: String
)
