package com.vistony.app.ui.theme.Screen.Generic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CustomButton(
    text: String,
    modifier: Modifier = Modifier,
    search: ImageVector? = null,
    onClick: () -> Unit,
) {
    ElevatedButton(
        onClick = { onClick() },
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = Color(0xFF0054A3),
            contentColor = Color.White,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.Black
        )
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(38.dp)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (search != null)
                Icon(search, contentDescription = null, tint = Color.White)
            Text(text = text, color = Color.White)
        }

    }
}

@Composable
@Preview(showBackground = true)
fun CustomButton2() {
    ElevatedButton(
        onClick = { },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = Color(0xFF0054A3),
            contentColor = Color.White,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.Black
        )
    ) {
        Row(
        ) {
            Icon(Icons.Filled.Search, contentDescription = null)
            Text(text = "Guardar")
        }

    }
}