package com.vistony.app.Screen.ParadaMantenimiento

import android.app.Activity
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.vistony.app.Entidad.Actividad
import com.vistony.app.Entidad.UserState
import com.vistony.app.Extras.randomMutedColor
import com.vistony.app.Screen.Generic.CustomOutlinedTextField
import com.vistony.app.Screen.Generic.DateOutlinedTextField
import com.vistony.app.Screen.Generic.GenericDropdownMenu2
import com.vistony.app.ViewModel.ActividadViewModel
import com.vistony.app.ViewModel.OTViewModel
import com.vistony.app.ui.theme.theme.Dimensions

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BodyActividad(
    userState: UserState,
    navController: NavController,
    viewModel: ActividadViewModel, // = hiltViewModel(),0
    otViewModel: OTViewModel = hiltViewModel(),
    onClose: () -> Unit,
) {
    // ======================= VARIABLES DE CONFIGURATION =======================
    val context = LocalContext.current
    val activity = context as Activity
    val windowSize = calculateWindowSizeClass(context)
    val padding_res = Dimensions.getPadding(windowSize.widthSizeClass)
    val buttonHeight = Dimensions.getButtonHeight(windowSize.widthSizeClass)
    val bodyFontSize = Dimensions.getBodyFontSize(windowSize.widthSizeClass)

    // ============================ ESTADO GENERAL DE LA UI ===========================
    val uiState by viewModel.uiState.collectAsState()

    // ============================ VARIABLES DE SCREEN ============================
    val images = remember { mutableStateListOf<Uri>() }
    var new_ot by rememberSaveable { mutableStateOf("") }
    val otraMaquina = rememberSaveable { mutableStateOf("") }
    val otroEquipo = rememberSaveable { mutableStateOf("") }

    // ============================ LISTAS Y VARIABLES DE CONTROL  ============================

    // ============================ ESTADO DEL BOTON  ============================
    val stateButton = true

    // ============================ SCANEADO DE OT ============================
    val scanLauncher = rememberLauncherForActivityResult(
        contract = ScanContract(),
        onResult = { reslt ->
            //ot = reslt.contents ?: "Sin Lectura"
            val scannedText = reslt.contents ?: "Sin Lectura"
            val startIndex = scannedText.indexOf("(10)") + 4
            val endIndex = scannedText.indexOf("(17)")
            if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
                new_ot = scannedText.substring(startIndex, endIndex)
                viewModel.onOTChange(new_ot)
            } else {
                //ot = "Codigo de barra, no válido"
                viewModel.onOTChange("Codigo de barra, no válido")
            }

        }
    )
    val currentOt by rememberUpdatedState(uiState.selectedActividad.OT)
    LaunchedEffect(currentOt) {
        if (currentOt.isNotEmpty()) {
            otViewModel.getCodigoBarra(currentOt)

            //um = otState.productoResponse?.data?.UM.toString()
            //description = otState.productoResponse?.data?.Producto.toString()
            //linea = otState.productoResponse?.data?.Linea.toString()
            //newLinea.value = lineaState.lineaResponse?.data?.find { it.ID == linea }?.Descripcion ?: newLinea.value
            Log.i("VER", "ENTRO AL NO VACIO")
        } else {
            //7um = ""
            //description = ""
            //linea = ""
            Log.i("VER", "ENTRO AL VACIO")
        }
    }
    //(01)1110001100303(10)250008213(17)280601


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize()
            .verticalScroll(rememberScrollState())
    ) {

        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = "Registro de Actividad",
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
        )
        Text(
            text = "Registro de mantenimiento correctivo y ocurrencias de fallas. Complete el formulario para registrar una actividad",
            fontSize = 12.sp,
            lineHeight = 14.sp,
        )
        Spacer(Modifier.height(25.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            DateOutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                "Fecha Inicio",
                readonly = true,
                selectedDate = uiState.selectedActividad.startTime,
                onDateChange = viewModel::onStartTimeChange,
                showDialog = uiState.showDialogDateIni,
                onShowDialogChange = viewModel::onShowDialogDateIniChange
            )
        }

        Spacer(Modifier.height(8.dp))
        CustomOutlinedTextField( // ---- OT ----
            value = uiState.selectedActividad.OT,
            onValueChange = viewModel::onOTChange,
            label = "OT",
            trailingIcon = {
                IconButton(onClick = { scanLauncher.launch(ScanOptions()) }
                ) {
                    Icon(Icons.Filled.CameraAlt, contentDescription = "Camera")
                }
            },
            keyboardOption = KeyboardOptions().copy(keyboardType = KeyboardType.Number),
            readOnly = false
        )
        Spacer(Modifier.height(8.dp))
        GenericDropdownMenu2(
            label = "Area",
            options = uiState.listAreas,
            selectedOption = uiState.selectedActividad.area,
            onOptionSelected = {
                viewModel.onAreaChange(it)
                viewModel.getMachinesByLine(it)
                viewModel.getEquipmentByLine(it)
            },
            optionToText = { it },
            expanded = uiState.expandedArea,
            onExpandedChange = viewModel::onExpandedAreaChange
        )
        Spacer(Modifier.height(8.dp))
        GenericDropdownMenu2(
            label = "Máquina",
            options = uiState.listMaquinas,
            selectedOption = uiState.selectedActividad.machine,
            onOptionSelected = viewModel::onMachineChange,
            optionToText = { it.name },
            expanded = uiState.expandedMaquina,
            onExpandedChange = viewModel::onExpandedMaquinaChange
        )
