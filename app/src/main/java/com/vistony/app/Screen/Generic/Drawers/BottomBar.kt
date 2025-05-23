package com.vistony.app.Screen.Generic.Drawers

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vistony.app.Entidad.ListaRequest
import com.vistony.app.Screen.Generic.DateOutlinedTextField
import com.vistony.app.ViewModel.InspectionViewModel
import com.vistony.app.ViewModel.ParadaViewModel
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomBar(
    type : String,
    paradaViewModel: ParadaViewModel = hiltViewModel(),
    inspViewModel: InspectionViewModel = hiltViewModel()
) {

    var selectedDateIni = paradaViewModel.fechaIni.value.toLocalDate()
    var selectedDateFin = paradaViewModel.fechaFin.value.toLocalDate()

    var showDialogDateIni by remember { mutableStateOf(false) }
    var showDialogDateFin by remember { mutableStateOf(false) }

    LaunchedEffect(selectedDateIni, selectedDateFin) {
        val newfechaIni = selectedDateIni.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        val newfechaFin = selectedDateFin.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        when(type){
            "inspection" -> { inspViewModel.getListInspeccion(newfechaIni, newfechaFin) }
            "parada"->{ paradaViewModel.obtenerParadas(ListaRequest(newfechaIni, newfechaFin, "T")) }
        }

    }


    Column(modifier = Modifier
        .padding(16.dp)
        .fillMaxWidth()) {
        Text(text = "Seleccione un Rango de fechas")
        // Aquí pones las opciones del menú inferior
        Spacer(modifier = Modifier.height(32.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            DateOutlinedTextField(
                modifier = Modifier
                    .padding(start = 8.dp, top = 16.dp, bottom = 8.dp, end = 4.dp)
                    .weight(1f)
                    .fillMaxWidth(),
                "Fecha Inicio",
                readonly = true,
                selectedDate = selectedDateIni,
                onDateChange = {
                    selectedDateIni = it
                    paradaViewModel.setFechaIni(it) },
                showDialog = showDialogDateIni,
                onShowDialogChange = { showDialogDateIni = it }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DateOutlinedTextField(
                modifier = Modifier
                    .padding(start = 4.dp, top = 16.dp, bottom = 8.dp, end = 8.dp)
                    .weight(1f)
                    .fillMaxWidth(),
                "Fecha Final",
                readonly = true,
                selectedDate = selectedDateFin,
                onDateChange = { selectedDateFin = it
                               paradaViewModel.setFechaFin(it)},
                showDialog = showDialogDateFin,
                onShowDialogChange = { showDialogDateFin = it }
            )
        }
    }
}