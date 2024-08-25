package com.vistony.app.ui.theme.Screen.Parada

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.vistony.app.R
import com.vistony.app.ui.theme.Screen.Generic.CustomSpinner
import com.vistony.app.ui.theme.Screen.Generic.TopBar
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeParada(navController: NavController) {
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
                    .clip(RoundedCornerShape(25.dp))
                    .background(Color.White)
            ) {
                Image(
                    modifier = Modifier.size(width = 600.dp, height = 200.dp),
                    painter = painterResource(id = R.drawable.vistony),
                    contentDescription = "Logo"
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            BodyParada(navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BodyParada(navController: NavController) {

    var fechaParada by remember { mutableStateOf(SimpleDateFormat("dd/MM/yyy").format(Date())) }
    var maquina = remember { mutableStateOf("") }
    var area by remember { mutableStateOf("") }
    var motivoParada by remember { mutableStateOf("") }
    var comentarios by remember { mutableStateOf("") }

    val expanded = remember { mutableStateOf(false) }
    var opciones = listOf<String>("Maquina 1","Maquina 2", "Maquina 3")
    var textFieldSize by remember { mutableStateOf(Size.Zero) }

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
                .padding(vertical = 16.dp, horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "PARADA DE MÁQUINA",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp, top = 16.dp),
                style = TextStyle(color = Color.Black, fontSize = 22.sp, textAlign = TextAlign.Center)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp)
            ) {
                Text(
                    text = "Fecha",
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 16.dp, top = 16.dp),
                    style = TextStyle(
                        color = Color.Black,
                        textAlign = TextAlign.Center    ,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    ),
                )
                TextField(
                    modifier = Modifier
                        .width(100.dp)
                        .weight(0.5f),
                    value = fechaParada,
                    onValueChange = { fechaParada = it },
                    readOnly = true,
                    maxLines = 1,
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0xFFD3CECE),
                        focusedIndicatorColor = Color.Transparent,

                    )
                )
                Spacer(modifier = Modifier
                    .width(150.dp)
                    .weight(1f))

            }

            //----------- FILA 2 -----------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Máquina ",
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(end = 32.dp, top = 16.dp),
                    style = TextStyle(
                        color = Color.Black,
                        textAlign = TextAlign.Center    ,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    ),
                )
                CustomSpinner(expanded = expanded, options = opciones, operador = maquina, textFieldSize = textFieldSize)
                /*TextField(
                    modifier = Modifier.weight(1f),
                    value = maquina,
                    onValueChange = { maquina = it },
                    readOnly = false,
                    maxLines = 1,
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0xFFD3CECE),
                        focusedIndicatorColor = Color.Transparent
                    )
                )*/

            }
            //----------- FILA 3 -----------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Área ",
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(end = 32.dp, top = 16.dp),
                    style = TextStyle(
                        color = Color.Black,
                        textAlign = TextAlign.Center    ,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    ),
                )
                TextField(
                    modifier = Modifier.weight(1f),
                    value = area,
                    onValueChange = { area = it },
                    readOnly = false,
                    maxLines = 1,
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0xFFD3CECE),
                        focusedIndicatorColor = Color.Transparent
                    )
                )

            }
            //----------- FILA 4 -----------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Motivo Parada ",
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(end = 32.dp, top = 16.dp),
                    style = TextStyle(
                        color = Color.Black,
                        textAlign = TextAlign.Center    ,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    ),
                )
                TextField(
                    modifier = Modifier.weight(1f),
                    value = motivoParada,
                    onValueChange = { motivoParada = it },
                    readOnly = false,
                    maxLines = 1,
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0xFFD3CECE),
                        focusedIndicatorColor = Color.Transparent
                    )
                )

            }
            //----------- FILA 5 -----------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Comentarios ",
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(end = 32.dp, top = 16.dp),
                    style = TextStyle(
                        color = Color.Black,
                        textAlign = TextAlign.Center    ,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    ),
                )
                TextField(
                    modifier = Modifier.weight(1f),
                    value = comentarios,
                    onValueChange = { comentarios = it },
                    readOnly = false,
                    maxLines = 5    ,
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0xFFD3CECE),
                        focusedIndicatorColor = Color.Transparent
                    )
                )

            }
            BotonParada(maquina.value,area,motivoParada,comentarios)
        }
    }
}

@Composable
fun BotonParada(maquina: String, area: String, motivoParada: String, comentarios: String) {

    var stateButton = maquina.isNotEmpty() && area.isNotEmpty() && motivoParada.isNotEmpty() && comentarios.isNotEmpty()

    val buttonColors = ButtonDefaults.elevatedButtonColors(
        containerColor = if (stateButton) Color(0xFF0054A3) else Color(0XFF9C9B9B),
        contentColor = if (stateButton) Color(0xFFC9C9C9) else Color.White,
        disabledContentColor = Color.White,
        disabledContainerColor = Color(0XFF9C9B9B)
    )

    ElevatedButton(
        modifier = Modifier
            .width(300.dp)
            .height(58.dp),
        enabled = stateButton,
        onClick = {
        },
        colors = buttonColors,
        shape = RoundedCornerShape(18.dp)
    ) {
        Text(text = "Guardar", style = TextStyle(fontSize = 20.sp))
    }
}