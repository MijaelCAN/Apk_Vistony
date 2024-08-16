package com.vistony.app.ui.theme.Screen.Inspeccion

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withAnnotation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.vistony.app.Entidad.Evaluacion
import com.vistony.app.R
import com.vistony.app.ViewModel.EvalViewModel
import com.vistony.app.ViewModel.SharedViewModel
import com.vistony.app.ui.theme.Screen.Generic.CustomDrawer
import com.vistony.app.ui.theme.Screen.Generic.CustomOutlinedTextField
import com.vistony.app.ui.theme.Screen.Generic.CustomOutlinedTextField2
import com.vistony.app.ui.theme.Screen.Generic.CustomText
import com.vistony.app.ui.theme.Screen.Generic.SuccessDialog
import com.vistony.app.ui.theme.Screen.Generic.TopBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleScreen(
    navController: NavHostController,
    sharedViewModel: SharedViewModel,
    viewModel: EvalViewModel = hiltViewModel(),
    id: String
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { CustomDrawer(navController = navController, id = id) }) {
        Scaffold(
            topBar = { TopBar("Detalle de Inspección", navController = navController, onMenuClick = {
                scope.launch { drawerState.open() }
            }) }
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
                BodyDetalle(navController, viewModel, sharedViewModel,id)
            }
        }
    }
}

