package com.vistony.app.clean.presentation.view.organisms

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vistony.app.clean.core.utils.ZebraDW
import com.vistony.app.clean.core.utils.ZebraDWComunication
import com.vistony.app.clean.presentation.view.atoms.CardM3
import com.vistony.app.clean.presentation.view.moleculs.ManuFacturingOrderDetail
import com.vistony.app.clean.presentation.view.moleculs.ManuFacturingOrderHead
import com.vistony.app.clean.presentation.view.moleculs.ManufacturingOrderCustomDialog
import com.vistony.app.clean.presentation.viewmodels.ManufacturingOrderViewModel
import com.vistony.app.clean.presentation.viewmodels.ScanViewModel
import com.vistony.app.ui.theme.theme.Dimensions
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun ManuFacturingOrderSectionMain(
    scanViewModel: ScanViewModel,
    viewModel: ManufacturingOrderViewModel  = hiltViewModel(),
) {
    val context = LocalContext.current
    val activity = context as Activity
    val windowSize = calculateWindowSizeClass(context)
    val padding_res = Dimensions.getPadding(windowSize.widthSizeClass)
    val reasonForRejectionsResponseModel = viewModel.reasonForRejectionsResponseModel.collectAsState()

    if(reasonForRejectionsResponseModel.value.data.isEmpty()){
        LaunchedEffect(key1 = reasonForRejectionsResponseModel.value){
            viewModel.getReasonForRejections()
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding_res)
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White.copy(alpha = 0.44f)),
    ) {
        Spacer(modifier = Modifier.padding(10.dp))
        //TuPantallaConScanner(viewModel = scanViewModel)
                ManufacturingOrderCustomDialog()
                ManuFacturingOrderHead(scanViewModel=scanViewModel)
                ManuFacturingOrderDetail()

        }
}
