package com.vistony.app.Screen.Generic

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vistony.app.ui.theme.theme.Dimensions
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

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
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun DateOutlinedTextField(
    modifier: Modifier = Modifier,
    texto: String,
    readonly: Boolean = false,
    selectedDate: LocalDate,
    onDateChange: (LocalDate) -> Unit,
    showDialog: Boolean,
    onShowDialogChange: (Boolean) -> Unit
) {
    /*var showDialog by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }*/
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    var selectedDateMillis by remember { mutableStateOf(0L) }

    val context = LocalContext.current
    val activity = context as Activity
    val windowSize = calculateWindowSizeClass(context)

    val padding_res = Dimensions.getPadding(windowSize.widthSizeClass)
    val buttonHeight = Dimensions.getButtonHeight(windowSize.widthSizeClass)
    val textFieldHeight = Dimensions.getTextFieldHeight(windowSize.widthSizeClass)
    val titleFontSize = Dimensions.getTitleFontSize(windowSize.widthSizeClass)
    val bodyFontSize = Dimensions.getBodyFontSize(windowSize.widthSizeClass)
    val imageSize = Dimensions.getImageSize(windowSize.widthSizeClass)

    /*OutlinedTextField(
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
    )*/
    TextField(
        modifier = modifier
            .fillMaxWidth()
            //.height(textFieldHeight)
            //.padding(horizontal = padding_res)
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color.White, RoundedCornerShape(12.dp)),
        value = selectedDate.format(dateFormatter),
        label = {
            Text(
                text = texto,
                color = if( selectedDate.format(dateFormatter).isNotEmpty())Color.Gray else Color.LightGray,
                fontWeight = FontWeight.Bold,
                fontSize = if( selectedDate.format(dateFormatter).isNotEmpty()) 12.sp else bodyFontSize.sp
            )
        },
        onValueChange = { },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedTextColor = Color.Gray,
            unfocusedTextColor = Color.Gray,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = Color.Gray
        ),
        trailingIcon = {
            IconButton(onClick = { onShowDialogChange(true) }) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Calendario",
                    tint = Color.Gray
                )
            }
        }
    )
    /*Column(modifier = modifier.height(50.dp)) {
        Text(
            text = texto,
            style = TextStyle(fontSize = 14.sp, color = Color.Gray)
        )
        Spacer(modifier = Modifier.height(2.dp))
        BasicTextField(
            value = selectedDate.format(dateFormatter),
            onValueChange = { },
            modifier = Modifier
                .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .fillMaxWidth(),
            textStyle = TextStyle(color = Color.Gray, fontSize = 14.sp),
            singleLine = true,
            readOnly = readonly,
            cursorBrush = SolidColor(Color.Red),
            decorationBox = { innerTextField ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { onShowDialogChange(true) }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Calendario",
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    innerTextField()
                }
            }
        )
    }*/
    
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

@Preview
@Composable
fun CustomBasicTextField(){
    var text by remember { mutableStateOf("Texto inicial") }
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Correo electrónico",
            style = TextStyle(fontSize = 14.sp, color = Color.Gray)
        )
        Spacer(modifier = Modifier.height(4.dp))
        BasicTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier
                .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .fillMaxWidth(),
            textStyle = TextStyle(color = Color.Gray, fontSize = 14.sp),
            singleLine = true,
            cursorBrush = SolidColor(Color.Red),
            decorationBox = { innerTextField ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Email, contentDescription = "Email icon")
                    Spacer(modifier = Modifier.width(8.dp))
                    innerTextField()
                }
            }
        )
    }
}
