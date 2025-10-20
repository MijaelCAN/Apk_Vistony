package com.vistony.app.Screen.Generic

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericDropdownMenu(
    modifier: Modifier = Modifier,
    label: String,
    options: List<Pair<String, String>>, // List<Pair<Code, Name>>
    selectedValue: String,
    onValueChange: (String) -> Unit,
    onCodeChange: (String) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange
    ) {
        CustomOutlinedTextField(
            modifier = modifier.menuAnchor(),
            value = selectedValue,
            onValueChange = {},
            label = label,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            readOnly = true
        )

        ExposedDropdownMenu(
            modifier = Modifier
                .background(Color.White)
                .clip(RoundedCornerShape(8.dp)),
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            options.forEach { (code, name) ->
                DropdownMenuItem(
                    text = { Text(name, color = Color.Black) },
                    onClick = {
                        onValueChange(name)
                        onCodeChange(code)
                        onExpandedChange(false)
                    }
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> GenericDropdownMenu2(
    label: String,
    options: List<T>,
    selectedOption: T?,
    onOptionSelected: (T) -> Unit,
    optionToText: (T) -> String,
    onExpandedChange: (Boolean) -> Unit,
    expanded: Boolean,
    modifier: Modifier = Modifier
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier
    ) {
        CustomOutlinedTextField(
            modifier = Modifier.menuAnchor(),
            value = selectedOption?.let { optionToText(it) } ?: "",
            onValueChange = {},
            readOnly = true,
            label = label,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = optionToText(option))},
                    onClick = {
                        onOptionSelected(option)
                        onExpandedChange(false)
                    }
                )
                Log.e("MDCR", "option: $option")
            }
        }
    }
}
