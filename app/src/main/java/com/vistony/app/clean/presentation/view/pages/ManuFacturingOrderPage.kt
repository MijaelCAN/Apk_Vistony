package com.vistony.app.clean.presentation.view.pages

import android.app.Activity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.vistony.app.Entidad.UserState
import com.vistony.app.ViewModel.LoginViewModel
import com.vistony.app.clean.domain.model.ManufacturingOrderModel
import com.vistony.app.clean.presentation.view.templates.ManuFacturingOrderTemplate
import com.vistony.app.clean.presentation.viewmodels.ManufacturingOrderViewModel
import com.vistony.app.clean.presentation.viewmodels.ScanViewModel
import com.vistony.app.ui.theme.theme.Dimensions
import com.vistony.salesforce.kotlin.view.Atoms.theme.VistonyTheme

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun ManuFacturingOrderPage(
    navController: NavHostController,
    id: String,
    userState: UserState,
    scanViewModel : ScanViewModel
) {

    //VistonyTheme(){
        ManuFacturingOrderTemplate(navController,id,userState,scanViewModel)
    //}


}