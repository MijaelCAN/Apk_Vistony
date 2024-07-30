package com.vistony.app.ui.theme.Screen.Generic

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties

@Composable
fun MyCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: CheckboxColors = CheckboxDefaults.colors()
) {
    Checkbox(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        colors = colors
    )
}

@Composable
fun CustomSpinner(
    expanded: MutableState<Boolean>,
    options: List<String>,
    operador: MutableState<String>,
    textFieldSize: Size,
) {
    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false },
        modifier = Modifier
            .width(with(LocalDensity.current){textFieldSize.width.toDp()})
            .background(Color.White)
            .clip(RoundedCornerShape(8.dp)),
        properties = PopupProperties(focusable = true)
    ) {
        options.forEach { option ->
            DropdownMenuItem(
                text = { Text(option, color = Color.Black) },
                onClick = {
                    operador.value = option
                    expanded.value = false
                }
            )
        }
    }
}

@Composable
fun CustomCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    checkboxSize: Dp = 24.dp,  // Tamaño del checkbox, ajusta según tus necesidades
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(checkboxSize)  // Tamaño del checkbox
            .background(Color.White)  // Fondo blanco para el checkbox
            .border(
                width = 2.dp,
                color = if (checked) Color.Green else Color.Gray
            )
            .padding(2.dp)  // Espacio interno para mostrar el color del checkmark
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = Color.Green,
                uncheckedColor = Color.Gray,
                checkmarkColor = Color.White
            ),
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxHeight()  // Ajusta la altura
        )
    }
}