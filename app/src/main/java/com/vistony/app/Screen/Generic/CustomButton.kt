package com.vistony.app.Screen.Generic

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomSearchText(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    borderColor: Color = Color.LightGray,
    contentColor: Color = Color.LightGray,
    containerColor: Color = Color.Transparent,
    searchIcon: ImageVector = Icons.Default.Search,
    enabled: Boolean = true,
    placeholderText: String = "Buscar..."
) {
    // Estado para controlar si el campo tiene foco
    var isFocused by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .height(45.dp)
            .defaultMinSize(minWidth = 64.dp)
            .background(containerColor, shape = RoundedCornerShape(20.dp))
            .border(
                width = 0.5.dp,
                color = if (isFocused) borderColor else borderColor.copy(alpha = 0.5f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = searchIcon,
                contentDescription = "Search Icon",
                tint = contentColor,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))

            Box(modifier = Modifier.weight(1f)) {
                BasicTextField(
                    value = text,
                    onValueChange = onTextChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { focusState ->
                            isFocused = focusState.isFocused
                        },
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(
                        color = contentColor,
                        fontSize = 14.sp
                    ),
                    enabled = enabled,
                    cursorBrush = SolidColor(contentColor)
                )
                if (text.isEmpty()) {
                    Text(
                        text = placeholderText,
                        color = contentColor.copy(alpha = 0.5f),
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}


@Composable
fun ThinOutlinedButton(
    text: String,
    selected: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (selected) Color(0xFFFC6A68) else Color.LightGray
    val containerColor = if (selected) Color(0xFFFC6A68) else Color.Transparent
    val contentColor = if (selected) Color.Black  else Color.LightGray

    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .height(32.dp) // altura baja, delgada
            .defaultMinSize(minWidth = 64.dp), // ancho mínimo
        shape = RoundedCornerShape(20.dp), // esquinas redondeadas
        border = BorderStroke(0.5.dp, borderColor),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = contentColor,
            containerColor = containerColor
        ),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp), // padding horizontal y vertical pequeño
        elevation = null // sin sombra para mantener minimalista
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun FilterButtonsRow(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { option ->
            ThinOutlinedButton(
                text = option,
                selected = option == selectedOption,
                onClick = { onOptionSelected(option) }
            )
        }
    }
}


@Composable
@Preview(showBackground = true)
fun CustomButton2() {

    var selected by remember { mutableStateOf("Todos") }
    val options = listOf("Todos", "Iniciado", "Finalizado", "Cerrado")
    var searchText by remember { mutableStateOf("") }

    CustomSearchText(
        text = searchText,
        onTextChange = { searchText = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        searchIcon = Icons.Default.Search,
        placeholderText = "Buscar..."
    )

}