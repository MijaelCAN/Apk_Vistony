package com.vistony.app.clean.presentation.view.atoms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@Composable
fun DialogM3(
    title: String,
    subtitle: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    confirmText: String = "Aceptar",
    dismissText: String = "Cerrar",
    showConfirmButton: Boolean = true,
    content: (@Composable () -> Unit)? = null,
    statusSubtitle: Boolean = true,
    fullScreen: Boolean = false
) {
    if(statusSubtitle) {
        AlertDialog(
            modifier = if (fullScreen) Modifier.fillMaxSize()  else Modifier,
            onDismissRequest = onDismiss,
            properties = DialogProperties(usePlatformDefaultWidth = false),
            title = {
                Column(
                    modifier = if (fullScreen) Modifier.fillMaxWidth() else Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                }
            },
            text = {
                Column(
                    modifier = if (fullScreen) Modifier.fillMaxSize() else Modifier
                ) {
                    if (subtitle.isNotBlank()) {
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    content?.invoke()
                }

            },
            confirmButton = {
                if (showConfirmButton) {
                    Button(onClick = onConfirm,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.secondary
                        ),) {
                        Text(confirmText, color = MaterialTheme.colorScheme.background)
                    }
                }
            },
            dismissButton = {
                /*OutlinedButton(onClick = onDismiss) {
                    Text(dismissText, textAlign = TextAlign.Center,color = MaterialTheme.colorScheme.onSurface )
                }*/
                TextButton(onClick = onDismiss) {
                    Text(dismissText, textAlign = TextAlign.Center,color = MaterialTheme.colorScheme.secondary )
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }else {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            confirmButton = {
                if (showConfirmButton) {
                    Button(onClick = onConfirm) {
                        Text(confirmText)
                    }
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text(dismissText)
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }
}