0
        if(uiState.selectedActividad.machine.name == "Otro"){
            Spacer(Modifier.height(8.dp))
            CustomOutlinedTextField(
                value = otraMaquina.value,
                onValueChange = {otraMaquina.value = it},
                label = "Otra máquina",
                readOnly = false
            )

        }
        Spacer(Modifier.height(8.dp))
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = "Equipo",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
        )
        Spacer(Modifier.height(8.dp))
        GenericDropdownMenu2(
            label = "Equipo",
            options = uiState.listEquipos,
            selectedOption = uiState.selectedActividad.equipment,
            onOptionSelected = viewModel::onEquipamentChange,
            optionToText = { it.name },
            expanded = uiState.expandedEquipo,
            onExpandedChange = viewModel::onExpandedEquipoChange
        )
        Spacer(Modifier.height(8.dp))
        if(uiState.selectedActividad.equipment.name == "Otro"){
            Spacer(Modifier.height(8.dp))
            CustomOutlinedTextField(
                value = otroEquipo.value,
                onValueChange = {otroEquipo.value = it},
                label = "Otro equipo",
                readOnly = false
            )

        }
        Spacer(Modifier.height(8.dp))


        Spacer(modifier = Modifier.height(32.dp)) // En caso d eque no haya espacio suficiente
        Spacer(modifier = Modifier.weight(1f))
        Button(
            enabled = stateButton,
            onClick = {
                viewModel.onInitialChange(userState.currentUser)
                viewModel.crearActividad(otraMaquina, otroEquipo)
                onClose()
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

            Text(text = "Crear actividad", fontSize = bodyFontSize.sp)
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

/*Actividad(
                        id = "",
                        equipo = equipo.value,
                        tipoFalla = tipoFalla,
                        descripcionActividad = descripcionFalla,
                        causaParada = causaFalla,
                        fechaInicio = activityDate,
                        listaImagenes = images,
                        statusActividad = false,
                        tecnico = "1",
                        paradaDocEntry = ""
                    )*/

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TarjetaActividad(
    actividad: Actividad,
    actividadViewModel: ActividadViewModel,
    id: String,
    function: () -> Unit,
) {

    Log.i("MDCR", "actividad: $actividad")
    val actividades by actividadViewModel.actividades.collectAsState()
    var approved by remember { mutableStateOf(actividad.statusActividad) } // Estado persistente
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()


    LaunchedEffect(isPressed) {
        if (isPressed) {
            actividadViewModel.setStatusActividad(actividades.indexOf(actividad))
            approved = !approved
        }
    }



    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = rememberRipple(), // o rememberRipple() si quieres efecto visual
                onClick = {
                    actividadViewModel.setStatusActividad(actividades.indexOf(actividad))
                    //approved = !approved
                }
            )
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        //border = BorderStroke(0.5.dp, if (approved) Color(0xFF4CAF50) else Color.Transparent),
        elevation = 0.dp,
        backgroundColor = Color.Transparent //if (approved) Color(0xFFE8F5E9) else Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    //.weight(0.7f)
                    .size(75.dp)
                    .clip(if (approved) CircleShape else RoundedCornerShape(0.dp))
                    .background(if (approved) Color(0xFF4CAF50) else randomMutedColor()),
                contentAlignment = Alignment.Center
            ) {
                if (approved) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Aprobado",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                } else {
                    val palabras = actividad.equipo.split(" ")
                    val cadena = if (palabras.size > 1) palabras[1] else palabras[0]
                    Text(
                        text = cadena.take(1).uppercase(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .weight(3f)
                    .padding(vertical = 0.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = actividad.equipo,
                    fontSize = 12.sp,
                    lineHeight = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (approved) Color(0xFF2E7D32) else Color(0xFF1B2733)
                )
                Text(
                    text = actividad.descripcionActividad,
                    fontSize = 12.sp,
                    color = if (approved) Color(0xFF4CAF50) else Color(0xFF455A64),
                    maxLines = 2,
                    lineHeight = 14.sp,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 2.dp)
                )
                Spacer(modifier = Modifier.height(32.dp))
                Divider(
                    color = if (approved) Color(0xFFC8E6C9) else Color(0xFFE0E0E0),
                    thickness = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun EquipoCard(
    maquina: String?,
    modifier: Modifier = Modifier,
    primaryColor: Color = Color(0xFFFC6A68)
) {
    Column {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(14.dp),
            color = Color(0xFFF7F7F7),
            //elevation = 4.dp,
            //border = BorderStroke(1.dp, Color.LightGray)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Ícono representativo: Usamos un icono de fábrica o engranaje industrial
                Icon(
                    imageVector = Icons.Default.Settings, // engranaje
                    contentDescription = "Ícono de máquina",
                    tint = primaryColor,
                    modifier = Modifier.size(32.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = maquina?.takeIf { it.isNotBlank() } ?: "No especificado",
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}


// MODO DE EJEMPLO DE COMO UTLIZAR UN TEXTFIELD CON EL UISTATE
/*CustomOutlinedTextField(
    value = uiState.selectedActividad.area,
    onValueChange = viewModel::onAreaChange,
    label = "Area",
    readOnly = false
)*/

// COMPONENTE DE COMO UTLIZAR LA VERSION ANTERIOR CON PAIR(1 to "Nombre")}
/*GenericDropdownMenu(
    label = "Equipo",
    options = listaEquipos,
    selectedValue = equipo.value,
    onValueChange = { equipo.value = it },
    onCodeChange = { equipoId = it },
    expanded = expandedEquipo.value,
    onExpandedChange = { expandedEquipo.value = it }
)*/

// TIPO DE FALLA OPCIONAL EN UN SPINNER, REEMPLAZANDO A GRAFICO
/*GenericDropdownMenu2(
    label = "Motivo de parada",
    options = uiState.listMotivos,
    selectedOption = uiState.selectedActividad.reason,
    onOptionSelected = viewModel::onReasonChange,
    optionToText = { it.name },
    expanded = uiState.expandedMotivo,
    onExpandedChange = viewModel::onExpandedMotivoChange
)
Spacer(Modifier.height(8.dp))*/