package com.vistony.app.Screen.Generic

import android.app.Activity
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vistony.app.Extras.formatoUsuario
import com.vistony.app.ui.theme.theme.Dimensions

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@ExperimentalMaterial3Api
@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit = {},
    label: String,
    modifier: Modifier = Modifier,
    trailingIcon: (@Composable () -> Unit)? = null,
    readOnly: Boolean = false,
    minLines: Int = 1,
    maxLines: Int = Int.MAX_VALUE,
    keyboardOption: KeyboardOptions = KeyboardOptions.Default
) {
    val context = LocalContext.current
    val activity = context as Activity
    val windowSize = calculateWindowSizeClass(context)
    val bodyFontSize = Dimensions.getBodyFontSize(windowSize.widthSizeClass)

    TextField(
        modifier = modifier
            .fillMaxWidth()
            //.height(textFieldHeight)
            //.padding(horizontal = padding_res)
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color.White, RoundedCornerShape(12.dp)),
        value = value,
        minLines = minLines,
        maxLines = maxLines,
        label = {
            Text(
                text = label,
                color = if( value.isNotEmpty())Color.Gray else Color.LightGray,
                fontWeight = FontWeight.Bold,
                fontSize = if( value.isNotEmpty()) 12.sp else bodyFontSize.sp,
                overflow = TextOverflow.Ellipsis
            )
        },
        onValueChange = onValueChange,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedTextColor = Color.Gray,
            unfocusedTextColor = Color.Gray,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = Color.Gray
        ),
        trailingIcon = trailingIcon,
        keyboardOptions = keyboardOption,
        readOnly = readOnly
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
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clickable { keyboardController?.hide() },
        label = {
            Text(label)
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color(0xFF0054A3),
            unfocusedBorderColor = Color(0xFFA2A2A2),
            cursorColor = Color(0xFF0054A3),
            //textColor = Color.Black,
            //placeholderColor = Color.White,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            unfocusedPlaceholderColor = Color.LightGray,
            disabledPlaceholderColor = Color.Black,
            unfocusedLabelColor = Color.LightGray
        ),
        trailingIcon = trailingIcon,
        readOnly = readOnly
    )
}

@Composable
fun CustomText(
    text: String
) {
    Text(
        text = text,
        modifier = Modifier
            .padding(end = 8.dp, top = 16.dp)
            .width(70.dp),
        style = TextStyle(color = Color.Black)
    )
}