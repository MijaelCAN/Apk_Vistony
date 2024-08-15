package com.vistony.app.ui.theme.Screen.Generic

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
/*
@Composable
fun TableCell(
    text: String = "",
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
){
  content()
    Text(
        text = text,
        modifier = modifier
            .border(1.dp, Color.Gray),
        textAlign = TextAlign.Center
    )
}*/
@Composable
fun <T> TableCell(
    value: T,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit
) {
    Box(
        modifier = modifier
            .padding(8.dp)
    ) {
        content(value)
    }
}

// Sobrecarga para String
@Composable
fun TableCell(
    text: String,
    modifier: Modifier = Modifier
) {
    TableCell(text, modifier) {
        Text(text = it, textAlign = TextAlign.Left)
    }
}