package com.vistony.app.clean.presentation.view.organisms

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.vistony.app.clean.presentation.view.atoms.CardM3
import com.vistony.app.clean.presentation.view.moleculs.ManuFacturingOrderDetail
import com.vistony.app.clean.presentation.view.moleculs.ManuFacturingOrderHead
import com.vistony.app.clean.presentation.view.moleculs.ManufacturingOrderCustomDialog
import com.vistony.app.ui.theme.theme.Dimensions

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun ManuFacturingOrderSectionMain() {
    //CardM3(
     //   contentBody = {
    val context = LocalContext.current
    val activity = context as Activity
    val windowSize = calculateWindowSizeClass(context)

    val padding_res = Dimensions.getPadding(windowSize.widthSizeClass)
    val buttonHeight = Dimensions.getButtonHeight(windowSize.widthSizeClass)
    val textFieldHeight = Dimensions.getTextFieldHeight(windowSize.widthSizeClass)
    val titleFontSize = Dimensions.getTitleFontSize(windowSize.widthSizeClass)
    val bodyFontSize = Dimensions.getBodyFontSize(windowSize.widthSizeClass)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding_res)
            //.border(2.dp, Color.White, RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White.copy(alpha = 0.44f)),
        //.graphicsLayer { alpha = 0.44f },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

                ManufacturingOrderCustomDialog()
                ManuFacturingOrderHead()
                ManuFacturingOrderDetail()

        }//
    //)
}