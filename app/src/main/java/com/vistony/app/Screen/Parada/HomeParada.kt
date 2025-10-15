package com.vistony.app.Screen.Parada


import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.vistony.app.Entidad.Area
import com.vistony.app.Entidad.Maquina
import com.vistony.app.Entidad.Motivo
import com.vistony.app.Entidad.ParadaRequest
import com.vistony.app.Extras.formatoHora
import com.vistony.app.Extras.formatoServidor
import com.vistony.app.Extras.formatoUsuario
import com.vistony.app.Screen.Generic.CustomAlertDialog
import com.vistony.app.Screen.Generic.CustomOutlinedTextField
import com.vistony.app.Screen.Generic.DialogType
import com.vistony.app.Screen.Generic.GenericDropdownMenu
import com.vistony.app.ViewModel.EstadoParada
import com.vistony.app.ViewModel.ParadaViewModel
import com.vistony.app.ui.theme.theme.Dimensions
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun BodyParada(navController: NavController, paradaViewModel: ParadaViewModel, id: String) {
    val areaState by paradaViewModel.areas.collectAsState()
    var areaId by remember { mutableStateOf("") }
    val maquinaState by paradaViewModel.maquinas.collectAsState()
    var maquinaId by remember { mutableStateOf("") }
    val motivoState by paradaViewModel.motivos.collectAsState()
    var motivoId by remember { mutableStateOf("") }

    var fec_Parada_Ini by remember { mutableStateOf(LocalDateTime.now()) }
    var maquina = remember { mutableStateOf("") }
    var area = remember { mutableStateOf("") }
    var motivoParada = remember { mutableStateOf("") }
    var ordenMezcla by remember { mutableStateOf("") }
    var comentarios by remember { mutableStateOf("") }

    val expandedMaquina = remember { mutableStateOf(false) }
    val expandedArea = remember { mutableStateOf(false) }
    val expandedParada = remember { mutableStateOf(false) }

    val context = LocalContext.current
    val activity = context as Activity
    val windowSize = calculateWindowSizeClass(context)

    val padding_res = Dimensions.getPadding(windowSize.widthSizeClass)
    val buttonHeight = Dimensions.getButtonHeight(windowSize.widthSizeClass)
    val bodyFontSize = Dimensions.getBodyFontSize(windowSize.widthSizeClass)

    Log.d("TAG", "BodyParada: $areaState")
    val listAreas = areaState.areaResponse.data.map { it.Code to it.Name }
    val listMaquinas = maquinaState.maquinaResponse.data.map { it.Code to it.Name }
    val listMotivos = motivoState.motivoResponse.data.map { it.Code to it.Name }



    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = "Registro de Parada",
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
        )
        Text(
            text = "Seleccione todos los campos y registre una Parada de Máquina",
            fontSize = 12.sp,
        )
        Spacer(Modifier.height(25.dp))

        // ---------- FILA 1 -----------
        CustomOutlinedTextField(
            value = formatoUsuario(fec_Parada_Ini),
            label = "Fecha de registro",
            readOnly = true
        )
        Spacer(Modifier.height(8.dp))
        Log.d("TAG", "BodyParada: $listAreas")
        //----------- FILA 2 -----------
        GenericDropdownMenu(
            label = "Área",
            options = listAreas,
            selectedValue = area.value,
            onValueChange = { area.value = it },
            onCodeChange = { areaId = it
                paradaViewModel.obtenerMotivos(areaId.toInt())
                motivoParada.value = ""
            },
            expanded = expandedArea.value,
            onExpandedChange = { expandedArea.value = it }
        )

        Spacer(Modifier.height(8.dp))
        //----------- FILA 3 -----------
        GenericDropdownMenu(
            label = "Máquina",
            options = listMaquinas,
            selectedValue = maquina.value,
            onValueChange = { maquina.value = it },
            onCodeChange = { maquinaId = it
                //paradaViewModel.obtenerMotivos(areaId.toInt())
                           },
            expanded = expandedMaquina.value,
            onExpandedChange = { expandedMaquina.value = it }
        )



        Spacer(Modifier.height(8.dp))
        //----------- FILA 4 -----------
        GenericDropdownMenu(
            label = "Motivo Parada",
            options = listMotivos,
            selectedValue = motivoParada.value,
            onValueChange = { motivoParada.value = it },
            onCodeChange = {
                motivoId = it
                 },
            expanded = expandedParada.value,
            onExpandedChange = { expandedParada.value = it }
        )

        Spacer(Modifier.height(8.dp))
        //----------- FILA 5 -----------
        CustomOutlinedTextField(
            value = ordenMezcla,
            onValueChange = { ordenMezcla = it },
            label = "Orden de Mezcla",
            readOnly = false,
        )

        Spacer(Modifier.height(8.dp))
        //----------- FILA 6 -----------
        CustomOutlinedTextField(
            value = comentarios,
            onValueChange = { comentarios = it },
            label = "Observación",
            readOnly = false,
            minLines = 3,
            maxLines = 3
        )
        val stateBoton = maquina.value.isNotEmpty() && area.value.isNotEmpty() && motivoParada.value.isNotEmpty()
        Spacer(modifier = Modifier.weight(1f))
        BotonParada(
            maquinaId,
            areaId,
            ordenMezcla,
            comentarios,
            navController,
            id,
            paradaViewModel,
            stateBoton,
            motivoId,
            buttonHeight,
            padding_res,
            bodyFontSize
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BotonParada(
    maquinaId: String,
    areaId: String,
    ordenMezcla: String,
    comentarios: String,
    navController: NavController,
    id: String,
    paradaViewModel: ParadaViewModel,
    stateBoton: Boolean,
    motivoId: String,
    buttonHeight: Dp,
    padding_res: Dp,
    bodyFontSize: Float,
) {
    var showDialog by remember { mutableStateOf(false) }
    val estado by paradaViewModel.estadoParada.collectAsState()
    val paradaState by paradaViewModel.paradas.collectAsState()
    var stateButton = stateBoton

    Button(
        enabled = stateButton,
        onClick = {
            val newFecha = LocalDateTime.now()
            paradaViewModel.registrarParada(
                ParadaRequest(
                    formatoServidor(newFecha),
                    maquinaId,
                    areaId,
                    ordenMezcla,
                    comentarios,
                    "Y",
                    formatoServidor(newFecha),
                    //"",
                    formatoHora(newFecha),
                    //"",
                    id,
                    motivoId
                )
            )
            showDialog = true
        },
        modifier = Modifier
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
        Text(text = "Registrar Parada", fontSize = bodyFontSize.sp )
    }

    if (showDialog) {
        when (estado) {
            EstadoParada.Cargando -> {
                CustomAlertDialog(
                    showDialog = showDialog,
                    title = "Cargando",
                    message = "Espere por favor...",
                    confirmButtonText = "",
                    dismissButtonText = null,
                    onDismiss = { showDialog = false },
                    dialogType = DialogType.LOADING,
                )
            }

            EstadoParada.Exitoso -> {
                CustomAlertDialog(
                    showDialog = showDialog,
                    title = "Éxito",
                    message = paradaState.paradaResponse.data,
                    confirmButtonText = "OK",
                    dismissButtonText = null,
                    onConfirm = { paradaViewModel.actualizarEstadoParada(EstadoParada.Idle) },
                    onDismiss = {
                        showDialog = false
                        paradaViewModel.actualizarEstadoParada(EstadoParada.Idle)
                        navController.navigate("listaParada/$id")
                    },
                    dialogType = DialogType.SUCCESS,
                )
            }

            is EstadoParada.Error -> {
                CustomAlertDialog(
                    showDialog = showDialog,
                    title = "Error",
                    message = (estado as EstadoParada.Error).mensaje,
                    confirmButtonText = "OK",
                    dismissButtonText = null,
                    onConfirm = { paradaViewModel.actualizarEstadoParada(EstadoParada.Idle) },
                    onDismiss = {
                        showDialog = false
                        paradaViewModel.actualizarEstadoParada(EstadoParada.Idle)
                    },
                    dialogType = DialogType.ERROR,
                )
            }
            else -> {}
        }
    }
}