@OptIn(ExperimentalTextApi::class)
@ExperimentalMaterial3Api
@Composable
fun BodyDetalle(
    navController: NavHostController,
    viewModel: EvalViewModel,
    sharedViewModel: SharedViewModel,
    id: String
) {
    var pesoCheck by rememberSaveable { mutableStateOf(ToggleableState.Off) }
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
    var paletComment by rememberSaveable { mutableStateOf("") }
    var conformidad = rememberSaveable { mutableStateOf("") }
    var conformidadComment by rememberSaveable { mutableStateOf("") }

    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    fun buildEvaluacion(): Evaluacion {
        return Evaluacion(
            U_Fecha = sharedViewModel.fecha,
            U_Turno = sharedViewModel.turno,
            U_OT = sharedViewModel.ot,
            U_Peso_Check = when (pesoCheck) {
                ToggleableState.On -> "Y"
                ToggleableState.Off -> "N"
                ToggleableState.Indeterminate -> "X"
            },
            U_Peso_Comment = pesoComment,
            U_Etiq_Check = when (etiqCheck) {
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
            U_Palet_Comment = paletComment,
            U_Conformidad = if (conformidad.value.equals("CONFORME")) "Y" else "N",
            U_Conformidad_Comment = conformidadComment,
            U_Usuario = sharedViewModel.usuario,
            U_Cantidad = sharedViewModel.cantidad,
        )

    }
    Log.e("USUARIO",sharedViewModel.usuario)
    val expanded = remember { mutableStateOf(false) }
    val options = listOf("CONFORME", "NO CONFORME")
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .wrapContentSize()
            .clip(RoundedCornerShape(25.dp))
            .background(color = Color(0xFFFFFFFF))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize()
                .padding(vertical = 16.dp, horizontal = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "OBSERVACIONES",
                modifier = Modifier.padding(end = 8.dp, top = 16.dp),
                style = TextStyle(color = Color.Black)
            )
            val keyboardController = LocalSoftwareKeyboardController.current
            val focusManager = LocalFocusManager.current
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomText(text = "PESO")
                TriStateCheckbox(
                    state = pesoCheck,
                    onClick = {
                        pesoComment = ""
                        pesoCheck = when (pesoCheck) {
                            ToggleableState.Off -> ToggleableState.On
                            ToggleableState.On -> ToggleableState.Indeterminate
                            ToggleableState.Indeterminate -> ToggleableState.Off
                        }
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = when (pesoCheck) {
                            ToggleableState.On -> Color.Green
                            ToggleableState.Indeterminate -> Color.Yellow
                            ToggleableState.Off -> Color.Gray
                        },
                        uncheckedColor = Color.Gray,
                        checkmarkColor = Color.White,
                        disabledCheckedColor = Color.Gray,
                        disabledUncheckedColor = Color.LightGray,
                        disabledIndeterminateColor = Color.Red
                    )
                )

                CustomOutlinedTextField2(
                    value = when (pesoCheck) {
                        ToggleableState.On -> pesoComment
                        ToggleableState.Indeterminate -> pesoComment
                        ToggleableState.Off -> ""
                    },
                    onValueChange = { it ->
                        if (pesoCheck == ToggleableState.On || pesoCheck == ToggleableState.Indeterminate) {
                            pesoComment = it
                        }
                    },
                    label = when (pesoCheck) {
                        ToggleableState.On -> "Comentario"
                        ToggleableState.Indeterminate -> "Observacion"
                        ToggleableState.Off -> ""
                    },
                    readOnly = pesoCheck == ToggleableState.Off
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomText(text = "ETIQ.")
                TriStateCheckbox(
                    state = etiqCheck,
                    onClick = {
                        etiqComment = ""
                        etiqCheck = when (etiqCheck) {
                            ToggleableState.Off -> ToggleableState.On
                            ToggleableState.On -> ToggleableState.Indeterminate
                            ToggleableState.Indeterminate -> ToggleableState.Off
                        }
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = when (etiqCheck) {
                            ToggleableState.On -> Color.Green
                            ToggleableState.Indeterminate -> Color.Yellow
                            ToggleableState.Off -> Color.Gray
                        },
                        uncheckedColor = Color.Gray,
                        checkmarkColor = Color.White,
                        disabledCheckedColor = Color.Gray,
                        disabledUncheckedColor = Color.LightGray,
                        disabledIndeterminateColor = Color.Red
                    )
                )
                CustomOutlinedTextField2(
                    value = when (etiqCheck) {
                        ToggleableState.On -> etiqComment
                        ToggleableState.Indeterminate -> etiqComment
                        ToggleableState.Off -> ""
                    },
                    onValueChange = { it ->
                        if (etiqCheck == ToggleableState.On || etiqCheck == ToggleableState.Indeterminate) {
                            etiqComment = it
                        }
                    },
                    label = when (etiqCheck) {
                        ToggleableState.On -> "Comentario"
                        ToggleableState.Indeterminate -> "Observacion"
                        ToggleableState.Off -> ""
                    },
                    readOnly = etiqCheck == ToggleableState.Off
                )
            }
            //----------------------------------------------------------------------
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomText(text = "LOT.")
                TriStateCheckbox(
                    state = lotCheck,
                    onClick = {
                        lotComment = ""
                        lotCheck = when (lotCheck) {
                            ToggleableState.Off -> ToggleableState.On
                            ToggleableState.On -> ToggleableState.Indeterminate
                            ToggleableState.Indeterminate -> ToggleableState.Off
                        }
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = when (lotCheck) {
                            ToggleableState.On -> Color.Green
                            ToggleableState.Indeterminate -> Color.Yellow
                            ToggleableState.Off -> Color.Gray
                        },
                        uncheckedColor = Color.Gray,
                        checkmarkColor = Color.White,
                        disabledCheckedColor = Color.Gray,
                        disabledUncheckedColor = Color.LightGray,
                        disabledIndeterminateColor = Color.Red
                    )
                )
                CustomOutlinedTextField2(
                    value = when (lotCheck) {
                        ToggleableState.On -> lotComment
                        ToggleableState.Indeterminate -> lotComment
                        ToggleableState.Off -> ""
                    },
                    onValueChange = { it ->
                        if (lotCheck == ToggleableState.On || lotCheck == ToggleableState.Indeterminate) {
                            lotComment = it
                        }
                    },
                    label = when (lotCheck) {
                        ToggleableState.On -> "Comentario"
                        ToggleableState.Indeterminate -> "Observacion"
                        ToggleableState.Off -> ""
                    },
                    readOnly = lotCheck == ToggleableState.Off
                )
            }
            //----------------------------------------------------------------------
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomText(text = "LIMP.")
                TriStateCheckbox(
                    state = limpCheck,
                    onClick = {
                        limpComment = ""
                        limpCheck = when (limpCheck) {
                            ToggleableState.Off -> ToggleableState.On
                            ToggleableState.On -> ToggleableState.Indeterminate
                            ToggleableState.Indeterminate -> ToggleableState.Off
                        }
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = when (limpCheck) {
                            ToggleableState.On -> Color.Green
                            ToggleableState.Indeterminate -> Color.Yellow
                            ToggleableState.Off -> Color.Gray
                        },
                        uncheckedColor = Color.Gray,
                        checkmarkColor = Color.White,
                        disabledCheckedColor = Color.Gray,
                        disabledUncheckedColor = Color.LightGray,
                        disabledIndeterminateColor = Color.Red
                    )
                )
                CustomOutlinedTextField2(
                    value = when (limpCheck) {
                        ToggleableState.On -> limpComment
                        ToggleableState.Indeterminate -> limpComment
                        ToggleableState.Off -> ""
                    },
                    onValueChange = { it ->
                        if (limpCheck == ToggleableState.On || limpCheck == ToggleableState.Indeterminate) {
                            limpComment = it
                        }
                    },
                    label = when (limpCheck) {
                        ToggleableState.On -> "Comentario"
                        ToggleableState.Indeterminate -> "Observacion"
                        ToggleableState.Off -> ""
                    },
                    readOnly = limpCheck == ToggleableState.Off
                )
            }
            //----------------------------------------------------------------------
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomText(text = "SELL.")
                TriStateCheckbox(
                    state = sellCheck,
                    onClick = {
                        sellComment = ""
                        sellCheck = when (sellCheck) {
                            ToggleableState.Off -> ToggleableState.On
                            ToggleableState.On -> ToggleableState.Indeterminate
                            ToggleableState.Indeterminate -> ToggleableState.Off
                        }
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = when (sellCheck) {
                            ToggleableState.On -> Color.Green
                            ToggleableState.Indeterminate -> Color.Yellow
                            ToggleableState.Off -> Color.Gray
                        },
                        uncheckedColor = Color.Gray,
                        checkmarkColor = Color.White,
                        disabledCheckedColor = Color.Gray,
                        disabledUncheckedColor = Color.LightGray,
                        disabledIndeterminateColor = Color.Red
                    )
                )
                CustomOutlinedTextField2(
                    value = when (sellCheck) {
                        ToggleableState.On -> sellComment
                        ToggleableState.Indeterminate -> sellComment
                        ToggleableState.Off -> ""
                    },
                    onValueChange = { it ->
                        if (sellCheck == ToggleableState.On || sellCheck == ToggleableState.Indeterminate) {
                            sellComment = it
                        }
                    },
                    label = when (sellCheck) {
                        ToggleableState.On -> "Comentario"
                        ToggleableState.Indeterminate -> "Observacion"
                        ToggleableState.Off -> ""
                    },
                    readOnly = sellCheck == ToggleableState.Off
                )
            }
            //----------------------------------------------------------------------
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomText(text = "ENC.")
                TriStateCheckbox(
                    state = encCheck,
                    onClick = {
                        encComment = ""
                        encCheck = when (encCheck) {
                            ToggleableState.Off -> ToggleableState.On
                            ToggleableState.On -> ToggleableState.Indeterminate
                            ToggleableState.Indeterminate -> ToggleableState.Off
                        }
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = when (encCheck) {
                            ToggleableState.On -> Color.Green
                            ToggleableState.Indeterminate -> Color.Yellow
                            ToggleableState.Off -> Color.Gray
                        },
                        uncheckedColor = Color.Gray,
                        checkmarkColor = Color.White,
                        disabledCheckedColor = Color.Gray,
                        disabledUncheckedColor = Color.LightGray,
                        disabledIndeterminateColor = Color.Red
                    )
                )
                CustomOutlinedTextField2(
                    value = when (encCheck) {
                        ToggleableState.On -> encComment
                        ToggleableState.Indeterminate -> encComment
                        ToggleableState.Off -> ""
                    },
                    onValueChange = { it ->
                        if (encCheck == ToggleableState.On || encCheck == ToggleableState.Indeterminate) {
                            encComment = it
                        }
                    },
                    label = when (encCheck) {
                        ToggleableState.On -> "Comentario"
                        ToggleableState.Indeterminate -> "Observacion"
                        ToggleableState.Off -> ""
                    },
                    readOnly = encCheck == ToggleableState.Off
                )
            }
            //----------------------------------------------------------------------
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomText(text = "ROTUL.")
                TriStateCheckbox(
                    state = rotuloCheck,
                    onClick = {
                        rotuloComment = ""
                        rotuloCheck = when (rotuloCheck) {
                            ToggleableState.Off -> ToggleableState.On
                            ToggleableState.On -> ToggleableState.Indeterminate
                            ToggleableState.Indeterminate -> ToggleableState.Off
                        }
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = when (rotuloCheck) {
                            ToggleableState.On -> Color.Green
                            ToggleableState.Indeterminate -> Color.Yellow
                            ToggleableState.Off -> Color.Gray
                        },
                        uncheckedColor = Color.Gray,
                        checkmarkColor = Color.White,
                        disabledCheckedColor = Color.Gray,
                        disabledUncheckedColor = Color.LightGray,
                        disabledIndeterminateColor = Color.Red
                    )
                )
                CustomOutlinedTextField2(
                    value = when (rotuloCheck) {
                        ToggleableState.On -> rotuloComment
                        ToggleableState.Indeterminate -> rotuloComment
                        ToggleableState.Off -> ""
                    },
                    onValueChange = { it ->
                        if (rotuloCheck == ToggleableState.On || rotuloCheck == ToggleableState.Indeterminate) {
                            rotuloComment = it
                        }
                    },
                    label = when (rotuloCheck) {
                        ToggleableState.On -> "Comentario"
                        ToggleableState.Indeterminate -> "Observacion"
                        ToggleableState.Off -> ""
                    },
                    readOnly = rotuloCheck == ToggleableState.Off
                )
            }
            //----------------------------------------------------------------------
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomText(text = "PALET.")
                TriStateCheckbox(
                    state = paletCheck,
                    onClick = {
                        paletComment = ""
                        paletCheck = when (paletCheck) {
                            ToggleableState.Off -> ToggleableState.On
                            ToggleableState.On -> ToggleableState.Indeterminate
                            ToggleableState.Indeterminate -> ToggleableState.Off
                        }
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = when (paletCheck) {
                            ToggleableState.On -> Color.Green
                            ToggleableState.Indeterminate -> Color.Yellow
                            ToggleableState.Off -> Color.Gray
                        },
                        uncheckedColor = Color.Gray,
                        checkmarkColor = Color.White,
                        disabledCheckedColor = Color.Gray,
                        disabledUncheckedColor = Color.LightGray,
                        disabledIndeterminateColor = Color.Red
                    )
                )
                //CustomCheckbox(checked = isChecked, onCheckedChange = {isChecked = it}, modifier = Modifier.padding(top = 8.dp), checkboxSize = 24.dp)
                CustomOutlinedTextField2(
                    value = when (paletCheck) {
                        ToggleableState.On -> paletComment
                        ToggleableState.Indeterminate -> paletComment
                        ToggleableState.Off -> ""
                    },
                    onValueChange = { it ->
                        if (paletCheck == ToggleableState.On || paletCheck == ToggleableState.Indeterminate) {
                            paletComment = it
                        }
                    },
                    label = when (paletCheck) {
                        ToggleableState.On -> "Comentario"
                        ToggleableState.Indeterminate -> "Observacion"
                        ToggleableState.Off -> ""
                    },
                    readOnly = paletCheck == ToggleableState.Off
                )
            }
            var prueba by rememberSaveable { mutableStateOf("") }

            val text = buildAnnotatedString {
                append("This is not clickable ")
                withAnnotation("tag", "annotation") {
                    append("This is clickable")
                }
            }

            //------------------------------ EVALUACION ----------------------------------------
            Text(
                text = "EVALUACIÓN",
                modifier = Modifier.padding(end = 8.dp, top = 16.dp),
                style = TextStyle(color = Color.Black)
            )
            Divider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = Color.Gray)
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ExposedDropdownMenuBox(
                    modifier = Modifier.width(200.dp),
                    expanded = expanded.value,
                    onExpandedChange = {
                        expanded.value = !expanded.value
                        conformidad.value = ""
                        conformidadComment = ""
                    }) {
                    CustomOutlinedTextField(
                        modifier = Modifier.menuAnchor(),
                        value = conformidad.value,
                        onValueChange = { },
                        label = "Estado",
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
                        readOnly = true
                    )
                    ExposedDropdownMenu(
                        modifier = Modifier
                            .background(Color.White)
                            .clip(RoundedCornerShape(8.dp)),
                        expanded = expanded.value,
                        onDismissRequest = { expanded.value = false }) {
                        options.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option, color = Color.Black) },
                                onClick = {
                                    conformidad.value = option
                                    expanded.value = false
                                }
                            )
                        }
                    }
                }

                /*CustomOutlinedTextField(
                    modifier = Modifier
                        .width(200.dp)
                        .onGloballyPositioned { textFieldSize = it.size.toSize() },
                    value = conformidad.value,
                    onValueChange = { conformidad.value = it },
                    label = "Estado",
                    trailingIcon = {
                        IconButton(onClick = { expanded.value = true }) {
                            Icon(Icons.Filled.ArrowDropDown, contentDescription = "Expandir")
                        }
                    },
                    readOnly = true
                )
                CustomSpinner(
                    expanded = expanded,
                    options = options,
                    operador = conformidad,
                    textFieldSize = textFieldSize
                )*/
                CustomOutlinedTextField2(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    value = conformidadComment,
                    onValueChange = { conformidadComment = it },
                    label = "Observacion",
                    readOnly = !conformidad.value.isNotEmpty()
                )
            }
            BotonD(
                navController = navController,
                viewModel,
                sharedViewModel,
                buildEvaluacion(),
                conformidad,
                id = id
            )

        }
    }
}

