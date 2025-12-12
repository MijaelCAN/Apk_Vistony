package com.vistony.app.clean.presentation.view.atoms

import android.app.Activity
import android.util.Size
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vistony.app.ui.theme.theme.Dimensions
import com.vistony.salesforce.kotlin.view.Atoms.theme.BlueVistony


@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EditTextM3(
    id:Int=0,
    status: Boolean,
    value:String,
    placeholder:String,
    label:String,
    leadingiconResourceId:Painter,
    keyboardType:KeyboardType,
    statusMaxCharacter:Boolean=true,
    countMaxCharacter:Int=254,
    limitNumericStatus:Boolean=false,
    limitNumericNumber:Double=0.0,
    trailingiconResourceId:Painter,
    leadingiconColor:Color,
    trailingiconColor:Color,
    textDownEditext:String="",
    trailingIconStatus:Boolean=false,
    trailingIconOnClick: (String) -> Unit,
    resultEditText: (String) -> Unit,
    leadingIconStatus: Boolean=false,
    statusTextDownEditext:Boolean=true,
    readOnly: Boolean= false,
    modifier: Modifier,
    isError: Boolean = false,
    errorMessage: String? = null,
    leadingIconOnClick: (String) -> Unit,
    textSize: TextUnit = 14.sp
){
    val keyboardController = LocalSoftwareKeyboardController.current
    var text = remember { mutableStateOf(value) }
    //var text = value
    val context = LocalContext.current
    // Este efecto actualiza el texto cuando cambia el valor de entrada
    LaunchedEffect(value) {
        text.value = value
    }

    Box(
        contentAlignment = Alignment.Center,
        //modifier = modifier
    ) {
        Column {
            if(leadingIconStatus&&trailingIconStatus)
            {
                OutlinedTextField(
                    readOnly = readOnly,
                    enabled = status,
                    singleLine = false,
                    value = text.value,
                    onValueChange =
                        { newText ->
                            if (newText.length <= countMaxCharacter) {
                                when (keyboardType) {
                                    KeyboardType.Decimal -> {
                                        if ((newText.isNotEmpty() && newText.first() != '.') || newText.length > 1) {
                                            val cleanedText = newText.filterIndexed { index, char ->
                                                char.isDigit() || (char == '.' && index == newText.indexOf(
                                                    '.'
                                                ))
                                            }
                                            if (limitNumericStatus) {
                                                if (cleanedText.toDoubleOrNull() != null && cleanedText.toDoubleOrNull()!! <= limitNumericNumber) {
                                                    text.value = cleanedText
                                                    resultEditText(cleanedText)
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        "El valor debe ser menor o igual a $limitNumericNumber",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            } else {
                                                if (cleanedText.toDoubleOrNull() != null) {
                                                    text.value = cleanedText
                                                    resultEditText(cleanedText)
                                                }
                                            }
                                        } else if (newText == ".") {
                                            text.value = ""
                                            resultEditText("")
                                        } else {
                                            text.value = newText
                                            resultEditText(newText)
                                        }
                                    }

                                    else -> {
                                        text.value = newText
                                        resultEditText(newText)
                                    }
                                }
                            }
                        },
                    label = {
                        Text(
                            text = label,
                            fontSize = textSize,
                            //color = Color.Gray
                        )},
                    leadingIcon = {
                        IconButton(onClick = {
                            leadingIconOnClick(text.value)
                        }) {
                            Icon(
                                //painter = if (text.value.isNotBlank()) painterResource(id = R.drawable.baseline_check_circle_24) else leadingiconResourceId,
                                painter = leadingiconResourceId,
                                contentDescription = null,
                                tint = (leadingiconColor)
                            )
                        }
                    },
                    trailingIcon = {
                        if ((statusMaxCharacter && text.value.isNotBlank()) || trailingIconStatus) {
                            IconButton(onClick = {
                                trailingIconOnClick(text.value)
                            }) {
                                Icon(
                                    painter = trailingiconResourceId,
                                    contentDescription = null,
                                    tint = (trailingiconColor)
                                )
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = keyboardType,
                        imeAction = ImeAction.Go
                    ),
                    keyboardActions = KeyboardActions(
                        onGo = {
                            keyboardController?.hide()
                        },
                    ),
                    modifier = modifier,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        disabledBorderColor = Color.Gray,
                        disabledLabelColor = Color.Gray,
                        disabledTextColor = Color.Gray,
                        disabledPlaceholderColor = Color.Gray,
                        focusedBorderColor = if (isError) MaterialTheme.colorScheme.error else BlueVistony,
                        unfocusedBorderColor = if (isError) MaterialTheme.colorScheme.error else Color.Gray,
                        focusedLabelColor = if (isError) MaterialTheme.colorScheme.error else BlueVistony,
                        unfocusedLabelColor = if (isError) MaterialTheme.colorScheme.error else Color.Gray,
                        cursorColor = BlueVistony, // Cambia el color del cursor

                    ),
                    isError = isError,
                    supportingText = if (isError) {
                        { Text(errorMessage ?: "Campo requerido") }
                    } else null,
                    textStyle = TextStyle.Default.copy(
                        fontSize = textSize,
                        color = Color.Black
                    )
                )
            }else if(!leadingIconStatus&&trailingIconStatus){
                OutlinedTextField(
                    readOnly = readOnly,
                    enabled = status,
                    singleLine = false,
                    value = text.value,
                    onValueChange =
                        { newText ->
                            if (newText.length <= countMaxCharacter) {
                                when (keyboardType) {
                                    KeyboardType.Decimal -> {
                                        if ((newText.isNotEmpty() && newText.first() != '.') || newText.length > 1) {
                                            val cleanedText = newText.filterIndexed { index, char ->
                                                char.isDigit() || (char == '.' && index == newText.indexOf(
                                                    '.'
                                                ))
                                            }
                                            if (limitNumericStatus) {
                                                if (cleanedText.toDoubleOrNull() != null && cleanedText.toDoubleOrNull()!! <= limitNumericNumber) {
                                                    text.value = cleanedText
                                                    resultEditText(cleanedText)
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        "El valor debe ser menor o igual a $limitNumericNumber",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            } else {
                                                if (cleanedText.toDoubleOrNull() != null) {
                                                    text.value = cleanedText
                                                    resultEditText(cleanedText)
                                                }
                                            }
                                        } else if (newText == ".") {
                                            text.value = ""
                                            resultEditText("")
                                        } else {
                                            text.value = newText
                                            resultEditText(newText)
                                        }
                                    }

                                    else -> {
                                        text.value = newText
                                        resultEditText(newText)
                                    }
                                }
                            }
                        },
                    /*placeholder = {
                        Text(text = placeholder, fontSize = 14.sp)
                    },*/
                    label = {
                        Text(
                            text = label,
                            fontSize = textSize,
                            //color = Color.Gray
                        )},
                    trailingIcon = {
                        if ((statusMaxCharacter && text.value.isNotBlank()) || trailingIconStatus) {
                            IconButton(onClick = {
                                trailingIconOnClick(text.value)
                            }) {
                                Icon(
                                    painter = trailingiconResourceId,
                                    contentDescription = null,
                                    tint = (trailingiconColor)
                                )
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = keyboardType,
                        imeAction = ImeAction.Go
                    ),
                    keyboardActions = KeyboardActions(
                        onGo = {
                            keyboardController?.hide()
                        },
                    ),
                    modifier = modifier,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        disabledBorderColor = Color.Gray,
                        disabledLabelColor = Color.Gray,
                        disabledTextColor = Color.Gray,
                        disabledPlaceholderColor = Color.Gray,
                        focusedBorderColor = if (isError) MaterialTheme.colorScheme.error else BlueVistony,
                        unfocusedBorderColor = if (isError) MaterialTheme.colorScheme.error else Color.Gray,
                        focusedLabelColor = if (isError) MaterialTheme.colorScheme.error else BlueVistony,
                        unfocusedLabelColor = if (isError) MaterialTheme.colorScheme.error else Color.Gray,
                        cursorColor = BlueVistony, // Cambia el color del cursor
                    ),
                    isError = isError,
                    supportingText = if (isError) {
                        { Text(errorMessage ?: "Campo requerido") }
                    } else null,
                    textStyle = TextStyle.Default.copy(
                        fontSize = textSize,
                        color = Color.Black
                    )
                )
            }else if(leadingIconStatus&&!trailingIconStatus){
                OutlinedTextField(
                    readOnly = readOnly,
                    enabled = status,
                    singleLine = false,
                    value = text.value,
                    onValueChange =
                        { newText ->
                            if (newText.length <= countMaxCharacter) {
                                when (keyboardType) {
                                    KeyboardType.Decimal -> {
                                        if ((newText.isNotEmpty() && newText.first() != '.') || newText.length > 1) {
                                            val cleanedText = newText.filterIndexed { index, char ->
                                                char.isDigit() || (char == '.' && index == newText.indexOf(
                                                    '.'
                                                ))
                                            }
                                            if (limitNumericStatus) {
                                                if (cleanedText.toDoubleOrNull() != null && cleanedText.toDoubleOrNull()!! <= limitNumericNumber) {
                                                    text.value = cleanedText
                                                    resultEditText(cleanedText)
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        "El valor debe ser menor o igual a $limitNumericNumber",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            } else {
                                                if (cleanedText.toDoubleOrNull() != null) {
                                                    text.value = cleanedText
                                                    resultEditText(cleanedText)
                                                }
                                            }
                                        } else if (newText == ".") {
                                            text.value = ""
                                            resultEditText("")
                                        } else {
                                            text.value = newText
                                            resultEditText(newText)
                                        }
                                    }

                                    else -> {
                                        text.value = newText
                                        resultEditText(newText)
                                    }
                                }
                            }
                        },
                    /*placeholder = {
                        Text(text = placeholder, fontSize = 14.sp)
                    },*/
                    label = {
                        Text(
                            text = label,
                            fontSize = textSize,
                            //color = Color.Gray
                        )},
                    leadingIcon = {
                        IconButton(onClick = {
                            leadingIconOnClick(text.value)
                        }) {
                            Icon(
                                //painter = if (text.value.isNotBlank()) painterResource(id = R.drawable.baseline_check_circle_24) else leadingiconResourceId,
                                painter = leadingiconResourceId,
                                contentDescription = null,
                                tint = (leadingiconColor)
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = keyboardType,
                        imeAction = ImeAction.Go
                    ),
                    keyboardActions = KeyboardActions(
                        onGo = {
                            keyboardController?.hide()
                        },
                    ),
                    modifier = modifier,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        disabledBorderColor = Color.Gray,
                        disabledLabelColor = Color.Gray,
                        disabledTextColor = Color.Gray,
                        disabledPlaceholderColor = Color.Gray,
                        focusedBorderColor = if (isError) MaterialTheme.colorScheme.error else BlueVistony,
                        unfocusedBorderColor = if (isError) MaterialTheme.colorScheme.error else Color.Gray,
                        focusedLabelColor = if (isError) MaterialTheme.colorScheme.error else BlueVistony,
                        unfocusedLabelColor = if (isError) MaterialTheme.colorScheme.error else Color.Gray,
                        cursorColor = BlueVistony, // Cambia el color del cursor
                    ),
                    isError = isError,
                    supportingText = if (isError) {
                        { Text(errorMessage ?: "Campo requerido") }
                    } else null,
                    textStyle = TextStyle.Default.copy(
                        fontSize = textSize,
                        color = Color.Black
                    )
                )
            }else if(!leadingIconStatus&&!trailingIconStatus){
                OutlinedTextField(
                    readOnly = readOnly,
                    enabled = status,
                    singleLine = false,
                    value = text.value,
                    onValueChange =
                        { newText ->
                            if (newText.length <= countMaxCharacter) {
                                when (keyboardType) {
                                    KeyboardType.Decimal -> {
                                        if ((newText.isNotEmpty() && newText.first() != '.') || newText.length > 1) {
                                            val cleanedText = newText.filterIndexed { index, char ->
                                                char.isDigit() || (char == '.' && index == newText.indexOf(
                                                    '.'
                                                ))
                                            }
                                            if (limitNumericStatus) {
                                                if (cleanedText.toDoubleOrNull() != null && cleanedText.toDoubleOrNull()!! <= limitNumericNumber) {
                                                    text.value = cleanedText
                                                    resultEditText(cleanedText)
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        "El valor debe ser menor o igual a $limitNumericNumber",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            } else {
                                                if (cleanedText.toDoubleOrNull() != null) {
                                                    text.value = cleanedText
                                                    resultEditText(cleanedText)
                                                }
                                            }
                                        } else if (newText == ".") {
                                            text.value = ""
                                            resultEditText("")
                                        } else {
                                            text.value = newText
                                            resultEditText(newText)
                                        }
                                    }

                                    else -> {
                                        text.value = newText
                                        resultEditText(newText)
                                    }
                                }
                            }
                        },
                    /*placeholder = {
                        Text(text = placeholder, fontSize = 14.sp)
                    },*/
                    label = {
                        Text(
                            text = label,
                            fontSize = textSize,
                            //color = Color.Gray
                        )},
                    keyboardOptions = KeyboardOptions(
                        keyboardType = keyboardType,
                        imeAction = ImeAction.Go
                    ),
                    keyboardActions = KeyboardActions(
                        onGo = {
                            keyboardController?.hide()
                        },
                    ),
                    modifier = modifier,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        disabledBorderColor = Color.Gray,
                        disabledLabelColor = Color.Gray,
                        disabledTextColor = Color.Gray,
                        disabledPlaceholderColor = Color.Gray,
                        focusedBorderColor = if (isError) MaterialTheme.colorScheme.error else BlueVistony,
                        unfocusedBorderColor = if (isError) MaterialTheme.colorScheme.error else Color.Gray,
                        focusedLabelColor = if (isError) MaterialTheme.colorScheme.error else BlueVistony,
                        unfocusedLabelColor = if (isError) MaterialTheme.colorScheme.error else Color.Gray,
                        cursorColor = BlueVistony, // Cambia el color del cursor
                    ),
                    isError = isError,
                    supportingText = if (isError) {
                        { Text(errorMessage ?: "Campo requerido") }
                    } else null,
                    textStyle = TextStyle.Default.copy(
                        fontSize = textSize,
                        color = Color.Black
                    )
                )
            }

            Row {
                if(statusTextDownEditext) {
                    Row(horizontalArrangement = Arrangement.Start) {
                        Text(
                            text = textDownEditext,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
                            modifier = modifier
                        )
                    }
                }
                if (statusMaxCharacter)
                {
                    Row(
                        modifier = modifier,
                        horizontalArrangement = Arrangement.End
                    ){
                        Text(
                            text = "${text.value.length}/$countMaxCharacter",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (text.value.length > countMaxCharacter) Color.Red else Color.Gray,
                            textAlign = TextAlign.End
                            //modifier = modifier
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun TextWithDivider(text: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val activity = context as Activity
    val windowSize = calculateWindowSizeClass(context)
    val paddingRes = Dimensions.getPadding(windowSize.widthSizeClass)
    val textFieldHeight = Dimensions.getTextFieldHeight(windowSize.widthSizeClass)
    val bodyFontSize = Dimensions.getBodyFontSize(windowSize.widthSizeClass)

    Row(
        modifier = modifier.padding(paddingRes),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(
            modifier = Modifier
                .weight(1f)
                .height(1.dp),
            color = MaterialTheme.colorScheme.tertiary
        )

        if (text.isNotEmpty()) {
            Text(
                text = text,
                modifier = Modifier.padding(horizontal = 8.dp),
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = bodyFontSize.sp
            )

            Divider(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp),
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TextM3(
    id:Int=0,
    status: Boolean,
    text:String,
    placeholder:String,
    label:String,
    leadingiconResourceId:Painter,
    keyboardType:KeyboardType,
    statusMaxCharacter:Boolean=true,
    countMaxCharacter:Int=254,
    limitNumericStatus:Boolean=false,
    limitNumericNumber:Double=0.0,
    trailingiconResourceId:Painter,
    leadingiconColor:Color,
    trailingiconColor:Color,
    textDownEditext:String="",
    trailingiconStatus:Boolean=false,
    trailingIconOnClick:(String) -> Unit,
    resultEditText: (String) -> Unit,
    leadingiconStatus:Boolean=true,
    leadingIconOnClick:(String) -> Unit = { _ ->  },
    readOnly:Boolean=false,
    isError: Boolean = false,
    errorMessage: String? = null,
    modifier: Modifier,
    textSize: TextUnit = 14.sp
){
    val keyboardController = LocalSoftwareKeyboardController.current
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            //.padding(10.dp)
            .fillMaxWidth()
    ) {
        Column {
            if(trailingiconStatus&&leadingiconStatus){
                OutlinedTextField(
                    readOnly = readOnly,
                    enabled = status,
                    singleLine = false,
                    value = text,
                    onValueChange =
                        {
                        },
                    label = {
                        Text(
                            text = label,
                            fontSize = textSize,
                            //color = if (readOnly) Color.Gray else Color.Unspecified
                        )},
                    leadingIcon = {
                        if (leadingiconStatus) {
                            IconButton(onClick = {
                                leadingIconOnClick("")
                            }) {
                                Icon(
                                    painter = leadingiconResourceId,
                                    contentDescription = null,
                                    tint = if(status){leadingiconColor}else{Color.LightGray}
                                )
                            }
                        } else {
                            null
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = keyboardType,
                        imeAction = ImeAction.Go
                    ),
                    keyboardActions = KeyboardActions(
                        onGo = {
                            keyboardController?.hide()
                        },
                    ),
                    modifier = modifier,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        //focusedBorderColor = BlueVistony,
                        //focusedLabelColor = BlueVistony,
                        //unfocusedBorderColor = Color.Gray,
                        //unfocusedLabelColor = Color.Gray,
                        disabledBorderColor = Color.Gray,
                        disabledLabelColor = Color.Gray,
                        disabledTextColor = Color.Gray,
                        disabledPlaceholderColor = Color.Gray,
                        focusedBorderColor = if (isError) MaterialTheme.colorScheme.error else BlueVistony,
                        unfocusedBorderColor = if (isError) MaterialTheme.colorScheme.error else Color.Gray,
                        focusedLabelColor = if (isError) MaterialTheme.colorScheme.error else BlueVistony,
                        unfocusedLabelColor = if (isError) MaterialTheme.colorScheme.error else Color.Gray
                    ),
                    trailingIcon = {
                        if (trailingiconStatus) {
                            IconButton(onClick = {
                                trailingIconOnClick("")
                            }) {
                                Icon(
                                    painter = trailingiconResourceId,
                                    contentDescription = null,
                                    tint = if(status){trailingiconColor}else{Color.LightGray}
                                    //tint = trailingiconColor
                                )
                            }
                        }
                    },
                    isError = isError,
                    supportingText = if (isError) {
                        { Text(errorMessage ?: "Campo requerido") }
                    } else null,
                    textStyle = TextStyle.Default.copy(
                        fontSize = textSize,
                        color = Color.Black
                    )
                )
            }else if(!leadingiconStatus&&trailingiconStatus) {
                OutlinedTextField(
                    readOnly = readOnly,
                    enabled = status,
                    singleLine = false,
                    value = text,
                    onValueChange =
                        {
                        },
                    /*)placeholder = {
                        Text(text = placeholder, fontSize = 14.sp)
                    }*/
                    label = {
                        Text(
                            text = label,
                            fontSize = textSize,
                            //color = if (readOnly) Color.Gray else Color.Unspecified
                        )},
                    keyboardOptions = KeyboardOptions(
                        keyboardType = keyboardType,
                        imeAction = ImeAction.Go
                    ),
                    keyboardActions = KeyboardActions(
                        onGo = {
                            keyboardController?.hide()
                        },
                    ),
                    modifier = modifier,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        disabledBorderColor = Color.Gray,
                        disabledLabelColor = Color.Gray,
                        disabledTextColor = Color.Gray,
                        disabledPlaceholderColor = Color.Gray,
                        focusedBorderColor = if (isError) MaterialTheme.colorScheme.error else BlueVistony,
                        unfocusedBorderColor = if (isError) MaterialTheme.colorScheme.error else Color.Gray,
                        focusedLabelColor = if (isError) MaterialTheme.colorScheme.error else BlueVistony,
                        unfocusedLabelColor = if (isError) MaterialTheme.colorScheme.error else Color.Gray
                    ),
                    trailingIcon = {
                        if (trailingiconStatus) {
                            IconButton(onClick = {
                                trailingIconOnClick("")
                            }) {
                                Icon(
                                    painter = trailingiconResourceId,
                                    contentDescription = null,
                                    //tint = trailingiconColor
                                    tint = if(status){trailingiconColor}else{Color.LightGray}
                                )
                            }
                        } else {
                            null
                        }
                    },
                    isError = isError,
                    supportingText = if (isError) {
                        { Text(errorMessage ?: "Campo requerido") }
                    } else null,
                    textStyle = TextStyle.Default.copy(
                        fontSize = textSize,
                        color = Color.Black
                    )
                )
            }else if(leadingiconStatus&&!trailingiconStatus) {
                OutlinedTextField(
                    readOnly = readOnly,
                    enabled = status,
                    singleLine = false,
                    value = text,
                    onValueChange =
                        {
                        },
                    /*)placeholder = {
                        Text(text = placeholder, fontSize = 14.sp)
                    }*/
                    label = {
                        Text(
                            text = label,
                            fontSize = textSize,
                            //color = if (readOnly) Color.Gray else Color.Unspecified
                        )},
                    keyboardOptions = KeyboardOptions(
                        keyboardType = keyboardType,
                        imeAction = ImeAction.Go
                    ),
                    keyboardActions = KeyboardActions(
                        onGo = {
                            keyboardController?.hide()
                        },
                    ),
                    modifier = modifier,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        disabledBorderColor = Color.Gray,
                        disabledLabelColor = Color.Gray,
                        disabledTextColor = Color.Gray,
                        disabledPlaceholderColor = Color.Gray,
                        focusedBorderColor = if (isError) MaterialTheme.colorScheme.error else BlueVistony,
                        unfocusedBorderColor = if (isError) MaterialTheme.colorScheme.error else Color.Gray,
                        focusedLabelColor = if (isError) MaterialTheme.colorScheme.error else BlueVistony,
                        unfocusedLabelColor = if (isError) MaterialTheme.colorScheme.error else Color.Gray
                    ),
                    leadingIcon = {
                        if (leadingiconStatus) {
                            IconButton(onClick = {
                                leadingIconOnClick("")
                            }) {
                                Icon(
                                    painter = leadingiconResourceId,
                                    contentDescription = null,
                                    //tint = trailingiconColor
                                    tint = if(status){leadingiconColor}else{Color.LightGray}
                                )
                            }
                        } else {
                            null
                        }
                    },
                    isError = isError,
                    supportingText = if (isError) {
                        { Text(errorMessage ?: "Campo requerido") }
                    } else null,
                    textStyle = TextStyle.Default.copy(
                        fontSize = textSize,
                        color = Color.Black
                    )
                )
            }else if(!leadingiconStatus&&!trailingiconStatus) {
                OutlinedTextField(
                    readOnly = readOnly,
                    enabled = status,
                    singleLine = false,
                    value = text,
                    onValueChange =
                        {
                        },
                    /*)placeholder = {
                        Text(text = placeholder, fontSize = 14.sp)
                    }*/
                    label = {
                        Text(
                            text = label,
                            fontSize = textSize,
                            //color = if (readOnly) Color.Gray else Color.Unspecified
                        )},
                    keyboardOptions = KeyboardOptions(
                        keyboardType = keyboardType,
                        imeAction = ImeAction.Go
                    ),
                    keyboardActions = KeyboardActions(
                        onGo = {
                            keyboardController?.hide()
                        },
                    ),
                    modifier = modifier,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        disabledBorderColor = Color.Gray,
                        disabledLabelColor = Color.Gray,
                        disabledTextColor = Color.Gray,
                        disabledPlaceholderColor = Color.Gray,
                        focusedBorderColor = if (isError) MaterialTheme.colorScheme.error else BlueVistony,
                        unfocusedBorderColor = if (isError) MaterialTheme.colorScheme.error else Color.Gray,
                        focusedLabelColor = if (isError) MaterialTheme.colorScheme.error else BlueVistony,
                        unfocusedLabelColor = if (isError) MaterialTheme.colorScheme.error else Color.Gray
                    ),
                    isError = isError,
                    supportingText = if (isError) {
                        { Text(errorMessage ?: "Campo requerido") }
                    } else null,
                    textStyle = TextStyle.Default.copy(
                        fontSize = textSize,
                        color = Color.Black
                    )
                )
            }
            if (statusMaxCharacter)
            {
                Row {
                    Row( horizontalArrangement = Arrangement.Start){
                        Text(
                            text = textDownEditext,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (text.length > countMaxCharacter) Color.Red else Color.Gray,
                            modifier = Modifier
                                //.align(Alignment.BottomEnd)
                                .padding(10.dp, 0.dp)
                        )
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End){
                        Text(
                            text = "${text.length}/$countMaxCharacter",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (text.length > countMaxCharacter) Color.Red else Color.Gray,
                            modifier = Modifier
                                //.align(Alignment.BottomEnd)
                                .padding(10.dp, 0.dp)
                        )
                    }
                }
            }
        }
    }
}