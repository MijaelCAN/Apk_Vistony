package com.vistony.app.Screen.Generic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DriveFolderUpload
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.vistony.app.R

@Composable
fun ConfirmationDialog(
    isVisible: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    title: String = "Confirmación",
    message: String = "¿Estás seguro de que deseas continuar?"
) {
    if (isVisible) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = title) },
            text = { Text(text = message) },
            confirmButton = {
                TextButton(onClick = {
                    onConfirm()
                    onDismiss()
                }) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancelar")
                }
            },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        )
    }
}

@Composable
fun SuccessDialog(
    isVisible: Boolean,
    message: String = "Cargando..",
    titulo: String = "Enviando",
    usuario: String = "",
    onDismiss: () -> Unit,
    navController: NavController,
    id: String
) {
    if (isVisible) {
        AlertDialog(
            modifier = Modifier
                .width(400.dp)
                .height(300.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(Color.White),
            onDismissRequest = onDismiss,
            title = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (titulo == "Envio Exitoso") {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Success",
                            tint = Color.Green,
                            modifier = Modifier.size(64.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Filled.DriveFolderUpload,
                            contentDescription = "Cargando",
                            tint = Color.Gray,
                            modifier = Modifier.size(64.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = titulo, fontSize = 20.sp, color = Color.Black)
                }
            },
            text = {
                Text(
                    modifier = Modifier.fillMaxSize(),
                    text = message,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                if (titulo == "Envio Exitoso") {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 48.dp, vertical = 0.dp),
                        onClick = { navController.navigate("listaInsp/$id") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0054A3))
                    ) {
                        Text(text = "VER INSPECCIÓN", color = Color.White)
                    }
                }
            },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            ),
            containerColor = Color.White,
        )
    }
}

@Composable
fun <T> Detalle(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    titulo: String = "Destalles",
    data: T,
    content: @Composable (T) -> Unit,
    navController: NavController = rememberNavController()
) {
    if (isVisible) {
        AlertDialog(
            modifier = Modifier
                .width(700.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(Color.White),
            onDismissRequest = { onDismiss() },
            title = {
                Text(
                    text = titulo,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 8.dp, top = 16.dp),
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                content(data)
            },
            confirmButton = {
                /*Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 100.dp, vertical = 0.dp),
                    onClick = { onDismiss() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0054A3))
                ) {
                    Text(text = "Cerrar", color = Color.White)
                }*/
            },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            ),
            containerColor = Color.Transparent,
        )
    }
}

@Composable
fun CustomAlertDialog(
    showDialog: Boolean,
    title: String,
    message: String,
    confirmButtonText: String = "Confirmar",
    dismissButtonText: String? = "Cancelar",
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit,
    icon: ImageVector? = null,
    dialogType: DialogType = DialogType.DEFAULT
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                if (icon != null) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            icon, contentDescription = null, tint = colorResource(
                                id = when (dialogType) {
                                    DialogType.ERROR -> R.color.error
                                    DialogType.SUCCESS -> R.color.success
                                    DialogType.LOADING -> R.color.loading
                                    DialogType.UPLOAD -> R.color.upload
                                    else -> R.color.black
                                }
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = title)
                    }
                } else {
                    Text(text = title)
                }
            },
            text = {
                when (dialogType) {
                    DialogType.ERROR -> {
                        BodyMessage(message = message)
                    }

                    DialogType.DEFAULT -> {
                        BodyMessage(message = message)
                    }

                    DialogType.SUCCESS -> {
                        BodyMessage(message = message)
                    }

                    DialogType.LOADING -> {
                        BodyMessage(message = message)
                    }

                    DialogType.UPLOAD -> {
                        BodyMessage(Icons.Filled.DriveFolderUpload, message)
                    }
                }
            },
            confirmButton = {
                if (confirmButtonText.isNotEmpty()) {
                    TextButton(onClick = {
                        onConfirm()
                        onDismiss()
                    }) {
                        Text(confirmButtonText)
                    }
                }
            },
            dismissButton = {
                if (dismissButtonText != null) {
                    TextButton(onClick = onDismiss) {
                        Text(dismissButtonText)
                    }
                }
            },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        )
    }
}

enum class DialogType {
    DEFAULT,
    SUCCESS,
    ERROR,
    LOADING,
    UPLOAD
}

@Composable
fun BodyMessage(icon: ImageVector? = null, message: String) {
    Row {
        if (icon != null) {
            Icon(
                icon, contentDescription = null, tint = colorResource(id = R.color.black)
            )
        } else if (message == "Espere por favor..." || message == "Cargando..." || message == "Enviando...") {
            CircularProgressIndicator(
                color = Color(0xFF0054A3),
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = message)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    var showConfirmationDialog by remember { mutableStateOf(true) }
    var showSuccessDialog by remember { mutableStateOf(true) }
    var showLoadingDialog by remember { mutableStateOf(true) }
    var showUploadDialog by remember { mutableStateOf(true) }
    var showErrorDialog by remember { mutableStateOf(true) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        /*CustomAlertDialog(
            showDialog = showConfirmationDialog,
            title = "Envio exitoso",
            message = "Se envio correctamente la solicitud",
            icon = Icons.Filled.Check,
            dialogType = DialogType.SUCCESS,
            onConfirm = { /* Acción de confirmación */ },
            onDismiss = { showConfirmationDialog = false }
        )*/
        /*CustomAlertDialog(
            showDialog = showLoadingDialog,
            title = "Cargando",
            message = "Espere por favor...",
            confirmButtonText = "",
            dismissButtonText = null,
            onDismiss = { showLoadingDialog = false }
        )*/
        /*CustomAlertDialog(
            showDialog = showConfirmationDialog,
            title = "Confirmación",
            message = "¿Estás seguro?",
            onConfirm = { /* Acción de confirmación */ },
            onDismiss = { showConfirmationDialog = false }
        )*/
        CustomAlertDialog(
            showDialog = showSuccessDialog,
            title = "Envio Exitoso",
            message = "Recibido la inspeccion N° 32",
            icon = Icons.Filled.Check,
            onDismiss = {showSuccessDialog = false},
            dialogType = DialogType.SUCCESS
        )
    }
}