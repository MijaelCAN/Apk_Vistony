package com.vistony.app.ui.theme.Screen.Generic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DriveFolderUpload
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotApplyResult
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.ModifierLocalReadScope
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController

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
                    onDismiss() // Cierra el diálogo después de confirmar
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
                // Puedes ajustar las propiedades del diálogo aquí si es necesario
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
    onDismiss: () -> Unit,
    navController: NavController
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
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 48.dp, vertical = 0.dp),
                    onClick = { navController.navigate("home") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0054A3))
                ) {
                    Text(text = "Cerrar", color = Color.White)
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
/*
@Preview
@Composable
fun Prueba(){
    var titulo = "Envio Exitoso"
    AlertDialog(
        modifier = Modifier
            .width(400.dp)
            .height(300.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(Color.White),
        onDismissRequest = {  },
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
                //Text(text = "message ....... muy largo , esto deberia aparecer", fontSize = 12.sp, color = Color.Black)
            }
        },
        text = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Se inspecciono el pallet 45",
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp, vertical = 0.dp),
                onClick = {  },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0054A3))
            ) {
                Text(text = "Cerrar", color = Color.White)
            }
        },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        ),
        containerColor = Color.Transparent,
    )
}*/