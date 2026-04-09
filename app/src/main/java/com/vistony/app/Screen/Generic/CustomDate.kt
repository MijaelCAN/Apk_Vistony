package com.vistony.app.Screen.Generic

import android.R
import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.vistony.app.ui.theme.theme.Dimensions
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun DateOutlinedTextField(
    modifier: Modifier = Modifier,
    texto: String,
    readonly: Boolean = false,
    selectedDate: LocalDateTime?,
    onDateChange: (LocalDateTime?) -> Unit,
    showDialog: Boolean,
    onShowDialogChange: (Boolean) -> Unit
) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")


    val context = LocalContext.current
    val activity = context as Activity
    val windowSize = calculateWindowSizeClass(context)
    val bodyFontSize = Dimensions.getBodyFontSize(windowSize.widthSizeClass)


    if (selectedDate != null) {
        TextField(
            modifier = modifier
                .fillMaxWidth()
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
            },
            readOnly = readonly
        )
    }

    if (showDialog) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate?.atZone(ZoneId.systemDefault())
                ?.toInstant()?.toEpochMilli()
        )

        // Track if this is the initial selection
        var isInitialSelection by remember { mutableStateOf(true) }

        LaunchedEffect(datePickerState.selectedDateMillis) {
            if (datePickerState.selectedDateMillis != null && !isInitialSelection) {
                val newDateTime = Instant.ofEpochMilli(datePickerState.selectedDateMillis!!)
                    .atZone(ZoneOffset.UTC)
                    .toLocalDateTime()
                onDateChange(newDateTime)
                onShowDialogChange(false)
            }
            isInitialSelection = false
        }

        DatePickerDialog(
            onDismissRequest = { onShowDialogChange(false) },
            confirmButton = {}
        ) {
            DatePicker(
                state = datePickerState,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}


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

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun TimeOutlinedTextField(
    modifier: Modifier = Modifier,
    texto: String,
    readonly: Boolean = false,
    selectedTime: LocalDateTime?,
    onTimeChange: (LocalDateTime?) -> Unit,
    showDialog: Boolean,
    onShowDialogChange: (Boolean) -> Unit,
    is24HourFormat: Boolean = true,
    minTime: LocalDateTime? = null
) {
    val timeFormatter = DateTimeFormatter.ofPattern(if (is24HourFormat) "HH:mm" else "hh:mm a")

    val context = LocalContext.current
    val activity = context as Activity
    val windowSize = calculateWindowSizeClass(context)
    val bodyFontSize = Dimensions.getBodyFontSize(windowSize.widthSizeClass)

    TextField(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color.White, RoundedCornerShape(12.dp)),
        value = selectedTime?.format(timeFormatter) ?: "",
        label = {
            Text(
                text = texto,
                color = if (selectedTime?.format(timeFormatter)?.isNotEmpty() == true) Color.Gray else Color.LightGray,
                fontWeight = FontWeight.Bold,
                fontSize = if (selectedTime?.format(timeFormatter)?.isNotEmpty() == true) 12.sp else bodyFontSize.sp
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
                    imageVector = Icons.Default.Schedule,
                    contentDescription = "Reloj",
                    tint = Color.Gray
                )
            }
        },
        readOnly = readonly
    )

    if (showDialog) {
        TimePickerDialog(
            selectedTime = selectedTime,
            onTimeSelected = { newTime ->
                onTimeChange(newTime)
                onShowDialogChange(false)
            },
            onDismiss = { onShowDialogChange(false) },
            is24HourFormat = is24HourFormat,
            minTime = minTime
        )
    }
}
@Preview
@Composable
@RequiresApi(Build.VERSION_CODES.O)
fun visulizar(){
    TimePickerDialog(
        selectedTime = LocalDateTime.now(),
        onTimeSelected = { },
        onDismiss = { },
        is24HourFormat = true,
        minTime = null
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimePickerDialog(
    selectedTime: LocalDateTime?,
    onTimeSelected: (LocalDateTime?) -> Unit,
    onDismiss: () -> Unit,
    is24HourFormat: Boolean = true,
    minTime: LocalDateTime? = null
) {
    val initialTime = selectedTime ?: LocalDateTime.now()
    var currentTime by remember { mutableStateOf(initialTime) }
    var isAm by remember {
        mutableStateOf(initialTime.hour < 12)
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .wrapContentHeight()
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Text(
                    text = "Seleccionar hora",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                // Time Picker
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    // Hours
                    TimeWheel(
                        value = currentTime.hour,
                        onValueChange = { newHour ->
                            val hour = if (!is24HourFormat && !isAm) {
                                (newHour % 12) + 12
                            } else if (!is24HourFormat) {
                                newHour % 12
                            } else {
                                newHour % 24
                            }
                            val newTime = currentTime.withHour(hour)
                            
                            // Validar que la nueva hora no sea menor a la hora mínima
                            if (minTime == null || !newTime.isBefore(minTime)) {
                                currentTime = newTime
                            }
                        },
                        range = if (is24HourFormat) 0..23 else 1..12,
                        label = { value ->
                            if (is24HourFormat) {
                                String.format("%02d", value)
                            } else {
                                value.toString()
                            }
                        }
                    )

                    Text(
                        text = ":",
                        style = MaterialTheme.typography.displaySmall,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    // Minutes
                    TimeWheel(
                        value = currentTime.minute,
                        onValueChange = { newMinute ->
                            val newTime = currentTime.withMinute(newMinute % 60)
                            
                            // Validar que la nueva hora no sea menor a la hora mínima
                            if (minTime == null || !newTime.isBefore(minTime)) {
                                currentTime = newTime
                            }
                        },
                        range = 0..59,
                        label = { value -> String.format("%02d", value) }
                    )

                    // AM/PM Selector (only for 12-hour format)
                    if (!is24HourFormat) {
                        Column(
                            modifier = Modifier.padding(start = 16.dp)
                        ) {
                            IconButton(
                                onClick = {
                                    isAm = true
                                    // Adjust hour when switching to AM
                                    val newTime = if (currentTime.hour >= 12) {
                                        currentTime.withHour(currentTime.hour - 12)
                                    } else {
                                        currentTime
                                    }
                                    
                                    // Validar que la nueva hora no sea menor a la hora mínima
                                    if (minTime == null || !newTime.isBefore(minTime)) {
                                        currentTime = newTime
                                    }
                                },
                                modifier = Modifier.size(40.dp)
                            ) {
                                Text(
                                    text = "AM",
                                    color = if (isAm) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                    fontWeight = if (isAm) FontWeight.Bold else FontWeight.Normal
                                )
                            }

                            IconButton(
                                onClick = {
                                    isAm = false
                                    // Adjust hour when switching to PM
                                    val newTime = if (currentTime.hour < 12) {
                                        currentTime.withHour(currentTime.hour + 12)
                                    } else {
                                        currentTime
                                    }
                                    
                                    // Validar que la nueva hora no sea menor a la hora mínima
                                    if (minTime == null || !newTime.isBefore(minTime)) {
                                        currentTime = newTime
                                    }
                                },
                                modifier = Modifier.size(40.dp)
                            ) {
                                Text(
                                    text = "PM",
                                    color = if (!isAm) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                    fontWeight = if (!isAm) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }

                // Buttons
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    TextButton(
                        onClick = onDismiss,
                        text = "Cancelar"
                    )

                    Spacer(modifier = Modifier.width(0.dp))

                    TextButton(
                        onClick = { onTimeSelected(currentTime) },
                        text = "Aceptar",
                        isPrimary = true
                    )
                }
            }
        }
    }
}

@Composable
fun TimeWheel(
    value: Int,
    onValueChange: (Int) -> Unit,
    range: IntRange,
    label: @Composable (Int) -> String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        // Up button
        IconButton(
            onClick = {
                val newValue = if (value == range.last) range.first else value + 1
                onValueChange(newValue)
            },
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.arrow_up_float),
                contentDescription = "Incrementar"
            )
        }

        // Current value
        Text(
            text = label(value),
            style = MaterialTheme.typography.displaySmall.copy(fontSize = 32.sp),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Down button
        IconButton(
            onClick = {
                val newValue = if (value == range.first) range.last else value - 1
                onValueChange(newValue)
            },
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.arrow_down_float),
                contentDescription = "Decrementar"
            )
        }
    }
}

@Composable
fun TextButton(
    onClick: () -> Unit,
    text: String,
    isPrimary: Boolean = false,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(
            text = text,
            color = if (isPrimary) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurface,
            fontWeight = if (isPrimary) FontWeight.Bold else FontWeight.Normal
        )
    }
}
