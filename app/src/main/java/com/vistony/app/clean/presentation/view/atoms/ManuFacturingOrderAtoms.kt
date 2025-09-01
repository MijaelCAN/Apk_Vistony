package com.vistony.app.clean.presentation.view.atoms

import android.app.Activity
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.common.api.Status
import com.vistony.app.R
import com.vistony.app.ui.theme.theme.Dimensions

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun ManuFacturingOrderTextFieldView(
    value: String,
    color: Color = Color.Gray,
    textAlign: TextAlign = TextAlign.Center
) {
    val context = LocalContext.current
    val activity = context as Activity
    val windowSize = calculateWindowSizeClass(activity)
    val bodyFontSize = Dimensions.getBodyFontSize(windowSize.widthSizeClass)
    val paddingRes = Dimensions.getPadding(windowSize.widthSizeClass)
    Text(text = value, color = color, fontSize = bodyFontSize.sp, modifier = Modifier.padding(horizontal = paddingRes)
        , fontWeight = FontWeight.Bold, textAlign = textAlign)
}


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun ManuFacturingOrderEditTextView(
    status: Boolean= true,
    text: String="",
    label: String="",
    onClick: (String) -> Unit,
    countMaxCharacter: Int = 254,
    keyboardType: KeyboardType = KeyboardType.Text,
    onClickLeadingIcon: (String) -> Unit = { _ -> },
    leadingIconStatus: Boolean = false,
    trailingIconStatus: Boolean = false,
    onClickTrailingIcon: (String) -> Unit = { _ -> },
    trailingIconResourceId: Int = R.drawable.outline_search_24,
) {
    val context = LocalContext.current
    val activity = context as Activity
    val windowSize = calculateWindowSizeClass(context)
    val paddingRes = Dimensions.getPadding(windowSize.widthSizeClass)
    val textFieldHeight = Dimensions.getTextFieldHeight(windowSize.widthSizeClass)
    val bodyFontSize = Dimensions.getBodyFontSize(windowSize.widthSizeClass)

    EditTextM3(
        id = 0,
        status = status,
        value = text,
        placeholder = "placeholder",
        label = label,
        leadingiconResourceId = painterResource(id = R.drawable.outline_search_24),
        keyboardType = keyboardType,
        trailingiconResourceId = painterResource(trailingIconResourceId),
        leadingiconColor = MaterialTheme.colorScheme.secondary,
        trailingiconColor = MaterialTheme.colorScheme.secondary,
        textDownEditext = "Campo obligatorio",
        trailingIconStatus = trailingIconStatus,
        trailingIconOnClick = {
            onClickTrailingIcon(it)
        },
        countMaxCharacter = countMaxCharacter,
        resultEditText = { result ->
            onClick(result)
        },
        leadingIconStatus = leadingIconStatus,
        statusTextDownEditext = false,
        readOnly = false,
        modifier = Modifier
            .fillMaxWidth()
            //.height(textFieldHeight)
            .padding(horizontal = paddingRes)
            //.clip(RoundedCornerShape(12.dp))
            //.border(1.dp, Color.White, RoundedCornerShape(12.dp)
                ,
        leadingIconOnClick = {
            onClickLeadingIcon(it)
        },
        isError = false,
        errorMessage = "Seleccione el Telefono de la llamada",
    )
}