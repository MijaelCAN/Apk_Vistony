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
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.vistony.app.ui.theme.Screen.Generic.CustomDrawer
import com.vistony.app.ui.theme.Screen.Generic.CustomOutlinedTextField
import com.vistony.app.ui.theme.Screen.Generic.CustomOutlinedTextField2
import com.vistony.app.ui.theme.Screen.Generic.CustomSpinner
import com.vistony.app.ui.theme.Screen.Generic.TopBar
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeParada(navController: NavController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { CustomDrawer(navController = navController) }
    ){
        Scaffold(
            topBar = {
                TopBar("Parada de Máquina", navController = navController, onMenuClick = {
                    scope.launch { drawerState.open() }
                })
            }
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BodyParada(navController: NavController) {

    var fechaParada by remember { mutableStateOf(SimpleDateFormat("dd/MM/yyy").format(Date())) }
    var maquina = remember { mutableStateOf("") }
    var area = remember { mutableStateOf("") }
    var motivoParada = remember { mutableStateOf("") }
    var comentarios by remember { mutableStateOf("") }

    val expandedMaquina = remember { mutableStateOf(false) }
    val expandedArea = remember { mutableStateOf(false) }
    val expandedParada = remember { mutableStateOf(false) }
    var opciones = listOf<String>("Maquina 1", "Maquina 2", "Maquina 3")
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
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center
                )
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
                        .padding(end = 18.dp, top = 16.dp),
                    style = TextStyle(
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    ),
                )
                CustomOutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = fechaParada,
                    label = "Fecha",
                    readOnly = true
                )
                Spacer(
                    modifier = Modifier
                        .width(150.dp)
                        .weight(1f)
                )

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
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    ),
                )
                ExposedDropdownMenuBox(
                    modifier = Modifier.weight(1f),
                    expanded = expandedMaquina.value,
                    onExpandedChange = { expandedMaquina.value = !expandedMaquina.value }) {
                    CustomOutlinedTextField(
                        modifier = Modifier.menuAnchor(),
                        value = maquina.value,
                        onValueChange = {},
                        label = "Máquina",
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMaquina.value) },
                        readOnly = true
                    )
                    //val options2 = operarioState.operarioResponse?.data?.map { it.Nonbre } ?: emptyList()
                    val options = listOf("Maquina 1", "Maquina 2", "Maquina 3")

                    ExposedDropdownMenu(
                        modifier = Modifier
                            .background(Color.White)
                            .clip(RoundedCornerShape(8.dp)),
                        expanded = expandedMaquina.value,
                        onDismissRequest = { expandedMaquina.value = false }) {
                        options.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option, color = Color.Black) },
                                onClick = {
                                    maquina.value = option
                                    expandedMaquina.value = false
                                }
                            )
                        }
                    }
                }
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
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    ),
                )
                ExposedDropdownMenuBox(
                    modifier = Modifier.weight(1f),
                    expanded = expandedArea.value,
                    onExpandedChange = { expandedArea.value = !expandedArea.value }) {
                    CustomOutlinedTextField(
                        modifier = Modifier.menuAnchor(),
                        value = area.value,
                        onValueChange = {},
                        label = "Área",
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedArea.value) },
                        readOnly = true
                    )
                    //val options2 = operarioState.operarioResponse?.data?.map { it.Nonbre } ?: emptyList()
                    val options = listOf("Área 1", "Área 2", "Área 3")

                    ExposedDropdownMenu(
                        modifier = Modifier
                            .background(Color.White)
                            .clip(RoundedCornerShape(8.dp)),
                        expanded = expandedArea.value,
                        onDismissRequest = { expandedArea.value = false }) {
                        options.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option, color = Color.Black) },
                                onClick = {
                                    area.value = option
                                    expandedArea.value = false
                                }
                            )
                        }
                    }
                }

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
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    ),
                )
                ExposedDropdownMenuBox(
                    modifier = Modifier.weight(1f),
                    expanded = expandedParada.value,
                    onExpandedChange = { expandedParada.value = !expandedParada.value }) {
                    CustomOutlinedTextField(
                        modifier = Modifier.menuAnchor(),
                        value = motivoParada.value,
                        onValueChange = {},
                        label = "Motivo Parada",
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedParada.value) },
                        readOnly = true
                    )
                    //val options2 = operarioState.operarioResponse?.data?.map { it.Nonbre } ?: emptyList()
                    val options = listOf("Motivo 1", "Motivo 2", "Motivo 3")

                    ExposedDropdownMenu(
                        modifier = Modifier
                            .background(Color.White)
                            .clip(RoundedCornerShape(8.dp)),
                        expanded = expandedParada.value,
                        onDismissRequest = { expandedParada.value = false }) {
                        options.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option, color = Color.Black) },
                                onClick = {
                                    motivoParada.value = option
                                    expandedParada.value = false
                                }
                            )
                        }
                    }
                }

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
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    ),
                )
                /*TextField(
                    modifier = Modifier.weight(1f),
                    value = comentarios,
                    onValueChange = { comentarios = it },
                    readOnly = false,
                    maxLines = 5,
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0xFFD3CECE),
                        focusedIndicatorColor = Color.Transparent
                    )
                )*/
                CustomOutlinedTextField2(
                    modifier = Modifier
                        .weight(1f)
                        .padding(0.dp),
                    value = comentarios,
                    onValueChange = { comentarios = it },
                    label = "Observacion",
                    readOnly = false
                )

            }
            BotonParada(maquina.value, area.value, motivoParada.value, comentarios)
        }
    }
}

@Composable
fun BotonParada(maquina: String, area: String, motivoParada: String, comentarios: String) {

    var stateButton =
        maquina.isNotEmpty() && area.isNotEmpty() && motivoParada.isNotEmpty() && comentarios.isNotEmpty()

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