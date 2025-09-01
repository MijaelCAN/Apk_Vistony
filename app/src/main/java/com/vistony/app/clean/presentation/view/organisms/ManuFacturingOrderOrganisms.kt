package com.vistony.app.clean.presentation.view.organisms

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.vistony.app.clean.presentation.view.moleculs.ManuFacturingOrderDetail
import com.vistony.app.clean.presentation.view.moleculs.ManuFacturingOrderHead
import com.vistony.app.clean.presentation.view.moleculs.ManufacturingOrderCustomDialog


@Composable
fun ManuFacturingOrderSectionMain() {
    Column {
        ManufacturingOrderCustomDialog()
        ManuFacturingOrderHead()
        ManuFacturingOrderDetail()
    }
}