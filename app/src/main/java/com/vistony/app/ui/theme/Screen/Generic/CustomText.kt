package com.vistony.app.ui.theme.Screen.Generic

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@ExperimentalMaterial3Api
@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit = {},
    label: String,
    modifier: Modifier = Modifier,
    trailingIcon: (@Composable () -> Unit)? = null,
    readOnly: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        label = {
            Text(
                label,
                overflow = TextOverflow.Ellipsis
            )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color(0xFF0054A3),
            unfocusedBorderColor = Color(0xFFA2A2A2),
            cursorColor = Color(0xFF0054A3),
            textColor = Color.Black
        ),
        trailingIcon = trailingIcon,
        readOnly = readOnly,
    )
}

@ExperimentalMaterial3Api
@Composable
fun CustomOutlinedTextField2(
    value: String,
    onValueChange: (String) -> Unit = {},
    label: String,
    modifier: Modifier = Modifier,
    trailingIcon: (@Composable () -> Unit)? = null,
    readOnly: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        label = {
            Text(label)
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color(0xFF0054A3),
            unfocusedBorderColor = Color(0xFFA2A2A2),
            cursorColor = Color(0xFF0054A3),
            textColor = Color.Black,
            placeholderColor = Color.White,
            disabledPlaceholderColor = Color.Black
        ),
        trailingIcon = trailingIcon,
        readOnly = readOnly,
    )
}

@Composable
fun CustomText(
    text: String
){
    Text(
        text = text,
        modifier = Modifier.padding(end = 8.dp, top = 16.dp).width(70.dp),
        style = TextStyle(color = Color.Black)
    )
}