@Composable
fun BotonD(
    navController: NavController,
    viewModel: EvalViewModel,
    sharedViewModel: SharedViewModel,
    buildEvaluacion: Evaluacion,
    conformidad: MutableState<String>,
    id: String
) {

    val evalState by viewModel.evalState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var stateButton = conformidad.value.isNotEmpty();
    var showDialog by remember { mutableStateOf(false) }
    var dialogTitle by remember { mutableStateOf("") }
    var dialogMessage by remember { mutableStateOf(evalState.evalResponde.data) }


    val buttonColors = ButtonDefaults.elevatedButtonColors(
        containerColor = if (stateButton) Color(0xFF0054A3) else Color(0XFF9C9B9B),
        contentColor = if (stateButton) Color(0xFFC9C9C9) else Color.White,
        disabledContentColor = Color.White,
        disabledContainerColor = Color(0XFF9C9B9B)
    )

    ElevatedButton(
        modifier = Modifier
            .width(300.dp)
            .height(58.dp)
            .padding(top = 20.dp)
            .systemBarsPadding()
            .imePadding(),
        onClick = {
            val evaluacion = buildEvaluacion
            viewModel.EnviarEvaluacion(evaluacion)
            showDialog = true
            /*titulo = "Envio Exitóso"
            if (evalState.state) {
                showDialog = true
                Log.e("Resultado2", evalState.evalResponde.data)
            }*/
            Log.e("Evaluacion", evaluacion.toString())
        },
        colors = buttonColors,
        shape = RoundedCornerShape(18.dp)
    ) {
        Text(text = "ENVIAR")
    }
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        //showDialog = true
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

    if (showDialog) {
        if (evalState.state) {
            SuccessDialog(
                isVisible = showDialog,
                message = dialogMessage,
                titulo = dialogTitle,
                usuario = sharedViewModel.usuario,
                onDismiss = { showDialog = false },
                navController = navController,
                id = id
            )
        } else {
            /*ErrorDialog(
                isVisible = showDialog,
                onDismiss = { showDialog = false },
                message = dialogMessage,
                navController = navController,
                titulo = dialogTitle
            )*/
        }
    }


    //ConfirmationDialog(isVisible = showDialog, onConfirm = { viewModel._evalState.data }, onDismiss = { showDialog = false })
    SuccessDialog(
        isVisible = showDialog,
        onDismiss = { showDialog = false },
        message = dialogMessage, navController = navController,
        titulo = dialogTitle,
        id = id
    )
}

