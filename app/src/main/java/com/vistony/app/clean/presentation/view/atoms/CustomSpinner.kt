package com.vistony.app.clean.presentation.view.atoms

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vistony.app.ui.theme.theme.Dimensions
import com.vistony.salesforce.kotlin.view.Atoms.theme.BlueVistony
import android.content.ContextWrapper

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class,ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun SpinnerM3(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    enabled: Boolean = true,
    iconColor:Color,
    isError: Boolean = false,
    errorMessage: String? = null

) {
    var expanded by remember { mutableStateOf(false) }
    var textFieldWidth by remember { mutableStateOf(0) }
    val context = LocalContext.current
    //val activity = context as Activity


// Buscar la Activity de forma segura
    val activity = remember(context) {
        var ctx = context
        while (ctx is ContextWrapper) {
            if (ctx is Activity) {
                return@remember ctx
            }
            ctx = ctx.baseContext
        }
        null
    }

// Si no se encuentra Activity, usar valores por defecto
    val windowSize = activity?.let { calculateWindowSizeClass(it) }
    val paddingRes = windowSize?.let { Dimensions.getPadding(it.widthSizeClass) } ?: 8.dp
    val textFieldHeight = windowSize?.let { Dimensions.getTextFieldHeight(it.widthSizeClass) } ?: 56.dp
    val bodyFontSize = windowSize?.let { Dimensions.getBodyFontSize(it.widthSizeClass) } ?: 14.sp

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(horizontal = //8.dp
                paddingRes
            )
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = if (isError) MaterialTheme.colorScheme.error else BlueVistony,
                unfocusedBorderColor = if (isError) MaterialTheme.colorScheme.error else Color.Gray,
                focusedLabelColor = if (isError) MaterialTheme.colorScheme.error else BlueVistony,
                unfocusedLabelColor = if (isError) MaterialTheme.colorScheme.error else Color.Gray
            ),
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            enabled = enabled,
            label = { Text(label, fontSize = 14.sp) },
            isError = isError,
            supportingText = if (isError) {
                { Text(errorMessage ?: "Campo requerido") }
            } else null,
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.clickable { if(enabled) {expanded = !expanded} },
                    tint = if(enabled) iconColor else Color.LightGray
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { if(enabled) {expanded = true} }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
            // .background(MaterialTheme.colorScheme.surface) // <- Aquí defines el fondo
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}