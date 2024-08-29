package com.vistony.app.Screen.Generic

import android.service.autofill.OnClickAction
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vistony.app.Entidad.Parada

@Composable
fun TableScreen(data: List<Parada>, onClickAction: () -> Unit) {
    // Just a fake data... a Pair of Int and String
    val tableData = (1..10).mapIndexed { index, item ->
        index to "Item $index"
    }
    // Each cell of a column must have the same weight.
    val column1Weight = .3f // 30%
    val column2Weight = .7f // 70%
    // The LazyColumn will be our table. Notice the use of the weights below
    LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {
        // Here is the header
        item {
            Row(Modifier.background(Color.Gray)) {
                TableCell(text = "ID", weight = column1Weight)
                TableCell(text = "Maquina", weight = column2Weight)
                TableCell(text = "Fecha Inicio", weight = column2Weight)
                TableCell(text = "Fecha Final", weight = column2Weight)
                TableCell(text = "Detalle", weight = column2Weight)
                TableCell(text = "Finalizar", weight = column2Weight)
            }
        }
        // Here are all the lines of your table.
        items(data) {it->
            //val (id, text) = it
            Row(Modifier.fillMaxWidth()) {
                TableCell(text = it.DocEntry.toString(), weight = column1Weight)
                TableCell(text = it.Maquina, weight = column2Weight)
                TableCell(text = it.FechaHoraInicio, weight = column2Weight)
                TableCell(text = it.FechaHoraFin, weight = column2Weight)
                TableCell(weight = column2Weight){
                    TextButton(
                        modifier = Modifier.width(100.dp),
                        onClick = onClickAction,
                        /*colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF0B4FAF),
                            contentColor = Color.White,
                            disabledContainerColor = Color.Gray,
                            disabledContentColor = Color.White
                        )*/
                    ) {
                        Text("Ver")
                    }
                }
            }
        }
    }
}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float
) {
    Text(
        text = text,
        Modifier
            .border(1.dp, Color.Black)
            .weight(weight)
            .padding(8.dp)
    )
}

@Composable
fun RowScope.TableCell(
    weight: Float,
    content: @Composable () -> Unit
) {
    Box(
        modifier =Modifier
            .border(1.dp, Color.Black)
            .weight(weight)
            .padding(8.dp)
    ) {
        content()
    }
}