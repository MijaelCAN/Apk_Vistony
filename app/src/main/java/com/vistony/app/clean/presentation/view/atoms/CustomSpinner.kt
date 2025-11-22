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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit

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
    errorMessage: String? = null,
    textSize: TextUnit = 14.sp,
    modifier: Modifier = Modifier,

) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.Center,
        /*modifier = Modifier
            .padding(horizontal = //8.dp
                paddingRes
            )
            .fillMaxWidth()*/
        modifier = modifier
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
            label = { Text(label, fontSize = textSize) },
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
                .clickable { if(enabled) {expanded = true} },
            textStyle = TextStyle.Default.copy(
                fontSize = textSize,
                color = Color.Black
            )
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
                    text = { Text(option
                        , fontSize = textSize
                    ) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}