@Composable
fun MyTable(data: List<List<String>>) {
    LazyColumn(modifier = Modifier.fillMaxWidth()){
        items(data) { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                row.forEach { item ->
                    Text(
                        text =item,
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                    )
                }
                Button(onClick = { /* Acción para visualizar el registro */ }) {
                    Text("Visualizar")
                }
            }
        }
    }
}

/*
@Composable
fun Prueba(){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .padding(start = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DrawerMenuItem(
            icon = Icons.Filled.Camera,
            text = "Lista Inspecciones",
            onClick = { /* Action for Lista Inspecciones */ }
        )
        Button(
            onClick = { /* Action for NUEVA INSPECCIÓN */ },
            modifier = Modifier.fillMaxWidth(), // Ensure button takes full width
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue, // Customizebackground color
                contentColor = Color.White    // Customize text color
            )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Nueva Inspección"
                )
                Spacer(modifier = Modifier.width(8.dp)) // Add space between icon and text
                Text("NUEVA INSPECCIÓN")
            }
        }
    }
}*/

/*
@Composable
fun FilterableDropdownMenu(options: List<String>) {
    val expanded = remember { mutableStateOf(false) }
    val conformidad = remember { mutableStateOf("") }
    val filteredOptions = remember { mutableStateOf(options) }

    // Función para filtrar las opciones
    fun filterOptions(query: String) {
        filteredOptions.value = if (query.isEmpty()) {
            options
        } else {
            options.filter { it.contains(query, ignoreCase = true) }
        }
    }

    ExposedDropdownMenuBox(
        modifier = Modifier.width(200.dp),
        expanded = expanded.value,
        onExpandedChange = {
            expanded.value = !expanded.value
            if (!expanded.value) {
                conformidad.value = ""
                filteredOptions.value = options // Resetear las opciones filtradas
            }
        }
    ) {
        CustomOutlinedTextField(
            modifier = Modifier.menuAnchor(),
            value = conformidad.value,
            onValueChange = { newValue ->
                conformidad.value = newValue
                filterOptions(newValue) // Filtrar opciones al cambiar el texto
            },
            label = { Text("Estado") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
            readOnly = false // Permitir la edición
        )
        ExposedDropdownMenu(
            modifier = Modifier
                .background(Color.White)
                .clip(RoundedCornerShape(8.dp)),
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
        ) {
            filteredOptions.value.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, color = Color.Black) },
                    onClick = {
                        conformidad.value = option
                        expanded.value = false
                    }
                )
            }
        }
    }
}*/
