package com.vistony.app.Screen.Generic

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.tooling.preview.Preview
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
            .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
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
@ExperimentalMaterial3Api
@Composable
fun CustomSpinner2(
    expanded: MutableState<Boolean>,
    options: List<String>,
    operador: MutableState<String>
) {
    ExposedDropdownMenuBox(
        expanded = expanded.value,
        onExpandedChange = { expanded.value = false },
        modifier = Modifier
            .background(Color.White)
            .clip(RoundedCornerShape(8.dp))
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

@Preview
@Composable
fun CustomTriStateCheckbox() {
    var state by remember { mutableStateOf(ToggleableState.Off) }

    Column {
        TriStateCheckbox(
            state = state,
            onClick = {
                state = when (state) {
                    ToggleableState.On -> ToggleableState.Off
                    ToggleableState.Off -> ToggleableState.Indeterminate
                    ToggleableState.Indeterminate -> ToggleableState.On
                }
            },
            colors = CheckboxDefaults.colors(
                checkedColor = Color.Green, // Color para el estado On
                uncheckedColor = Color.Transparent, // Color para el estado Off (normal)
                checkmarkColor = Color.White, // Color del checkmark
                disabledCheckedColor = Color.Gray,
                disabledUncheckedColor = Color.LightGray,
                disabledIndeterminateColor = Color.Red // Color para el estado Indeterminate
            )
        )
        Text(text = "Seleccionar opción")
    }
}

@Preview
@Composable
fun Prueba(){
    var checked by remember {
        mutableStateOf(true)
    }
    /*val colors = if (checked) {
        CheckboxDefaults.colors(
            checkedColor = Color.Green,
            uncheckedColor = Color.Red,
            checkmarkColor = Color.White
        )
    } else {
        CheckboxDefaults.colors(
            checkedColor = Color.Red,
            uncheckedColor = Color.Red,
            checkmarkColor = Color.Transparent
        )
    }
    Checkbox(
        checked = checked,
        onCheckedChange = { checked = it },
        colors = colors
    )*/
    /*Box(
        modifier = Modifier
            .size(48.dp)
            .border(2.dp, if (checked) Color.Green else Color.Red)
            .background(Color.Transparent)
            .clip(RoundedCornerShape(38.dp))
            .clickable(enabled = true) { checked},
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color.Green
            )
        } else {
            Canvas(modifier = Modifier.size(16.dp)) {
                drawLine(
                    color = Color.Red,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, size.height),
                    strokeWidth = 2.dp.toPx()
                )
                drawLine(
                    color = Color.Red,
                    start = Offset(size.width, 0f),
                    end = Offset(0f, size.height),
                    strokeWidth = 2.dp.toPx()
                )
            }
        }
    }*/
    var state by remember { mutableStateOf(ToggleableState.Off) }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "TriState Checkbox Example")

        Spacer(modifier = Modifier.height(8.dp))

        TriStateCheckbox(
            state = state,
            onClick = {
                state = when (state) {
                    ToggleableState.Off -> ToggleableState.On
                    ToggleableState.On -> ToggleableState.Indeterminate
                    ToggleableState.Indeterminate -> ToggleableState.Off
                }
            },
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = when (state) {
            ToggleableState.On -> "Checked"
            ToggleableState.Off -> "Unchecked"
            ToggleableState.Indeterminate -> "Indeterminate"
        })
    }
}