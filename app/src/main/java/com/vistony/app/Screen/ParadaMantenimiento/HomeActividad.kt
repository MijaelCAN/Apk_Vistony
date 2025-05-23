package com.vistony.app.Screen.ParadaMantenimiento

import android.app.Activity
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.sharp.PersonalInjury
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.vistony.app.Entidad.Actividad
import com.vistony.app.Entidad.Parada
import com.vistony.app.Extras.randomMutedColor
import com.vistony.app.Screen.Generic.CustomOutlinedTextField
import com.vistony.app.Screen.Generic.FilterBoxsRow
import com.vistony.app.Screen.Generic.Images.ImagePickerExample
import com.vistony.app.ViewModel.ActividadViewModel
import com.vistony.app.ViewModel.ParadaViewModel
import com.vistony.app.ui.theme.theme.Dimensions
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BodyActividad(
    parada: Parada?,
    navController: NavController,
    paradaViewModel: ParadaViewModel,
    actividadViewModel: ActividadViewModel,
    id: String,
    onClose: () -> Unit,
) {

    val context = LocalContext.current
    val activity = context as Activity
    val windowSize = calculateWindowSizeClass(context)

    val padding_res = Dimensions.getPadding(windowSize.widthSizeClass)
    val buttonHeight = Dimensions.getButtonHeight(windowSize.widthSizeClass)
    val bodyFontSize = Dimensions.getBodyFontSize(windowSize.widthSizeClass)

    val images = remember { mutableStateListOf<Uri>() }
    val newfecha by remember { mutableStateOf(SimpleDateFormat("yyyyMMdd").format(Date())) }

    var maquina = remember { mutableStateOf("") }
    var maquinaId by remember { mutableStateOf("") }
    val expandedMaquina = remember { mutableStateOf(false) }
    val maquinaState by paradaViewModel.maquinas.collectAsState()
    val listMaquinas = maquinaState.maquinaResponse.data.map { it.Code to it.Name }
    val options = listOf("Mecanica", "Electrica", "Operativa")
    val optionsFalla = listOf(
        "Mecanica" to Icons.Default.Settings,
        "Electrica" to Icons.Default.ElectricBolt,
        "Operativa" to Icons.Sharp.PersonalInjury
    )
    var selected by remember { mutableStateOf("Mecanica") }
    var descripcionFalla by remember { mutableStateOf("") }
    var causaFalla by remember { mutableStateOf("") }

    val stateButton = selected.isNotEmpty() && descripcionFalla.isNotEmpty() && causaFalla.isNotEmpty()


    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
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

        /*Text(
            modifier = Modifier.padding(top = 16.dp),
            text = "Equipo",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
        )
        Spacer(Modifier.height(8.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(4.dp),
            shape = RoundedCornerShape(12.dp),
            backgroundColor = Color(0XFFF7F7F7),
            border = BorderStroke(1.dp, color = Color.LightGray),
            elevation = 0.dp,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Computer,
                    contentDescription = "e",
                    tint = Color.LightGray,
                )
                Spacer(Modifier.width(10.dp))
                if (parada != null) {
                    Text(text = parada.Maquina ?: "-")
                }
            }
        }*/
        EquipoCard(
            maquina = parada?.Maquina,
            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
        )


        /*GenericDropdownMenu(
            label = "Máquina",
            options = listMaquinas,
            selectedValue = maquina.value,
            onValueChange = { maquina.value = it },
            onCodeChange = { maquinaId = it },
            expanded = expandedMaquina.value,
            onExpandedChange = { expandedMaquina.value = it }
        )*/
        Spacer(Modifier.height(8.dp))
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = "Tipo de Falla",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
        )
        Spacer(Modifier.height(8.dp))
        FilterBoxsRow(
            options = optionsFalla,
            selectedOption = selected,
            onOptionSelected = { selected = it },
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        )
        Spacer(Modifier.height(8.dp))
        CustomOutlinedTextField(
            value = descripcionFalla,
            onValueChange = { descripcionFalla = it },
            label = "Descripción del Problema",
            readOnly = false,
            minLines = 3,
            maxLines = 3
        )
        Spacer(Modifier.height(8.dp))
        CustomOutlinedTextField(
            value = causaFalla,
            onValueChange = { causaFalla = it },
            label = "Causa de la parada",
            readOnly = false,
            minLines = 3,
            maxLines = 3
        )
        Spacer(Modifier.height(8.dp))
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = "Imagenes",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
        )
        Spacer(Modifier.height(8.dp))
        /*ImagePickerRow(
            images = images,
            itemSize = 90.dp,
            onAddClick = {
                // Aquí abres galería o cámara y agregas la imagen a la lista
                // Por ejemplo, simular agregando una imagen dummy:
                // images = images + someImageBitmap
            }
        )*/
        ImagePickerExample(images)

        Spacer(modifier = Modifier.weight(1f))
        Button(
            enabled = stateButton,
            onClick = {
                Log.e("MDCR", "CANTIDAD DE FOTOS: ${images.size}")
                actividadViewModel.crearActividad(
                    Actividad(
                        id = "",
                        maquina = parada?.Maquina ?: "",
                        tipoFalla = selected,
                        descripcionActividad = descripcionFalla,
                        causaParada = causaFalla,
                        fecha = newfecha,
                        listaImagenes = images,
                        statusActividad = false,
                        usuario = "1",
                        paradaDocEntry = parada?.DocEntry ?: ""
                    )
                )
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
            //Text(text = "Cerrar actividad y genear PDF", fontSize = bodyFontSize.sp)
            Text(text = "Crear actividad", fontSize = bodyFontSize.sp)
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TarjetaActividad(
    actividad: Actividad,
    actividadViewModel: ActividadViewModel,
    //navController: NavController,
    id: String,
    function: () -> Unit,
) {

    /*val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val backgroundColor = if (isPressed) Color.Red else Color.Transparent*/

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
                    //actividadViewModel.setStatusActividad(actividades.indexOf(actividad))
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
                    val cadena = actividad.maquina.split(" ")[1]
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
                    text = actividad.maquina,
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
        Text(
            text = "Equipo",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
        )
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
