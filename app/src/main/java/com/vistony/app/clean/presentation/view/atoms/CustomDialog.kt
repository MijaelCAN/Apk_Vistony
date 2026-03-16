package com.vistony.app.clean.presentation.view.atoms

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.vistony.app.clean.presentation.view.moleculs.ManufacturingOrderCustomDialog
import com.vistony.app.ui.theme.theme.Dimensions

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
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
    val context = LocalContext.current
    val activity = context as Activity
    val windowSize = calculateWindowSizeClass(context)
    val paddingRes = Dimensions.getPadding(windowSize.widthSizeClass)
    val textFieldHeight = Dimensions.getTextFieldHeight(windowSize.widthSizeClass)
    val bodyFontSize = Dimensions.getBodyFontSize(windowSize.widthSizeClass)


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
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = paddingRes)
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
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = paddingRes)
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
                        ),modifier= Modifier.padding(horizontal = paddingRes)) {
                        Text(confirmText, color = MaterialTheme.colorScheme.background)
                    }
                }
            },
            dismissButton = {
                /*OutlinedButton(onClick = onDismiss) {
                    Text(dismissText, textAlign = TextAlign.Center,color = MaterialTheme.colorScheme.onSurface )
                }*/
                TextButton(onClick = onDismiss, modifier= Modifier.padding(horizontal = paddingRes)) {
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
                    Button(onClick = onConfirm, modifier= Modifier.padding(horizontal = paddingRes)) {
                        Text(confirmText)
                    }
                }
            },
            dismissButton = {
                Button(onClick = onDismiss, modifier= Modifier.padding(horizontal = paddingRes)) {
                    Text(dismissText)
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
fun ResultDialog(
    isSuccess: Boolean,
    message: String,
    onDismiss: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = if (isSuccess)
                    Icons.Default.CheckCircle
                else
                    Icons.Default.Warning,
                contentDescription = if (isSuccess) "Éxito" else "Error",
                modifier = Modifier.size(64.dp),
                tint = if (isSuccess)
                    Color(0xFF4CAF50) // Verde
                else
                    Color(0xFFFFDB58) // ✅ Correcto - con alfa FF (opacidad completa)
            )
        },
        title = {
            Text(
                text = if (isSuccess) "Éxito" else "Advertencia",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                color = Color.Black
            )
        },
        confirmButton = {
            Row {
                ButtonView(
                    description = "Aceptar",
                    OnClick = onDismiss,
                    status = true,
                    IconActive = false,
                    context = LocalContext.current
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    )
}