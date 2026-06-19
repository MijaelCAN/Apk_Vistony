package com.vistony.app.Screen.muestraProduccion

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vistony.app.Entidad.ConsultaNuevaMuestra
import com.vistony.app.Entidad.UserResponse
import com.vistony.app.ViewModel.MuestraViewModel
import com.vistony.app.ui.theme.theme.AppTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevaMuestraScreen(
    modifier: Modifier = Modifier,
    numOf: String? = null,
    numEn: String? = null,
    type: String? = null,
    currentUser: UserResponse = UserResponse(),
    muestraViewModel: MuestraViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onEnviarClick: () -> Unit = {},
    onCancelarClick: () -> Unit = {}
) {
    val opciones by muestraViewModel.consultaNuevaMuestra.collectAsState()
    val muestraRegistrada by muestraViewModel.muestraRegistrada.collectAsState()
    val isLoading by muestraViewModel.isLoading.collectAsState()
    val isCreating by muestraViewModel.isCreating.collectAsState()
    val errorMessage by muestraViewModel.errorMessage.collectAsState()
    val muestrasProduccion by muestraViewModel.muestrasProduccion.collectAsState()
    val pendientesCount = muestrasProduccion.count { it.status == "PENDIENTE" }

    var entrega by remember { mutableStateOf("") }
    var observacion by remember { mutableStateOf("") }
    var ordenBuscada by remember { mutableStateOf(numOf ?: "") }
    var envaseSeleccionado by remember { mutableStateOf<ConsultaNuevaMuestra?>(null) }
    var envaseBloqueadoMensaje by remember { mutableStateOf<String?>(null) }

    val tieneOrdenPrecargada = !numOf.isNullOrBlank() && !type.isNullOrBlank()

    LaunchedEffect(Unit) {
        // Limpia cualquier dato/estado de una visita anterior a esta pantalla
        muestraViewModel.limpiarConsultaNuevaMuestra()

        if (tieneOrdenPrecargada) {
            muestraViewModel.consultarNuevaMuestra(numOf!!, numEn, type!!)
        }
    }

    LaunchedEffect(opciones) {
        // Si solo hay un envase, se selecciona automáticamente (salvo que ya tenga un intento
        // pendiente, en cuyo caso se bloquea). Si hay varios, el usuario debe elegir.
        val unico = opciones.singleOrNull()
        when {
            unico == null -> {
                envaseSeleccionado = null
                envaseBloqueadoMensaje = null
            }
            unico.tieneIntentoPendiente -> {
                envaseSeleccionado = null
                envaseBloqueadoMensaje = unico.mensajeIntentoPendiente
            }
            else -> {
                envaseSeleccionado = unico
                envaseBloqueadoMensaje = null
            }
        }
    }

    LaunchedEffect(muestraRegistrada) {
        if (muestraRegistrada != null) {
            onEnviarClick()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Text(
                        text = "Nueva muestra",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.weight(1f)
                    )
                    BadgedBox(
                        badge = {
                            Badge(
                                containerColor = if (pendientesCount > 0) Color(0xFFD32F2F) else Color.Transparent,
                                contentColor = Color.White,
                                modifier = Modifier.offset(x = (-4).dp, y = 4.dp)
                            ) {
                                Text("$pendientesCount")
                            }
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .border(1.dp, Color.LightGray, CircleShape)
                                .clickable { onNotificationClick() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = null,
                                tint = if(pendientesCount > 0)Color(0xFFFBC02D) else Color.LightGray,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
                HorizontalDivider(color = Color.Black, thickness = 1.dp)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            val datos = envaseSeleccionado

            if (!tieneOrdenPrecargada) {
                // Abierto desde el FAB: el usuario debe ingresar la OF y buscarla
                Label(text = "ORDEN DE FABRICACIÓN")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomTextField(
                        value = ordenBuscada,
                        onValueChange = { ordenBuscada = it },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                        )
                    )
                    Surface(
                        modifier = Modifier
                            .height(56.dp)
                            .clickable(enabled = ordenBuscada.isNotBlank() && !isLoading) {
                                muestraViewModel.consultarNuevaMuestra(ordenBuscada.trim(), null, "MEZCLA")
                            },
                        color = Color(0xFF212121),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Box(
                            modifier = Modifier.padding(horizontal = 20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (isLoading) "Buscando..." else "Buscar",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                            )
                        }
                    }
                }

                if (errorMessage != null && opciones.isEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = errorMessage ?: "",
                        color = Color(0xFF9E4B4B),
                        fontSize = 14.sp
                    )
                }

                if (opciones.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            if (isLoading && opciones.isEmpty() && tieneOrdenPrecargada) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (opciones.isEmpty() && tieneOrdenPrecargada) {
                Text(
                    text = errorMessage ?: "No se pudo cargar la información de la muestra",
                    color = Color(0xFF9E4B4B),
                    fontSize = 14.sp
                )
            } else if (datos == null && opciones.size == 1) {
                // El único envase ya tiene un intento pendiente: no se puede registrar otro
                Text(
                    text = envaseBloqueadoMensaje ?: "No se puede registrar una nueva muestra para este envase",
                    color = Color(0xFF9E4B4B),
                    fontSize = 14.sp
                )
            } else if (datos == null && opciones.size > 1) {
                // Hay varios envases (OE) bajo esta OF: el usuario debe elegir cuál registrar
                Label(text = "SELECCIONA EL ENVASE")

                if (envaseBloqueadoMensaje != null) {
                    Text(
                        text = envaseBloqueadoMensaje ?: "",
                        color = Color(0xFF9E4B4B),
                        fontSize = 13.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    opciones.forEach { opcion ->
                        EnvaseOptionCard(
                            opcion = opcion,
                            onClick = {
                                if (opcion.tieneIntentoPendiente) {
                                    envaseBloqueadoMensaje = "Envase ${opcion.numEn}: ${opcion.mensajeIntentoPendiente}"
                                } else {
                                    envaseBloqueadoMensaje = null
                                    envaseSeleccionado = opcion
                                }
                            }
                        )
                    }
                }
            } else if (datos != null) {
                // Summary Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E0E0).copy(alpha = 0.5f)),
                    border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row {
                            Text(
                                text = "OF",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                            Text(
                                text = " ${datos.numOf} · ${datos.descripcion}",
                                fontSize = 16.sp,
                                color = Color.DarkGray
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Lote ${datos.lote}",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Form Fields (Tipo / N° intento / Versión vienen calculados por el servidor)
                Label(text = "TIPO")
                CustomTextField(
                    value = datos.type,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        Label(text = "N° INTENTO")
                        CustomTextField(
                            value = datos.counterSiguiente?.toString() ?: "-",
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Label(text = "VERSIÓN")
                        CustomTextField(
                            value = datos.versionSiguiente ?: "-",
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Label(text = "FECHA Y HORA")
                CustomTextField(
                    value = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Label(text = "ENTREGA")
                CustomTextField(
                    value = entrega,
                    onValueChange = { entrega = it },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Label(text = "OBSERVACIÓN (OPCIONAL)")
                CustomTextField(
                    value = observacion,
                    onValueChange = { observacion = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    singleLine = false,
                    placeholder = "Ej. ajuste de espesante 1.2%..."
                )

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = errorMessage ?: "",
                        color = Color(0xFF9E4B4B),
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Action Buttons
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .clickable(enabled = !isCreating) {
                            muestraViewModel.crearMuestraProduccion(datos.numOf, datos.numEn, datos.type, currentUser.dni)
                        },
                    color = if (isCreating) Color.Gray else Color(0xFF212121),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = if (isCreating) "Enviando..." else "Enviar a Laboratorio",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .clickable { onCancelarClick() }
                        .drawBehind {
                            val stroke = Stroke(
                                width = 1.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                            )
                            drawRoundRect(
                                color = Color.Gray,
                                style = stroke,
                                cornerRadius = CornerRadius(12.dp.toPx())
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Cancelar",
                        color = Color.Black,
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun EnvaseOptionCard(opcion: ConsultaNuevaMuestra, onClick: () -> Unit) {
    val bloqueado = opcion.tieneIntentoPendiente
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = if (bloqueado) Color(0xFFF5F5F5) else Color.White),
        border = BorderStroke(1.dp, if (bloqueado) Color.Gray else Color.Black)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = opcion.descripcion,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = if (bloqueado) Color.Gray else Color.Black
                )
                if (bloqueado) {
                    Text(
                        text = opcion.ultimoIntento?.status ?: "PENDIENTE",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF8B7A4D)
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Lote ${opcion.lote} · OE ${opcion.numEn ?: "-"}",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun Label(text: String) {
    Text(
        text = text,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Gray,
        modifier = Modifier.padding(bottom = 6.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    readOnly: Boolean = false,
    placeholder: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        singleLine = singleLine,
        readOnly = readOnly,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = Color.Gray.copy(alpha = 0.8f),
            cursorColor = Color.Black
        ),
        placeholder = placeholder?.let { { Text(text = it, color = Color.LightGray) } }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
private fun NuevaMuestraScreenPreview() {
    AppTheme {
        NuevaMuestraScreen()
    }
}
