package com.vistony.app.ui.theme.Screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.vistony.app.Entidad.Evaluacion
import com.vistony.app.R
import com.vistony.app.ViewModel.EvalViewModel
import com.vistony.app.ViewModel.SharedViewModel
import com.vistony.app.ui.theme.Screen.Generic.CustomOutlinedTextField
import com.vistony.app.ui.theme.Screen.Generic.CustomOutlinedTextField2
import com.vistony.app.ui.theme.Screen.Generic.CustomSpinner
import com.vistony.app.ui.theme.Screen.Generic.CustomText
import com.vistony.app.ui.theme.Screen.Generic.MyCheckbox
import com.vistony.app.ui.theme.Screen.Generic.SuccessDialog
import com.vistony.app.ui.theme.Screen.Generic.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleScreen(
    navController: NavHostController,
    sharedViewModel: SharedViewModel,
    viewModel: EvalViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    Scaffold(
        topBar = { TopBar(navController = navController) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(color = Color(0xFF0B4FAF))
                .verticalScroll(scrollState),
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
            BodyDetalle(navController, viewModel, sharedViewModel)
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun BodyDetalle(
    navController: NavHostController,
    viewModel: EvalViewModel,
    sharedViewModel: SharedViewModel
) {
    var pesoCheck by rememberSaveable { mutableStateOf(false) }
    var pesoComment by rememberSaveable { mutableStateOf("") }
    var etiqCheck by rememberSaveable { mutableStateOf(false) }
    var etiqComment by rememberSaveable { mutableStateOf("") }
    var lotCheck by rememberSaveable { mutableStateOf(false) }
    var lotComment by rememberSaveable { mutableStateOf("") }
    var limpCheck by rememberSaveable { mutableStateOf(false) }
    var limpComment by rememberSaveable { mutableStateOf("") }
    var sellCheck by rememberSaveable { mutableStateOf(false) }
    var sellComment by rememberSaveable { mutableStateOf("") }
    var encCheck by rememberSaveable { mutableStateOf(false) }
    var encComment by rememberSaveable { mutableStateOf("") }
    var rotuloCheck by rememberSaveable { mutableStateOf(false) }
    var rotuloComment by rememberSaveable { mutableStateOf("") }
    var paletCheck by rememberSaveable { mutableStateOf(false) }
    var paletComment by rememberSaveable { mutableStateOf("") }
    var conformidad = rememberSaveable { mutableStateOf("") }
    var conformidadComment by rememberSaveable { mutableStateOf("") }

    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    fun buildEvaluacion(): Evaluacion {
        return Evaluacion(
            U_Fecha = sharedViewModel.fecha,
            U_Turno = sharedViewModel.turno,
            U_OT = sharedViewModel.ot,
            U_Peso_Check = if (pesoCheck) "Y" else "N",
            U_Peso_Comment = pesoComment,
            U_Etiq_Check = if (etiqCheck) "Y" else "N",
            U_Etiq_Comment = etiqComment,
            U_Lot_Check = if (lotCheck) "Y" else "N",
            U_Lot_Comment = lotComment,
            U_Limp_Check = if (limpCheck) "Y" else "N",
            U_Limp_Comment = limpComment,
            U_Sell_Check = if (sellCheck) "Y" else "N",
            U_Sell_Comment = sellComment,
            U_Enc_Check = if (encCheck) "Y" else "N",
            U_Enc_Comment = encComment,
            U_Rotulo_Check = if (rotuloCheck) "Y" else "N",
            U_Rotulo_Comment = rotuloComment,
            U_Palet_Check = if (paletCheck) "Y" else "N",
            U_Palet_Comment = paletComment,
            U_Conformidad = if (conformidad.value.equals("CONFORME")) "Y" else "N",
            U_Conformidad_Comment = conformidadComment
        )
    }

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
                text = "COMENTARIO",
                modifier = Modifier.padding(end = 8.dp, top = 16.dp),
                style = TextStyle(color = Color.Black)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomText(text = "PESO")
                MyCheckbox(
                    checked = pesoCheck,
                    onCheckedChange = { it ->
                        if (!it) pesoComment = ""
                        pesoCheck = it
                    },
                    modifier = Modifier.padding(top = 8.dp)
                )
                CustomOutlinedTextField2(
                    value = if (pesoCheck) pesoComment else "",
                    onValueChange = { it -> if (pesoCheck) pesoComment = it },
                    label = if (pesoCheck) "Comentario" else "",
                    readOnly = !pesoCheck
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomText(text = "ETIQ.")
                MyCheckbox(
                    checked = etiqCheck,
                    onCheckedChange = { it ->
                        if (!it) etiqComment = ""
                        etiqCheck = it
                    },
                    modifier = Modifier.padding(top = 8.dp)
                )
                CustomOutlinedTextField2(
                    value = etiqComment,
                    onValueChange = { it -> if (etiqCheck) etiqComment = it },
                    label = if (etiqCheck) "Comentario" else "",
                    readOnly = !etiqCheck
                )
            }
            //----------------------------------------------------------------------
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomText(text = "LOT.")
                MyCheckbox(
                    checked = lotCheck,
                    onCheckedChange = { it ->
                        if (!it) lotComment = ""
                        lotCheck = it
                    },
                    modifier = Modifier.padding(top = 8.dp)
                )
                CustomOutlinedTextField2(
                    value = lotComment,
                    onValueChange = { it -> if (lotCheck) lotComment = it },
                    label = if (lotCheck) "Comentario" else "",
                    readOnly = !lotCheck
                )
            }
            //----------------------------------------------------------------------
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomText(text = "LIMP.")
                MyCheckbox(
                    checked = limpCheck,
                    onCheckedChange = { it ->
                        if (!it) limpComment = ""
                        limpCheck = it
                    },
                    modifier = Modifier.padding(top = 8.dp)
                )
                CustomOutlinedTextField2(
                    value = limpComment,
                    onValueChange = { it -> if (limpCheck) limpComment = it },
                    label = if (limpCheck) "Comentario" else "",
                    readOnly = !limpCheck
                )
            }
            //----------------------------------------------------------------------
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomText(text = "SELL.")
                MyCheckbox(
                    checked = sellCheck,
                    onCheckedChange = { it ->
                        if (!it) sellComment = ""
                        sellCheck = it
                    },
                    modifier = Modifier.padding(top = 8.dp)
                )
                CustomOutlinedTextField2(
                    value = sellComment,
                    onValueChange = { it -> if (sellCheck) sellComment = it },
                    label = if (sellCheck) "Comentario" else "",
                    readOnly = !sellCheck
                )
            }
            //----------------------------------------------------------------------
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomText(text = "ENC.")
                MyCheckbox(
                    checked = encCheck,
                    onCheckedChange = { it ->
                        if (!it) encComment = ""
                        encCheck = it
                    },
                    modifier = Modifier.padding(top = 8.dp)
                )
                CustomOutlinedTextField2(
                    value = encComment,
                    onValueChange = { it -> if (encCheck) encComment = it },
                    label = if (encCheck) "Comentario" else "",
                    readOnly = !encCheck
                )
            }
            //----------------------------------------------------------------------
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomText(text = "ROTUL.")
                MyCheckbox(
                    checked = rotuloCheck,
                    onCheckedChange = { it ->
                        if (!it) rotuloComment = ""
                        rotuloCheck = it
                    },
                    modifier = Modifier.padding(top = 8.dp)
                )
                CustomOutlinedTextField2(
                    value = rotuloComment,
                    onValueChange = { it -> if (rotuloCheck) rotuloComment = it },
                    label = if (rotuloCheck) "Comentario" else "",
                    readOnly = !rotuloCheck
                )
            }
            //----------------------------------------------------------------------
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomText(text = "PALET.")
                MyCheckbox(
                    checked = paletCheck,
                    onCheckedChange = { it ->
                        if (!it) paletComment = ""
                        paletCheck = it
                    },
                    modifier = Modifier.padding(top = 8.dp)
                )
                //CustomCheckbox(checked = isChecked, onCheckedChange = {isChecked = it}, modifier = Modifier.padding(top = 8.dp), checkboxSize = 24.dp)
                CustomOutlinedTextField2(
                    value = paletComment,
                    onValueChange = { it->if(paletCheck)paletComment = it },
                    label = if (paletCheck) "Comentario" else "",
                    readOnly = !paletCheck
                )
            }
            //----------------------------------------------------------------------
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
                CustomOutlinedTextField(
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
                    readOnly = true,
                    onclick = { expanded.value = true }
                )
                CustomSpinner(
                    expanded = expanded,
                    options = options,
                    operador = conformidad,
                    textFieldSize = textFieldSize
                )
                CustomOutlinedTextField2(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    value = conformidadComment,
                    onValueChange = { conformidadComment = it },
                    label = "Comentario",
                )
            }
            BotonD(
                navController = navController,
                viewModel,
                sharedViewModel,
                buildEvaluacion(),
                conformidad
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
    conformidad: MutableState<String>
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
            .padding(top = 20.dp),
        onClick = {
            val evaluacion = buildEvaluacion
            viewModel.EnviarEvaluacion(evaluacion)
            showDialog = true
            /*titulo = "Envio Exitóso"
            if (evalState.state) {
                showDialog = true
                Log.e("Resultado2", evalState.evalResponde.data)
            }
            Log.e("Evaluacion", evaluacion.toString())*/
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
                onDismiss = { showDialog = false },
                message = dialogMessage,
                navController = navController,
                titulo = dialogTitle
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
        titulo = dialogTitle
    )
}
