package com.vistony.app.Screen.Generic

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateOutlinedTextField() {
    val context = LocalContext.current
    var selectedDate by remember { mutableStateOf("") }
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val datePickerDialog = DatePickerDialog(
        onDateSet = { _, year, month, dayOfMonth ->
            selectedDate = dateFormatter.format(Calendar.getInstance().apply{
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
            }.time)
        },
        dismissButton = {
            TextButton(onClick = { datePickerDialog.hide() }) {
                Text("Cancelar")
            }
        },
        confirmButton = {
            TextButton(onClick = { datePickerDialog.hide() }) {
                Text("Aceptar")
            }
        }
    )

    OutlinedTextField(
        value = selectedDate,
        onValueChange = { selectedDate = it },
        readOnly = true,
        modifier = modifier
            .fillMaxWidth()
            .clickable { /*datePickerDialog.show()*/ },
        label = { Text(text = texto) },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.CalendarToday,
                contentDescription = "Calendario"
            )
        }
    )
}*/

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateOutlinedTextField(
    modifier: Modifier = Modifier,
    texto: String,
    selectedDate: LocalDate,
    onDateChange: (LocalDate) -> Unit,
    showDialog: Boolean,
    onShowDialogChange: (Boolean) -> Unit
) {
    /*var showDialog by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }*/
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    var selectedDateMillis by remember { mutableStateOf(0L) }

    OutlinedTextField(
        value = selectedDate.format(dateFormatter),
        onValueChange = { },
        readOnly = true,
        modifier = modifier
            .fillMaxWidth()
            .clickable { onShowDialogChange(true) },
        label = { Text(texto) },
        trailingIcon = {
            IconButton(onClick = { onShowDialogChange(true) }) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Calendario"
                )
            }
        }
    )
    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { onShowDialogChange(false) },
            confirmButton = {}
        ) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = selectedDate.atStartOfDay(ZoneId.systemDefault())
                    .toInstant().toEpochMilli()
            )


            LaunchedEffect(datePickerState.selectedDateMillis) {
                if (datePickerState.selectedDateMillis != null) {
                    val newDate = Instant.ofEpochMilli(datePickerState.selectedDateMillis!!)
                        //.atZone(ZoneId.systemDefault())
                        .atZone(ZoneOffset.UTC)
                        .toLocalDate()
                    if (newDate != selectedDate) {
                        onDateChange(newDate)
                        onShowDialogChange(false)
                    }
                }
            }

            DatePicker(
                state = datePickerState,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}