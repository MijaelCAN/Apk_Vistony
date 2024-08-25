package com.vistony.app.ui.theme.Screen.Parada

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun ListInspeccion() {
    var id by remember { mutableStateOf("") }
    var maquina by remember { mutableStateOf("") }
    var fechaHoraInicio by remember { mutableStateOf("") }
    var fechaHoraFin by remember { mutableStateOf("09/08/2024") }
    var detalle by remember { mutableStateOf(false) }
    var finalizar by remember { mutableStateOf(false) }


    Row(
        modifier = Modifier.fillMaxWidth()
    ){
        Text(text = id)
        Text(text = maquina)
        Text(text = fechaHoraInicio)
        Text(text = fechaHoraFin)
        ElevatedButton(onClick = { detalle = true }) {
            Text(text = "Visualizar")
        }
        OutlinedButton(onClick = { finalizar = true }) {
            Text(text = "Detener")
        }
        
    }
}